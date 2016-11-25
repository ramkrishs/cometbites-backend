package com.cometbites.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Order {
	@Id
	public String id;
	
	private Status status;
	private double total;
	private Date date;
	private Map<String, LineItem> orderItems;
	

	public Order() {
		orderItems = new HashMap<String, LineItem>();
	}
	
	public void addItem(Item item) {
		LineItem lineItem = new LineItem(item);
		
		orderItems.put(item.getId(), lineItem);
	}

	public void updateQuantity(String itemID, int quantity) {
		if(orderItems.containsKey(itemID)) {
			LineItem lineItem = orderItems.get(itemID);
			lineItem.setQuantity(lineItem.getQuantity() + quantity);
			
			orderItems.put(itemID, lineItem); 
		} else {
			LineItem lineItem = new LineItem(new Item(itemID));
			lineItem.setQuantity(quantity);
			
			orderItems.put(itemID, lineItem); 
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
}
