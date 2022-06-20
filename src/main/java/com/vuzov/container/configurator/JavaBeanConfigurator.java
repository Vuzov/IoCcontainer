package com.vuzov.container.configurator;

import com.vuzov.container.annotations.Service;
import org.reflections.Reflections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavaBeanConfigurator implements BeanConfigurator {

    private static final Logger logger = LoggerFactory.getLogger(JavaBeanConfigurator.class);

    private final Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;

    public JavaBeanConfigurator(Reflections scanner) {
        logger.trace("Бин конфигуратор создан");
        this.scanner = scanner;
        this.interfaceToImplementation = new HashMap<>();
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, clazz -> {
            logger.info("Конфигуратор ищет имплементацию для {}", interfaceClass.getSimpleName());
            Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
            if (implementationClasses.size() != 1) {
                logger.error("В пакете нет релизаций интерфейса или их больше одной");
                throw new RuntimeException(String.format("%s has 0 or more than one implementations", interfaceClass.getSimpleName()));
            }
            if (!implementationClasses.iterator().next().isAnnotationPresent(Service.class)) {
                logger.error("В пакете имеется имплементация для {}, но она не помечена аннотацией @Service", interfaceClass.getSimpleName());
                throw new RuntimeException(String.format("%s is not marked with an annotation Service", interfaceClass.getSimpleName()));
            }
            logger.info("Бин-конфигуратор вернул имплементацию для {}", interfaceClass.getSimpleName());
            return implementationClasses.iterator().next();
        });
    }
}
