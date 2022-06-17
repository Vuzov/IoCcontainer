package com.vuzov.container;

import com.vuzov.container.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CurrentTimeService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentTimeService.class);

    public long currentMillis() {
        logger.trace("Вызван метод currentMillis()");
        return System.currentTimeMillis();
    }
}
