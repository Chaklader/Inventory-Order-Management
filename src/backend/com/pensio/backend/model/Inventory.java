package com.pensio.backend.model;

public class Inventory 
{
	private Product product;

	// amount of the product in the stock
	private int inventory;
	
	public Product getProduct() 
	{
		return product;
	}
	
	public void setProduct(Product product) 
	{
		this.product = product;
	}
	
	public int getInventory() 
	{
		return inventory;
	}
	
	public void setInventory(int inventory) 
	{
		this.inventory = inventory;
	}
}
