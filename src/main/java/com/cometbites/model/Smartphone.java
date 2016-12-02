package com.cometbites.model;

public class Smartphone implements IDevice {
	private int phoneID;
	private String OS;

	public Smartphone(int phoneID, String oS) {
		super();
		this.phoneID = phoneID;
		OS = oS;
	}

	@Override
	public int getID() {
		return phoneID;
	}

	public void setPhoneID(int phoneID) {
		this.phoneID = phoneID;
	}

	public String getOS() {
		return OS;
	}

	public void setOS(String oS) {
		OS = oS;
	}

	@Override
	public String toString() {
		return String.format("Item[phoneID='%s', OS='%s']", Integer.toString(phoneID), OS);
	}
}
