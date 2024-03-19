package com.app.business.service;

import com.app.business.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class PerformanceService {
    private final AppConfig appConfig;
    private final ExecutorService executor;
    private final ConcurrentHashMap<Integer, ItemFuture> futures = new ConcurrentHashMap<>();

    public PerformanceService(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.executor = Executors.newFixedThreadPool(appConfig.getParallelism().getThreadPool());
    }
    public PerformanceService start() {
        this.stop();
        for (int i = 1; i < appConfig.getParallelism().getThreadCount(); i++) {
            var runner = new RequestRunner(i, this);
            Future<?> future = executor.submit(runner);
            futures.put(i,
                    ItemFuture
                            .builder()
                            .id(i)
                            .future(future)
                            .runner(runner)
                            .build()
            );
        }
        return this;
    }

    private void finished(int id) {
        var itemFuture = this.futures.get(id);
        if (itemFuture == null)
            return;
        itemFuture.future.cancel(true);
        this.futures.remove(id);
        if (this.futures.isEmpty())
            this.quit();
    }

    private void sleep1ms() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    public void waitToFinished() {
        while(!this.futures.isEmpty()){
            for (Map.Entry<Integer, ItemFuture> entry : this.futures.entrySet()) {
                if (entry.getValue().future.isDone()) {
                    this.futures.remove(entry.getKey());
                    break;
                }
                this.sleep1ms();
            }
        }
    }

    public PerformanceService stop() {
        if(this.futures.isEmpty())
            return this;
        for (Map.Entry<Integer, ItemFuture> entry : this.futures.entrySet())
            entry.getValue().future.cancel(true);
        this.waitToFinished();
        this.futures.clear();
        return this;
    }

    public void quit() {
        this
                .stop()
                .waitToFinished();
        System.exit(0);
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class ItemFuture {
        PerformanceService performanceService;
        private int id;
        private Future<?> future;
        private RequestRunner runner;

    }

    @Slf4j
    public static class RequestRunner implements Runnable {
        private final PerformanceService perfService;
        private final int id;
        private final AppConfig appConfig;
        private HttpRequest.BodyPublisher bodyPublisher = null;

        public RequestRunner(int id, PerformanceService perfService) {
            this.perfService = perfService;
            this.id = id;
            this.appConfig = perfService.appConfig;
        }

        private HttpRequest.BodyPublisher getBody() {
            if (bodyPublisher == null) {
                var body = appConfig.getBody();
                var srcBody = body.getBody();
                if (srcBody != null) {
                    return bodyPublisher = HttpRequest.BodyPublishers.ofString(body.getSource());
                }

                if (body.getSizeKb() > 0) {
                    List<String> rows = new ArrayList<>();
                    int totalBytes = 0;
                    int count = 1;
                    while (totalBytes < body.getSizeKb() * 1024) {
                        var row = String.format("{\"id_%s\":\"%s\"}", count, UUID.randomUUID());
                        totalBytes += row.length();
                        rows.add(row);
                    }
                    return bodyPublisher = HttpRequest.BodyPublishers.ofString("[" + String.join(", ", rows) + "]");
                }
                return bodyPublisher = HttpRequest.BodyPublishers.noBody();
            }
            return bodyPublisher;
        }

        private boolean waitForHost() {
            var uri = this.appConfig.getTarget().getUri();
            try {
                var socket = new Socket();
                socket.connect(new InetSocketAddress(uri.getHost(), uri.getPort()), 5000); // Timeout de 5 segundos
                socket.close();
                return true;
            } catch (Exception e) {
                log.error("thread[{}]: fail: {}", this.id, e.getMessage());
                log.info("wait for " + uri);
                return false;
            }
        }


        private void requestExec() {
            var httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).build();
            var target = appConfig.getTarget();
            var uri = target.getUri();
            HttpRequest.Builder requestBuilder;

            String[] headers = target.getHeaders().toArray(new String[0]);

            if (target.getMethod().equals(AppConfig.Method.POST))
                requestBuilder = HttpRequest.newBuilder().headers(headers).uri(uri).timeout(Duration.ofSeconds(10L)).POST(getBody());
            else if (target.getMethod().equals(AppConfig.Method.PUT))
                requestBuilder = HttpRequest.newBuilder().headers(headers).uri(uri).timeout(Duration.ofSeconds(10L)).PUT(getBody());
            else if (target.getMethod().equals(AppConfig.Method.DELETE))
                requestBuilder = HttpRequest.newBuilder().headers(headers).uri(uri).timeout(Duration.ofSeconds(10L)).DELETE();
            else
                requestBuilder = HttpRequest.newBuilder().headers(headers).uri(uri).timeout(Duration.ofSeconds(10L)).GET();

            HttpRequest request = requestBuilder.build();
            HttpResponse<?> response = null;
            try {
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                log.error("thread[{}]: fail: {}", this.id, e.getMessage());
            } finally {
                var statusCode = response == null ? -1 : response.statusCode();
                log.info("thread[{}]: finished, statusCode:{}", this.id, statusCode);
            }

        }

        private void sleep1s() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            log.info("thread[{}]: started", this.id);

            while (!waitForHost())
                sleep1s();

            var timeout =
                    appConfig.getParallelism().getTimeoutSecs() <= 0
                            ? null
                            : LocalDateTime.now().plusSeconds(appConfig.getParallelism().getTimeoutSecs());
            var tryCount = appConfig.getParallelism().getTryCount();
            while (tryCount > 0) {
                while (timeout == null || timeout.isAfter(LocalDateTime.now())) {
                    if (timeout != null && tryCount > 0)
                        log.info("thread[{}]: exec: {}, remaining: {}, timeout: {}", this.id, LocalDateTime.now(), tryCount, timeout);
                    else if (timeout != null)
                        log.info("thread[{}]: exec: {}, timeout: {}", this.id, LocalDateTime.now(), timeout);
                    else
                        log.info("thread[{}]: exec: {}, remaining: {}", this.id, LocalDateTime.now(), tryCount);

                    this.requestExec();
                    --tryCount;
                }
            }
            this.perfService.finished(this.id);
            log.info("thread[{}]: finished", this.id);
        }
    }


}
