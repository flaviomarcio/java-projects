package com.org.business.dto;

import com.org.business.domain.DispatcherMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserResetPassword {
    private String clientId;
    private DispatcherMode dispatcher;

    public UserResetPassword() {
    }
}
