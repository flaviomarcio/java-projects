package com.org.business.dto;

import com.org.business.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionOut {
    private UUID id;
    private Profile profile;
    private TokenOut token;

    public static UserSessionOut from(User user) {
        return UserSessionOut.builder()
                .id(user.getId())
                .profile(
                        Profile.builder()
                                .id(user.getId())
                                .scopeId(user.getScopeId())
                                .name(user.getName())
                                .dtBirth(user.getDtBirth())
                                .document(user.getDocument())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNumber())
                                .build()
                )
                .build();

    }

    public static UserSessionOut from(User user, TokenOut tokenOut) {
        return UserSessionOut.builder()
                .id(user.getId())
                .profile(
                        Profile.builder()
                                .id(user.getId())
                                .scopeId(user.getScopeId())
                                .name(user.getName())
                                .dtBirth(user.getDtBirth())
                                .document(user.getDocument())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNumber())
                                .validated(true)
                                .build()
                )
                .token(tokenOut)
                .build();

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private UUID id;
        private UUID scopeId;
        private String name;
        private String document;
        private LocalDate dtBirth;
        private String email;
        private String phoneNumber;
        private boolean validated;
    }
}
