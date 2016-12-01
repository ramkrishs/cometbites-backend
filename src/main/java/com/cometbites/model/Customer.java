package com.cometbites.model;

import org.springframework.data.annotation.Id;


public class Customer {

    @Id
    public String id;

    private String firstName;
    private String lastName;
    private int phoneNumber;
    
    public Customer() {}
    
	public Customer(String id) {
		this.id = id;
	}

	public Customer(String id, String firstName, String lasstName, int phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lasstName;
		this.phoneNumber = phoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLasstName() {
		return lastName;
	}

	public void setLasstName(String lasstName) {
		this.lastName = lasstName;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
    public String toString() {
		StringBuilder aux = new StringBuilder("{ \"netid\": \"%s\", \"phoneNumber\": \"%s\" }");
		return String.format(aux.toString(), id, Integer.toString(phoneNumber));
    }

}