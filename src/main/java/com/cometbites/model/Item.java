package com.cometbites.model;

import org.springframework.data.annotation.Id;

public class Item {
	@Id
	public String id;

	private String name;
	private String description;
	private String image;
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
	
	public Item(String id, String name, String description, double price) {
		this.id = id;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder("{ \"id\": \"%s\", \"name\": \"%s\", \"description\": \"%s\", \"price\": \"%s\", \"image\": \"%s\" }");
		return String.format(aux.toString(), id, name, description, Double.toString(price), image);
	}
}
