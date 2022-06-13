package com.vuzov.container.service;

import com.vuzov.container.annotations.Autowired;
import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.*;
import org.apache.log4j.Logger;


@Service
public class GetTaxi {

    Logger logger = Logger.getLogger(GetTaxi.class);

    @Autowired
    private CostCalculationByDistance costCalculationByDistance;

    @Autowired
    private SearchSystem searchSystem;

    @Autowired
    private PaymentSystem paymentSystem;

    @Autowired
    private NotificationSystem notificationSystem;

    //TODO дописать логику
    public void get() {
        logger.trace("Вызван метод get()");
        costCalculationByDistance.calculate();
        searchSystem.search();
        paymentSystem.pay();
        notificationSystem.notify("Машина подана.");
        drive();
        notificationSystem.notify("Поездка завершена.");
    }

    private void drive() {
        System.out.println("Мы едем, едем, едем в далекие края...");
    }
}
