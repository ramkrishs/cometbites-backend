package com.cometbites.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.cometbites.config.DatabaseManager;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Component
@Path("hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorld {
	private static Logger log = LogManager.getLogger();
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@GET
	public String message(@Context HttpHeaders headers) {
		
		DBCollection ms = mongoTemplate.getCollection("users");
		DBCursor cursor = ms.find();
		while(cursor.hasNext()) {
		    System.out.println(cursor.next());
		}

		log.info(ms.find().limit(10));
		
		

		return "{\"result\": \"Hello CometBites\"}";
	}

}
