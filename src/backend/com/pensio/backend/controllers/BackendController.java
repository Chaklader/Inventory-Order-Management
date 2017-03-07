package com.pensio.backend.controllers;

import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;
import com.pensio.backend.services.ShopOrderService;

/* We are looking for people with a firm background in TDD using IoC. 

1. In detail we expected tests to be introduced earlier in your checkins and we expected to 
see the IoC container be used for class instantiation, not population.

2. Some of your tests were also overly verbose and did not use the @Before method 
to setup the base of the tests.*/
public class BackendController {

    private ShopOrderService shopOrderService;

    public BackendController(ShopOrderService shopOrderService) {
        this.shopOrderService = shopOrderService;
    }

    public ShopOrder capturePayment(String shopOrderId) {
        ShopOrder shopOrder = shopOrderService.get(shopOrderId);
        shopOrderService.capture(shopOrder);
        return shopOrder;
    }

    public ShopOrder releasePayment(String shopOrderId) {
        ShopOrder shopOrder = shopOrderService.get(shopOrderId);
        shopOrderService.release(shopOrder);
        return shopOrder;
    }
}
