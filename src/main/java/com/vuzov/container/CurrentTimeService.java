package com.vuzov.container;

import com.vuzov.container.annotations.Service;
import org.apache.log4j.Logger;


@Service
public class CurrentTimeService {

    private static final Logger logger = Logger.getLogger(CurrentTimeService.class);

    public long currentMillis() {
        logger.trace("Вызван метод currentMillis()");
        return System.currentTimeMillis();
    }
}
