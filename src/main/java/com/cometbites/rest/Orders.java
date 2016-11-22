package com.cometbites.rest;

import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Component
@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
public class Orders {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GET
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
			orders.put(order);
		}
		
		return orders.toString();
	}
	
	
	@GET
	@Path("/user/{netid}")
	public String getOrderByNetID(@PathParam("netid") String netid) {
		
		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid",netid);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj =  cursor.next();
			order.put("netid", userObj.get("netid"));
			order.put("fjID", userObj.get("fjID"));
			order.put("total", userObj.get("total"));
			order.put("cardNo", userObj.get("cardNo"));
			order.put("date", userObj.get("date"));
			order.put("invoice", userObj.get("invoice"));
			orders.put(order);
		}
		
		return orders.toString();
	}
	
	@GET
	@Path("/foodjoint/{fjID}")
	public String getOrderByFoodJointID(@PathParam("fjID") String fjID) {
		
		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID",fjID);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj =  cursor.next();
			order.put("netid", userObj.get("netid"));
			order.put("fjID", userObj.get("fjID"));
			order.put("total", userObj.get("total"));
			order.put("cardNo", userObj.get("cardNo"));
			order.put("date", userObj.get("date"));
			order.put("invoice", userObj.get("invoice"));
			orders.put(order);
		}
		
		return orders.toString();
	}
	
	@GET
	@Path("/{invoice}")
	public String getOrderByInvoice(@PathParam("invoice") String invoice) {
		
		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("invoice", invoice);
		DBCursor cursor = ms.find(query);
		
		DBObject res = new BasicDBObject();
		res.put("result", 401);
		
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
		
		if(orders.length() > 0){
			return orders.get(0).toString();
		}
		
		return res.toString();
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveOrder(@FormParam("fjID") String fjID, @FormParam("total") String total, @FormParam("netid") String netid,
			@FormParam("cardNo") String cardNo, @FormParam("date") String date, @FormParam("invoice") String invoice, @FormParam("items") String items, 
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {
		
		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);
		try{
			document.put("fjID", fjID);
			document.put("total", total);
			document.put("netid", netid);
			document.put("cardNo", cardNo);
			document.put("date", date);
			document.put("invoice", invoice);
			JSONArray item_array = new JSONArray(items);
			document.put("items", item_array);
			DBCollection ms = mongoTemplate.getCollection("orders");
			//insert
			WriteResult result = ms.insert(document);
			
			if(result.wasAcknowledged()){
				res.put("result", 200);
			}
		}
		catch(Exception e){
		}
		
		return res.toString();
	}
	
	//TODO ADD ITEM (UNUSED)
	//FIXME NOT FINISHED
	@POST
	@Path("/{invoice}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String addItemToOrder(@PathParam("invoice") String invoice, @FormParam("item") String itemName, @FormParam("quantity") String quantity, 
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {
		
		try{
			JSONObject order = new JSONObject(getOrderByInvoice(invoice));
			JSONArray items = new JSONArray(order.get("items").toString());
			
			int newQty = Integer.parseInt(quantity);
			
			Iterator<Object> itemIterator = items.iterator();
			
			boolean exists = false;
			while(itemIterator.hasNext()) {
				JSONObject item = (JSONObject) itemIterator.next();
				
				if (item.get("name").equals(itemName)) {
					exists = true;
					
					int oldQty = Integer.parseInt(item.get("quantity").toString());
					
					item.put("quantity", Integer.toString(oldQty + newQty));
					break;
				}
			}
			
			if(!exists) {
				JSONObject newItem = new JSONObject();
				
				newItem.put("name", itemName);
				newItem.put("quantity", quantity);
				
				items.put(newItem);
			}
			
			//FIXME UPDATE ON DB
//			DBCollection ms = mongoTemplate.getCollection("orders");
			
//			WriteResult result = ms.update(query, value);
			
			return order.toString();
			
		}
		catch(Exception e){
			System.out.println("error?");
		}
		
		return "401";
	}
	
}
