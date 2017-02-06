package com.pensio.backend.controllers;

import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;
import com.pensio.backend.services.ShopOrderService;

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
