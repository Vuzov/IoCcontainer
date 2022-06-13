package com.vuzov.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class LogTickerTest {

    CurrentTimeService currentTimeService = Mockito.mock(CurrentTimeService.class);

    @Test
    void tick() {
        Mockito.when(currentTimeService.currentMillis()).thenReturn(System.currentTimeMillis());
        long millisFromMock = currentTimeService.currentMillis();
        long myMillis = System.currentTimeMillis();
        long difference = myMillis - millisFromMock;

        Assertions.assertEquals(myMillis - difference, millisFromMock);
    }
}
