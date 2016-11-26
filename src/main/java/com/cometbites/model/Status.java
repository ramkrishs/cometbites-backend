package com.cometbites.model;

public enum Status {
	NEW(1),
	IN_PREPARATION(2),
	PREPARED(3),
	READY_FOR_PICKUP(4),
	PICKED_UP(5),
	DELAYED(6),
	EXPIRED(7);
	
	private final int id;
    Status(int id) { this.id = id; }
    public int getValue() { return id; }
}
