package com.cometbites.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Menu {
	@Id
	public String id;

	private int numberOfItems;
	private List<Item> items;

	public Menu() {
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	@Override
	public String toString() {
		StringBuilder aux = new StringBuilder("[");

		for (Item item : items) {
			aux.append(item.toString()).append(",");
		}

		// remove the ',' after last item
		if (aux.length() > 1)
			aux.deleteCharAt(aux.length() - 1);

		aux.append("]");

		return aux.toString();
	}
}
