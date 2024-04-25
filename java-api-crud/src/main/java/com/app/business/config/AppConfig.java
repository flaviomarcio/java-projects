package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.config.title:NoTitle}")
    private String title;
    @Value("${app.config.className:NoClass}")
    private String className;
    @Value("${app.configcallBack.enabled:false}")
    private boolean callBackEnabled;
    @Value("${app.configcallBack.queue:NoQueueName}")
    private String callBackQueue;
    @Value("${app.configcallBack.autoCreate:false}")
    private boolean callBackQueueAutoCreate;
    @Value("${app.configcallBack.client.id:}")
    private String callBackClientId;
    @Value("${app.configcallBack.client.secret:}")
    private String callBackClientSecret;
}
