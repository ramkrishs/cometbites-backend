package com.cometbites.model;

import org.springframework.data.annotation.Id;


public class Ticket {

    @Id
    public String id;

    private String code;

    public Ticket() {}
    
	public Ticket(String id, String code) {
		super();
		this.id = id;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code2) {
		this.code = code2;
	}

	@Override
    public String toString() {
		StringBuilder aux = new StringBuilder("{ \"code\": \"%s\" }");
		return String.format(aux.toString(), code);
    }

}