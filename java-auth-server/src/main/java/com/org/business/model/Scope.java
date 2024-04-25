package com.org.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scope")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Scope {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private LocalDateTime dt;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 200)
    private boolean enabled;

    public Scope() {
    }
}
