package com.vuzov.container;

import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.annotations.Scheduled;
import com.vuzov.container.annotations.Service;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class LogTicker {

    private static final Logger logger = LoggerFactory.getLogger(LogTicker.class);

    private CurrentTimeService currentTimeService;

    /**
     * TODO для чего нужен этот конструктор?
     */
    public LogTicker() {}

    @Autowired
    public LogTicker(CurrentTimeService currentTimeService) {
        this.currentTimeService = currentTimeService;
    }

    @Scheduled(rate = 5)
    public void tick() {
        logger.trace("Вызван метод tick()");
        System.out.println(new Date(currentTimeService.currentMillis()));
    }
}
