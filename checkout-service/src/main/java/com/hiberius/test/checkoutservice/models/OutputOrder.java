package com.hiberius.test.checkoutservice.models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class OutputOrder implements OrderParent{
	
	private long id;
	private long clientId;
	private String clientName;
	private String date;
	private Map<String,String> address;
	private Map<Long,List<Object>> productDetail;
	private Map<String,Double> total;
	
	public OutputOrder() {}
	
	public OutputOrder(long id, long clientId, String clientName,
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
	public String getDate() {
		return date;
	}
	
	public void setDate(Date thedate) {
		SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		this.date = dtf.format(thedate);

	}
	
	public void setDate() {
		//this.date = date;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.date=dtf.format(now);
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
