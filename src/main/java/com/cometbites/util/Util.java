package com.cometbites.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Util {

	public static final String ACCOUNT_SID = "ACb447e910cc2c38920a8733f10210063e";
	public static final String AUTH_TOKEN = "a6556df008a04685cd0b5ea3070d2945";
	public static final String INVOICE_PREFIX = "CB";

	public static final String ORDER_DATE_FORMAT = "MM/dd/yyyy HH:mm:ssa";
	public static final String EXP_DATE_FORMAT = "MM/dd/yyyy";

	public static void SendSms(String phoneno, Integer code) {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		String number = "+1" + phoneno;

		Message sms = Message
				.creator(new PhoneNumber(number), new PhoneNumber("+14109819706"), "Your Verification code " + code)
				.create();

		System.out.println(sms.getSid());

	}

	public static void SendSms(String phoneno, String customMessage) {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		String number = "+1" + phoneno;

		Message sms = Message.creator(new PhoneNumber(number), new PhoneNumber("+14109819706"), customMessage).create();

		System.out.println(sms.getSid());

	}

	public static String generateNewInvoce(int numberOfItems) {
		Calendar cal = Calendar.getInstance();

		StringBuilder newInvoce = new StringBuilder();

		newInvoce.append(INVOICE_PREFIX);

		newInvoce.append(cal.get(Calendar.MONTH) + 1);
		newInvoce.append(cal.get(Calendar.DATE));
		newInvoce.append(cal.get(Calendar.YEAR));

		newInvoce.append(numberOfItems + 1);

		return newInvoce.toString();
	}

	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(ORDER_DATE_FORMAT);

		return format.format(cal.getTime());
	}

	public static Date parseDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
