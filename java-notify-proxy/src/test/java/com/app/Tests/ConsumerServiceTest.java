package com.app.Tests;

import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTest {
    private final FactoryByTests factoryByTests = new FactoryByTests();
//    private final EventService notifyEventService = factoryByTests.getNotifyEventService();

    @Test
    public void UT_000_CHECK() {

    }


}
