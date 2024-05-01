package com.app.business.model;

import com.app.business.domain.ExecutionType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
