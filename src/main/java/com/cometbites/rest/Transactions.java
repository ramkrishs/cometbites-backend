package com.cometbites.rest;

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
@Path("transactions")
@Produces(MediaType.APPLICATION_JSON)
public class Transactions {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GET
	public String getTransactions() {
		JSONArray transactions = new JSONArray();

		DBCollection ms = mongoTemplate.getCollection("transactions");
		DBCursor cursor = ms.find();
		
		while(cursor.hasNext()) {
			JSONObject transaction = new JSONObject();
			DBObject userObj =  cursor.next();
			transaction.put("netid", userObj.get("netid"));
			transaction.put("fjID", userObj.get("fjID"));
			transaction.put("amount", userObj.get("amount"));
			transaction.put("cardNo", userObj.get("cardNo"));
			transaction.put("date", userObj.get("date"));
			transaction.put("invoice", userObj.get("invoice"));
			transactions.put(transaction);
		}
		
		return transactions.toString();
	}
	
	@GET
	@Path("/user/{netid}")
	public String getTransactionByNetID(@PathParam("netid") String netid) {
		
		DBCollection ms = mongoTemplate.getCollection("transactions");
		JSONArray transactions = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid",netid);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			JSONObject transaction = new JSONObject();
			DBObject userObj =  cursor.next();
			transaction.put("netid", userObj.get("netid"));
			transaction.put("fjID", userObj.get("fjID"));
			transaction.put("amount", userObj.get("amount"));
			transaction.put("cardNo", userObj.get("cardNo"));
			transaction.put("date", userObj.get("date"));
			transaction.put("invoice", userObj.get("invoice"));
			transactions.put(transaction);
		}
		
		return transactions.toString();
	}
	
	@GET
	@Path("/foodjoint/{fjID}")
	public String getTransactionByFoodJointID(@PathParam("fjID") String fjID) {
		
		DBCollection ms = mongoTemplate.getCollection("transactions");
		JSONArray transactions = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("fjID",fjID);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			JSONObject transaction = new JSONObject();
			DBObject userObj =  cursor.next();
			transaction.put("netid", userObj.get("netid"));
			transaction.put("fjID", userObj.get("fjID"));
			transaction.put("amount", userObj.get("amount"));
			transaction.put("cardNo", userObj.get("cardNo"));
			transaction.put("date", userObj.get("date"));
			transaction.put("invoice", userObj.get("invoice"));
			transactions.put(transaction);
		}
		
		return transactions.toString();
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveTransaction(@FormParam("fjID") String fjID, @FormParam("amount") String amount, @FormParam("netid") String netid,
			@FormParam("cardNo") String cardNo, @FormParam("date") String date, @FormParam("invoice") String invoice, 
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {
		
		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);
		try{
			document.put("fjID", fjID);
			document.put("amount", amount);
			document.put("netid", netid);
			document.put("cardNo", cardNo);
			document.put("date", date);
			document.put("invoice", invoice);
			DBCollection ms = mongoTemplate.getCollection("transactions");
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

}
