package com.app.business.service;

import com.app.business.dto.NotifyGroupIn;
import com.app.business.model.ofservice.NotifyGroup;
import com.app.business.repository.ofservice.NotifyGroupRepository;
import com.littlecode.containers.ObjectReturn;
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
public class CRUDGroupService extends CRUDBase<NotifyGroup, NotifyGroupIn> {
    private final NotifyGroupRepository notifyGroupRepository;

    public NotifyGroupIn inFrom(NotifyGroup in) {
        return NotifyGroupIn
                .builder()
                .id(in.getId())
                .dtCreate(in.getDtCreate())
                .dtChange(in.getDtCreate())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
    }

    @Transactional
    public ObjectReturn saveIn(NotifyGroupIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(NotifyGroupIn.class);

        var notifyGroup =
                (in.getId() == null)
                        ? null
                        : notifyGroupRepository.findById(in.getId()).orElse(null);

        notifyGroup = NotifyGroup
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(notifyGroup == null ? LocalDateTime.now() : notifyGroup.getDtCreate())
                .dtChange(LocalDateTime.now())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
        notifyGroupRepository.save(notifyGroup);
        in.setId(notifyGroup.getId());
        in.setDtCreate(notifyGroup.getDtCreate());
        in.setDtChange(notifyGroup.getDtChange());
        return ObjectReturn.of(in);
    }

    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var notifyGroup = notifyGroupRepository.findById(id).orElse(null);
        if (notifyGroup == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);

        notifyGroup.setDtChange(LocalDateTime.now());
        notifyGroup.setEnabled(false);
        notifyGroupRepository.save(notifyGroup);
        return ObjectReturn.of(notifyGroup);
    }

    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var notifyGroup = notifyGroupRepository.findById(id).orElse(null);
        if (notifyGroup == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(notifyGroup));
    }

    public ObjectReturn list() {
        List<NotifyGroupIn> notifyInList = new ArrayList<>();
        notifyGroupRepository.findAll()
                .forEach(notifyTarget -> {
                    notifyInList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(notifyInList);
    }

}
