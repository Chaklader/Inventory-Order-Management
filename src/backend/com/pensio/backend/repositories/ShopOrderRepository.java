package com.pensio.backend.repositories;

import java.util.ArrayList;
import java.util.List;
import com.pensio.backend.model.IModelFactory;
import com.pensio.backend.model.OrderLine;
import com.pensio.backend.model.Product;
import com.pensio.backend.model.ShopOrder;

public class ShopOrderRepository {

    private IModelFactory modelFactory;

    public ShopOrderRepository(IModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    public ShopOrder loadShopOrder(String shopOrderId) {
        ShopOrder order = modelFactory.getShopOrder();
        order.setId(shopOrderId);
        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        orderLines.add(getOrderLine("1", "Keyboard", 1));
        order.setOrderLines(orderLines);
        return order;
    }

    private OrderLine getOrderLine(String productId, String name, int quantity) {
        OrderLine orderLine = modelFactory.getOrderLine();
        Product product = modelFactory.getProduct();
        product.setId(productId);
        product.setName(name);
        orderLine.setProduct(product);
        orderLine.setQuantity(quantity);
        return orderLine;
    }

    public void saveShopOrder(ShopOrder shopOrder) {
        System.out.println("Hurray, you saved the shopOrder: " + shopOrder);
    }
}
