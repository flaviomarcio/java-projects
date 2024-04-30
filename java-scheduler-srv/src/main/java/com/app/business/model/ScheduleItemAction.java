package com.app.business.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemAction {
    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ExecutionType executionType;
    private String headers;
    private boolean enabled;

    public enum ExecutionType {
        PRINT, REQUEST, AMQP
    }
}
