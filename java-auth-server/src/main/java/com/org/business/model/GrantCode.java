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
@Table(name = "grant_code")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class GrantCode {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private LocalDateTime dt;
    @Column(nullable = false)
    private UUID scopeId;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private LocalDateTime expires;
    @Column(nullable = false)
    private boolean enabled;

    public GrantCode() {

    }

    public boolean isExpired() {
        return !this.enabled || this.expires == null || this.expires.isBefore(LocalDateTime.now());
    }

}
