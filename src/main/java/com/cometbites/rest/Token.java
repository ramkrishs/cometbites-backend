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

import com.cometbites.util.Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Component
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
public class Token {

	@Autowired
	private MongoTemplate mongoTemplate;

	@GET
	@Path("/{netid}/send_token")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendCode(@PathParam("netid") String netid) {
		JSONArray transactions = new JSONArray();

		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);

		try {

			DBCollection ms = mongoTemplate.getCollection("users");

			DBObject query = new BasicDBObject();
			query.put("netid", netid);
			String phone = "";
			DBCursor cursor = ms.find(query);
			if (cursor.hasNext()) {
				DBObject userObj = cursor.next();
				phone = userObj.get("phone").toString();
			}
			DBObject value = new BasicDBObject();

			Integer randomPIN = (int) (Math.random() * 9000) + 1000;
			document.put("auth_code", randomPIN);
			value.put("$set", document);

			WriteResult result = ms.update(query, value);

			if (result.wasAcknowledged()) {
				Util.SendSms(phone, randomPIN);
				res.put("result", 200);
			}

		} catch (Exception e) {

		}

		return res.toString();
	}

	@POST
	@Path("/{netid}/verify_phone")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public String verifyCode(@PathParam("netid") String netid, @FormParam("code") String code,
			@Context HttpHeaders header, @Context HttpServletResponse response) throws Exception {

		JSONArray transactions = new JSONArray();
		DBObject document = new BasicDBObject();
		DBObject res = new BasicDBObject();
		res.put("result", 401);

		try {

			DBCollection ms = mongoTemplate.getCollection("users");

			DBObject query = new BasicDBObject();
			query.put("netid", netid);
			Integer auth_code = 0;
			DBCursor cursor = ms.find(query);
			if (cursor.hasNext()) {
				DBObject userObj = cursor.next();
				auth_code = Integer.parseInt(userObj.get("auth_code").toString());
			}

			if (auth_code == Integer.parseInt(code)) {
				res.put("result", 200);
			}

		} catch (Exception e) {

		}

		return res.toString();
	}

}
