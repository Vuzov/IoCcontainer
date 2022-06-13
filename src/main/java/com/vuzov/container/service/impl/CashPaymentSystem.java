package com.vuzov.container.service.impl;

import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.PaymentSystem;


@Service
public class CashPaymentSystem implements PaymentSystem {

    @Override
    public void pay() {
        System.out.println("Поездка подтверждена. Оплата наличными водителю. Пожалуйста, ожидайте машину.");
    }
}
