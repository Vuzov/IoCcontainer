package com.vuzov.container.configuration;

import com.vuzov.container.service.impl.CardPaymentSystem;
import com.vuzov.container.service.impl.CashPaymentSystem;
import com.vuzov.container.service.interfaces.PaymentSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;


public class JavaConfiguration implements Configuration {

    private static final Logger logger = LoggerFactory.getLogger(JavaConfiguration.class);

    public JavaConfiguration() {
        logger.trace("Конфигурационный файл создан");
    }

    @Override
    public String getPackageToScan() {
        /**
         * TODO лучше брать пакет относительно какого либо класса.
         * Вы можете переименовать пакеты и приложение перестанет работать
         */
        return "com.vuzov.container";
    }

    @Override
    public Map<Class, Class> getInterfaceToImplementation() {
        // TODO как потребителю фреймворка добавлять сюда свои классы?
        return Map.of(PaymentSystem.class, CashPaymentSystem.class);
    }
}
