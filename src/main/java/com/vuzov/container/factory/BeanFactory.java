package com.vuzov.container.factory;

//TODO сократить импорты
import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.configuration.Configuration;
import com.vuzov.container.configuration.JavaConfiguration;
import com.vuzov.container.configurator.BeanConfigurator;
import com.vuzov.container.configurator.JavaBeanConfigurator;
import com.vuzov.container.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;


public class BeanFactory {

    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * TODO нужно ли это выносить в отдельное поле?
     */
    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;
    /**
     * TODO нужно ли это выносить в отдельное поле?
     */
    private ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        configuration = new JavaConfiguration();
        beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementation());
        logger.trace("Фабрика бинов создана");
    }

    public <T> T getBean(Class<T> clazz) {
        logger.info("Контекст просит фабрику создать бин для {}", clazz);
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            logger.info("Фбрика просит бин-конфигуратор найти имплементацию для {}", implementationClass);
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        try {
            logger.info("Фабрика создаёт бин для {}", implementationClass);
            // TODO как работает внедрение через конструктор?
            T bean = implementationClass.getDeclaredConstructor().newInstance();


            for (Field field : Arrays.stream(bean.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList())) {
                field.setAccessible(true);
                field.set(bean, applicationContext.getBean(field.getType()));
                logger.info("Внедряем зависимость в поле {}, помеченное аннотацией Autowired по его типу {}", field.getName(), field.getType());
            }

            //TODO raw
            for (Constructor constructor : Arrays.stream(bean.getClass().getConstructors())
                    .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList())) {
                Class <?> [] paramTypes = constructor.getParameterTypes();
                for (Class<?> paramType : paramTypes) {
                    // TODO что, если потребитель использует зависимость только для инициализации класса и не присваивает ее ни в какое поле?
                    for (Field field : bean.getClass().getDeclaredFields()) {
                        if (paramType == (field.getType())) {
                            field.setAccessible(true);
                            if (field.get(bean) == null) {
                                field.set(bean, applicationContext.getBean(paramType));
                                logger.info("Внедряем зависимость в поле {} по типу параметра из конструктора {}, помеченного аннотацией Autowired", field.getName(), paramType);
                            }
                        }
                    }
                }
            }

            for (Method method : Arrays.stream(bean.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList())) {
                if (method.getName().toLowerCase().startsWith("set") && method.getParameterTypes().length == 1) {
                    Class <?> [] paramTypes = method.getParameterTypes();
                    for (Class<?> paramType : paramTypes) {
                        for (Field field : bean.getClass().getDeclaredFields()) {
                            if (paramType == (field.getType())) {
                                field.setAccessible(true);
                                if (field.get(bean) == null) {
                                    field.set(bean, applicationContext.getBean(paramType));
                                    logger.info("Внедряем зависимость в поле {} по типу параметра set-метода {}, помеченного аннотацией Autowired", field.getName(), paramType);
                                }
                            }
                        }
                    }
                }
            }

            //TODO question
//            for (Method method : Arrays.stream(bean.getClass().getMethods())
//                    .filter(method -> method.isAnnotationPresent(Autowired.class))
//                    .collect(Collectors.toList())) {
//                if (method.getName().toLowerCase().startsWith("set") && method.getParameterTypes().length == 1) {
//                    method.invoke(bean, applicationContext.getBean(Arrays.stream(method.getParameterTypes()).findFirst().get()));
//                }
//            }

            return bean;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Произошла ошибка во время внедрения зависимости", e);
        }
        /**
         * TODO стоит ли возвращать null? Spring в этом случае возвращает исключение, как думаете, почему?
         */
        return null;
    }
}
