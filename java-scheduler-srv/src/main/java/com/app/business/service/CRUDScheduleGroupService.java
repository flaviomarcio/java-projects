package com.app.business.service;

import com.app.business.model.ScheduleGroup;
import com.app.business.repository.ScheduleGroupRepository;
import com.littlecode.containers.ObjectReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CRUDScheduleGroupService {
    private final ScheduleGroupRepository scheduleGroupRepository;

    public ObjectReturn get(UUID id) {
        return ObjectReturn.of(scheduleGroupRepository.findById(id).orElse(null));
    }

    public ObjectReturn save(ScheduleGroup scheduleItem) {
        scheduleGroupRepository.save(scheduleItem);
        return ObjectReturn.of(scheduleItem);
    }

    public ObjectReturn list() {
        return ObjectReturn.of(scheduleGroupRepository.findAll());
    }
}
