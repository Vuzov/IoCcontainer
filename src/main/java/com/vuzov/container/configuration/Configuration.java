package com.vuzov.container.configuration;

import java.util.Map;


public interface Configuration {
    String getPackageToScan();

    Map<Class, Class> getInterfaceToImplementation();
}
