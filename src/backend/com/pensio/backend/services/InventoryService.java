package com.pensio.backend.services;

import com.pensio.backend.model.Inventory;
import com.pensio.backend.model.Product;
import com.pensio.backend.repositories.InventoryRepository;

import java.util.Objects;

public class InventoryService {
    private InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository repository) {
        this.inventoryRepository = repository;
    }

    /**
     * Check product sufficiency of inventory
     * <p>
     * Return true if existing product quantity is greather than or equal to quantity param
     *
     * @param product  the product
     * @param quantity quantity to check
     * @return is sufficient
     */
    public boolean checkInventory(Product product, int quantity) {

        if (Objects.isNull(product) || quantity <= 0) {
            return false;
        }

        final Inventory inventory = inventoryRepository.load(product.getId());
        if(Objects.isNull(inventory)){
            return false;
        }
        return quantity <= inventory.getInventory();
    }

    /**
     * Decrease quantity from Product's Inventory
     * <p>
     * No checking if quantity is sufficient
     * <p>
     * Return true if able to update
     * Return false if inventory repo throws exception
     *
     * @param product  the product
     * @param quantity quantity to remove
     * @return successfully updated
     */
    public boolean takeFromInventory(Product product, int quantity) {

        if (Objects.isNull(product) || quantity <= 0) {
            return false;
        }

        final Inventory inventory = inventoryRepository.load(product.getId());

        if (Objects.isNull(inventory)) {
            return false;
        }
        // if the quantity of the product the customer would like to buy
        // is more than stored in the inventory, they wount be able to buy the product
        if(inventory.getInventory() <= 0 || inventory.getInventory() < quantity){
            return false;
        }

        try {
            inventory.setInventory(inventory.getInventory() - quantity);
            inventoryRepository.save(inventory);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
