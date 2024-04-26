package com.app.business.config;

import com.app.business.service.ConsumerService;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class StartedConfig {
    private final ConsumerService consumerService;

    public StartedConfig(ConsumerService consumerService) {
        this.consumerService = consumerService;
        this.consumerService.consumer();
    }
}
