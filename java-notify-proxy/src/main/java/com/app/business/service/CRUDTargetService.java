package com.app.business.service;

import com.app.business.dto.NotifyTargetIn;
import com.app.business.model.ofservice.NotifyForwarder;
import com.app.business.model.ofservice.NotifyTarget;
import com.app.business.model.ofservice.NotifyTargetRev;
import com.app.business.repository.ofservice.NotifyForwarderRepository;
import com.app.business.repository.ofservice.NotifyGroupRepository;
import com.app.business.repository.ofservice.NotifyTargetRepository;
import com.app.business.repository.ofservice.NotifyTargetRevRepository;
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
public class CRUDTargetService extends CRUDBase<NotifyTarget, NotifyTargetIn> {
    private final NotifyGroupRepository notifyGroupRepository;
    private final NotifyTargetRevRepository notifyTargetRevRepository;
    private final NotifyTargetRepository notifyTargetRepository;
    private final NotifyForwarderRepository notifyDispatcherRepository;


    @Override
    public NotifyTargetIn inFrom(NotifyTarget notifyTarget) {
        return NotifyTargetIn
                .builder()
                .id(notifyTarget.getTargetRev().getId())
                .dtCreate(notifyTarget.getDtCreate())
                .dtChange(notifyTarget.getDtCreate())
                .groupId(notifyTarget.getGroup().getId())
                .name(notifyTarget.getName())
                .description(notifyTarget.getDescription())
                .enabled(notifyTarget.isEnabled())
                .build();
    }

    @Transactional
    public ObjectReturn saveIn(NotifyTargetIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(NotifyForwarder.class);

        if (!notifyGroupRepository.existsById(in.getGroupId()))
            return ObjectReturn.BadRequest("Invalid target.groupId: {}", in.getGroupId());

        var notifyTargetRev =
                (in.getId() == null)
                        ? null
                        : notifyTargetRevRepository.findById(in.getId()).orElse(null);

        if (notifyTargetRev == null)
            notifyTargetRev = NotifyTargetRev
                    .builder()
                    .id(UUID.randomUUID())
                    .dt(LocalDateTime.now())
                    .rev(null)
                    .enabled(true)
                    .build();

        notifyTargetRev.setRev(UUID.randomUUID());

        var notifyTarget = NotifyTarget
                .builder()
                .id(notifyTargetRev.getRev())
                .dtCreate(notifyTargetRev.getDt())
                .dtChange(LocalDateTime.now())
                .targetRev(notifyTargetRev)
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();

        List<NotifyForwarder> notifySettingList = new ArrayList<>();
        for (var setting : in.getDispatchers()) {
            notifySettingList.add(
                    NotifyForwarder
                            .builder()
                            .id(UUID.randomUUID())
                            .dt(notifyTarget.getDtCreate())
                            .targetRev(notifyTargetRev)
                            .enabled(setting.isEnabled())
                            .build()
            );
        }

        notifyTargetRevRepository.save(notifyTargetRev);
        notifyTargetRepository.save(notifyTarget);
        notifyDispatcherRepository.saveAll(notifySettingList);
        in.setId(notifyTarget.getTargetRev().getId());
        in.setDtChange(notifyTarget.getDtCreate());
        return ObjectReturn.of(in);
    }

    @Override
    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var notifyRev = notifyTargetRevRepository.findById(id).orElse(null);
        if (notifyRev == null)
            return ObjectReturn.NoContent("Invalid id: {}", id);

        notifyRev.setRev(null);
        notifyRev.setEnabled(false);
        notifyTargetRevRepository.save(notifyRev);
        return ObjectReturn.Empty();
    }

    @Override
    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");
        var notifyTargetRev = notifyTargetRevRepository.findById(id).orElse(null);
        if (notifyTargetRev == null)
            return ObjectReturn.BadRequest("Invalid id: %s", id);

        var notifyTarget = notifyTargetRev.getRev() == null
                ? null
                : notifyTargetRepository.findById(notifyTargetRev.getRev())
                .orElse(null);
        if (notifyTarget == null)
            return ObjectReturn.NotFound("Invalid id %s", id);

        var out = inFrom(notifyTarget);

        out.setDispatchers(new ArrayList<>());
        notifyDispatcherRepository
                .findByTargetRev(notifyTargetRev)
                .forEach(notifyDispatcher -> {
                    out.getDispatchers()
                            .add(
                                    NotifyTargetIn.Dispatcher
                                            .builder()
                                            .name(notifyDispatcher.getName())
                                            .enabled(notifyDispatcher.isEnabled())
                                            .dispatcher(notifyDispatcher.getDispatcher())
                                            .build()
                            );
                });

        return ObjectReturn.of(out);
    }

    @Override
    public ObjectReturn list() {
        List<NotifyTargetIn> notifyInList = new ArrayList<>();
        notifyTargetRepository.findAll()
                .forEach(notifyTarget -> {
                    notifyInList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(notifyInList);
    }

    public ObjectReturn dispatchers() {
        return ObjectReturn.of(List.of(NotifyForwarder.Dispatcher.values()));
    }

}
