package com.cometbites.model;

import java.util.Date;

import org.springframework.data.annotation.Id;


public class CreditCard extends Card {

    @Id
    public String id;

    private int cardNumber;
    private Date expirationDate;
    private int cvv;
    
	public CreditCard(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public CreditCard(int cardNumber, Date expirationDate, int cvv) {
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.cvv = cvv;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	@Override
	public int getNumber() {
		return cardNumber;
	}

	@Override
    public String toString() {
        return String.format(
                "CreditCard[id=%s, utdID='%s']",
                id, Integer.toString(cardNumber));
    }

}