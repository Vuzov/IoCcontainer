package com.vuzov.container.schedule;

import com.vuzov.container.annotations.Scheduled;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.log4j.Logger;


public class Scheduller {

    private static final Logger logger = Logger.getLogger(Scheduller.class);

    private ScheduledExecutorService scheduledExecutorService;

    public void start(Object bean, Method method) {
        logger.info("Создан " + this.getClass() + " для " + bean.getClass() + " и вызван метод start(). Входящие парамметры аннотации: rate = " + method.getAnnotation(Scheduled.class).rate() + ", unit = " + method.getAnnotation(Scheduled.class).unit());
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            //TODO understand
            try {
                method.invoke(bean);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }, 0, method.getAnnotation(Scheduled.class).rate(), method.getAnnotation(Scheduled.class).unit());
    }
}
