package com.vuzov.container.factory;


import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.configuration.Configuration;
import com.vuzov.container.configuration.JavaConfiguration;
import com.vuzov.container.configurator.BeanConfigurator;
import com.vuzov.container.configurator.JavaBeanConfigurator;
import com.vuzov.container.context.ApplicationContext;
import org.apache.log4j.Logger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;


public class BeanFactory {

    private static final Logger logger = Logger.getLogger(BeanFactory.class);

    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;
    private ApplicationContext applicationContext;

    public BeanFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        configuration = new JavaConfiguration();
        beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementation());
        logger.trace("Фабрика бинов создана");
    }

    public <T> T getBean(Class<T> clazz) {
        logger.info("Контекст просит фабрику создать бин для " + clazz);
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            logger.info("Фбрика просит бин-конфигуратор найти имплементацию для " + implementationClass);
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        try {
            logger.info("Фабрика создаёт бин для " + implementationClass);
            T bean = implementationClass.getDeclaredConstructor().newInstance();


            for (Field field : Arrays.stream(bean.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList())) {
                field.setAccessible(true);
                field.set(bean, applicationContext.getBean(field.getType()));
                logger.info("Внедряем зависимость в поле " + field.getName() + ", помеченное аннотацией Autowired по его типу " + field.getType());
            }

            for (Constructor constructor : Arrays.stream(bean.getClass().getConstructors())
                    .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList())) {
                Class <?> [] paramTypes = constructor.getParameterTypes();
                for (Class paramType : paramTypes) {
                    for (Field field : bean.getClass().getDeclaredFields()) {
                        if (paramType == (field.getType())) {
                            field.setAccessible(true);
                            if (field.get(bean) == null) {
                                field.set(bean, applicationContext.getBean(paramType));
                                logger.info("Внедряем зависимость в поле " + field.getName() + " по типу параметра из конструктора " + paramType + ", помеченного аннотацией Autowired");
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
                    for (Class paramType : paramTypes) {
                        for (Field field : bean.getClass().getDeclaredFields()) {
                            if (paramType == (field.getType())) {
                                field.setAccessible(true);
                                if (field.get(bean) == null) {
                                    field.set(bean, applicationContext.getBean(paramType));
                                    logger.info("Внедряем зависимость в поле " + field.getName() + " по типу параметра set-метода " + paramType + ", помеченного аннотацией Autowired");
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

        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
