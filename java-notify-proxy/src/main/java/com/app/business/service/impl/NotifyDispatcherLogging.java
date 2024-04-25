package com.app.business.service.impl;

import com.app.business.config.AppConfig;
import com.app.business.model.ofservice.NotifyEventItem;
import com.app.business.model.ofservice.NotifyForwarder;
import com.littlecode.parsers.ObjectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NotifyDispatcherLogging {
    private final AppConfig appConfig;

    public NotifyDispatcherLogging(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.Logging;
    }

    public void dispatcher(NotifyForwarder forwarder, NotifyEventItem eventItem) {
        log.info(ObjectUtil.toString(eventItem));
    }

}
