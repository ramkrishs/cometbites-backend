package com.cometbites.model;

import org.springframework.data.annotation.Id;


public class CometCard extends Card {

    @Id
    public String id;

    private long utdID;
	
    public CometCard(long utdID) {
		this.utdID = utdID;
	}

	@Override
	public long getNumber() {
		return utdID;
	}

	@Override
    public String toString() {
        return String.format(
                "CometCard[id=%s, utdID='%s']",
                id, Long.toString(utdID));
    }

}