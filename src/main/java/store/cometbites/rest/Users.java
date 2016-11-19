package store.cometbites.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import store.cometbites.config.DatabaseManager;

@Component
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class Users {
	private static Logger log = LogManager.getLogger();
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@GET
	@Path("/{netid}")
	public String getProfile(@PathParam("netid") String netid) {
		
		DBCollection ms = mongoTemplate.getCollection("users");
		JSONArray users = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid",netid);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			JSONObject user = new JSONObject();
			DBObject userObj =  cursor.next();
			user.put("netid", userObj.get("netid"));
			user.put("firstname", userObj.get("firstname"));
			user.put("lastname", userObj.get("lastname"));
			user.put("emailid", userObj.get("emailid"));
			users.put(user);
		}
		
		return users.toString();
	}
	@GET
	@Path("/{netid}/payment")
	public String getPayment(@PathParam("netid") String netid) {
		
		DBCollection ms = mongoTemplate.getCollection("users");
		JSONArray users = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid",netid);
		DBCursor cursor = ms.find(query);
		
		while(cursor.hasNext()) {
			DBObject userObj =  cursor.next();
			users.put(userObj.get("paymentoptions"));
		}
		
		return users.get(0).toString();
	}

}
