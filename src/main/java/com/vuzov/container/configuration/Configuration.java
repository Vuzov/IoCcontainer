package com.vuzov.container.configuration;

import java.util.Map;


public interface Configuration {
    String getPackageToScan();

    /**
     * TODO конфигуратор сам должен находить в пакетах реализацию интерфейса.
     * Прямой маппинг не нужен
     */
    Map<Class, Class> getInterfaceToImplementation();
}
