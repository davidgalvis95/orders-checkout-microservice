package com.hiberius.test.logisticservice.models;

import java.util.List;
import java.util.Map;

public class Order {
	
	private long id;
	private long clientId;
	private String clientName;
	private Map<String,String> address;
	private Map<Long,List<Object>> productDetail;
	private Map<String,Double> total;
	
	public Order() {}
	
	public Order(long id, long clientId, String clientName,
			Map<Long,List<Object>> productDetail, Map<String, Double> total) {
		super();
		this.id = id;
		this.clientId = clientId;
		this.clientName = clientName;
		//this.address = address;
		this.productDetail = productDetail;
		this.total = total;
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public Map<String, String> getAddress() {
		return address;
	}
	public void setAddress(Map<String, String> address) {
		this.address = address;
	}
	public Map<Long,List<Object>> getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(Map<Long,List<Object>> productDetail) {
		this.productDetail = productDetail;
	}
	public Map<String, Double> getTotal() {
		return total;
	}
	public void setTotal(Map<String, Double> total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		return "Order [Id=" + id + ", clientId=" + clientId + ", clientName=" + clientName + ", address=" + address
				+ ", productDetail=" + productDetail + ", total=" + total + "]";
	}
	
	
	
	

}
