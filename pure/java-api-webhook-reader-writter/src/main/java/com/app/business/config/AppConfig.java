package com.app.business.config;

import lombok.Data;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@UtilityClass
public class AppConfig {

    @Data
    @Configuration
    public class FluxConfig {
        @Value("${app.config.flux.secs:2}")
        private int secs;
        @Value("${app.config.flux.max:2}")
        private int max;
    }

    @Data
    @Configuration
    public class ReaderConfig {
        @Value("${app.config.reader.uri:http://localhost:8080}")
        private String uri;
        @Value("${app.config.reader.path:/events/read}")
        private String path;
        @Value("${app.config.reader.version:0.0.0}")
        private String version;
        @Value("${app.config.reader.apiKey:}")
        private String apiKey;
        @Value("${app.config.reader.intervalSeconds:2}")
        private int intervalSeconds;
        @Value("${app.config.reader.jitter:0.75}")
        private double jitter;
        @Value("${app.config.reader.maxAttempts:2}")
        private int maxAttempts;
    }

}
