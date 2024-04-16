package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    @Value("${enabled}")
    private boolean enabled;
    @Value("${state.sent}")
    private String stateSent;
}
