package com.app.factory;

import com.app.business.config.AppConfig;
import com.app.business.service.IngesterService;
import com.littlecode.mq.MQ;
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
    private IngesterService ingesterService;
    private AppConfig appConfig;
    private MQ mq;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.mq = Mockito.mock(MQ.class);
        Mockito.when(mq.dispatcher(Mockito.any(Object.class))).thenReturn(null);

        this.appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.isEnabled()).thenReturn(true);
        Mockito.when(appConfig.getStateSent()).thenReturn("Sent");
        this.ingesterService = new IngesterService(appConfig, mq);
    }

    private void makeMockEnvironment() {
        List<Map<String, String>> envList = List.of(
                Map.of(
                        "app.proxy.state.sent", "Sent",
                        "app.proxy.enabled", "true"
                )
        );

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
