package com.pensio.backend.services;

/**
 * Created by Chaklader on 2/5/17.
 */

import com.pensio.backend.model.Inventory;
import com.pensio.backend.model.Product;
import com.pensio.backend.repositories.InventoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepo;

    private Inventory inventory;

    private InventoryService inventoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test product name");

        inventory = new Inventory();
        inventory.setInventory(10);
        inventory.setProduct(product);

        when(inventoryRepo.load(product.getId())).thenReturn(inventory);
        inventoryService = new InventoryService(inventoryRepo);
    }

    @Test
    public void test_checkInventory_ShouldReturnTrueWhenQuantityIsLessThanExistingInventory() {
        boolean bol = inventoryService.checkInventory(inventory.getProduct(), 1);
        assertTrue(bol);
    }

    @Test
    public void checkInventoryShouldReturnTrueWhenQuantityEqualToExistingInventory() {
        boolean bol = inventoryService.checkInventory(inventory.getProduct(), inventory.getInventory());
        assertTrue(bol);
    }

    @Test
    public void test_checkInventory_ShouldReturnFalseWhenQuantityGreatherThanExistingInventory() {
        boolean actual = inventoryService.checkInventory(inventory.getProduct(), inventory.getInventory() + 5);
        assertFalse(actual);
    }

    @Test
    public void test_takeFromInventory_ShouldReturnTrueWhenInventorySuccessfullySave() {
        boolean actual = inventoryService.takeFromInventory(inventory.getProduct(), inventory.getInventory() - 5);
        assertTrue(actual);
    }

    // the inventory quantity (stock) is lesser than the product quantity customer would like to buy
    @Test
    public void test_takeFromInventory_ShouldReturnFalseWhenInventoryIsNotSuccessfullySave() {
        boolean actual = inventoryService.takeFromInventory(inventory.getProduct(), inventory.getInventory() + 5);
        assertFalse(actual);
    }
}
