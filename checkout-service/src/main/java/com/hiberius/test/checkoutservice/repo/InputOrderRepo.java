package com.hiberius.test.checkoutservice.repo;

import java.util.List;


import org.springframework.stereotype.Repository;

import com.hiberius.test.checkoutservice.models.InputOrder;


@Repository
public class InputOrderRepo {

	private List<InputOrder> originalOrdersHistory;


	public List<InputOrder> getOriginalOrdersHistory() {
		return originalOrdersHistory;
	}

	public void setOriginalOrdersHistory(List<InputOrder> originalOrdersHistory) {
		this.originalOrdersHistory = originalOrdersHistory;
	}

}
