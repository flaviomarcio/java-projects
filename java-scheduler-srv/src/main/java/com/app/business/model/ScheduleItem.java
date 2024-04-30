package com.app.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleItem {
    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne
    private ScheduleGroup scheduleGroup;
    private String name;
    private String description;
    private int pallelism;
    private boolean enabled;

    @OneToMany
    @JoinColumn
    private List<ScheduleItemAction> actions;
}
