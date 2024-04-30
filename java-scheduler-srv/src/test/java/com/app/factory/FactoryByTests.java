package com.app.factory;

import com.app.business.config.AppConfig;
import com.app.business.model.ScheduleGroup;
import com.app.business.model.ScheduleItem;
import com.app.business.model.ScheduleItemAction;
import com.app.business.model.ScheduleItemCheckPoint;
import com.app.business.repository.ScheduleGroupRepository;
import com.app.business.repository.ScheduleItemActionsRepository;
import com.app.business.repository.ScheduleItemCheckPointRepository;
import com.app.business.repository.ScheduleItemRepository;
import com.app.business.service.AgentService;
import com.app.business.service.CRUDScheduleGroupService;
import com.app.business.service.CRUDScheduleItemService;
import com.app.business.service.RunnerService;
import com.littlecode.mq.MQ;
import com.littlecode.parsers.ObjectUtil;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private Environment mockEnvironment;
    private CRUDScheduleGroupService crudScheduleGroupService;
    private CRUDScheduleItemService crudScheduleItemService;
    private AgentService agentService;
    private RunnerService runnerService;
    private AppConfig appConfig;
    private MQ mq;

    private final List<ScheduleGroup> scheduleGroupList=new ArrayList<>();
    private final List<ScheduleItem> scheduleItemList=new ArrayList<>();
    private final List<ScheduleItemAction> scheduleItemActionList=new ArrayList<>();
    private final List<ScheduleItemCheckPoint> scheduleItemCheckPointList=new ArrayList<>();

    private ScheduleGroupRepository scheduleGroupRepository;
    private ScheduleItemRepository scheduleItemRepository;
    private ScheduleItemActionsRepository scheduleItemActionsRepository;
    private ScheduleItemCheckPointRepository scheduleItemCheckPointRepository;

    public FactoryByTests() {
        this.mockClasses();
        this.mockMockEnvironment();
        this.mockScheduleGroup();
        this.mockScheduleItem();
    }

    private void mockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.mq = Mockito.mock(MQ.class);
        Mockito.when(mq.dispatcher(Mockito.any(Object.class))).thenReturn(null);

        this.scheduleGroupRepository=Mockito.mock(ScheduleGroupRepository.class);
        this.scheduleItemRepository=Mockito.mock(ScheduleItemRepository.class);
        this.scheduleItemActionsRepository=Mockito.mock(ScheduleItemActionsRepository.class);
        this.scheduleItemCheckPointRepository=Mockito.mock(ScheduleItemCheckPointRepository.class);

        this.appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.isEnabled()).thenReturn(true);

        this.crudScheduleGroupService=new CRUDScheduleGroupService(this.scheduleGroupRepository);
        this.crudScheduleItemService=new CRUDScheduleItemService(this.scheduleItemRepository);

        this.agentService=new AgentService(scheduleItemRepository, scheduleItemCheckPointRepository);
        this.runnerService=new RunnerService();
    }

    private void mockMockEnvironment() {
        List<Map<String, String>> envList = List.of(
                Map.of(
                        "app.proxy.state.sent", "Sent",
                        "app.proxy.enabled", "true"
                )
        );

        envList.forEach(envMap -> {
            envMap.forEach((key, value) ->
                    {
                        Mockito.when(mockEnvironment.getProperty(key)).thenReturn(value);
                        Mockito.when(mockEnvironment.getProperty(key, "")).thenReturn(value);
                    }
            );
        });
    }

    private void mockScheduleGroup(){
        this.scheduleGroupList.clear();
        this.scheduleItemList.clear();
        this.scheduleItemActionList.clear();
        this.scheduleItemCheckPointList.clear();

        this.scheduleGroupList
                .add(
                        ScheduleGroup
                                .builder()
                                .id(UUID.randomUUID())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .name("default")
                                .enabled(true)
                                .items(new ArrayList<>())
                                .build()
                );
        this.scheduleGroupList
                .forEach(
                        scheduleGroup -> {
                            Mockito.when(scheduleGroupRepository.findById(scheduleGroup.getId())).thenReturn(Optional.of(scheduleGroup));
                        }
                );

        Mockito.when(scheduleGroupRepository.findAll()).thenReturn(this.scheduleGroupList);
        Mockito.when(scheduleGroupRepository.findByEnabled(true)).thenReturn(this.scheduleGroupList);


        //this.scheduleGroupRepository
    }

    private void mockScheduleItem(){

        for(var scheduleGroup:this.scheduleGroupList){
            var actions=List.of(
                    ScheduleItemAction
                            .builder()
                            .id(UUID.randomUUID())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .enabled(true)
                            .executionType(ScheduleItemAction.ExecutionType.PRINT)
                            .headers(ObjectUtil.toString(Map.of("Content-Type","application.json")))
                            .build(),
                    ScheduleItemAction
                            .builder()
                            .id(UUID.randomUUID())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .enabled(true)
                            .executionType(ScheduleItemAction.ExecutionType.PRINT)
                            .headers(ObjectUtil.toString(Map.of("Content-Type","application.json")))
                            .build()
            );
            this.scheduleItemActionList.addAll(actions);

            this.scheduleItemList.addAll(
                    List.of(
                            ScheduleItem
                                    .builder()
                                    .id(UUID.randomUUID())
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .scheduleGroup(scheduleGroup)
                                    .name("default")
                                    .description("default")
                                    .enabled(true)
                                    .pallelism(1)
                                    .actions(actions)
                                    .build()
                    )
            );
        }

        this.scheduleItemActionList
                .forEach(
                        scheduleItemAction -> {
                            Mockito.when(scheduleItemActionsRepository.findById(scheduleItemAction.getId())).thenReturn(Optional.of(scheduleItemAction));
                        }
                );
        Mockito.when(scheduleItemRepository.findAll()).thenReturn(this.scheduleItemList);

        this.scheduleItemList
                .forEach(
                        scheduleItem -> {
                            Mockito.when(scheduleItemRepository.findById(scheduleItem.getId())).thenReturn(Optional.of(scheduleItem));
                        }
                );
        Mockito.when(scheduleItemRepository.findAll()).thenReturn(this.scheduleItemList);
    }


}
