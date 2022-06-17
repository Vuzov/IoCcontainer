package com.vuzov.container.schedule;

import com.vuzov.container.annotations.Scheduled;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Scheduller {

    private static final Logger logger = LoggerFactory.getLogger(Scheduller.class);

    private ScheduledExecutorService scheduledExecutorService;

    public void start(Object bean, Method method) {
        logger.info("Создан {} для {} и вызван метод start()", this.getClass(), bean.getClass());

        /**
         * TODO попробуйте использовать один ScheduledExecutorService для всех методов с аннотацией
         */
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                method.invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Произошла ошибка во время запуска метода в ScheduledExecutorService", e);
            }
        }, 0, method.getAnnotation(Scheduled.class).rate(), method.getAnnotation(Scheduled.class).unit());
    }
}
