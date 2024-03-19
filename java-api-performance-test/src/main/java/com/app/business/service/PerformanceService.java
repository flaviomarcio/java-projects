package com.app.business.service;

import com.app.business.config.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final AppConfig appConfig;
    private ExecutorService executor=null;

    private final ConcurrentHashMap<Integer, Future<?>> futures =new ConcurrentHashMap<>();
    public PerformanceService start() {
        this.stop();
        this.executor=Executors.newFixedThreadPool(appConfig.getParallelism().getThreadPool());
        for (int i = 1; i < appConfig.getParallelism().getThreadCount(); i++) {
            Future<?> future = executor.submit(new RequestRunner(i, appConfig));
            futures.put(i,future);
        }
        return this;
    }

    public PerformanceService waitToFinished() {
        while(!this.futures.isEmpty()){
            for (Map.Entry<Integer, Future<?>> entry : this.futures.entrySet()) {
                if (entry.getValue().isDone()) {
                    this.futures.remove(entry.getKey());
                    break;
                }
                else{
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
        return this;
    }

    public PerformanceService stop() {
        if(this.futures.isEmpty())
            return this;
        for (Map.Entry<Integer, Future<?>> entry : this.futures.entrySet())
            entry.getValue().cancel(true);
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

    @Slf4j
    @RequiredArgsConstructor
    public static class RequestRunner implements Runnable {
        private final int id;
        private final AppConfig appConfig;

        @Override
        public void run() {
            log.info("thread[{}]: started", this.id);
            var timeout= LocalDateTime.now().plusSeconds(appConfig.getParallelism().getTimeoutSecs());
            while(timeout.isAfter(LocalDateTime.now())){
                log.info("thread[{}]: exec: {}", this.id,LocalDateTime.now());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("thread[{}]: finished", this.id);
        }
    }


}
