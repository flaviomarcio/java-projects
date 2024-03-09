package com.app.business.service;

import com.app.business.dto.NotifyEventIn;
import com.app.business.model.ofservice.NotifyEvent;
import com.littlecode.containers.ObjectReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventIngesterService {
    private final EventQueueService eventQueueService;
    private final EventService notifyEventService;

    public ObjectReturn register(NotifyEventIn eventIn) {
        var __return = notifyEventService.register(eventIn);
        if (!__return.isOK())
            return __return;
        return eventQueueService.send(__return.cast(NotifyEvent.class));
    }
}
