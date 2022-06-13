package com.vuzov.container.context;

import com.vuzov.container.annotations.Scheduled;
import com.vuzov.container.annotations.Service;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.schedule.Scheduller;
import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ApplicationContext {

    private static final Logger logger = Logger.getLogger(ApplicationContext.class);

    public ApplicationContext() {
        logger.trace("Контекст создан");
    }

    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public <T> T getBean(Class<T> clazz) {
        logger.info("Запрошен бин из контекста для " + clazz);
        if (beanMap.containsKey(clazz)) {
            logger.info("Контекст проверил наличие бина в cache (beanMap) и возвратил его для " + clazz);
            return (T) beanMap.get(clazz);
        }

        T bean = beanFactory.getBean(clazz);

        if (bean.getClass().isAnnotationPresent(Service.class)) {
            logger.info("Контекст поместил бин в cache (beanMap), т.к. " + bean.getClass() + " помечен аннотацией Service");
            beanMap.put(bean.getClass(), bean);
        }

        for (Method method : bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(Scheduled.class)) {
                logger.info("Метод " + method.getName() + " из " + bean.getClass() + " помечен аннотацией Scheduled. Контекст создает Scheduller.");
                new Scheduller().start(bean, method);
            }
        }

        return bean;
    }

    //TODO finish
    public <T> T getBean(String beanName) {
        logger.info("Запрошен бин из контекста по имени класса - " + beanName);
        Set<Class> classSet = beanMap.keySet();
        for (Class clazz : classSet) {
            if (beanName.equals(clazz.getSimpleName())) {
                logger.info("Возвращен бин из cache (beanMap) по имени кдасса для " + clazz.getName());
                return (T) beanMap.get(clazz);
            } else {
                logger.warn("Бин отсутствует в cache (beanMap)");
            }
        }
        return null;
    }
}
