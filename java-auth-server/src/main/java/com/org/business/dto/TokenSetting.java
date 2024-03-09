package com.org.business.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenSetting {
    private String path;
    private String secret;
    private Expires expires;
    private Setting access;
    private Setting refresh;

    @Builder
    @Getter
    public static class Setting {
        private String keyPathPrivate;
        private String keyPathPublic;
    }

    @Builder
    @Getter
    public static class Expires {
        private int days;
        private int minutes;
    }
}
