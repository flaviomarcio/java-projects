package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.dto.NotifyEventIn;
import com.littlecode.parsers.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final AppConfig appConfig;
    private final WebClient webClient;

    public void consumer() {
        this.webClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path("/events")
                                        .queryParam("version", appConfig.getVersion())
                                        .queryParam("api_key", appConfig.getApiKey())
                                        .build()
                )
                .retrieve()
                .bodyToFlux(List.class)
                .retryWhen(
                        Retry
                                .backoff(3, Duration.ofSeconds(2))
                                .jitter(0.75)
                                .onRetryExhaustedThrow
                                        (
                                                (spec, signal) ->
                                                {
                                                    throw new RuntimeException(String.format("fail: [%s]", signal.totalRetries()));
                                                }
                                        )
                )
                .subscribe(
                        this::receiver,
                        error -> Arrays.stream(error.getStackTrace()).forEach(System.out::println),
                        () -> log.info("completed")
                );
    }

    public void receiver(List<NotifyEventIn> events) {
        log.info(ObjectUtil.toString(events));
    }
}
