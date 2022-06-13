package com.vuzov.container.factory;

import com.vuzov.container.configurator.JavaBeanConfigurator;
import com.vuzov.container.service.impl.CardPaymentSystem;
import com.vuzov.container.service.impl.CashPaymentSystem;
import com.vuzov.container.service.interfaces.PaymentSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class BeanFactoryTest {

    JavaBeanConfigurator javaBeanConfigurator;

    @Test
    void getBean_WithEmptyMapAndSeveralImpl() {
        javaBeanConfigurator = new JavaBeanConfigurator("com.vuzov.container", new HashMap<>());
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            Object implementationClass = javaBeanConfigurator.getImplementationClass(PaymentSystem.class);
        }, "RuntimeException was expected");

        Assertions.assertEquals("PaymentSystem has 0 or more than 1 implementations", thrown.getMessage());
    }

    @Test
    void getBean_WithInstalledImpl1() {
        javaBeanConfigurator = new JavaBeanConfigurator("com.vuzov.container", Map.of(PaymentSystem.class, CashPaymentSystem.class));
        Object implementationClass = javaBeanConfigurator.getImplementationClass(PaymentSystem.class);

        Assertions.assertSame(CashPaymentSystem.class, implementationClass);
    }

    @Test
    void getBean_WithInstalledImpl2() {
        javaBeanConfigurator = new JavaBeanConfigurator("com.vuzov.container", Map.of(PaymentSystem.class, CardPaymentSystem.class));
        Object implementationClass = javaBeanConfigurator.getImplementationClass(PaymentSystem.class);

        Assertions.assertSame(CardPaymentSystem.class, implementationClass);
    }
}