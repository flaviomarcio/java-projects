package com.app.business.config;

import com.app.business.service.EventConsumerService;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ConsumerStarted {
    private final EventConsumerService eventConsumerService;

    public ConsumerStarted(EventConsumerService eventConsumerService) {
        this.eventConsumerService = eventConsumerService;
        this.eventConsumerService.consumer();
    }
}
