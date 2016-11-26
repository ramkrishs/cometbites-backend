package com.cometbites.model;

public interface IExternalPayment {
	
	boolean authorize(int cardNumber);
	boolean charge(int cardNumber, double value);

}
