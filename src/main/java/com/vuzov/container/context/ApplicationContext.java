package com.vuzov.container.context;

import com.vuzov.container.annotations.Scheduled;
import com.vuzov.container.annotations.Service;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.schedule.Scheduller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    public ApplicationContext() {
        logger.trace("Контекст создан");
    }

    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new HashMap<>();

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public <T> T getBean(Class<T> clazz) {
        logger.info("Запрошен бин из контекста для {}", clazz);
        if (beanMap.containsKey(clazz)) {
            logger.info("Контекст проверил наличие бина в cache (beanMap) и возвратил его для {}", clazz);
            return (T) beanMap.get(clazz);
        }

        T bean = beanFactory.getBean(clazz);

        // TODO аннотация Service влияет только на помещение в кэш?
        if (bean.getClass().isAnnotationPresent(Service.class)) {
            logger.info("Контекст поместил бин в cache (beanMap), т.к. {} помечен аннотацией Service", bean.getClass());
            beanMap.put(bean.getClass(), bean);
        }

        // TODO должна быть возможность без обращения к бину запускать Scheduled-методы (обычно к таким бинам никто не обращается из контекста)
        for (Method method : bean.getClass().getMethods()) {
            if (method.isAnnotationPresent(Scheduled.class)) {
                logger.info("Метод {} из {} помечен аннотацией Scheduled. Контекст создает Scheduller", method.getName(), bean.getClass());
                new Scheduller().start(bean, method);
            }
        }

        return bean;
    }
}
