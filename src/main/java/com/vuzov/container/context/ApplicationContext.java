package com.vuzov.container.context;

import com.vuzov.container.annotations.*;
import com.vuzov.container.configurator.*;
import java.util.*;
import com.vuzov.container.factory.BeanFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.stream.Collectors;


public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private final Reflections scanner;
    private final BeanFactory beanFactory;
    private final BeanConfigurator beanConfigurator;
    private final Map<Class<?>, Object> cache = new HashMap<>();

    public ApplicationContext(String packageToScan, Map<Class, Class> interfaceToImplementation) {
        logger.trace("Контекст создан. Пакет для сканирования: {}", packageToScan);
        this.scanner = new Reflections(packageToScan);
        this.beanFactory = new BeanFactory(this);
        this.beanConfigurator = new JavaBeanConfigurator(scanner, interfaceToImplementation);
    }

    public void run() {
        logger.trace("Запущен метод run()");
        logger.info("Контекст сканирует пакет, ищет сервисы и запускает scheduled методы");
        Set<Class<?>> typesAnnotatedWithService = scanner.getTypesAnnotatedWith(Service.class);
        for (Class<?> service : typesAnnotatedWithService) {
            List<Method> methods = Arrays.stream(service.getDeclaredMethods())
                    .filter(e -> e.isAnnotationPresent(Scheduled.class))
                    .collect(Collectors.toList());
            if (!methods.isEmpty()) {
                if (!cache.containsKey(service)) {
                    Object bean = beanFactory.createBean(service);
                    cache.put(service, bean);
                }
            }
        }
    }

    public <T> T getBean(Class<T> type) {
        logger.info("У контекста запрошен бин для {}", type.getSimpleName());
        Class<T> implClass = getImplementationClass(type);
        if (cache.containsKey(implClass)) {
            logger.info("Контекст вернул бин из кэша для {}", type.getSimpleName());
            return (T) cache.get(implClass);
        }
        T bean = beanFactory.createBean(implClass);
        if (bean.getClass().isAnnotationPresent(Service.class)) {
            cache.put(implClass, bean);
        }
        return bean;
    }

    private <T> Class<T> getImplementationClass(Class<T> type) {
        if (type.isInterface()) {
            logger.info("Контекст просит бин-конфигуратор найти имплементацию для {}", type.getSimpleName());
            type = (Class<T>) beanConfigurator.getImplementationClass(type);
        }
        return type;
    }

    //for test
    public void getCache() {
        for (Class<?> clazz : cache.keySet()) {
            System.out.println(clazz.getSimpleName());
        }
    }
}
