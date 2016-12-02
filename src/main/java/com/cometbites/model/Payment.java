package com.cometbites.model;

import org.springframework.data.annotation.Id;

public class Payment {

	@Id
	public String id;

	private double amount;
	private boolean authorized;
	private Card card;

	public Payment() {
	}

	public Payment(Card card) {
		this.card = card;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	@Override
	public String toString() {
		return String.format("Payment[id=%s, amount='%s', authorized='%s']", id, Double.toString(amount),
				Boolean.toString(authorized));
	}

}