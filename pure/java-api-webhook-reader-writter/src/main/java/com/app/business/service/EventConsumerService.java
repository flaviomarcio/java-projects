package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.dto.EventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class EventConsumerService {
    private final AppConfig.ReaderConfig readerConfig;
    private final WebClient webClient;

    public void consumer() {
        this.webClient
                .get()
                .uri(
                        uriBuilder ->
                                uriBuilder
                                        .path(readerConfig.getPath())
                                        .queryParam("version", readerConfig.getVersion())
                                        .queryParam("api_key", readerConfig.getApiKey())
                                        .build()
                )
                .retrieve()
                .bodyToFlux(List.class)
                .retryWhen(
                        Retry
                                .backoff(readerConfig.getMaxAttempts(), Duration.ofSeconds(readerConfig.getIntervalSeconds()))
                                .jitter(readerConfig.getJitter())
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

    public void receiver(List<EventDTO> events) {
        var mapper = new ObjectMapper();
        try {
            log.info(mapper.writeValueAsString(events));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
