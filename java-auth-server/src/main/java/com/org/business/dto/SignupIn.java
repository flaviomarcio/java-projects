package com.org.business.dto;

import com.org.business.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SignupIn {

    private String username;
    private String password;
    private String name;
    private String document;
    private String email;
    private String phoneNumber;
    private List<String> roles;

    public static SignupIn from(User user) {
        return SignupIn
                .builder()
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .document(user.getDocument())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
