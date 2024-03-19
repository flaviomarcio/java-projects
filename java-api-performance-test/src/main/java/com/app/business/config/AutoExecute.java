package com.app.business.config;

import com.app.business.service.PerformanceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Configuration
public class AutoExecute {
    private final PerformanceService performanceService;

    public AutoExecute(PerformanceService performanceService) {
        this.performanceService = performanceService;
        this.performanceService
                .start();
    }
}
