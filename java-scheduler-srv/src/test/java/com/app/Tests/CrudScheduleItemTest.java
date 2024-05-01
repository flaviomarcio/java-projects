package com.app.Tests;

import com.app.business.service.CRUDScheduleItemService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CrudScheduleItemTest {

    private final FactoryByTests factory = new FactoryByTests();
    private final CRUDScheduleItemService crudScheduleItemService = factory.getCrudScheduleItemService();

    @Test
    public void UT_000_CHECK_SAVE() {

        factory
                .getInScheduleItemList()
                .forEach(scheduleItem -> {

                    Assertions.assertDoesNotThrow(() -> crudScheduleItemService.saveIn(scheduleItem));
                    Assertions.assertDoesNotThrow(() -> crudScheduleItemService.findIn(scheduleItem.getId()));
                    Assertions.assertDoesNotThrow(() -> crudScheduleItemService.list());

                });
    }


}
