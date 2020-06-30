package com.hiberius.test.logisticservice.models;

import java.util.List;
import java.util.Map;

public class ShippingSpecifications {
	
	private int userId;
	private String userName;
	private String address;
	private Map<Long,List<Object>> theDetailedSum;
	private Map<String,Double> theTotal;
	
	
	
	public ShippingSpecifications(int userId, String userName, String address, Map<Long,List<Object>> theDetailedSum,
			Map<String, Double> theTotal) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.address = address;
		this.theDetailedSum = theDetailedSum;
		this.theTotal = theTotal;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Map<Long,List<Object>> getTheDetailedSum() {
		return theDetailedSum;
	}
	public void setTheDetailedSum(Map<Long,List<Object>> theDetailedSum) {
		this.theDetailedSum = theDetailedSum;
	}
	public Map<String, Double> getTheTotal() {
		return theTotal;
	}
	public void setTheTotal(Map<String, Double> theTotal) {
		this.theTotal = theTotal;
	}

}
