package com.app.business.dto;

import com.app.business.domain.ExecutionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItemIn {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID groupId;
    private String name;
    private String description;
    private int pallelism;
    private boolean enabled;
    private List<ActionIn> actions;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionIn {
        private UUID id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private ExecutionType executionType;
        private String headers;
        private boolean enabled;
    }

}
