package com.cometbites.rest;

import java.util.ArrayList;
import java.util.List;

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

import com.cometbites.db.DBFacade;
import com.cometbites.model.Status;
import com.cometbites.util.Util;
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

	@Autowired
	private DBFacade dBFacade;

	@GET
	public String getOrders() {
		JSONArray orders = new JSONArray();

		DBCollection ms = mongoTemplate.getCollection("orders");
		DBCursor cursor = ms.find();

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
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

	@GET
	@Path("/user/{netid}")
	public String getOrderByNetID(@PathParam("netid") String netid) {

		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid", netid);
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
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

	@GET
	@Path("/inpreparation/foodjoint/{fjID}")
	public String getInPreparationByFoodJointID(@PathParam("fjID") String fjID) {

		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", fjID);
		query.put("status", Status.IN_PREPARATION.getValue());
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
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
	
	@GET
	@Path("/prepared/foodjoint/{fjID}")
	public String getPreparedByFoodJointID(@PathParam("fjID") String fjID) {

		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", fjID);
		query.put("status", Status.PREPARED.getValue());
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
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
	
	@GET
	@Path("/pickup/foodjoint/{fjID}")
	public String getReadyForPickupByFoodJointID(@PathParam("fjID") String fjID) {

		DBCollection ms = mongoTemplate.getCollection("orders");
		JSONArray orders = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", fjID);
		query.put("status", Status.READY_FOR_PICKUP.getValue());
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
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

		while (cursor.hasNext()) {
			JSONObject order = new JSONObject();
			DBObject userObj = cursor.next();
			order.put("netid", userObj.get("netid"));
			order.put("fjID", userObj.get("fjID"));
			order.put("total", userObj.get("total"));
			order.put("cardNo", userObj.get("cardNo"));
			order.put("date", userObj.get("date"));
			order.put("invoice", userObj.get("invoice"));
			order.put("items", userObj.get("items"));
			orders.put(order);
		}

		if (orders.length() > 0) {
			return orders.get(0).toString();
		}

		return res.toString();
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveOrder(@FormParam("fjID") String fjID, @FormParam("total") String total,
			@FormParam("netid") String netid, @FormParam("cardNo") String cardNo, @FormParam("date") String date,
			@FormParam("items") String items, @Context HttpHeaders header, @Context HttpServletResponse response)
			throws Exception {

		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);

		JSONArray orders = new JSONArray(getOrders());
		String newInvoice = Util.generateNewInvoce(orders.length());

		try {
			document.put("fjID", fjID);
			document.put("total", total);
			document.put("netid", netid);
			document.put("cardNo", cardNo);
			document.put("date", date);
			document.put("invoice", newInvoice);

			List<DBObject> itemObjects = new ArrayList<>();

			JSONArray itemsJSON = new JSONArray(items);
			for (Object object : itemsJSON) {
				JSONObject itemJSON = (JSONObject) object;

				DBObject item = new BasicDBObject();
				item.put("name", itemJSON.get("name"));
				item.put("quantity", itemJSON.get("quantity"));
				item.put("price", itemJSON.get("price"));

				itemObjects.add(item);
			}

			document.put("items", itemObjects);

			DBCollection ms = mongoTemplate.getCollection("orders");
			// insert
			WriteResult result = ms.insert(document);

			if (result.wasAcknowledged()) {
				res.put("result", 200);
				res.put("invoice", newInvoice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res.toString();
	}

	public float getFoodJointWaitTime(int id) {
		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		DBObject query = new BasicDBObject();
		query.put("fjID", id);
		DBCursor cursor = ms.find(query);

		float waitTime = 0f;

		while (cursor.hasNext()) {
			DBObject foodJointObj = cursor.next();

			waitTime = Float.parseFloat(foodJointObj.get("wait_time").toString());
		}

		return waitTime;
	}

	@GET
	@Path("/update/kitchen/{invoice}")
	public String updateStatus(@PathParam("invoice") String invoice) {

		DBObject res = new BasicDBObject();
		res.put("result", 400);
		try {

			DBCollection ms = mongoTemplate.getCollection("orders");
			DBObject query = new BasicDBObject();
			query.put("invoice", invoice);

			DBObject update = new BasicDBObject();
			DBObject values = new BasicDBObject("status", Status.PREPARED.getValue());
			values.put("date", Util.getCurrentTime());
			update.put("$set", values);

			WriteResult result = ms.update(query, update);
			if (result.wasAcknowledged()) {
				res.put("result", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	@GET
	@Path("/update/pickup/{invoice}")
	public String completeOrder(@PathParam("invoice") String invoice) {

		DBObject res = new BasicDBObject();
		res.put("result", 400);
		try {

			DBCollection ms = mongoTemplate.getCollection("orders");
			DBObject query = new BasicDBObject();
			query.put("invoice", invoice);

			DBObject update = new BasicDBObject();

			DBObject values = new BasicDBObject("status", Status.READY_FOR_PICKUP.getValue());
			values.put("date", Util.getCurrentTime());
			update.put("$set", values);

			WriteResult result = ms.update(query, update);
			if (result.wasAcknowledged()) {
				String netid = dBFacade.getNetidFromInvoice(invoice);

				String phone = dBFacade.getPhonenumberbyNetid(netid);

				Util.SendSms(phone,  "Thank you for using CometBites!"
						+ "\nYour order is complete and is Ready for Pickup."
						+ "\nInvoice: "+invoice+".");
				res.put("result", 200);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res.toString();

	}

}
