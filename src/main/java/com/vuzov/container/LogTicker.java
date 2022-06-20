package com.vuzov.container;

import com.vuzov.container.annotations.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogTicker {

    private static final Logger logger = LoggerFactory.getLogger(LogTicker.class);

//    @Autowired
    private CurrentTimeService currentTimeService;

//    @Autowired
//    public void setCurrentTimeService(CurrentTimeService currentTimeService) {
//        logger.info("Внедрение зависимости через сеттер в {}", currentTimeService);
//        this.currentTimeService = currentTimeService;
//    }

    @Autowired
    public LogTicker(CurrentTimeService currentTimeService) {
        logger.info("Внедрение зависимости через конструктор в {}", currentTimeService.toString());
        this.currentTimeService = currentTimeService;
    }

    @Scheduled(rate = 5, unit = TimeUnit.SECONDS)
    public void tick() {
        logger.trace("Вызван метод tick()");
        System.out.println(new Date(currentTimeService.currentMillis()));
    }
}
