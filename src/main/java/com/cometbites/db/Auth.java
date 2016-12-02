package com.cometbites.db;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.cometbites.model.Customer;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Controller
public class Auth {

	Map<String, Customer> customerList;
	FirebaseOptions options;
	FirebaseApp fbApp;

	public Auth() throws FileNotFoundException {
		customerList = new HashMap<>();

	}

	public boolean isUserExist(String uid) {
		return customerList.containsKey(uid);
	}

	public void setCustomerID(String uid, String customerID) {
		customerList.put(uid, new Customer(customerID));
	}

	public String getCustomerID(String uid) {

		return customerList.get(uid).getId();
	}

}
