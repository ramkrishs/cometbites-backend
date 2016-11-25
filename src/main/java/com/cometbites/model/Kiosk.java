package com.cometbites.model;

public class Kiosk implements IDevice {
    private int kioskID;
    
	public Kiosk(int kioskID) {
		super();
		this.kioskID = kioskID;
	}
	
	@Override
	public int getID() {
		return kioskID;
	}
	
	public void setKioskID(int kioskID) {
		this.kioskID = kioskID;
	}

	@Override
    public String toString() {
        return String.format(
                "Item[phoneID='%s']",
                Integer.toString(kioskID));
    }
}
