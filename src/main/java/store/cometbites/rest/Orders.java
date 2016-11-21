package store.cometbites.rest;

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
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveOrder(@FormParam("fjID") String fjID, @FormParam("total") String total, @FormParam("netid") String netid,
			@FormParam("cardNo") String cardNo, @FormParam("date") String date, @FormParam("invoice") String invoice, 
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
	
	//TODO addItems
	
}
