package com.app.business.model;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleGroup {
    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private boolean enabled;

    @OneToMany
    @JoinColumn
    private List<ScheduleItemAction> items;
}
