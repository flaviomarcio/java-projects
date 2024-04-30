package com.app.business.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.config.enabled:false}")
    private boolean enabled;
    private Git git;


    @Getter
    @Configuration
    public static class Git{
        @Value("${app.config.git.url:}")
        private String gitUrl;

        @Value("${app.config.git.ssh:}")
        private String gitSshKeyFilePublic;

        @Value("${app.config.git.sent:}")
        private String gitSshKeyFilePrivate;
    }

}
