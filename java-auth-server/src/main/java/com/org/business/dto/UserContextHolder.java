package com.org.business.dto;

import com.org.business.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserContextHolder {
    private UUID scopeId;
    private UUID userId;
    private String userName;

    public static UserContextHolder from(User user) {
        return builder()
                .scopeId(user.getScopeId())
                .userId(user.getId())
                .userName(user.getUsername())
                .build();
    }
}
