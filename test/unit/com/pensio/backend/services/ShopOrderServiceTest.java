package com.pensio.backend.services;

/**
 * Created by Chaklader on 2/5/17.
 */

import com.pensio.backend.exceptions.MerchantApiServiceException;
import com.pensio.backend.model.OrderLine;
import com.pensio.backend.model.Product;
import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.UUID;


public class ShopOrderServiceTest {

    // create the class instance to unit test
    private ShopOrderService shopOrderService;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private MerchantApiService merchantApiService;

    @Mock
    private ShopOrderRepository shopOrderRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        shopOrderService = new ShopOrderService(inventoryService, merchantApiService, shopOrderRepository);
    }

    @Test
    public void test_get_ShouldReturnShopOrder() {

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(UUID.randomUUID().toString());
        shopOrder.setPaymentId(UUID.randomUUID().toString());
        shopOrder.setCaptured(true);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test product name");

        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(1);

        shopOrder.setOrderLines(Arrays.asList(orderLine));

        CaptureResponse captureResponse = new CaptureResponse();
        captureResponse.setSuccessful(true);

        when(shopOrderRepository.loadShopOrder(shopOrder.getId())).thenReturn(shopOrder);

        final ShopOrder actual = shopOrderService.get(shopOrder.getId());
        assertEquals(shopOrder.getId(), actual.getId());
    }

    @Test
    public void test_capture_ShouldReturnCapturedWhenInventorySufficientAndPaymentCapturedSuccessfully()
            throws MerchantApiServiceException {

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(UUID.randomUUID().toString());
        shopOrder.setPaymentId(UUID.randomUUID().toString());

        // need to explicitly set as true, as the default
        // boolean field value of an object is false
        shopOrder.setCaptured(true);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test product name");

        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(1);

        shopOrder.setOrderLines(Arrays.asList(orderLine));

        CaptureResponse captureResponse = new CaptureResponse();
        captureResponse.setSuccessful(true);

        when(inventoryService.checkInventory(product, 1)).thenReturn(true);
        when(merchantApiService.capturePayment(shopOrder)).thenReturn(captureResponse);

        ShopOrder actual = shopOrderService.capture(shopOrder);
        assertTrue(actual.isCaptured());
    }

    @Test
    public void test_capture_ShouldReturnNotCapturedWhenInventorySufficientAndPaymentNotCaptured()
            throws MerchantApiServiceException {

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(UUID.randomUUID().toString());
        shopOrder.setPaymentId(UUID.randomUUID().toString());
        shopOrder.setCaptured(true);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test product name");

        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(1);

        shopOrder.setOrderLines(Arrays.asList(orderLine));

        CaptureResponse captureResponse = new CaptureResponse();
        captureResponse.setSuccessful(false);

        when(inventoryService.checkInventory(product, 1)).thenReturn(true);
        // mock that the payment is not sucessful
        when(merchantApiService.capturePayment(shopOrder)).thenReturn(captureResponse);

        final ShopOrder actual = shopOrderService.capture(shopOrder);
        assertFalse(actual.isCaptured());
    }


    @Test
    public void test_capture_ShouldReturnNotCapturedWhenInventoryNotSufficient() {

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setId(UUID.randomUUID().toString());
        shopOrder.setPaymentId(UUID.randomUUID().toString());
        shopOrder.setCaptured(true);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test product name");

        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(1);

        shopOrder.setOrderLines(Arrays.asList(orderLine));

        // dont need to mock the MerchantApiService in
        // this case as the inventory is insufficient
        when(inventoryService.checkInventory(product, 1)).thenReturn(false);

        final ShopOrder actual = shopOrderService.capture(shopOrder);
        assertFalse(actual.isCaptured());
    }
}
