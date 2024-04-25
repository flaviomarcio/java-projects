package com.app.business.service.impl;

import com.app.business.config.AppConfig;
import com.app.business.model.ofservice.NotifyForwarder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyDispatcherTeams extends NotifyDispatcherLogging {

    public NotifyDispatcherTeams(AppConfig appConfig) {
        super(appConfig);
    }

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.Teams;
    }

}
