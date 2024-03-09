package com.app.business.service.impl;

import com.app.business.model.ofservice.NotifyForwarder;

public class NotifyDispatcherEMail extends NotifyDispatcherNone {

    @Override
    public NotifyForwarder.Dispatcher dispatcher() {
        return NotifyForwarder.Dispatcher.eMail;
    }

}
