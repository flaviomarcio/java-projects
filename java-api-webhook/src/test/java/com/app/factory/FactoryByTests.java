package com.app.factory;

import com.app.business.config.AppConfig;
import com.app.business.service.EventConsumerService;
import com.app.business.service.EventWritterService;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Getter
@Service
public class FactoryByTests {
    private Environment mockEnvironment;
    private EventWritterService eventWritterService;
    private EventConsumerService consumerService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.eventWritterService = new EventWritterService();
        this.consumerService = new EventConsumerService(Mockito.mock(AppConfig.ReaderConfig.class), Mockito.mock(WebClient.class));
    }

    private void makeMockEnvironment() {
        List<Map<String, String>> envList = List.of(
//                Map.of(
//                        "dispatcher.international.code", "55",
//                        "dispatcher.cleanup.days", "7",
//                        "dispatcher.strategy.whatsapp", "NONE"
//                ),
        );

        //noinspection RedundantOperationOnEmptyContainer
        envList.forEach(envMap -> {
            envMap.forEach((key, value) ->
                    {
                        Mockito.when(mockEnvironment.getProperty(key)).thenReturn(value);
                        Mockito.when(mockEnvironment.getProperty(key, "")).thenReturn(value);
                    }
            );
        });
    }

}
