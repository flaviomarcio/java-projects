package com.app.business.service;

import com.app.business.dto.NotifyProducerIn;
import com.app.business.model.ofservice.NotifyProducer;
import com.app.business.repository.ofservice.NotifyProducerRepository;
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
public class CRUDProducerService extends CRUDBase<NotifyProducer, NotifyProducerIn> {
    private final NotifyProducerRepository notifyProducerRepository;

    public NotifyProducerIn inFrom(NotifyProducer in) {
        return NotifyProducerIn
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
    public ObjectReturn saveIn(NotifyProducerIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(NotifyProducerIn.class);

        var notifyGroup =
                (in.getId() == null)
                        ? null
                        : notifyProducerRepository.findById(in.getId()).orElse(null);

        notifyGroup = NotifyProducer
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(notifyGroup == null ? LocalDateTime.now() : notifyGroup.getDtCreate())
                .dtChange(LocalDateTime.now())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
        notifyProducerRepository.save(notifyGroup);
        in.setId(notifyGroup.getId());
        in.setDtCreate(notifyGroup.getDtCreate());
        in.setDtChange(notifyGroup.getDtChange());
        return ObjectReturn.of(in);
    }

    @Override
    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var notifyGroup = notifyProducerRepository.findById(id).orElse(null);
        if (notifyGroup == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);

        if (!notifyGroup.isEnabled())
            return ObjectReturn.of(notifyGroup);

        notifyGroup.setDtChange(LocalDateTime.now());
        notifyGroup.setEnabled(false);
        notifyProducerRepository.save(notifyGroup);
        return ObjectReturn.of(notifyGroup);
    }

    @Override
    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn
                    .BadRequest("Invalid id");
        var notifyProducer = notifyProducerRepository.findById(id).orElse(null);
        if (notifyProducer == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(notifyProducer));
    }

    @Override
    public ObjectReturn list() {
        List<NotifyProducerIn> notifyInList = new ArrayList<>();
        notifyProducerRepository.findAll()
                .forEach(notifyTarget -> {
                    notifyInList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(notifyInList);
    }

}
