package com.hiberius.test.checkoutservice.models;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Totals {
	
	Map<String,Double> total;
	
	public Totals() {}

	public Map<String, Double> getTotal() {
		return total;
	}

	public void setTotal(Map<String, Double> total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Totals [total=" + total + "]";
	}
	
	

}
