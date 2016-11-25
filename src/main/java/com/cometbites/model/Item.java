package com.cometbites.model;

import org.springframework.data.annotation.Id;

public class Item {
	@Id
	public String id;

	private String name;
	private String description;
	private double price;

	public Item() {
	}
	
	public Item(String id) {
		this.id = id;
	}

	public Item(String name, String description, double price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Item[id=%s, name='%s', description='%s', price='%s']", id, name, description,
				Double.toString(price));
	}
}
