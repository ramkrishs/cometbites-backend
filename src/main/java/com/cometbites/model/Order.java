package com.cometbites.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.cometbites.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Component
public class Order {

	public String invoice;

	private Status status;
	private double total;
	private Date date;
	private Map<String, LineItem> orderItems;

	private Customer customer;
	private FoodJoint foodJoint;

	@Autowired
	private MongoTemplate mongoTemplate;

	public Order() {
		orderItems = new HashMap<String, LineItem>();
		status = Status.NEW;
	}

	public void addItem(Item item) {
		LineItem lineItem = new LineItem(item);

		orderItems.put(item.getId(), lineItem);
	}

	public void updateQuantity(Item item, int quantity) {
		if (orderItems.containsKey(item.getId())) {
			LineItem lineItem = orderItems.get(item.getId());
			lineItem.setQuantity(lineItem.getQuantity() + quantity);

			orderItems.put(item.getId(), lineItem);
		} else {
			LineItem lineItem = new LineItem(item);
			lineItem.setQuantity(quantity);

			orderItems.put(item.getId(), lineItem);
		}

		updateTotal();
	}

	private void updateTotal() {
		total = 0;
		for (String itemID : orderItems.keySet()) {
			total += orderItems.get(itemID).getTotal();
		}
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public FoodJoint getFoodJoint() {
		return foodJoint;
	}

	public void setFoodJoint(FoodJoint foodJoint) {
		this.foodJoint = foodJoint;
	}

	@Override
	public String toString() {
		return String.format("Menu[id=%s]", invoice);
	}

	public Collection<LineItem> getOrderItems() {
		return orderItems.values();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Ticket concludeOrder(String paymentOption) {

		Payment payment = new Payment(paymentOption);
		payment.setAmount(total);
		long cardNumber = payment.getCard().getNumber();

		boolean authorized = UTDPaymentAdapter.getInstance().authorize(cardNumber);
		if (authorized) {
			UTDPaymentAdapter.getInstance().charge(cardNumber, payment.getAmount());
		}

		saveTransaction(payment, this);

		updateStatus();

		String waitTime = calculateWaitTime();

		Ticket ticket = new Ticket();
		ticket.setWaitTime(waitTime);

		return ticket;
	}

	private void updateStatus() {
		status = Status.IN_PREPARATION;
	}

	private String calculateWaitTime() {
		/*
		 * wait_time: "1.5", chef_total: "2", chef_efficiency: "0.5",
		 * helper_total: "1", helper_efficiency: "0.25", delay_time: "1"
		 */

		return "";
	}

	// public float getOrderWaitTime(int foodJointID) {
	// JSONArray orders = new JSONArray(getOrders());
	// int numberOfOrders = orders.length();
	// System.out.println(numberOfOrders);
	// float waitTime = getFoodJointWaitTime(foodJointID);
	//
	// float orderWaitTime = waitTime + waitTime*numberOfOrders;
	//
	// return orderWaitTime;
	// }

	public void saveTransaction(Payment payment, Order order) {
		DBObject document = new BasicDBObject();

		try {
			document.put("invoice", order.getInvoice());
			document.put("netid", order.getCustomer().getId());
			document.put("amount", payment.getAmount());
			document.put("cardNo", payment.getCard().getNumber());
			document.put("date", Util.getCurrentTime());

			DBCollection ms = mongoTemplate.getCollection("transactions");
			ms.insert(document);
		} catch (Exception e) {
		}
	}
}
