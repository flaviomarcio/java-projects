package com.app.business.service;

import com.app.business.dto.NotifyEventIn;
import com.littlecode.mq.MQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventConsumerService {
    private final EventIngesterService notifyEventIngesterService;


    public void register(MQ.Message.Task task) {
        if (task == null) {
            log.error("%{}, task is null", MQ.Message.Task.class);
            return;
        }
        log.info("msg:[{}], type:[{}]", task.getMessageId(), task.getTypeName());
        if (!task.canType(NotifyEventIn.class)) {
            log.error("[{}]: incompatible type: [{}]==>[{}]", task.getClass(), task.getTypeName(), NotifyEventIn.class);
            return;
        }

        var notifyEventIn = task.asObject(NotifyEventIn.class);
        if (notifyEventIn == null) {
            log.error("[{}]:, invalid cast: [{}]==>[{}]", task.getClass(), task.getTypeName(), NotifyEventIn.class);
            return;
        }

        notifyEventIngesterService.register(notifyEventIn);
    }

}
