package com.app.business.service.impl;

import com.app.business.model.ofservice.NotifyForwarder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyDispatcherTeams extends NotifyDispatcherNone {

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.Teams;
    }

}
