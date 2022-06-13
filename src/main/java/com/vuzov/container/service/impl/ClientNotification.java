package com.vuzov.container.service.impl;

import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.NotificationSystem;


@Service
public class ClientNotification implements NotificationSystem {

    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}
