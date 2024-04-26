package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.config.webhook.uri:}")
    private String uri;
    @Value("${app.config.webhook.version:}")
    private String version;
    @Value("${app.config.webhook.apiKey:}")
    private String apiKey;
}
