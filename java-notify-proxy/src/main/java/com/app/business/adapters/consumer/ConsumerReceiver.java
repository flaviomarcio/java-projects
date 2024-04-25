package com.app.business.adapters.consumer;

import com.app.business.domain.InputType;
import com.app.business.service.EventConsumerService;
import com.littlecode.mq.MQ;
import com.littlecode.mq.MQConsumer;
import com.littlecode.mq.MQReceiveMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RequiredArgsConstructor
@MQConsumer
public class ConsumerReceiver {
    @Value("${config.queue.receiver:type:}")
    private InputType type;

    @Value("${config.queue.receiver:}")
    private String name;

    @Value("${config.queue.receiver:client.id:}")
    private String clientId;

    @Value("${config.queue.receiver.client.secret:}")
    private String clientSecret;
    private final EventConsumerService service;

    @MQReceiveMethod
    public MQ.Executor execute() {
        return MQ.Executor
                .builder()
                .received(service::register)
                .build();
    }
}
