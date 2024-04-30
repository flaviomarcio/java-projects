package com.app.Tests;

import com.app.business.service.RunnerService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTest {

    private final FactoryByTests factory = new FactoryByTests();
    private final RunnerService runnerService = factory.getRunnerService();

    @Test
    public void UT_000_CHECK_SAVE() {
        Assertions.assertDoesNotThrow(() -> runnerService.start());
    }


}
