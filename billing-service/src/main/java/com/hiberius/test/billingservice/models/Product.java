package com.hiberius.test.billingservice.models;

public class Product {
	
	private long id;
	private String name;
	private int quantity;
	private double cost;
	
	public Product() {}
	public Product(long id, int quantity, double cost) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.cost = cost;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", quantity=" + quantity + ", cost=" + cost + "]";
	}
	
	

}
