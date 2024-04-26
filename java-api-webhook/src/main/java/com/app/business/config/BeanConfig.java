package com.app.business.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Getter
@Configuration
@RequiredArgsConstructor
public class BeanConfig {
    private final AppConfig appConfig;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(appConfig.getUri())
                .defaultHeader(CONNECTION, "Keep-alive")
                .defaultHeader(CONTENT_TYPE, "text/event-stream;charset=UTF-8")
                .build();
    }
}
