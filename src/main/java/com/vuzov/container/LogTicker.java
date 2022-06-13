package com.vuzov.container;

import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.annotations.Scheduled;
import com.vuzov.container.annotations.Service;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import org.apache.log4j.Logger;


@Service
public class LogTicker {

    private static final Logger logger = Logger.getLogger(LogTicker.class);

    private CurrentTimeService currentTimeService;

    public LogTicker() {}

    @Autowired
    public LogTicker(CurrentTimeService currentTimeService) {
        this.currentTimeService = currentTimeService;
    }

    @Scheduled(rate = 5, unit = TimeUnit.SECONDS)
    public void tick() {
        logger.trace("Вызван метод tick()");
        System.out.println(new Date(currentTimeService.currentMillis()));
    }
}
