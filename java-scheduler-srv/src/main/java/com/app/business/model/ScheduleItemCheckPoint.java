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
public class ScheduleItemCheckPoint {
    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Column(length = 100)
    private String value;

    @OneToMany
    @JoinColumn
    private List<ScheduleItemAction> actions;
}
