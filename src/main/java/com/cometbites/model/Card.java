package com.cometbites.model;

import org.springframework.data.annotation.Id;


public abstract class Card {

    @Id
    public String id;

    private String cardName;

	public abstract long getNumber();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@Override
    public String toString() {
        return String.format(
                "Card[id=%s, cardName='%s']",
                id, cardName);
    }

}