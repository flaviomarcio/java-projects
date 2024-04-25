package com.app.business.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Data
@Configuration
public class AppConfig {
    private UUID id;
}
