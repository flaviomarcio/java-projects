package com.org.business.dto;

import com.org.business.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class UserOut {
    private UUID id;
    private String username;

    public static UserOut from(User user) {
        return builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
