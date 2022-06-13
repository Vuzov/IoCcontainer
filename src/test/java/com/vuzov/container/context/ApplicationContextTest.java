package com.vuzov.container.context;

import com.vuzov.container.CurrentTimeService;
import com.vuzov.container.LogTicker;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.service.GetTaxi;
import com.vuzov.container.service.impl.TaxiFare;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;


class ApplicationContextTest {

    ApplicationContext applicationContext;
    BeanFactory beanFactory;

    @BeforeEach
    void beforeAll() {
        applicationContext = new ApplicationContext();
        beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);
    }

    @Test
    void getBeanByClass_ForGetTaxi() throws NoSuchFieldException, IllegalAccessException {
        GetTaxi getTaxi = beanFactory.getBean(GetTaxi.class);
        Field field = getTaxi.getClass().getDeclaredField("costCalculationByDistance");
        field.setAccessible(true);
        Object value = field.get(getTaxi);

        Assertions.assertInstanceOf(TaxiFare.class, value);
    }

    @Test
    void getBeanByClass_ForLogTicker() throws NoSuchFieldException, IllegalAccessException {
        LogTicker logTicker = beanFactory.getBean(LogTicker.class);
        Field field = logTicker.getClass().getDeclaredField("currentTimeService");
        field.setAccessible(true);
        Object value = field.get(logTicker);

        Assertions.assertInstanceOf(CurrentTimeService.class, value);
    }
}