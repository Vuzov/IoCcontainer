package com.vuzov.container.configurator;

import com.vuzov.container.annotations.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Creates and executes a periodic action that becomes enabled first
 * after the given initial delay, and subsequently with the given
 * period; that is executions will commence after
 * {@code initialDelay} then {@code initialDelay+rate}, then
 * {@code initialDelay + 2 * rate}, and so on.
 *
 * {@code initialDelay} the time to delay first execution
 * {@code rate} the period between successive executions
 * {@code unit} the time unit of the initialDelay and period parameters
 *
 * @throws RuntimeException if period less than or equal to zero
 */
public class ScheduledAnnotationBeanConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledAnnotationBeanConfigurator.class);

    public void configure(Object bean) {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Scheduled.class)) {
                if (method.getAnnotation(Scheduled.class).rate() <= 0) {
                    throw new RuntimeException(String.format("%s: the rate parameter in annotation @Scheduled is specified incorrectly. Method %s", bean.getClass().getSimpleName(), method.getName()));
                }
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(7);
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    try {
                        logger.info("Запущен метод {}, помеченный аннотацией @Scheduled", method.getName());
                        method.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("Упали в шедуллере", e.getCause());
                    }
                }, 1, method.getAnnotation(Scheduled.class).rate(), method.getAnnotation(Scheduled.class).unit());
            }
        }
    }
}
