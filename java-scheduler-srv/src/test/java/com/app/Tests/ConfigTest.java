package com.app.Tests;

import com.app.business.config.AppConfig;
import com.app.business.dto.CheckPointIn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ConfigTest {

    @Test
    public void UT_000_CHECK_AppConfig() {
        Assertions.assertDoesNotThrow(() -> new AppConfig());
        Assertions.assertDoesNotThrow(() -> {
            new AppConfig.Git();
        });

        var appConfig=new AppConfig();
        Assertions.assertDoesNotThrow(appConfig::isEnabled);
        Assertions.assertDoesNotThrow(appConfig::getGit);

        var git=new AppConfig.Git();;
        Assertions.assertDoesNotThrow(git::getGitUrl);
        Assertions.assertDoesNotThrow(git::getGitSshKeyFilePrivate);
        Assertions.assertDoesNotThrow(git::getGitSshKeyFilePublic);

    }

}
