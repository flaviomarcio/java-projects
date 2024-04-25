package com.org.business.service;

import com.org.business.domain.DispatcherMode;
import com.org.business.model.User;
import com.org.commons.config.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatcherService {
    private final SettingService settingService;

    public void userDispatcher(User user, DispatcherMode dispatcherMode, String message) {
        if (settingService.isDebugMode())
            log.info("new password: user: {}, {}", user.getUsername(), message);
    }

}
