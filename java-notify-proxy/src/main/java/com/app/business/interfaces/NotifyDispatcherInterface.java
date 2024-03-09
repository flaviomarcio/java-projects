package com.app.business.interfaces;

import com.app.business.model.ofservice.NotifyEventItem;
import com.app.business.model.ofservice.NotifyForwarder;

public interface NotifyDispatcherInterface {

    NotifyForwarder.Dispatcher dispatcher();

    void dispatcher(NotifyForwarder setting, NotifyEventItem eventItem);

}
