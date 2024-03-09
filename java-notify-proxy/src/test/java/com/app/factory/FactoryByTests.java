package com.app.factory;

import com.app.business.model.ofservice.*;
import com.app.business.repository.ofservice.*;
import com.app.business.service.*;
import com.littlecode.containers.ObjectReturn;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private final List<NotifyProducer> notifyProducerList = new ArrayList<>();
    private final List<NotifyGroup> notifyGroupList = new ArrayList<>();
    private final List<NotifyTarget> notifyTargetList = new ArrayList<>();
    private Environment mockEnvironment;
    private NotifyGroupRepository notifyGroupRepository;
    private NotifyTargetRevRepository notifyTargetRevRepository;
    private NotifyTargetRepository notifyTargetRepository;
    private NotifyForwarderRepository notifyDispatcherRepository;
    private NotifyProducerRepository notifyProducerRepository;
    private NotifyEventItemRepository notifyEventItemRepository;
    private NotifyEventRepository notifyEventRepository;

    private EventQueueService eventQueueService;
    private CRUDProducerService notifyProducerCRUD;
    private CRUDGroupService notifyGroupCRUD;
    private CRUDTargetService notifyTargetCRUD;
    private EventService notifyEventService;
    private EventIngesterService notifyEventIngesterService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
        makeMockQueueService();
        setupNotifyProducer();
        setupNotifyGroup();
        setupNotifyTarget();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.notifyGroupRepository = Mockito.mock(NotifyGroupRepository.class);
        this.notifyTargetRevRepository = Mockito.mock(NotifyTargetRevRepository.class);
        this.notifyTargetRepository = Mockito.mock(NotifyTargetRepository.class);
        this.notifyDispatcherRepository = Mockito.mock(NotifyForwarderRepository.class);
        this.notifyProducerRepository = Mockito.mock(NotifyProducerRepository.class);
        this.notifyEventItemRepository = Mockito.mock(NotifyEventItemRepository.class);
        this.notifyEventRepository = Mockito.mock(NotifyEventRepository.class);

        this.eventQueueService = Mockito.mock(EventQueueService.class);
        this.notifyProducerCRUD = new CRUDProducerService(notifyProducerRepository);
        this.notifyGroupCRUD = new CRUDGroupService(notifyGroupRepository);
        this.notifyTargetCRUD = new CRUDTargetService(notifyGroupRepository, notifyTargetRevRepository, notifyTargetRepository, notifyDispatcherRepository);
        this.notifyEventService = new EventService(notifyProducerRepository, notifyTargetRevRepository, notifyTargetRepository, notifyDispatcherRepository, notifyEventItemRepository, notifyEventRepository);
        this.notifyEventIngesterService = new EventIngesterService(eventQueueService, notifyEventService);
    }

    private void makeMockEnvironment() {
        List<Map<String, String>> envList = List.of(
//                Map.of(
//                        "dispatcher.international.code", "55",
//                        "dispatcher.cleanup.days", "7",
//                        "dispatcher.strategy.whatsapp", "NONE"
//                ),
        );

        //noinspection RedundantOperationOnEmptyContainer
        envList.forEach(envMap -> {
            envMap.forEach((key, value) ->
                    {
                        Mockito.when(mockEnvironment.getProperty(key)).thenReturn(value);
                        Mockito.when(mockEnvironment.getProperty(key, "")).thenReturn(value);
                    }
            );
        });
    }

    private void makeMockQueueService() {
        Mockito.when(this.eventQueueService.send(Mockito.any())).thenReturn(ObjectReturn.Empty());
    }

    private void setupNotifyProducer() {
        notifyProducerList.clear();
        notifyProducerList.add(
                NotifyProducer
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .name("record")
                        .description("description of record")
                        .enabled(true)
                        .build()
        );

        notifyProducerList.forEach(notifyProducer -> {
            Mockito.when(notifyProducerRepository.findById(notifyProducer.getId())).thenReturn(Optional.of(notifyProducer));
            Mockito.when(notifyProducerRepository.existsById(notifyProducer.getId())).thenReturn(true);
            Mockito.when(notifyProducerRepository.save(notifyProducer)).thenReturn(notifyProducer);
        });
        Mockito.when(notifyProducerRepository.findAll()).thenReturn(notifyProducerList);
    }

    private void setupNotifyGroup() {
        notifyGroupList.clear();
        notifyGroupList.add(
                NotifyGroup
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .name("record")
                        .description("description of record")
                        .enabled(true)
                        .build()
        );

        notifyGroupList.forEach(notifyGroup -> {
            Mockito.when(notifyGroupRepository.findById(notifyGroup.getId())).thenReturn(Optional.of(notifyGroup));
            Mockito.when(notifyGroupRepository.existsById(notifyGroup.getId())).thenReturn(true);
            Mockito.when(notifyGroupRepository.save(notifyGroup)).thenReturn(notifyGroup);
        });
        Mockito.when(notifyProducerRepository.findAll()).thenReturn(notifyProducerList);
    }

    private void setupNotifyTarget() {
        if (notifyGroupList.isEmpty())
            throw new RuntimeException("notifyGroupList is empty ");

        for (var notifyGroup : notifyGroupList) {
            List.of(UUID.randomUUID())
                    .forEach(rev ->
                            {
                                var notifyTargetRev = NotifyTargetRev
                                        .builder()
                                        .id(rev)
                                        .dt(LocalDateTime.now())
                                        .rev(rev)
                                        .enabled(true)
                                        .build();
                                var notifyTarget = NotifyTarget
                                        .builder()
                                        .id(notifyTargetRev.getRev())
                                        .dtCreate(notifyTargetRev.getDt())
                                        .targetRev(notifyTargetRev)
                                        .group(notifyGroup)
                                        .name(String.format("name: %s", notifyTargetRev.getRev()))
                                        .description(String.format("description: %s", notifyTargetRev.getRev()))
                                        .enabled(true)
                                        .build();
                                notifyTargetList.add(notifyTarget);
                                Mockito.when(notifyTargetRevRepository.findById(notifyTargetRev.getId())).thenReturn(Optional.of(notifyTargetRev));
                                Mockito.when(notifyTargetRepository.findById(notifyTarget.getId())).thenReturn(Optional.of(notifyTarget));
                                Mockito.when(notifyTargetRepository.save(notifyTarget)).thenReturn(notifyTarget);
                            }
                    );
        }
        Mockito.when(notifyTargetRepository.findAll()).thenReturn(notifyTargetList);

        List<NotifyForwarder> notifyDispatcherList = new ArrayList<>();
        for (var notifyTarget : notifyTargetList) {
            List<NotifyForwarder> list = new ArrayList<>();
            for (var e : NotifyForwarder.Dispatcher.values()) {
                var id = UUID.randomUUID();
                var notifyDispatcher = NotifyForwarder
                        .builder()
                        .id(id)
                        .dt(notifyTarget.getDtCreate())
                        .targetRev(notifyTarget.getTargetRev())
                        .dispatcher(e)
                        .name(String.format("name: %s", id))
                        .enabled(true)
                        .build();
                list.add(notifyDispatcher);
                notifyDispatcherList.add(notifyDispatcher);
            }
            Mockito.when(notifyDispatcherRepository.findByTargetRev(notifyTarget.getTargetRev())).thenReturn(list);
        }
        Mockito.when(notifyDispatcherRepository.findAll()).thenReturn(notifyDispatcherList);
    }

}
