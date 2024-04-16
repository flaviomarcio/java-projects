package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    @Value("${title:NoTitle}")
    private String title;
    @Value("${className:NoClass}")
    private String className;
    @Value("${callBack.enabled:false}")
    private boolean callBackEnabled;
    @Value("${callBack.queue:NoQueueName}")
    private String callBackQueue;
    @Value("${callBack.autoCreate:false}")
    private boolean callBackQueueAutoCreate;
    @Value("${callBack.client.id:}")
    private String callBackClientId;
    @Value("${callBack.client.secret:}")
    private String callBackClientSecret;
}
