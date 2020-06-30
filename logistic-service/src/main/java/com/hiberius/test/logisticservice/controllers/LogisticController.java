package com.hiberius.test.logisticservice.controllers;

import java.util.Arrays;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hiberius.test.logisticservice.models.Order;
import com.hiberius.test.logisticservice.models.ShippingSpecifications;
import com.hiberius.test.logisticservice.services.LogiscticService;

@RestController
@RequestMapping("/logistic")
public class LogisticController {
	
	@Autowired
	private LogiscticService theLogisticService;

	@ResponseBody
	@PostMapping("orders/{clientId}/{userName}/{orderId}")
	public Order getOrderToShip(@PathVariable("userName") String userName, @PathVariable("clientId") Long clientId,
			@PathVariable("orderId") Long orderId, @RequestBody ShippingSpecifications theOrderProcessed) {
		
		// The format of the US address is [Name, House/building number, Street Name,
		// Apartment(Optional), City, State, ZipCode]
		List<String> labels = Arrays.asList("Name", "House/building number", "Street Name", "Apartment", "City",
				"State", "ZipCode");
		String[] values = theOrderProcessed.getAddress().split("\\s*,\\s*");
		
		return theLogisticService.setOrderFormat(userName, clientId, orderId, theOrderProcessed, labels, values);

	}

}
