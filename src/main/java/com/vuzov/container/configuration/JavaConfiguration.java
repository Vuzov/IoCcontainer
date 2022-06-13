package com.vuzov.container.configuration;

import com.vuzov.container.service.impl.CardPaymentSystem;
import com.vuzov.container.service.impl.CashPaymentSystem;
import com.vuzov.container.service.interfaces.PaymentSystem;
import org.apache.log4j.Logger;
import java.util.Map;


public class JavaConfiguration implements Configuration {

    private static final Logger logger = Logger.getLogger(JavaConfiguration.class);

    public JavaConfiguration() {
        logger.trace("Конфигурационный файл создан");
    }

    @Override
    public String getPackageToScan() {
        return "com.vuzov.container";
    }

    @Override
    public Map<Class, Class> getInterfaceToImplementation() {
        return Map.of(PaymentSystem.class, CashPaymentSystem.class);
    }
}
