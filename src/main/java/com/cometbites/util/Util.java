package com.cometbites.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cometbites.model.Customer;
import com.cometbites.model.Order;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Util {
	
	//FIXME decide on customers online
	public static Map<Customer, Order> currentCustomers = new HashMap<>();
	
	public static final String ACCOUNT_SID = "ACb447e910cc2c38920a8733f10210063e";
	public static final String AUTH_TOKEN = "a6556df008a04685cd0b5ea3070d2945";
	public static final String INVOICE_PREFIX = "CB";

	public static void SendSms(String phoneno, Integer code) {
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		
		String number = "+1" + phoneno;
		
		Message sms = Message
		        .creator(new PhoneNumber(number), new PhoneNumber("+14109819706"),
		            "Your Verification code " + code)
		        .create();
		
		System.out.println(sms.getSid());	
		
	}
	
	public static String generateNewInvoce(int numberOfItems) {
		Calendar cal = Calendar.getInstance();
		
		StringBuilder newInvoce = new StringBuilder();
		
		newInvoce.append(INVOICE_PREFIX);
		
		newInvoce.append(cal.get(Calendar.MONTH)+1);
		newInvoce.append(cal.get(Calendar.DATE));
		newInvoce.append(cal.get(Calendar.YEAR));
		
		newInvoce.append(numberOfItems+1);
		
		return newInvoce.toString();
	}
	
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssa");

		return format.format(cal.getTime());
	}
	
	public static Date parseExpirationDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		
		try {
			return format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
