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
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class Users {

	@Autowired
	private MongoTemplate mongoTemplate;

	@GET
	@Path("/{netid}")
	public String getProfile(@PathParam("netid") String netid) {

		DBCollection ms = mongoTemplate.getCollection("users");
		JSONArray users = new JSONArray();
		DBObject query = new BasicDBObject();
		query.put("netid", netid);
		DBCursor cursor = ms.find(query);

		while (cursor.hasNext()) {
			JSONObject user = new JSONObject();
			DBObject userObj = cursor.next();
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
		query.put("netid", netid);
		DBCursor cursor = ms.find(query);

		DBObject res = new BasicDBObject();
		res.put("result", "Resource not found");

		while (cursor.hasNext()) {
			DBObject userObj = cursor.next();
			users.put(userObj.get("paymentoptions"));
		}

		if (users.length() > 0) {
			return users.get(0).toString();
		}

		return res.toString();
	}

	@POST
	@Path("/{netid}/payment")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String addPaymentOption(@PathParam("netid") String netid, @FormParam("cardname") String cardname,
			@FormParam("cardno") String cardno, @FormParam("cvv") String cvv, @FormParam("expdate") String expdate,
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {

		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);

		try {
			document.put("cardname", cardname);
			document.put("cardno", cardno);

			if (cvv != null && !"".equals(cvv)) {
				document.put("cvv", cvv);
				document.put("expdate", expdate);
			}

			DBCollection ms = mongoTemplate.getCollection("users");

			DBObject query = new BasicDBObject();
			query.put("netid", netid);

			DBObject value = new BasicDBObject();
			DBObject newOption = new BasicDBObject();

			newOption.put("paymentoptions", document);
			value.put("$push", newOption);

			WriteResult result = ms.update(query, value);

			if (result.wasAcknowledged()) {
				res.put("result", 200);
			}
		} catch (Exception e) {
		}

		return res.toString();
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveUser(@FormParam("firstname") String firstname, @FormParam("lastname") String lastname,
			@FormParam("netid") String netid, @FormParam("emailid") String emailid, @FormParam("phone") String phone,
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {

		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);
		try {
			document.put("firstname", firstname);
			document.put("lastname", lastname);
			document.put("netid", netid);
			document.put("emailid", emailid);
			document.put("phone", phone);
			DBCollection ms = mongoTemplate.getCollection("users");

			// insert
			WriteResult result = ms.insert(document);

			if (result.wasAcknowledged()) {
				res.put("result", 200);
			}
		} catch (Exception e) {
		}

		return res.toString();
	}

}
