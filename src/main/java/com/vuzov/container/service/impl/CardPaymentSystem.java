package com.vuzov.container.service.impl;

import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.PaymentSystem;


@Service
public class CardPaymentSystem implements PaymentSystem {

    @Override
    public void pay() {
        System.out.println("Оплата с карты произведена. Пожалуйста, ожидайте машину.");
    }
}
