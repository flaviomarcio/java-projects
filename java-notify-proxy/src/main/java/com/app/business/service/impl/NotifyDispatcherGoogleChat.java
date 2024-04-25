package com.app.business.service.impl;

import com.app.business.config.AppConfig;
import com.app.business.model.ofservice.NotifyForwarder;

public class NotifyDispatcherGoogleChat extends NotifyDispatcherLogging {

    public NotifyDispatcherGoogleChat(AppConfig appConfig) {
        super(appConfig);
    }

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.GoogleChat;
    }

}
