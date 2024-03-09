package br.com.factory;

import br.com.business.dto.Setting;
import br.com.business.service.SettingService;
import lombok.Getter;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private Environment environment;
    private final List<Setting> settingList = new ArrayList<>();

    private SettingService settingService;

    public FactoryByTests() {
        setupMockClasses();
        setupEnvironment();
        setupSettings();
        setupServices();
    }

    private void setupMockClasses() {
        environment= Mockito.mock(Environment.class);
    }

    private void setupEnvironment()
    {
        Map<String,String> envMap=Map.of(
                "service.context.port", "8080",
                "service.context.path", "/api",
                "service.config.environment", "development",
                "service.config.path", "/tmp/setting-cache"
        );
        envMap.forEach((key, value) ->{
            Mockito.when(environment.getProperty(key)).thenReturn(value);
            Mockito.when(environment.getProperty(key,value)).thenReturn(value);
        });
    }

    private void setupServices(){
        settingService = new SettingService(environment);
        settingService.init();
    }

    private void setupSettings() {
        settingList.clear();
        for (int i = 1; i <= 25; i++) {

            Object settings = null;
            if (i >= 20) {
                settings = new HashMap<>();
            } else if (i >= 15) {
                settings = Map.of("arg1", "v1", "arg2", "v3");
            } else if (i >= 10) {
                settings = new ArrayList<>();
            } else if (i >= 5) {
                settings = List.of(
                        String.format("env.value.e01=%s", LocalDateTime.now().plusDays(2).plusMinutes(127)),
                        String.format("env.value.e02=%s", LocalDateTime.now().plusDays(1).plusMinutes(245))
                );
            }

            var record = Setting
                    .builder()
                    .name(UUID.nameUUIDFromBytes(String.valueOf(i).getBytes()).toString())
                    .settings(settings)
                    .build();

            settingList.add(record);
        }
    }


}
