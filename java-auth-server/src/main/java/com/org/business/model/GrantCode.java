package com.org.business.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
