package com.cometbites.rest;

import javax.ws.rs.GET;
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

@Component
@Path("foodjoints")
@Produces(MediaType.APPLICATION_JSON)
public class FoodJoints {
	@Autowired
	private MongoTemplate mongoTemplate;

	@GET
	public String message(@Context HttpHeaders headers) {

		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		JSONArray foodJoints = new JSONArray();
		DBCursor cursor = ms.find();
		while (cursor.hasNext()) {
			JSONObject foodJoint = new JSONObject();
			DBObject foodJointObj = cursor.next();
			foodJoint.put("name", foodJointObj.get("name"));
			foodJoint.put("logo", foodJointObj.get("logo"));
			foodJoint.put("fjID", foodJointObj.get("fjID"));
			foodJoint.put("closed_time", foodJointObj.get("closed_time"));
			foodJoint.put("open_time", foodJointObj.get("open_time"));
			foodJoint.put("wait_time", foodJointObj.get("wait_time"));
			foodJoint.put("menu", foodJointObj.get("menu"));
			foodJoints.put(foodJoint);

		}

		return foodJoints.toString();
	}

	@GET
	@Path("/{id}")
	public String getFoodJoint(@PathParam("id") String id) {

		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		JSONArray foodJoints = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", Integer.parseInt(id));
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject foodJoint = new JSONObject();
			DBObject foodJointObj = cursor.next();
			foodJoint.put("name", foodJointObj.get("name"));
			foodJoint.put("logo", foodJointObj.get("logo"));
			foodJoint.put("fjID", foodJointObj.get("fjID"));
			foodJoint.put("closed_time", foodJointObj.get("closed_time"));
			foodJoint.put("open_time", foodJointObj.get("open_time"));
			foodJoint.put("wait_time", foodJointObj.get("wait_time"));
			foodJoint.put("menu", foodJointObj.get("menu"));
			foodJoints.put(foodJoint);

		}

		return foodJoints.toString();
	}

	@GET
	@Path("/{id}/menu")
	public String getMenu(@PathParam("id") String id) {

		DBCollection ms = mongoTemplate.getCollection("foodjoints");
		JSONArray foodJointMenu = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID", Integer.parseInt(id));
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			DBObject foodJointObj = cursor.next();
			foodJointMenu.put(foodJointObj.get("menu"));
		}

		return foodJointMenu.get(0).toString();
	}

}
