package com.vuzov.container.configurator;

import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;


public class AutowiredAnnotationBeanConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(AutowiredAnnotationBeanConfigurator.class);

    public void configure(Object bean, ApplicationContext applicationContext) {
        logger.info("Фабрика внедряет зависимости в {}", bean.getClass().getSimpleName());
        for (Field field : Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList())) {
            field.setAccessible(true);
            try {
                field.set(bean, applicationContext.getBean(field.getType()));
            } catch (IllegalAccessException e) {
                logger.error("Ошибка при внедрении зависимости в поле", e.getCause());
            }
        }

        for (Method method : Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList())) {
            if (method.getName().toLowerCase().startsWith("set") && method.getParameterTypes().length == 1) {
                try {
                    method.invoke(bean, applicationContext.getBean(Arrays.stream(method.getParameterTypes()).findFirst().get()));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Ошибка при внедрении зависимости через сеттер", e.getCause());
                }
            } else throw new RuntimeException(method.getName() + " - the method should start with \"set\" and have only one parameter");
        }
    }
}
