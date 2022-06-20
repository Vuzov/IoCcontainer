package com.vuzov.container.factory;

import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.configurator.AutowiredAnnotationBeanConfigurator;
import com.vuzov.container.configurator.ScheduledAnnotationBeanConfigurator;
import com.vuzov.container.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final ApplicationContext applicationContext;
    private final AutowiredAnnotationBeanConfigurator autowiredAnnotationBeanConfigurator;
    private final ScheduledAnnotationBeanConfigurator scheduledAnnotationBeanConfigurator;

    public BeanFactory(ApplicationContext applicationContext) {
        logger.trace("Фабрика бинов создана");
        this.applicationContext = applicationContext;
        this.autowiredAnnotationBeanConfigurator = new AutowiredAnnotationBeanConfigurator();
        this.scheduledAnnotationBeanConfigurator = new ScheduledAnnotationBeanConfigurator();
    }

    public <T> T createBean(Class<T> clazz) {
        logger.info("Контекст просит фабрику создать бин для {}", clazz.getSimpleName());
        T bean = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 1 && Arrays.stream(constructors).findFirst().get().getParameterCount() == 0) {
            try {
                bean = clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("Ощибка при создании бина с пустым конструктором", e.getCause());
            }
        } else {
            List<Constructor<?>> constructorList = Arrays.stream(constructors)
                    .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList());
            if (!constructorList.isEmpty()) {
                if (constructorList.size() != 1) {
                    throw new RuntimeException(String.format("Only one constructor can use an @Autowired annotation. Error in %s", clazz.getSimpleName()));
                }
                bean = createBeanWithConstructor(clazz, constructorList.get(0));
            } else throw new RuntimeException(String.format("Declare an empty constructor in the %s class or add an annotation @Autowired", clazz.getSimpleName()));
        }

        assert bean != null;
        autowiredAnnotationBeanConfigurator.configure(bean, applicationContext);
        scheduledAnnotationBeanConfigurator.configure(bean);

        return bean;
    }

    private <T> T createBeanWithConstructor(Class<T> clazz, Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] beansForParameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Object beanForInject = applicationContext.getBean(parameterTypes[i]);
            beansForParameters[i++] = beanForInject;
        }
        try {
            return clazz.getDeclaredConstructor(parameterTypes).newInstance(beansForParameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Ошибка при создании бина c конструктором с параметрами", e.getCause());
        }
        return null;
    }
}
