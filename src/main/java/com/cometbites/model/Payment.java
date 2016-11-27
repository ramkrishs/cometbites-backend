package com.cometbites.model;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import com.cometbites.util.Util;


public class Payment {

    @Id
    public String id;

    private double amount;
    private boolean authorized;
    private Card card;

    public Payment() {}
    
    public Payment(String paymentOption) {
    	JSONObject option = new JSONObject(paymentOption);
    	
    	if(option.getString("cvv") == null) {
    		card = new CometCard(Long.parseLong(option.getString("cardno")));
    	} else {
    		card = new CreditCard(Long.parseLong(option.getString("cardno")), Util.parseExpirationDate(option.getString("expdate")), Integer.parseInt(option.getString("cvv")));
    	}
    	
    	card.setCardName(option.getString("cardname"));
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
        return String.format(
                "Payment[id=%s, amount='%s', authorized='%s']",
                id, Double.toString(amount), Boolean.toString(authorized));
    }

}