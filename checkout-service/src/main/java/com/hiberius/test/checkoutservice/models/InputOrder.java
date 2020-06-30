package com.hiberius.test.checkoutservice.models;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InputOrder implements OrderParent{
	
	private long orderId;
	private int clientId;
	private String clientName;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
	private Date date;
	private String address;
	private List<Product> productsList;
	
	public InputOrder(int clientId,Date date,String clientName, String address, List<Product> productsList) {
		super();
		this.clientId = clientId;
		this.clientName = clientName;
		this.date = date;
		this.address = address;
		this.productsList = productsList;
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Product> getProductsList() {
		return productsList;
	}
	public void setProductsList(List<Product> productsList) {
		this.productsList = productsList;
	}

	@Override
	public String toString() {
		return "InputOrder [clientId=" + clientId + ", clientName=" + clientName + ", date=" + date + ", address="
				+ address + ", productsList=" + productsList + "]";
	}

}
