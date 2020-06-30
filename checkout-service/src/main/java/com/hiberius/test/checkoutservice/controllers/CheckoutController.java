package com.hiberius.test.checkoutservice.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hiberius.test.checkoutservice.models.InputOrder;
import com.hiberius.test.checkoutservice.models.Order;
import com.hiberius.test.checkoutservice.models.OrderParent;
import com.hiberius.test.checkoutservice.models.OutputOrder;
import com.hiberius.test.checkoutservice.models.Product;
import com.hiberius.test.checkoutservice.models.ProductWrapper;
import com.hiberius.test.checkoutservice.models.ProductsOutput;
import com.hiberius.test.checkoutservice.models.ShippingSpecifications;
import com.hiberius.test.checkoutservice.models.Totals;
import com.hiberius.test.checkoutservice.repo.InputOrderRepo;
import com.hiberius.test.checkoutservice.repo.OutputOrderRepo;
import com.hiberius.test.checkoutservice.services.CheckOutService;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

	@Autowired
	private OutputOrderRepo ordersHistory;

	@Autowired
	private InputOrderRepo originalOrderHistory;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private CheckOutService theChcekoutService;

	@ResponseBody
	@PostMapping("/create-order")
	public Map<String, Object> createOrder(@RequestBody InputOrder theInputOrder)
			throws JsonParseException, JsonMappingException, IOException {

		// The billing side
		long theOrderId;
		if (ordersHistory.getOrdersHistory() == null) {
			theOrderId = 100000;
		} else {
			theOrderId = ordersHistory.getOrdersHistory().size() + 100000;
		}

		if (theInputOrder.getOrderId() >= 100000) {

		} else {
			theInputOrder.setOrderId(theOrderId);
		}

		ShippingSpecifications theOrderFormatted = theChcekoutService.billingCalculationFormatted(theInputOrder);

		OutputOrder theOutputOrder = theChcekoutService.logisticFixesToBill(theInputOrder, theOrderFormatted,
				theOrderId);

		// System.out.println(theLogisticOrder);

		List<ObjectNode> messages = new ArrayList<ObjectNode>();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.put("code", HttpStatus.OK.value());
		objectNode.put("message", "The Order " + theOutputOrder.getId() + " has been successfully created.");
		messages.add(objectNode);
		ResponseEntity<Object> theResponse = new ResponseEntity<Object>(messages, HttpStatus.OK);
		Map<String, Object> theResult = new LinkedHashMap<String, Object>();
		theResult.put("response", theResponse);
		theResult.put("newOrder", theOutputOrder);

		return theResult;
	}

	@ResponseBody
	@GetMapping(value = { "/view-all-orders/{clientId}", "/view-all-orders" })
	public Object getAllOrders(@PathVariable Optional<Long> clientId, HttpServletRequest request) {

		return theChcekoutService.getOrders(clientId, request);
	}

	@ResponseBody
	@GetMapping("/view-order-summary/{orderId}")
	public Object getOrderByOrderId(@PathVariable Long orderId)
			throws JsonParseException, JsonMappingException, IOException {

		return theChcekoutService.getOrderById(orderId);

	}

	@ResponseBody
	@PostMapping("/update-order/{orderId}")
	public Object updateOrder(@PathVariable Long orderId, @RequestBody InputOrder theInputOrder)
			throws JsonParseException, JsonMappingException, IOException {

		return theChcekoutService.updateOrder(orderId, theInputOrder);

	}

	@ResponseBody
	@DeleteMapping("/delete-order/{orderId}")
	public Object deleteOrder(@PathVariable Long orderId) {

		// OutputOrder updatedOrder = this.createOrder(theInputOrder);
		Predicate<OutputOrder> byOrderId = order -> order.getId() == orderId;
		Predicate<InputOrder> byOrderId2 = order -> order.getOrderId() == orderId;

		List<OutputOrder> theOutputResults = ordersHistory.getOrdersHistory().stream().filter(byOrderId)
				.collect(Collectors.toList());
		List<InputOrder> theInputResults = originalOrderHistory.getOriginalOrdersHistory().stream().filter(byOrderId2)
				.collect(Collectors.toList());
		
		if (theInputResults == null || theOutputResults == null) {

			List<ObjectNode> errors = new ArrayList<ObjectNode>();
			ObjectNode objectNode = mapper.createObjectNode();

			objectNode.put("code", HttpStatus.NO_CONTENT.value());
			objectNode.put("message", "There are no orders present in the system with the id: " + orderId);
			errors.add(objectNode);
			return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

		} else {
			
		 return theChcekoutService.deleteOrder(orderId, theInputResults, theOutputResults);
		
		}
	}

}
