package com.app.business.service;

import com.app.business.dto.ScheduleGroupIn;
import com.app.business.model.ScheduleGroup;
import com.app.business.repository.ScheduleGroupRepository;
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
public class CRUDScheduleGroupService extends CrudServiceTemplate<ScheduleGroup, ScheduleGroupIn> {
    private final ScheduleGroupRepository scheduleGroupRepository;

    public ScheduleGroupIn inFrom(ScheduleGroup in) {
        return ScheduleGroupIn
                .builder()
                .id(in.getId())
                .createdAt(in.getCreatedAt())
                .updatedAt(in.getUpdatedAt())
                .name(in.getName())
                .enabled(in.isEnabled())
                .build();
    }

    @Transactional
    public ObjectReturn saveIn(ScheduleGroupIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(ScheduleGroupIn.class);

        var scheduleGroup =
                (in.getId() == null)
                        ? null
                        : scheduleGroupRepository.findById(in.getId()).orElse(null);

        scheduleGroup = ScheduleGroup
                .builder()
                .id(UUID.randomUUID())
                .createdAt(scheduleGroup == null ? LocalDateTime.now() : scheduleGroup.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .name(in.getName())
                .enabled(in.isEnabled())
                .build();
        scheduleGroupRepository.save(scheduleGroup);
        in.setId(scheduleGroup.getId());
        in.setCreatedAt(scheduleGroup.getCreatedAt());
        in.setUpdatedAt(scheduleGroup.getUpdatedAt());
        return ObjectReturn.of(in);
    }

    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var scheduleGroup = scheduleGroupRepository.findById(id).orElse(null);
        if (scheduleGroup == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);

        scheduleGroup.setUpdatedAt(LocalDateTime.now());
        scheduleGroup.setEnabled(false);
        scheduleGroupRepository.save(scheduleGroup);
        return ObjectReturn.of(scheduleGroup);
    }

    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var ScheduleGroup = scheduleGroupRepository.findById(id).orElse(null);
        if (ScheduleGroup == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(ScheduleGroup));
    }

    public ObjectReturn list() {
        List<ScheduleGroupIn> notifyInList = new ArrayList<>();
        scheduleGroupRepository.findAll()
                .forEach(notifyTarget -> {
                    notifyInList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(notifyInList);
    }
}
