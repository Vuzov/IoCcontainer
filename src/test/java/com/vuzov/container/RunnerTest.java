package com.vuzov.container;

import com.vuzov.container.context.ApplicationContext;
import com.vuzov.container.factory.BeanFactory;
import com.vuzov.container.service.GetTaxi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RunnerTest {

    ApplicationContext applicationContext;
    BeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        applicationContext = new ApplicationContext();
        beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);
    }


    @Test
    void main() {
        GetTaxi getTaxiActual = applicationContext.getBean(GetTaxi.class);
        GetTaxi getTaxiByStringActual = applicationContext.getBean("GetTaxi");

        Assertions.assertInstanceOf(GetTaxi.class, getTaxiActual);
        Assertions.assertInstanceOf(GetTaxi.class, getTaxiByStringActual);

        LogTicker logTickerActual = applicationContext.getBean(LogTicker.class);
        LogTicker logTickerByStringActual = applicationContext.getBean("LogTicker");

        Assertions.assertInstanceOf(LogTicker.class, logTickerActual);
        Assertions.assertInstanceOf(LogTicker.class, logTickerByStringActual);
    }
}