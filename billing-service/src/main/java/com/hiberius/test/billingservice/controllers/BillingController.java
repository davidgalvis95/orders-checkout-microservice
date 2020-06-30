package com.hiberius.test.billingservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hiberius.test.billingservice.models.ProductWrapper;
import com.hiberius.test.billingservice.models.ProductsOutput;
import com.hiberius.test.billingservice.models.Totals;

import com.hiberius.test.billingservice.services.BillingService;

@RestController
@RequestMapping("/products")
public class BillingController {

	@Autowired
	private BillingService billingService;

	@ResponseBody
	@PostMapping("total-per-product/{clientId}")
	public ProductsOutput getSumOfProducts(@PathVariable("clientId") Integer clientId,
			@RequestBody ProductWrapper theProducts) {
		try {

			// Here we create a copy of the input list
			ProductsOutput theSum = billingService.sumByProduct(clientId, theProducts);
			return theSum;

		} catch (Exception e) {

			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found", e);
		}

	}

	@PostMapping("total/{userId}")
	public Totals getTotal(@PathVariable("userId") Integer userId, @RequestBody ProductWrapper theProducts) {
		// Here we calculate the total for all the products
		try {

			Totals theTotal = billingService.getTotal(userId, theProducts);
			return theTotal;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found", e);

		}
	}

}
