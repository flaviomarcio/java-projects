package com.app.business.service;

import com.app.business.model.ofservice.NotifyEvent;
import com.littlecode.containers.ObjectReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventQueueService {


    public ObjectReturn send(NotifyEvent event) {
        return ObjectReturn.Empty();
    }

}
