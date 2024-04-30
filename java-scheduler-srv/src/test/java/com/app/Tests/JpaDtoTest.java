package com.app.Tests;

import com.app.business.dto.CheckPointIn;
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
public class JpaDtoTest {

    @Test
    public void UT_000_CHECK_CheckPointIn() {
        Assertions.assertDoesNotThrow(() -> new CheckPointIn());
        Assertions.assertDoesNotThrow(() -> {
            CheckPointIn
                    .builder()
                    .id(UUID.randomUUID())
                    .value("1901-01-01T00:00:00")
                    .build();
        });

        var dto=CheckPointIn
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
