package com.app.factory;

import com.app.business.config.AppConfig;
import com.app.business.model.ofservice.*;
import com.app.business.repository.ofservice.*;
import com.app.business.service.CommanderService;
import com.app.business.service.IngesterService;
import com.littlecode.mq.MQ;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private final List<CommanderModel> commanderModelList = new ArrayList<>();
    private final AppConfig appConfig=new AppConfig();
    private Environment mockEnvironment;
    private CommandRepository commandRepository;
    private CommanderService commanderService;
    private IngesterService ingesterService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
        setupCommanderModel();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.commandRepository = Mockito.mock(CommandRepository.class);
        this.commanderService = new CommanderService(commandRepository);
        this.ingesterService = new IngesterService(Mockito.mock(MQ.class), appConfig,commanderService);
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

    private void setupCommanderModel() {
        commanderModelList.clear();
        for (int i = 0; i < 100; i++) {
            commanderModelList.add(
                    CommanderModel
                            .builder()
                            .id(UUID.randomUUID())
                            .dtCreate(LocalDateTime.now())
                            .dtChange(LocalDateTime.now())
                            .name("record")
                            .description("description of record")
                            .enabled(true)
                            .build()
            );
        }


        commanderModelList.forEach(model -> {
            Mockito.when(commandRepository.findById(model.getId())).thenReturn(Optional.of(model));
            Mockito.when(commandRepository.existsById(model.getId())).thenReturn(true);
            Mockito.when(commandRepository.save(model)).thenReturn(model);
        });
        Mockito.when(commandRepository.findAll()).thenReturn(commanderModelList);
    }

}
