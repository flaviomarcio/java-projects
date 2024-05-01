package com.app.business.service;

import com.app.business.dto.ScheduleItemIn;
import com.app.business.model.ScheduleItem;
import com.app.business.repository.ScheduleItemRepository;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.jpa.crud.CrudServiceTemplate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CRUDScheduleItemService extends CrudServiceTemplate<ScheduleItem, ScheduleItemIn> {
    private final ScheduleItemRepository scheduleItemRepository;

    @Override
    public ScheduleItemIn inFrom(ScheduleItem in) {
        var __return = ScheduleItemIn
                .builder()
                .id(in.getId())
                .createdAt(in.getCreatedAt())
                .updatedAt(in.getUpdatedAt())
                .name(in.getName())
                .enabled(in.isEnabled())
                .pallelism(in.getPallelism())
                .actions(new ArrayList<>())
                .build();
        for (var a : in.getActions()) {
            __return
                    .getActions()
                    .add(
                            ScheduleItemIn.ActionIn
                                    .builder()
                                    .id(UUID.randomUUID())
                                    .createdAt(a.getCreatedAt())
                                    .updatedAt(a.getUpdatedAt())
                                    .executionType(a.getExecutionType())
                                    .headers(a.getHeaders())
                                    .enabled(a.isEnabled())
                                    .build()
                    );
        }
        return __return;
    }

    @Transactional
    @Override
    public ObjectReturn saveIn(ScheduleItemIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(ScheduleItemIn.class);

        var scheduleItem =
                (in.getId() == null)
                        ? null
                        : scheduleItemRepository.findById(in.getId()).orElse(null);

        scheduleItem = ScheduleItem
                .builder()
                .id(UUID.randomUUID())
                .createdAt(scheduleItem == null ? LocalDateTime.now() : scheduleItem.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .name(in.getName())
                .enabled(in.isEnabled())
                .pallelism(in.getPallelism())
                .build();
        scheduleItemRepository.save(scheduleItem);
        in.setId(scheduleItem.getId());
        in.setCreatedAt(scheduleItem.getCreatedAt());
        in.setUpdatedAt(scheduleItem.getUpdatedAt());
        return ObjectReturn.of(in);
    }

    @Override
    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");
        else {
            var scheduleItem = scheduleItemRepository.findById(id).orElse(null);
            if (scheduleItem == null)
                return ObjectReturn.NoContent("Invalid id: %s", id);

            scheduleItem.setUpdatedAt(LocalDateTime.now());
            scheduleItem.setEnabled(false);
            scheduleItemRepository.save(scheduleItem);
            return ObjectReturn.of(scheduleItem);
        }
    }

    @Override
    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var ScheduleItem = scheduleItemRepository.findById(id).orElse(null);
        if (ScheduleItem == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(ScheduleItem));
    }

    @Override
    public ObjectReturn list() {
        List<ScheduleItemIn> inList = new ArrayList<>();
        scheduleItemRepository.findAll()
                .forEach(notifyTarget -> {
                    inList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(inList);
    }
}
