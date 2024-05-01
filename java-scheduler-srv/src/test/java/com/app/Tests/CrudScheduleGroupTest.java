package com.app.Tests;

import com.app.business.service.CRUDScheduleGroupService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CrudScheduleGroupTest {
    private final FactoryByTests factory = new FactoryByTests();
    private final CRUDScheduleGroupService crudScheduleGroupService = factory.getCrudScheduleGroupService();

    @Test
    public void UT_000_CHECK_SAVE() {

        factory
                .getInScheduleGroupList()
                .forEach(scheduleGroup -> {

                    Assertions.assertDoesNotThrow(() -> crudScheduleGroupService.saveIn(scheduleGroup));
                    Assertions.assertDoesNotThrow(() -> crudScheduleGroupService.findIn(scheduleGroup.getId()));
                    Assertions.assertDoesNotThrow(() -> crudScheduleGroupService.list());

                });
    }


}
