package com.pensio.backend.services;

import com.pensio.backend.exceptions.MerchantApiServiceException;
import com.pensio.backend.model.OrderLine;
import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;

import java.util.Objects;

/**
 * Created by Chaklader on 2/5/17.
 */
public class ShopOrderService {

    private final InventoryService inventoryService;
    private final MerchantApiService merchantApiService;
    private final ShopOrderRepository shopOrderRepository;

    public ShopOrderService(InventoryService inventoryService, MerchantApiService merchantApiService,
                            ShopOrderRepository shopOrderRepository) {
        this.inventoryService = inventoryService;
        this.merchantApiService = merchantApiService;
        this.shopOrderRepository = shopOrderRepository;
    }

    /**
     * Get shop order by shopOrderId
     * <p>
     * Throw RunTimeException when Shop Order does not exists in database
     *
     * @param shopOrderId the shop order id
     * @return the shop order
     */
    public ShopOrder get(String shopOrderId) {

        final ShopOrder shopOrder = shopOrderRepository.loadShopOrder(shopOrderId);
        if (Objects.isNull(shopOrder)) {
            throw new RuntimeException();
        }
        return shopOrder;
    }

    /**
     * Check each ShopOrder's Product Inventory
     * Capture payment
     * Then decrease inventory
     * <p>
     * Return true if all product is sufficient and payment captured other wise return false
     *
     * @param shopOrder the order
     * @return is successfully captured
     */
    public ShopOrder capture(ShopOrder shopOrder) {

        for (OrderLine orderLine : shopOrder.getOrderLines()) {
            if (!inventoryService.checkInventory(orderLine.getProduct(), orderLine.getQuantity())) {
                shopOrder.release();
                break;
            }
        }

        // capture the payment from the merchant website
        if (shopOrder.isCaptured()) {
            try {
                final CaptureResponse captureResponseService = merchantApiService.capturePayment(shopOrder);

                // if the payment is sucessful, the capture the product
                if (captureResponseService.wasSuccessful()) {
                    shopOrder.capture();
                } else {
                    // the payment is not sucessfull
                    shopOrder.release();
                }
            } catch (MerchantApiServiceException e) {
                e.printStackTrace();
                shopOrder.release();
            }
        }

        // payment is done, now update the inventory
        if (shopOrder.isCaptured()) {
            for (OrderLine orderLine : shopOrder.getOrderLines()) {
                // deduact the quantity  of the product from the inventory
                inventoryService.takeFromInventory(orderLine.getProduct(), orderLine.getQuantity());
            }
        }
        shopOrderRepository.saveShopOrder(shopOrder);
        return shopOrder;
    }

    // Release is a synonym for canceling a payment
    public boolean release(ShopOrder shopOrder) {
        try {
            final ReleaseResponse releaseResponse = merchantApiService.releasePayment(shopOrder);
            return releaseResponse.wasSuccessful();
        } catch (MerchantApiServiceException e) {
            return false;
        }
    }
}
