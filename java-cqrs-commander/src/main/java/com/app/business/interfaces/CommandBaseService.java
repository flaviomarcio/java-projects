package com.app.business.interfaces;

import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public interface CommandBaseService<MODEL, DTO> {

    DTO outFrom(MODEL in);

    ObjectReturn saveIn(DTO in);

    ObjectReturn delete(String id);

    ObjectReturn delete(DTO in);

    ObjectReturn delete(UUID id);

    ObjectReturn get(UUID id);

}
