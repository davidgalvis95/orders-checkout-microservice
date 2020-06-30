package com.hiberius.test.checkoutservice.services;

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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hiberius.test.checkoutservice.controllers.CheckoutController;
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

@Service
public class CheckOutService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OutputOrder theOutputOrder;

	@Autowired
	private OutputOrderRepo ordersHistory;

	@Autowired
	private InputOrderRepo originalOrderHistory;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private CheckoutController theController;

	public ShippingSpecifications billingCalculationFormatted(InputOrder theInputOrder)
			throws JsonParseException, JsonMappingException, IOException {

		if (ordersHistory.getOrdersHistory() == null) {

			List<InputOrder> theInputList = new ArrayList<InputOrder>();
			theInputList.add(theInputOrder);
			originalOrderHistory.setOriginalOrdersHistory(theInputList);

		} else {
			originalOrderHistory.getOriginalOrdersHistory().add(theInputOrder);
		}

		List<Product> theProducts = theInputOrder.getProductsList();
		ProductWrapper theProductWrapper = new ProductWrapper();
		theProductWrapper.setProducts(theProducts);
		System.out.println(theProducts);
		// String theProductsJSON = mapper.writeValueAsString(theProducts);
//		String uriSumTotalsPerProduct = "http://localhost:8091/products/total-per-product/"
//				+ theInputOrder.getClientId();
		String uriSumTotalsPerProduct = "http://billing:8091/products/total-per-product/" + theInputOrder.getClientId();

//		String uriSumTotals = "http://localhost:8091/products/total/" + theInputOrder.getClientId();
		String uriSumTotals = "http://billing:8091/products/total/" + theInputOrder.getClientId();

		ProductsOutput respTotalPerProduct = new ProductsOutput();
		Totals respTotal = new Totals();

		try {
			respTotalPerProduct = restTemplate.postForObject(uriSumTotalsPerProduct, theProductWrapper,
					ProductsOutput.class);
			respTotal = restTemplate.postForObject(uriSumTotals, theProductWrapper, Totals.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Here is logistic part

		Map<Long, List<Object>> theDetailedSum = respTotalPerProduct.getTheProductsCalculated();
		Map<String, Double> theTotal = respTotal.getTotal();

		ShippingSpecifications formatOrder = new ShippingSpecifications(theInputOrder.getClientId(),
				theInputOrder.getClientName(), theInputOrder.getAddress(), theDetailedSum, theTotal);

		return formatOrder;

	}

	public OutputOrder logisticFixesToBill(InputOrder theInputOrder, ShippingSpecifications formatOrder,
			Long theOrderId) throws JsonParseException, JsonMappingException, IOException {
//		String uriToGetShippingOrder = "http://localhost:8092/logistic/orders/" + theInputOrder.getClientId() + "/"
//				+ theInputOrder.getClientName() + "/" + theOrderId;
		String uriToGetShippingOrder = "http://logistic:8092/logistic/orders/" + theInputOrder.getClientId() + "/"
				+ theInputOrder.getClientName() + "/" + theOrderId;

		Order theLogisticOrder = new Order();

		try {
			theLogisticOrder = restTemplate.postForObject(uriToGetShippingOrder, formatOrder, Order.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		theOutputOrder = new OutputOrder(theInputOrder.getOrderId(), theLogisticOrder.getClientId(),
				theLogisticOrder.getClientName(), theLogisticOrder.getProductDetail(), theLogisticOrder.getTotal());
		theOutputOrder.setAddress(theLogisticOrder.getAddress());
		theOutputOrder.setDate(theInputOrder.getDate());

		if (ordersHistory.getOrdersHistory() == null) {

			List<OutputOrder> theOutputList = new ArrayList<OutputOrder>();
			theOutputList.add(theOutputOrder);
			ordersHistory.setOrdersHistory(theOutputList);

		} else {

			ordersHistory.getOrdersHistory().add(theOutputOrder);
		}

		return theOutputOrder;
	}

	public Object getOrders(Optional<Long> clientId, HttpServletRequest request) {

		if (ordersHistory.getOrdersHistory() == null) {

			List<ObjectNode> errors = new ArrayList<ObjectNode>();
			ObjectNode objectNode = mapper.createObjectNode();

			objectNode.put("code", HttpStatus.NO_CONTENT.value());
			objectNode.put("message", "There are no orders present in the system");
			errors.add(objectNode);
			return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

		} else {

			if (clientId.isPresent()) {
				Predicate<OutputOrder> byClientId = order -> order.getClientId() == clientId.get();
				List<OutputOrder> theResults = ordersHistory.getOrdersHistory().stream().filter(byClientId)
						.collect(Collectors.toList());

				if (theResults == null) {
					List<ObjectNode> errors = new ArrayList<ObjectNode>();
					ObjectNode objectNode = mapper.createObjectNode();

					objectNode.put("code", HttpStatus.NO_CONTENT.value());
					objectNode.put("message", "There are no orders created by the user " + clientId.get());
					errors.add(objectNode);
					return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);
				} else {
					return theResults;
				}

			} else {
				return ordersHistory.getOrdersHistory();
			}
		}
	}

	public Object getOrderById(Long orderId) throws JsonParseException, JsonMappingException, IOException {
		if (ordersHistory.getOrdersHistory() == null) {

			List<ObjectNode> errors = new ArrayList<ObjectNode>();
			ObjectNode objectNode = mapper.createObjectNode();

			objectNode.put("code", HttpStatus.NO_CONTENT.value());
			objectNode.put("message", "There are no orders present in the system");
			errors.add(objectNode);
			return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

		} else {

			Predicate<OutputOrder> byOrderId = order -> order.getId() == orderId;
			List<OrderParent> theOutputResults = ordersHistory.getOrdersHistory().stream().filter(byOrderId)
					.collect(Collectors.toList());

			Predicate<InputOrder> byOrderId2 = order -> order.getOrderId() == orderId;
			List<OrderParent> theInputResults = originalOrderHistory.getOriginalOrdersHistory().stream()
					.filter(byOrderId2).collect(Collectors.toList());

			if (theOutputResults == null || theInputResults == null) {

				List<Object> errors = new ArrayList<Object>();
				ObjectNode objectNode = mapper.createObjectNode();

				objectNode.put("code", HttpStatus.NO_CONTENT.value());
				objectNode.put("message", "There are no orders present in the system with the id: " + orderId);
				errors.add(objectNode);
				Map<String, List<Object>> theResults = new LinkedHashMap<String, List<Object>>();
				List<Object> errors1 = new ArrayList<Object>();
				ResponseEntity<Object> theResponse = new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);
				errors1.add(theResponse);
				theResults.put("not found", null);
				return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

			} else {

				Map<String, List<OrderParent>> theResults = new LinkedHashMap<String, List<OrderParent>>();
				theResults.put("inputResults", theInputResults);
				theResults.put("outputResults", theOutputResults);
				return theResults;
			}

		}

	}

	public Object updateOrder(Long orderId, InputOrder theInputOrder)
			throws JsonParseException, JsonMappingException, IOException {

		if (ordersHistory.getOrdersHistory() == null) {

			List<ObjectNode> errors = new ArrayList<ObjectNode>();
			ObjectNode objectNode = mapper.createObjectNode();

			objectNode.put("code", HttpStatus.NO_CONTENT.value());
			objectNode.put("message", "There are no orders present in the system");
			errors.add(objectNode);
			return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

		} else {

			theInputOrder.setOrderId(orderId);
			OutputOrder updatedOrder = (OutputOrder) theController.createOrder(theInputOrder).get("newOrder");

			Predicate<OutputOrder> byOrderId = order -> order.getId() == orderId;
			Predicate<InputOrder> byOrderId2 = order -> order.getOrderId() == orderId;

			List<OutputOrder> theOutputResults = ordersHistory.getOrdersHistory().stream().filter(byOrderId)
					.collect(Collectors.toList());
			List<InputOrder> theInputResults = originalOrderHistory.getOriginalOrdersHistory().stream()
					.filter(byOrderId2).collect(Collectors.toList());

			if (theOutputResults == null || theInputResults == null) {

				List<ObjectNode> errors = new ArrayList<ObjectNode>();
				ObjectNode objectNode = mapper.createObjectNode();

				objectNode.put("code", HttpStatus.NO_CONTENT.value());
				objectNode.put("message", "There are no orders present in the system with the id: " + orderId);
				errors.add(objectNode);
				return new ResponseEntity<Object>(errors, HttpStatus.NO_CONTENT);

			} else {

				int indexO = ordersHistory.getOrdersHistory().indexOf(theOutputResults.get(0));
				int indexI = originalOrderHistory.getOriginalOrdersHistory().indexOf(theInputResults.get(0));
				ordersHistory.getOrdersHistory().set(indexO, updatedOrder);
				originalOrderHistory.getOriginalOrdersHistory().set(indexI, theInputOrder);

				ordersHistory.getOrdersHistory().remove(ordersHistory.getOrdersHistory().size() - 1);
				originalOrderHistory.getOriginalOrdersHistory()
						.remove(originalOrderHistory.getOriginalOrdersHistory().size() - 1);

				// Map<String, OutputOrder> theResult = new LinkedHashMap<String,
				// OutputOrder>();

				List<ObjectNode> messages = new ArrayList<ObjectNode>();
				ObjectNode objectNode = mapper.createObjectNode();
				objectNode.put("code", HttpStatus.OK.value());
				objectNode.put("message", "The Order " + orderId + " has been successfully updated.");
				messages.add(objectNode);
				ResponseEntity<Object> theResponse = new ResponseEntity<Object>(messages, HttpStatus.OK);
				Map<String, Object> theResult = new LinkedHashMap<String, Object>();
				theResult.put("response", theResponse);
				theResult.put("updatedOrder", theOutputOrder);

				return theResult;
			}
		}
	}

	public Object deleteOrder(Long orderId, List<InputOrder> theInputResults, List<OutputOrder> theOutputResults) {

		int indexO = ordersHistory.getOrdersHistory().indexOf(theOutputResults.get(0));
		int indexI = originalOrderHistory.getOriginalOrdersHistory().indexOf(theInputResults.get(0));
		ordersHistory.getOrdersHistory().remove(indexO);
		originalOrderHistory.getOriginalOrdersHistory().remove(indexI);

		List<ObjectNode> messages = new ArrayList<ObjectNode>();
		ObjectNode objectNode = mapper.createObjectNode();

		objectNode.put("code", HttpStatus.OK.value());
		objectNode.put("message", "The Order " + orderId + " has been successfully deleted.");
		messages.add(objectNode);

		return new ResponseEntity<Object>(messages, HttpStatus.OK);

	}
}
