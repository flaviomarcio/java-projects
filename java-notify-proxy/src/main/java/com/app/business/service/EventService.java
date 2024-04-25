package com.app.business.service;

import com.app.business.dto.NotifyContainer;
import com.app.business.dto.NotifyEventIn;
import com.app.business.model.ofservice.NotifyEvent;
import com.app.business.model.ofservice.NotifyEventItem;
import com.app.business.model.ofservice.NotifyForwarder;
import com.app.business.repository.ofservice.*;
import com.app.business.service.impl.*;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.ObjectUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final NotifyProducerRepository notifyProducerRepository;
    private final NotifyTargetRevRepository notifyTargetRevRepository;
    private final NotifyTargetRepository notifyTargetRepository;
    private final NotifyForwarderRepository notifyForwarderRepository;
    private final NotifyEventItemRepository notifyEventItemRepository;
    private final NotifyEventRepository notifyEventRepository;
    private final Map<NotifyForwarder.Dispatcher, Class<?>> interfaces = makeInterfaces();

    private static Map<NotifyForwarder.Dispatcher, Class<?>> makeInterfaces() {
        return Map.of(
                NotifyForwarder.Dispatcher.Logging, NotifyDispatcherLogging.class,
                NotifyForwarder.Dispatcher.eMail, NotifyDispatcherEMail.class,
                NotifyForwarder.Dispatcher.GoogleChat, NotifyDispatcherGoogleChat.class,
                NotifyForwarder.Dispatcher.Slack, NotifyDispatcherSlack.class,
                NotifyForwarder.Dispatcher.Teams, NotifyDispatcherTeams.class,
                NotifyForwarder.Dispatcher.Telegram, NotifyDispatcherTelegram.class,
                NotifyForwarder.Dispatcher.SMSTallos, NotifyDispatcherTallosSMS.class,
                NotifyForwarder.Dispatcher.SMSZenvia, NotifyDispatcherZenviaSMS.class,
                NotifyForwarder.Dispatcher.CallBack, NotifyDispatcherCallBack.class
        );

    }

    @Transactional
    public ObjectReturn register(NotifyEventIn event) {
        try {
            return internalRegister(event);
        } catch (Exception e) {
            return ObjectReturn.of(e);
        }
    }

    public NotifyContainer containerByTargetId(@NotNull UUID id) {
        if (id == null)
            return null;
        var notifyTargetRev = notifyTargetRevRepository.findById(id).orElse(null);

        if (notifyTargetRev == null)
            return null;

        var notifyTarget = notifyTargetRepository.findById(notifyTargetRev.getRev()).orElse(null);
        if (notifyTarget == null)
            return null;

        var forwarders = notifyForwarderRepository.findByTargetRev(notifyTargetRev);

        return NotifyContainer
                .builder()
                .target(notifyTarget)
                .forwarders(forwarders)
                .build();
    }


    private ObjectReturn findProducer(NotifyEventIn.Producer producerIn) {
        if (producerIn == null)
            return ObjectReturn
                    .BadRequest(NotifyEventIn.Producer.class);

        var producer = notifyProducerRepository.findById(producerIn.getId()).orElse(null);
        if (producer == null)
            return ObjectReturn.NoContent("Invalid producer: %s", producerIn.getId());

        if (!producer.getAccessKey().equals(producerIn.getAccessKey()))
            return ObjectReturn
                    .Conflict("Invalid producer: %s", producerIn.getId());

        return ObjectReturn.of(producer);
    }

    private ObjectReturn findTargetContainer(NotifyEventIn.Target targetIn) {
        if (targetIn == null || targetIn.getTargetId() == null)
            return ObjectReturn.Empty();

        var notifyContainer = this.containerByTargetId(targetIn.getTargetId());

        if (notifyContainer == null)
            return ObjectReturn.BadRequest("Invalid target: %s", targetIn.getTargetId());

        if (!notifyContainer.getTarget().isEnabled())
            return ObjectReturn.Conflict("Invalid target: %s", targetIn.getTargetId());

        if (notifyContainer.getForwarders().isEmpty())
            return ObjectReturn.Conflict(NotifyForwarder.class);

        return ObjectReturn.of(notifyContainer);
    }

    public List<NotifyForwarder> forwardersDistinct(List<NotifyForwarder> forwarders) {
        List<UUID> un = new ArrayList<>();
        List<NotifyForwarder> __return = new ArrayList<>();
        for (var f : forwarders) {
            if (un.contains(f.getId()))
                continue;
            __return.add(f);
            un.add(f.getId());
        }
        return __return;
    }

    public NotifyForwarder forwarderFindById(UUID id) {
        if (id == null)
            return null;
        return notifyForwarderRepository.findById(id).orElse(null);
    }

    private ObjectReturn createEvent(@NotNull NotifyEventIn eventIn) {
        var targetIn = eventIn.getTarget();
        if (targetIn == null)
            return ObjectReturn.BadRequest("Invalid event.target");

        var producer = this.findProducer(eventIn.getProducer());
        if (producer == null)
            return ObjectReturn.NoContent("Invalid producer: %s", eventIn.getProducer().getId());

        var event = NotifyEvent
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(LocalDateTime.now())
                .producerId(eventIn.getProducer().getId())
                .targetId(targetIn.getTargetId())
                .forwarderId(targetIn.getForwarderId())
                .status(NotifyEvent.Status.WAIT)
                .payload(ObjectUtil.toString(eventIn))
                .eventItems(new ArrayList<>())
                .build();

        var __return = findTargetContainer(targetIn);
        if (!__return.isOK())
            return __return;

        var notifyContainer = __return.cast(NotifyContainer.class);
        if (notifyContainer == null)
            return ObjectReturn.NoContent(NotifyContainer.class);

        List<String> destinations = new ArrayList<>();
        var forwarders = notifyContainer.getForwarders();
        {
            var forwarder = this.forwarderFindById(targetIn.getForwarderId());
            if (forwarder != null)
                forwarders.add(forwarder);
            forwarders = forwardersDistinct(forwarders);


            {
                List<String> destinationList = new ArrayList<>(eventIn.getTarget().getDestinations());
                if (notifyContainer.getTarget() != null) {
                    for (var targetItem : notifyContainer.getTarget().getItems()) {
                        if (targetItem == null)
                            continue;
                        destinationList.add(targetItem.getDestination());
                    }
                }

                for (var destination : destinationList) {
                    destination = destination.trim();
                    if (destination.isEmpty())
                        continue;
                    var contactSrc = destination.replace(" ", "");
                    var contactAr = contactSrc.split(",");
                    for (var contact : contactAr) {
                        contact = contact.trim().toLowerCase();
                        if (!destinations.contains(contact))
                            destinations.add(contact);
                    }
                }
            }
        }

        if (forwarders.isEmpty() || destinations.isEmpty())
            return ObjectReturn.of(event);

        for (var forwarder : forwarders) {
            for (var destination : destinations) {
                var eventItem = NotifyEventItem
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .event(event)
                        .forwarder(forwarder)
                        .destination(destination)
                        .status(NotifyEventItem.Status.WAITING)
                        .attempts(0)
                        .build();
                event.getItems().add(eventItem);
            }
        }
        return ObjectReturn.of(event);
    }


    private ObjectReturn internalRegister(NotifyEventIn eventIn) {
        if (eventIn == null)
            return ObjectReturn.BadRequest(NotifyEventIn.class);

        var __return = this.findProducer(eventIn.getProducer());
        if (!__return.isOK())
            return __return;


        __return = this.createEvent(eventIn);
        if (!__return.isOK())
            return __return;

        var event = __return.cast(NotifyEvent.class);

        notifyEventRepository.save(event);
        return ObjectReturn.of(event);
    }


    private NotifyDispatcherLogging getNotifyDispatcher(NotifyForwarder.Dispatcher dispatcher) {
        if (dispatcher == null)
            return null;
        synchronized (interfaces) {
            var aClass = interfaces.get(dispatcher);
            if (aClass == null)
                return null;
            return ObjectUtil.create(aClass);
        }
    }

    public void dispatcher(UUID eventItemId) {
        if (eventItemId == null)
            return;
        var eventItem = notifyEventItemRepository.findById(eventItemId).orElse(null);
        if (eventItem == null) {
            log.error("Invalid [{}]: [{}]", NotifyEventItem.class, eventItemId);
            return;
        }

        try {

            var notifyForwarder = eventItem.getForwarder();
            if (notifyForwarder == null) {
                eventItem.setStatus(NotifyEventItem.Status.CONFLICT);
                return;
            }

            if (notifyForwarder.getDispatcher().equals(NotifyForwarder.Dispatcher.Logging)) {
                eventItem.setStatus(NotifyEventItem.Status.SKIPPED);
                return;
            }

            NotifyDispatcherLogging notifyDispatcher = getNotifyDispatcher(notifyForwarder.getDispatcher());

            if (notifyDispatcher == null) {
                eventItem.setStatus(NotifyEventItem.Status.CONFLICT);
                return;
            }

            notifyDispatcher.dispatcher(notifyForwarder, eventItem);

            eventItem.setStatus(NotifyEventItem.Status.SENT);
        } finally {
            notifyEventItemRepository.save(eventItem);
        }
    }

}
