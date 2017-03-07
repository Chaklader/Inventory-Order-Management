package com.pensio.backend.ioc;

import com.pensio.backend.controllers.BackendController;
import com.pensio.backend.model.IModelFactory;
import com.pensio.backend.model.Inventory;
import com.pensio.backend.model.OrderLine;
import com.pensio.backend.model.Product;
import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;

import java.util.Arrays;
import java.util.UUID;



/* We are looking for people with a firm background in TDD using IoC. 

1. In detail we expected tests to be introduced earlier in your checkins and we expected to 
see the IoC container be used for class instantiation, not population.

2. Some of your tests were also overly verbose and did not use the @Before method 
to setup the base of the tests.*/


public class BackendContainer implements IModelFactory {

    private ShopOrderRepository shopOrderRepository;

    public BackendController getBackendController() {
        return new BackendController(null);
    }


    /**
     * Instantiates a singleton ShopOrderRepository instance per BackendContainer instance
     *
     * @return ShopOrderRepository
     */
    public ShopOrderRepository getShopOrderRepository() {

        // this if is good to improve the performance
        if (shopOrderRepository == null) {
            synchronized (ShopOrderRepository.class) {
                if (shopOrderRepository == null) {
                    shopOrderRepository = new ShopOrderRepository(this);
                }
            }
        }
        return shopOrderRepository;
    }

    @Override
    public ShopOrder getShopOrder() {
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(UUID.randomUUID().toString());
        shopOrder.setPaymentId(UUID.randomUUID().toString());
        shopOrder.setOrderLines(Arrays.asList(getOrderLine()));
        return shopOrder;
    }

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

    @Override
    public Product getProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(UUID.randomUUID().toString());
        return product;
    }
}
