package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.domain.Action;
import com.app.business.dto.Command;
import com.app.business.dto.Target;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.mq.MQ;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngesterService {
    private final MQ mq;
    private final AppConfig appConfig;
    private final CommanderService commanderService;

    public ObjectReturn select(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest(UUID.class);
        return ObjectReturn.of(commanderService.get(id));
    }

    public ObjectReturn insert(Command in) {
        if (in == null)
            return ObjectReturn.BadRequest(Command.class);
        in.setCommand(Action.INSERT);
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn update(Command in) {
        if (in == null)
            return ObjectReturn.BadRequest(Command.class);
        in.setCommand(Action.INSERT);
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn delete(Command in) {
        if (in == null)
            return ObjectReturn.BadRequest(Command.class);
        in.setCommand(Action.DELETE);
        return this.callBack(this.proxy(in));
    }

    public void register(MQ.Message.Task task) {
        if (task == null) {
            log.error("%{}, task is null", MQ.Message.Task.class);
            return;
        }
        log.info("msg:[{}], type:[{}]", task.getMessageId(), task.getTypeName());
        if (!task.canType(Command.class)) {
            log.error("[{}]: incompatible type: [{}]==>[{}]", task.getClass(), task.getTypeName(), Command.class);
            return;
        }

        var in = task.asObject(Command.class);
        if (in == null) {
            log.error("[{}]:, invalid cast: [{}]==>[{}]", task.getClass(), task.getTypeName(), Command.class);
            return;
        }

        this.callBack(this.proxy(in));
    }

    public ObjectReturn callBack(ObjectReturn objectReturn){
        if(!appConfig.isCallBackEnabled())
            return objectReturn;
        mq.setting().setClientId(appConfig.getCallBackClientId());
        mq.setting().setClientSecret(appConfig.getCallBackClientSecret());
        mq.setting().setAutoCreate(appConfig.isCallBackQueueAutoCreate());
        mq
                .queueName(appConfig.getCallBackQueue())
                .dispatcher(objectReturn);
        return objectReturn;
    }

    private ObjectReturn proxy(Command in) {
        if (in == null)
            return ObjectReturn.BadRequest(Command.class);

        if (in.getTarget() == null)
            return ObjectReturn.BadRequest(Target.class);

        var target=in.getTarget();

        return switch (in.getCommand()) {
            case INSERT, UPDATE, UPSERT -> commanderService.saveIn(target);
            case DELETE -> commanderService.delete(target);
            default -> ObjectReturn.BadRequest("Invalid proxy command: %s, object: %s",in.getCommand(), target.getClass());
        };
    }


}
