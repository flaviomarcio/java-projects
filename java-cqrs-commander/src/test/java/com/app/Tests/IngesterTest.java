package com.app.Tests;

import com.app.business.dto.Command;
import com.app.business.dto.Target;
import com.app.business.model.ofservice.CommanderModel;
import com.app.business.service.CommanderService;
import com.app.business.service.IngesterService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class IngesterTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CommanderService commanderService = FACTORY.getCommanderService();
    private final IngesterService ingesterService = FACTORY.getIngesterService();
    private final List<CommanderModel> CRUDList = FACTORY.getCommanderModelList();

    @Test
    public void UT_000_CHECK_COMMAND() {

        for (var model : CRUDList) {
            var target = commanderService.get(model.getId()).cast(Target.class);
            var in = Command
                    .builder()
                    .messageId(UUID.randomUUID())
                    .target(target)
                    .callBack(false)
                    .build();

            Assertions.assertDoesNotThrow(() -> ingesterService.select(in.getTarget().getId()));
            Assertions.assertDoesNotThrow(() -> ingesterService.insert(in));
            Assertions.assertDoesNotThrow(() -> ingesterService.update(in));
            Assertions.assertDoesNotThrow(() -> ingesterService.delete(in));

            Assertions.assertTrue(ingesterService.select(in.getTarget().getId()).isOK());
            Assertions.assertTrue(ingesterService.insert(in).isOK());
            Assertions.assertTrue(ingesterService.update(in).isOK());
            Assertions.assertTrue(ingesterService.delete(in).isOK());
        }
    }

}
