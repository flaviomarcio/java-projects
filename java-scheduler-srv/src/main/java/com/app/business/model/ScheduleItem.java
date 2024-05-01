package com.app.business.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "group_id")
    private ScheduleGroup group;
    private String name;
    private String description;
    private int pallelism;
    private boolean enabled;

    @OneToMany
    @JoinColumn
    private List<ScheduleItemAction> actions;
}
