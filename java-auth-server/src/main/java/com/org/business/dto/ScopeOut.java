package com.org.business.dto;

import com.org.business.model.Scope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ScopeOut {

    private UUID id;
    private LocalDateTime dt;
    private String name;
    private boolean enabled;

    public ScopeOut() {
    }

    public static ScopeOut from(Scope scope) {
        if (scope == null)
            return null;
        return ScopeOut.builder()
                .id(scope.getId())
                .dt(scope.getDt())
                .name(scope.getName())
                .enabled(scope.isEnabled())
                .build();
    }
}
