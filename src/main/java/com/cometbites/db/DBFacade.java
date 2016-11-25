package com.cometbites.db;

import java.util.List;

import com.cometbites.model.FoodJoint;
import com.cometbites.model.Item;

public class DBFacade {
	
	//TODO implement methods
	
	public static DBFacade getInstance() {
		return new DBFacade();
	}
	
	public List<FoodJoint> getFoodJoints() {
		return null;
	}

	public List<Item> getMenu(String foodJointID) {
		return null;
	}

	public List<String> getPaymentOptions(int id) {
		return null;
	}

}
