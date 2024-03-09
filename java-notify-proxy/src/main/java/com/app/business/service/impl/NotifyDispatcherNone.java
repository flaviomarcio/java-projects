package com.app.business.service.impl;

import com.app.business.interfaces.NotifyDispatcherInterface;
import com.app.business.model.ofservice.NotifyEventItem;
import com.app.business.model.ofservice.NotifyForwarder;
import com.littlecode.parsers.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyDispatcherNone implements NotifyDispatcherInterface {

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.None;
    }

    @Override
    public void dispatcher(NotifyForwarder setting, NotifyEventItem eventItem) {
        log.info(ObjectUtil.toString(eventItem));
    }
}
