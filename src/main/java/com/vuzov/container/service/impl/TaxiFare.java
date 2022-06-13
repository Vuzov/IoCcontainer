package com.vuzov.container.service.impl;

import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.CostCalculationByDistance;


@Service
public class TaxiFare implements CostCalculationByDistance {

    @Override
    public void calculate() {
        System.out.println("Стоимость поедки составит 100 рублей.");
    }
}
