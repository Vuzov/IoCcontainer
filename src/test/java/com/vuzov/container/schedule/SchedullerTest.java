package com.vuzov.container.schedule;

import com.vuzov.container.LogTicker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;


class SchedullerTest {

    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Test
    void start() throws InterruptedException {
        LogTicker logTicker = Mockito.mock(LogTicker.class);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                logTicker.tick();
            }
        }, 0, 1, TimeUnit.SECONDS);
        Thread.sleep(3000);

        Mockito.verify(logTicker, times(3)).tick();
    }
}