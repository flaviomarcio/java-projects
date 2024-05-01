package com.app.business.service;

import com.app.business.dto.ScheduleItemCheckPointIn;
import com.app.business.model.ScheduleItemCheckPoint;
import com.app.business.repository.ScheduleItemCheckPointRepository;
import com.app.business.repository.ScheduleItemRepository;
import com.littlecode.containers.ObjectReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService {
    private final ScheduleItemRepository scheduleItemRepository;
    private final ScheduleItemCheckPointRepository scheduleItemCheckPointRepository;

    public ObjectReturn exec(UUID uuid) {
        return ObjectReturn.Empty();
    }

    public ObjectReturn updateCheckPoint(ScheduleItemCheckPointIn in) {
        if(in==null)
            return ObjectReturn.BadRequest("[ScheduleItemCheckPointIn in] is null");
        var scheduleItem=scheduleItemRepository.findById(in.getId()).orElse(null);
        if(scheduleItem==null)
            return ObjectReturn.NotFound("Invalid %s",in.getId());


        var itemCheckPoint=scheduleItemCheckPointRepository
                .findById(scheduleItem.getId())
                .orElse(
                        ScheduleItemCheckPoint
                                .builder()
                                .id(scheduleItem.getId())
                                .createdAt(LocalDateTime.now())
                                .build()
                );
        itemCheckPoint.setUpdatedAt(LocalDateTime.now());
        itemCheckPoint.setValue(in.getValue());
        scheduleItemCheckPointRepository.save(itemCheckPoint);
        return ObjectReturn.of(itemCheckPoint);
    }

    public void start() {

    }
}
