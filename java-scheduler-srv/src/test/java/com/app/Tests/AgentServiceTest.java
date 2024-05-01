package com.app.Tests;

import com.app.business.dto.ScheduleItemCheckPointIn;
import com.app.business.service.AgentService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AgentServiceTest {

    private final FactoryByTests factory = new FactoryByTests();
    private final AgentService agentService = factory.getAgentService();

    @Test
    public void UT_000_CHECK_SAVE() {
        Assertions.assertDoesNotThrow(agentService::start);

        factory
                .getScheduleItemList()
                        .forEach(scheduleItem -> {
                            Assertions.assertDoesNotThrow(() -> agentService.exec(scheduleItem.getId()));
                            Assertions.assertDoesNotThrow(() ->
                                    agentService.updateCheckPoint(
                                            ScheduleItemCheckPointIn
                                                    .builder()
                                                    .id(scheduleItem.getId())
                                                    .value(UUID.randomUUID().toString())
                                                    .build()
                                    )
                            );

                        });

    }


}
