package com.pensio.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pensio.backend.services.ShopOrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pensio.backend.model.ShopOrder;
import com.pensio.backend.repositories.ShopOrderRepository;

public class BackendControllerTests {

	private static final String ORDER_ID = "Some order id";

	@Mock
    private ShopOrderService shopOrderService;

	@Mock
	private ShopOrder shopOrder;

	// The object to be tested
	private BackendController controller;

	@Before
	public void setup() 
	{
		MockitoAnnotations.initMocks(this);
		when(shopOrderService.get(ORDER_ID)).thenReturn(shopOrder);
		controller = new BackendController(shopOrderService);
	}


	// test the get method of the shop order service
	@Test
	public void captureReservationGetsTheOrderFromTheRepository()
	{
		controller.capturePayment(ORDER_ID);
		verify(shopOrderService).get(ORDER_ID);
	}

    // test the capture method of the shop order service
    @Test
	public void captureReservationMustInvokeCaptureOnTheOrder()
	{
		controller.capturePayment(ORDER_ID);
        verify(shopOrderService).capture(shopOrder);
	}
}
