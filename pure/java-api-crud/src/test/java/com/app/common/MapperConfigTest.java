package com.app.common;

import com.app.common.config.MapperConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
public class MapperConfigTest {
    @Mock
    private static ApplicationContext applicationContext;
    @Mock
    private static Environment environment;

    @Test
    public void UI_CHECK() {
        Assertions.assertDoesNotThrow(() -> {new MapperConfig(applicationContext,environment);});
        Assertions.assertTrue(MapperConfig.isConfigured());
        Assertions.assertNotNull(MapperConfig.getApplicationContext());
        Assertions.assertNotNull(MapperConfig.getEnvironment());
    }


}
