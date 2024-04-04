package com.app.common.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class SwaggerConfigTest {

    @Test
    public void UI_CHECK() {
        var settings= Mockito.mock(SwaggerConfig.Settings.class);
        Mockito.when(settings.getSrvDev()).thenReturn("localhost:8080");
        Mockito.when(settings.getSrvStg()).thenReturn("localhost:8080");
        Mockito.when(settings.getSrvPrd()).thenReturn("localhost:8080");
        Mockito.when(settings.getInfoContextPath()).thenReturn("/");
        Mockito.when(settings.getInfoCompany()).thenReturn("company");
        Mockito.when(settings.getInfoProduct()).thenReturn("product");
        Mockito.when(settings.getInfoTitle()).thenReturn("title");
        Mockito.when(settings.getInfoDescription()).thenReturn("description");
        Mockito.when(settings.getInfoVersion()).thenReturn("1.0.0");
        var swaggerConfig=new SwaggerConfig(settings);

        Assertions.assertNotNull(swaggerConfig.makeOpenAPI());
        Assertions.assertDoesNotThrow(() -> swaggerConfig.makeDefaultHeaders());

    }
}
