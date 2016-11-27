package com.cometbites.model;

public enum Status {
	NEW("1"),
	IN_PREPARATION("2"),
	PREPARED("3"),
	READY_FOR_PICKUP("4"),
	PICKED_UP("5"),
	DELAYED("6"),
	EXPIRED("7");
	
	private final String id;
    Status(String id) { this.id = id; }
    public String getValue() { return id; }
}
