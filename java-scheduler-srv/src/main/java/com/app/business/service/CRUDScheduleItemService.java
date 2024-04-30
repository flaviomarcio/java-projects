package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.model.ScheduleItem;
import com.app.business.repository.ScheduleItemRepository;
import com.littlecode.containers.ObjectReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CRUDScheduleItemService {
    private final ScheduleItemRepository scheduleItemRepository;

    public ObjectReturn get(UUID id) {
        return ObjectReturn.of(scheduleItemRepository.findById(id).orElse(null));
    }

    public ObjectReturn save(ScheduleItem scheduleItem) {
        scheduleItemRepository.save(scheduleItem);
        return ObjectReturn.of(scheduleItem);
    }

    public ObjectReturn list() {
        return ObjectReturn.of(scheduleItemRepository.findAll());
    }
}
