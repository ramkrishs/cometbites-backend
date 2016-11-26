package com.cometbites.model;

public class UTDPaymentAdapter implements IExternalPayment {

	private static UTDPaymentAdapter instance;
	
	public static synchronized UTDPaymentAdapter getInstance() {
		if(instance == null) {
			instance = new UTDPaymentAdapter();
		}
		return instance;
	}

	// TODO Implement real dummy operations
	@Override
	public boolean authorize(int cardNumber) {
		return true;
	}

	@Override
	public boolean charge(int cardNumber, double value) {
		return true;
	}

}
