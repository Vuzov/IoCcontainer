package com.vuzov.container.service.impl;

import com.vuzov.container.annotations.Service;
import com.vuzov.container.service.interfaces.SearchSystem;


@Service
public class SearchNearestTaxi implements SearchSystem {

    @Override
    public void search() {
        System.out.println("Машина найдена. Прибудет к Вам в течение 5 минут.");
    }
}
