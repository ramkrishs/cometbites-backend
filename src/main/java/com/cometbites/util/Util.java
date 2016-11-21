package com.cometbites.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Util {
	
	public static final String ACCOUNT_SID = "ACb447e910cc2c38920a8733f10210063e";
	public static final String AUTH_TOKEN = "a6556df008a04685cd0b5ea3070d2945";

	public static void SendSms(String phoneno, Integer code) {
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		
		String number = "+1" + phoneno;
		
		Message sms = Message
		        .creator(new PhoneNumber(number), new PhoneNumber("+14109819706"),
		            "Your Verification code " + code)
		        .create();
		
		System.out.println(sms.getSid());	
		
	}

}
