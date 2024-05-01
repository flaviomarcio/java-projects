package com.app.Tests;

import com.app.business.repository.ScheduleGroupRepository;
import com.app.business.service.CRUDScheduleGroupService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CrudScheduleGroupTest {
    private final FactoryByTests factory = new FactoryByTests();
    private final CRUDScheduleGroupService service = factory.getCrudScheduleGroupService();

    @Test
    public void UT_000_CHECK_CONSTRUCTOR() {
        Assertions.assertDoesNotThrow(() -> new CRUDScheduleGroupService(Mockito.mock(ScheduleGroupRepository.class)));
    }

    @Test
    public void UT_000_CHECK_SAVE() {

        factory
                .getInScheduleGroupList()
                .forEach(model -> {

                    Assertions.assertDoesNotThrow(() -> service.saveIn(null));
                    Assertions.assertDoesNotThrow(() -> service.disable((UUID)null));
                    Assertions.assertDoesNotThrow(() -> service.disable(UUID.randomUUID()));
                    Assertions.assertDoesNotThrow(() -> service.disable(model));
                    Assertions.assertDoesNotThrow(() -> service.saveIn(model));
                    Assertions.assertDoesNotThrow(() -> service.findIn(model.getId()));
                    Assertions.assertDoesNotThrow(() -> service.findIn(null));
                    Assertions.assertDoesNotThrow(service::list);

                    Assertions.assertNotNull(service.saveIn(null));
                    Assertions.assertNotNull(service.disable((UUID)null));
                    Assertions.assertNotNull(service.disable(UUID.randomUUID()));
                    Assertions.assertNotNull(service.disable(model));
                    Assertions.assertNotNull(service.saveIn(model));
                    Assertions.assertNotNull(service.findIn(model.getId()));
                    Assertions.assertNotNull(service.findIn(null));
                    Assertions.assertNotNull(service.list());

                    Assertions.assertFalse(service.saveIn(null).isOK());
                    Assertions.assertFalse(service.disable((UUID)null).isOK());
                    Assertions.assertFalse(service.disable(UUID.randomUUID()).isOK());
                    Assertions.assertTrue(service.disable(model).isOK());
                    Assertions.assertTrue(service.saveIn(model).isOK());
                    Assertions.assertTrue(service.findIn(model.getId()).isOK());
                    Assertions.assertFalse(service.findIn(null).isOK());
                    Assertions.assertTrue(service.list().isOK());

                });
    }


}
