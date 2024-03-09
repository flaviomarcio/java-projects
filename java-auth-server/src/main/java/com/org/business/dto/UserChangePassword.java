package com.org.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserChangePassword {
    private String password;

    public UserChangePassword() {
    }
}
