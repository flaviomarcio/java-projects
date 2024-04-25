package com.app.factory;

import com.app.business.service.EventService;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Getter
@Service
public class FactoryByTests {
    private Environment mockEnvironment;
    private EventService notifyEventService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.notifyEventService = new EventService();
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
