package com.hiberius.test.billingservice.models;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ProductsOutput {
	
	private long clientId;
	private Map<Long,List<Object>> theProductsCalculated;	
	
	public ProductsOutput() {}
	
	public ProductsOutput(long clientId, Map<Long,List<Object>> theProductsCalculated) {
		super();
		this.clientId = clientId;
		this.theProductsCalculated = theProductsCalculated;
	}	
	
	public long getClientId() {
		return clientId;
	}
	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	public Map<Long,List<Object>> getTheProductsCalculated() {
		return theProductsCalculated;
	}
	public void setTheProductsCalculated(Map<Long,List<Object>> theProductsCalculated) {
		this.theProductsCalculated = theProductsCalculated;
	}

	@Override
	public String toString() {
		return "ProductsOutput [clientId=" + clientId + ", theProductsCalculated=" + theProductsCalculated + "]";
	}


	
	

}
