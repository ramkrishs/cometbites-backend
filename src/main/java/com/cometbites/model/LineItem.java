package com.cometbites.model;

public class LineItem {
	private int quantity;
	private Item item;

	public LineItem() {
	}

	public LineItem(Item item) {
		this.item = item;
	}

	public LineItem(int quantity, Item item) {
		this.quantity = quantity;
		this.item = item;
	}

	public String getId() {
		return item.getId();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public double getTotal() {
		return quantity * item.getPrice();
	}

	@Override
	public String toString() {

		StringBuilder aux = new StringBuilder("{ \"quantity\": \"%s\",");

		aux.append("\"item\": {" + "\"id\": \"%s\", " + "\"name\": \"%s\", " + "\"price\": \"%s\"" + "}");

		aux.append("}");

		return String.format(aux.toString(), Integer.toString(quantity), item.getId(), item.getName(),
				Double.toString(item.getPrice()));
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;

		LineItem lineItem = (LineItem) obj;

		return getId().equals(lineItem.getId());
	}
}
