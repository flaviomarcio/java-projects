package com.app.Tests;

import com.app.business.dto.ScheduleGroupIn;
import com.app.business.dto.ScheduleItemCheckPointIn;
import com.app.business.dto.ScheduleItemIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class JpaDtoTest {

    @Test
    public void UT_000_CHECK_ScheduleGroupIn() {
        Assertions.assertDoesNotThrow(() -> new ScheduleGroupIn());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleGroupIn
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .name("default")
                    .enabled(true)
                    .build();
        });

        var model = ScheduleGroupIn
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .name("default")
                .enabled(true)
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getName);
        Assertions.assertDoesNotThrow(model::isEnabled);

        Assertions.assertDoesNotThrow(() -> model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setName(""));
        Assertions.assertDoesNotThrow(() -> model.setEnabled(false));
    }

    @Test
    public void UT_000_CHECK_ScheduleItemIn() {

        Assertions.assertDoesNotThrow(() -> new ScheduleItemIn());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItemIn
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

        var model = ScheduleItemIn
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .groupId(UUID.randomUUID())
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
        Assertions.assertDoesNotThrow(model::getGroupId);
        Assertions.assertDoesNotThrow(model::getDescription);
        Assertions.assertDoesNotThrow(model::isEnabled);
        Assertions.assertDoesNotThrow(model::getActions);
        Assertions.assertDoesNotThrow(model::getPallelism);

        Assertions.assertDoesNotThrow(() -> model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setName(""));
        Assertions.assertDoesNotThrow(() -> model.setGroupId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> model.setDescription("description"));
        Assertions.assertDoesNotThrow(() -> model.setEnabled(false));
        Assertions.assertDoesNotThrow(() -> model.setActions(new ArrayList<>()));
        Assertions.assertDoesNotThrow(() -> model.setPallelism(0));

    }

    @Test
    public void UT_000_CHECK_ScheduleItemInActionIn() {

        Assertions.assertDoesNotThrow(() -> new ScheduleItemIn.ActionIn());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItemIn.ActionIn
                    .builder()
                    .id(UUID.randomUUID())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .headers("{\"header\":\"value\"}")
                    .enabled(true)
                    .build();
        });

        var model = ScheduleItemIn.ActionIn
                .builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .headers("{\"header\":\"value\"}")
                .enabled(true)
                .build();

        Assertions.assertDoesNotThrow(model::getId);
        Assertions.assertDoesNotThrow(model::getCreatedAt);
        Assertions.assertDoesNotThrow(model::getUpdatedAt);
        Assertions.assertDoesNotThrow(model::getHeaders);
        Assertions.assertDoesNotThrow(model::isEnabled);
        Assertions.assertDoesNotThrow(model::getExecutionType);

        Assertions.assertDoesNotThrow(() -> model.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> model.setCreatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setUpdatedAt(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> model.setHeaders("{\"header\":\"value\"}"));
        Assertions.assertDoesNotThrow(() -> model.setEnabled(false));

    }

    @Test
    public void UT_000_CHECK_ScheduleItemCheckPointIn() {
        Assertions.assertDoesNotThrow(() -> new ScheduleItemCheckPointIn());
        Assertions.assertDoesNotThrow(() -> {
            ScheduleItemCheckPointIn
                    .builder()
                    .id(UUID.randomUUID())
                    .value("1901-01-01T00:00:00")
                    .build();
        });

        var dto = ScheduleItemCheckPointIn
                .builder()
                .id(UUID.randomUUID())
                .value("1901-01-01T00:00:00")
                .build();

        Assertions.assertDoesNotThrow(dto::getId);
        Assertions.assertDoesNotThrow(dto::getValue);

        Assertions.assertDoesNotThrow(()->dto.setId(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->dto.setValue("1901-01-01T00:00:00"));
    }

}
