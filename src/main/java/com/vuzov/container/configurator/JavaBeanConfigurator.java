package com.vuzov.container.configurator;

import org.reflections.Reflections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;


public class JavaBeanConfigurator implements BeanConfigurator {

    private static final Logger logger = Logger.getLogger(JavaBeanConfigurator.class);

    private final Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;

    public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImplementation) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>(interfaceToImplementation);
        logger.trace("Бин конфигуратор создан. Переданы:\nПакет для сканирования: " + packageToScan + "\nМапа с установками: " + interfaceToImplementation);
    }

    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, clazz -> {
            logger.info("Конфигуратор вычисляет имеются ли установки в конфигурационном файле и, если нет, то сканирует пакет и ищет имплементацию");
            Set<Class<? extends T>> implementationClasses = scanner.getSubTypesOf(interfaceClass);
            if (implementationClasses.size() != 1) {
                logger.error("В пакете нет релизаций интерфейса или их больше одной");
                throw new RuntimeException(interfaceClass.getSimpleName() + " has 0 or more than 1 implementations");
            }

            return implementationClasses.iterator().next();
        });
    }
}
