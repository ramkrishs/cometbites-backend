package com.cometbites.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.cometbites.model.Customer;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import com.google.firebase.tasks.Task;
import com.google.firebase.FirebaseApp;

@Controller
public class Auth {
	
	Map<String,Customer> customerList;
	FirebaseOptions options; 
	FirebaseApp fbApp;
			
	public Auth() throws FileNotFoundException {
		customerList = new HashMap<>();
		options = new FirebaseOptions.Builder()
				  .setServiceAccount(new FileInputStream("src/main/resources/privatekey.json"))
				  .setDatabaseUrl("https://cometbites.firebaseio.com/")
				  .build();
		fbApp = FirebaseApp.initializeApp(options);

	}
	
	public boolean isUserExist(String uid){
//		String token;
//		FirebaseAuth auth = FirebaseAuth.getInstance(fbApp);
//		auth.createCustomToken(uid)
//		.addOnSuccessListener(new OnSuccessListener<String>() {
//	        @Override
//	        public void onSuccess(String customToken) {
//	            System.out.println(customToken);
//	            token = customToken;
//	        }
//		});
		
        
		
		return customerList.containsKey(uid);
	}
	
	public void setCustomerID(String uid,String customerID){
		customerList.put(uid, new Customer(customerID));
	}
	
	public String getCustomerID(String uid){
		
		return customerList.get(uid).getId();
	}

}
