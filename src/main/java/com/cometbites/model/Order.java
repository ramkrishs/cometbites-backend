package com.cometbites.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import com.cometbites.db.DBFacade;

public class Order {
	@Id
	public String id;
	
	private Status status;
	private double total;
	private Date date;
	private Map<String, LineItem> orderItems;
	
	@Autowired
	private DBFacade dBFacade;
	
	public Order() {
		orderItems = new HashMap<String, LineItem>();
		status = Status.NEW;
	}
	
	public void addItem(Item item) {
		LineItem lineItem = new LineItem(item);
		
		orderItems.put(item.getId(), lineItem);
	}

	public void updateQuantity(Item item, int quantity) {
		if(orderItems.containsKey(item.getId())) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return String.format("Menu[id=%s]", id);
	}

	public Collection<LineItem> getOrderItems() {
		return orderItems.values();
	}

	public Ticket concludeOrder(String paymentOption) {
		
		Payment payment = new Payment(paymentOption);
		payment.setAmount(total);
		int cardNumber = payment.getCard().getNumber();
		
		
		boolean authorized = UTDPaymentAdapter.getInstance().authorize(cardNumber);
		if(authorized) {
			UTDPaymentAdapter.getInstance().charge(cardNumber, payment.getAmount());
		}
		
		//TODO giving null for db
//		dBFacade.saveTransaction(payment);
		
		updateStatus();
		calculateWaitTime();
		
		Ticket ticket = new Ticket();
		
		return ticket;
	}
	
	private void updateStatus() {
		status = Status.IN_PREPARATION;
	}

	private void calculateWaitTime() {
		// TODO Auto-generated method stub
	}
	
}
