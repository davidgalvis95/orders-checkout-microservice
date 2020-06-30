package com.hiberius.test.billingservice.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.hiberius.test.billingservice.controllers.BillingController;
import com.hiberius.test.billingservice.models.Product;
import com.hiberius.test.billingservice.models.ProductWrapper;
import com.hiberius.test.billingservice.models.ProductsOutput;
import com.hiberius.test.billingservice.models.Totals;
import com.hiberius.test.billingservice.repo.ProductRepo;

@Service
public class BillingService {

	@Autowired
	private Totals totals;
	
	@Autowired
	private ProductsOutput theProductResult;

	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private BillingController billingController;

	public ProductsOutput sumByProduct(Integer clientId, @RequestBody ProductWrapper theProducts) {

		List<Product> theNewProducts = new ArrayList<>();
		theProducts.getProducts().stream().map(pro -> theNewProducts.add(pro)).collect(Collectors.toList());

		// Calculate the total per product and store it in a map

		// List<Product> prodCalculated = new ArrayList<>();
		Map<Long, List<Object>> productCalculated = new HashMap<>();
		theNewProducts.stream().forEach(pro -> {
			long theId = pro.getId();
			double totalCostPerProduct = Double.valueOf(pro.getQuantity()) * pro.getCost();
			List<Object> theProductDetails = new ArrayList<>();
			theProductDetails.add(productRepo.getName(theId));
			theProductDetails.add(totalCostPerProduct);
			productCalculated.put(theId, theProductDetails);
			// return theNewProducts.add(pro);
		});

		theProductResult.setClientId(clientId);
		theProductResult.setTheProductsCalculated(productCalculated);
		// return ResponseEntity.ok(ProductCalculated);
		return theProductResult;
	}
	
	public Totals getTotal(Integer userId, ProductWrapper theProducts) {
		
		double theSum = 0;
		Map<Long, List<Object>> ProductCalculated = billingController.getSumOfProducts(userId, theProducts)
				.getTheProductsCalculated();
		for (Map.Entry<Long, List<Object>> entry : ProductCalculated.entrySet()) {
			theSum = theSum + (Double) entry.getValue().get(1);
		}

		Map<String, Double> Total = new HashMap<>();
		Total.put("Total", theSum);
		totals.setTotal(Total);
		return totals;
		
	}
}
