package com.hiberius.test.logisticservice.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hiberius.test.logisticservice.models.Order;
import com.hiberius.test.logisticservice.models.ShippingSpecifications;

@Service
public class LogiscticService {

	public Order setOrderFormat(String userName, Long clientId, Long orderId, ShippingSpecifications theOrderProcessed,
			List<String> labels, String[] values) {
		Map<String, String> adressJoin = new HashMap<>();

		Order theOrder = new Order(orderId, clientId, userName, theOrderProcessed.getTheDetailedSum(),
				theOrderProcessed.getTheTotal());

		labels.stream().forEach(la -> {
			int theIndex = labels.indexOf(la);
			adressJoin.put(la, values[theIndex]);
		});

		theOrder.setAddress(adressJoin);

		return theOrder;

	}

}
