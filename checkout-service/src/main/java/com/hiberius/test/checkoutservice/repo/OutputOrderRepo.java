package com.hiberius.test.checkoutservice.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hiberius.test.checkoutservice.models.OutputOrder;

@Repository
public class OutputOrderRepo {

	private List<OutputOrder> ordersHistory;

	public List<OutputOrder> getOrdersHistory() {
		return ordersHistory;
	}

	public void setOrdersHistory(List<OutputOrder> ordersHistory) {
		this.ordersHistory = ordersHistory;
	}

}
