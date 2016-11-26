package com.cometbites.model;

import org.springframework.data.annotation.Id;


public class CometCard extends Card {

    @Id
    public String id;

    private int utdID;
	
    public CometCard(int utdID) {
		this.utdID = utdID;
	}

	@Override
	public int getNumber() {
		return utdID;
	}

	@Override
    public String toString() {
        return String.format(
                "CometCard[id=%s, utdID='%s']",
                id, Integer.toString(utdID));
    }

}