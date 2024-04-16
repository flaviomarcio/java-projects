package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.config.title:NoTitle}")
    private String title;
    @Value("${app.config.className:NoClass}")
    private String className;
    @Value("${app.config.callBack.enabled:false}")
    private boolean callBackEnabled;
    @Value("${app.config.callBack.queue:NoQueueName}")
    private String callBackQueue;
    @Value("${app.config.callBack.autoCreate:false}")
    private boolean callBackQueueAutoCreate;
    @Value("${app.config.callBack.client.id:}")
    private String callBackClientId;
    @Value("${app.config.callBack.client.secret:}")
    private String callBackClientSecret;
}