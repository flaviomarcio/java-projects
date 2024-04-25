package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.config.enabled}")
    private boolean enabled;
    @Value("${app.config.state.sent}")
    private String stateSent;
}
