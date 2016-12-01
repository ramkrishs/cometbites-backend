package com.cometbites.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Controller;

import com.cometbites.model.Customer;
import com.cometbites.model.FoodJoint;
import com.cometbites.model.Item;
import com.cometbites.model.LineItem;
import com.cometbites.model.Order;
import com.cometbites.model.Status;
import com.cometbites.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Controller
public class DBFacade {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public String getFoodJoints() {
		
		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		JSONArray foodJoints = new JSONArray();
		DBCursor cursor = ms.find();
		
		while(cursor.hasNext()) {
			JSONObject foodJoint = new JSONObject();
			DBObject foodJointObj =  cursor.next();
			foodJoint.put("name", foodJointObj.get("name"));
			foodJoint.put("logo", foodJointObj.get("logo"));
			foodJoint.put("fjID", foodJointObj.get("fjID"));
			foodJoint.put("closed_time", foodJointObj.get("closed_time"));
			foodJoint.put("open_time", foodJointObj.get("open_time"));
			foodJoint.put("wait_time", foodJointObj.get("wait_time"));
			foodJoints.put(foodJoint);
			
		}
		
		return foodJoints.toString();
	}

	public List<Item> getMenu(String foodJointID) {
		
		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		JSONArray foodJointMenu = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", Integer.parseInt(foodJointID));
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			DBObject foodJointObj =  cursor.next();
			foodJointMenu.put(foodJointObj.get("menu"));
		}
		
		JSONArray menu = new JSONArray(foodJointMenu.get(0).toString());
		Iterator<Object> iterator = menu.iterator();
		
		List<Item> itemList = new ArrayList<>();
		while(iterator.hasNext()) {
			JSONObject menuItem = new JSONObject(iterator.next().toString());
			
			Item item = new Item();
			
			item.setId(menuItem.getString("id"));
			item.setName(menuItem.getString("name"));
			item.setDescription(menuItem.getString("description"));
			item.setPrice(Double.parseDouble(menuItem.getString("price")));
			item.setImage(menuItem.getString("image"));
			
			itemList.add(item);
		}
		
		return itemList;
	}

	public String getPaymentOptions(String id) {
		
		DBCollection ms = mongoTemplate.getCollection("users");
		JSONArray paymentOptions = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid", id);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			DBObject user =  cursor.next();
			paymentOptions.put(user.get("paymentoptions"));
		}
		
		return paymentOptions.get(0).toString();
	}

	public String saveOrder(Order order) {
		DBObject document = new BasicDBObject();
		
		JSONArray orders = new JSONArray(getOrders());
		String newInvoice = Util.generateNewInvoce(orders.length());
		
		try{
			document.put("netid", order.getCustomer().getId());
			document.put("status", order.getStatus().getValue());
			document.put("total", Double.toString(order.getTotal()));
			document.put("date", Util.getCurrentTime());
			document.put("invoice", newInvoice);
			document.put("fjID", order.getFoodJoint().getId());

			List<DBObject> itemObjects = new ArrayList<>();
			
			for (LineItem lineItem : order.getOrderItems()) {
				DBObject dbItem = new BasicDBObject();
				
				dbItem.put("id", lineItem.getItem().getId());
				dbItem.put("name", lineItem.getItem().getName());
				dbItem.put("price", Double.toString(lineItem.getItem().getPrice()));
				dbItem.put("quantity", Integer.toString(lineItem.getQuantity()));
				
				itemObjects.add(dbItem);
			}
			
			document.put("items", itemObjects);
			
			DBCollection ms = mongoTemplate.getCollection("orders");

			ms.insert(document);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return newInvoice;
	}
	
	public void updateOrder(Order order) {
		DBCollection ms = mongoTemplate.getCollection("orders");
		DBObject query = new BasicDBObject();
		query.put("invoice", order.getInvoice());
			
		DBObject newOrder = new BasicDBObject();
		
		newOrder.put("netid", order.getCustomer().getId());
		newOrder.put("status", order.getStatus().getValue());
		newOrder.put("total", Double.toString(order.getTotal()));
		newOrder.put("date", Util.getCurrentTime());
		newOrder.put("invoice", order.getInvoice());
		newOrder.put("fjID", order.getFoodJoint().getId());
		
		List<DBObject> itemObjects = new ArrayList<>();
		
		for (LineItem lineItem : order.getOrderItems()) {
			DBObject dbItem = new BasicDBObject();
			
			dbItem.put("id", lineItem.getItem().getId());
			dbItem.put("name", lineItem.getItem().getName());
			dbItem.put("price", Double.toString(lineItem.getItem().getPrice()));
			dbItem.put("quantity", Integer.toString(lineItem.getQuantity()));
			
			itemObjects.add(dbItem);
		}
		newOrder.put("items", itemObjects);
		
		ms.update(query, newOrder);
	}
	
	public String getOrders() {
		JSONArray orders = new JSONArray();

		DBCollection ms = mongoTemplate.getCollection("orders");
		DBCursor cursor = ms.find();
		
		while(cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj =  cursor.next();
			order.put("netid", userObj.get("netid"));
			order.put("fjID", userObj.get("fjID"));
			order.put("total", userObj.get("total"));
			order.put("cardNo", userObj.get("cardNo"));
			order.put("date", userObj.get("date"));
			order.put("invoice", userObj.get("invoice"));
			order.put("items", userObj.get("items"));
			orders.put(order);
		}
		
		return orders.toString();
	}

	public Order getNewOrderByCustomerID(String customerID) {
		DBCollection ms = mongoTemplate.getCollection("orders");
		DBObject query = new BasicDBObject();
		query.put("netid", customerID);
		query.put("status", Status.NEW.getValue());
		DBCursor cursor = ms.find(query);
		
		Order order = null;
		while(cursor.hasNext()) {
			DBObject userObj =  cursor.next();
			
			order = new Order();
			order.setInvoice(userObj.get("invoice").toString());
			order.setFoodJoint(new FoodJoint(userObj.get("fjID").toString()));
			order.setTotal(Double.parseDouble(userObj.get("total").toString()));
			order.setStatus(Status.NEW);
			order.setCustomer(new Customer(userObj.get("netid").toString()));
			order.setDate(Util.parseDate(userObj.get("date").toString(), Util.ORDER_DATE_FORMAT));

			JSONArray orders = new JSONArray(userObj.get("items").toString());
			
			for (Object object : orders) {
				JSONObject itemObj = new JSONObject(object.toString());
				
				Item item = new Item();
				item.setId(itemObj.get("id").toString());
				item.setName(itemObj.get("name").toString());
				item.setPrice(Double.parseDouble(itemObj.get("price").toString()));
				
				order.addItem(item);
				order.updateQuantity(item, Integer.parseInt(itemObj.get("quantity").toString()));
			}
			
		}
		
		return order;
	}
	
	public String getPhonenumberbyNetid(String netid){
			User user = null;
		try {
			
			DBObject quer = new BasicDBObject();
			quer.put("netid", netid);
			BasicQuery query = new BasicQuery(quer);
			
			user = mongoTemplate.findOne(query, User.class);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user.getPhone();
	}
	
	public String updateOrderDocument(DBObject query,DBObject update){
		DBObject res = new BasicDBObject();
		res.put("result", 400);
	try {
		DBCollection ms = mongoTemplate.getCollection("orders");
		
		WriteResult result = ms.update(query, update);
		if(result.wasAcknowledged()){
			res.put("result", 200);
		}	
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return res.toString();
}
	
	
	

}
