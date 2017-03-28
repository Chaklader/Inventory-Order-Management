package com.pensio.backend.controllers;

import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;
import com.pensio.backend.services.ShopOrderService;

/* We are looking for people with a firm background in TDD using IoC. 

1. In detail we expected tests to be introduced earlier in your checkins and we expected to 
see the IoC container be used for class instantiation, not population.

2. Some of your tests were also overly verbose and did not use the @Before method 
to setup the base of the tests.

In the BackendContainer code you wrote:

    @Override
    public Inventory getInventory() {
        Inventory inventory = new Inventory();
        inventory.setInventory(10);
        inventory.setProduct(getProduct());
        return inventory;
    }

    @Override
    public OrderLine getOrderLine() {
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(getProduct());
        orderLine.setQuantity(1);
        return orderLine;
    }

This instantiates the objects, but also adds values (inventory on the Inventory object 
and quantity on the OrderLine object). This increases the responsibility area of the container 
outside of what is normally the responsibility area of the container.*/

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
