package com.app.Tests;

import com.app.business.config.AppConfig;
import com.app.business.service.PerformanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceTest {

    @Test
    public void UT_000_CHECK_PERFORMANCE() {
        var appConfig=AppConfig.builder().build();
        var proxyService=new PerformanceService(appConfig);
        proxyService
                .start()
                .waitToFinished();
    }


}
