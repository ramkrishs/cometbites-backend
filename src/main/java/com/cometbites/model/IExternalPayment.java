package com.cometbites.model;

public interface IExternalPayment {
	
	boolean authorize(long cardNumber);
	boolean charge(long cardNumber, double value);

}
