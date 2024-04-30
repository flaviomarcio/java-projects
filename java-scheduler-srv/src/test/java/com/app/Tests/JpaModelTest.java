package com.app.Tests;

import com.app.business.model.ScheduleGroup;
import com.app.business.model.ScheduleItem;
import com.app.business.model.ScheduleItemAction;
import com.app.business.model.ScheduleItemCheckPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class JpaModelTest {

    @Test
    public void UT_000_CHECK_ScheduleGroup() {
        Assertions.assertDoesNotThrow(() -> new ScheduleGroup());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleGroup
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .name("default")
                    .items(new ArrayList<>())
                    .enabled(true)
                    .build();
        });

        var model=ScheduleGroup
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .name("default")
                .items(new ArrayList<>())
                .enabled(true)
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getName);
        Assertions.assertDoesNotThrow(model::isEnabled);
        Assertions.assertDoesNotThrow(model::getItems);

        Assertions.assertDoesNotThrow(()->model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setName(""));
        Assertions.assertDoesNotThrow(()->model.setEnabled(false));
        Assertions.assertDoesNotThrow(()->model.setItems(new ArrayList<>()));
    }

    @Test
    public void UT_000_CHECK_ScheduleItem() {

        Assertions.assertDoesNotThrow(() -> new ScheduleItem());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItem
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .name("default")
                    .description("description")
                    .enabled(true)
                    .actions(new ArrayList<>())
                    .pallelism(1)
                    .build();
        });

        var model=ScheduleItem
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .scheduleGroup(ScheduleGroup.builder().enabled(true).build())
                .name("default")
                .description("description")
                .enabled(true)
                .actions(new ArrayList<>())
                .pallelism(1)
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getName);
        Assertions.assertDoesNotThrow(model::getScheduleGroup);
        Assertions.assertDoesNotThrow(model::getDescription);
        Assertions.assertDoesNotThrow(model::isEnabled);
        Assertions.assertDoesNotThrow(model::getActions);
        Assertions.assertDoesNotThrow(model::getPallelism);

        Assertions.assertDoesNotThrow(()->model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setName(""));
        Assertions.assertDoesNotThrow(()->model.setScheduleGroup(ScheduleGroup.builder().enabled(false).build()));
        Assertions.assertDoesNotThrow(()->model.setDescription("description"));
        Assertions.assertDoesNotThrow(()->model.setEnabled(false));
        Assertions.assertDoesNotThrow(()->model.setActions(new ArrayList<>()));
        Assertions.assertDoesNotThrow(()->model.setPallelism(0));

    }

    @Test
    public void UT_000_CHECK_ScheduleItemAction() {

        Assertions.assertDoesNotThrow(() -> new ScheduleItemAction());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItemAction
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .headers("{\"header\":\"value\"}")
                    .enabled(true)
                    .executionType(ScheduleItemAction.ExecutionType.AMQP)
                    .build();
        });

        var model=ScheduleItemAction
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .headers("{\"header\":\"value\"}")
                .enabled(true)
                .executionType(ScheduleItemAction.ExecutionType.AMQP)
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getHeaders);
        Assertions.assertDoesNotThrow(model::isEnabled);
        Assertions.assertDoesNotThrow(model::getExecutionType);

        Assertions.assertDoesNotThrow(()->model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setHeaders("{\"header\":\"value\"}"));
        Assertions.assertDoesNotThrow(()->model.setEnabled(false));
        Assertions.assertDoesNotThrow(()->model.setExecutionType(ScheduleItemAction.ExecutionType.AMQP));

    }

    @Test
    public void UT_000_CHECK_ScheduleItemCheckPoint() {

        Assertions.assertDoesNotThrow(() -> new ScheduleItemCheckPoint());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItemCheckPoint
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .value("1901-01-01T00:00:00")
                    .build();
        });

        var model=ScheduleItemCheckPoint
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .value("1901-01-01T00:00:00")
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getValue);

        Assertions.assertDoesNotThrow(()->model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(()->model.setValue("1901-01-01T00:00:00"));

    }


}
