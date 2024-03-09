package com.org.business.dto;

import com.org.business.domain.AuthMode;
import com.org.business.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginIn {
    private AuthMode authMode;
    private String clientId;
    private String secret;

    public static LoginIn from(User user) {
        return LoginIn
                .builder()
                .clientId(user.getUsername())
                .secret(user.getPassword())
                .build();
    }
}
