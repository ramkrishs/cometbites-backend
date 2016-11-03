package store.cometbites.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;



@Component
@Path("hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorld {
	private static Logger log = LogManager.getLogger();

	@GET
	public String message(@Context HttpHeaders headers) {
		log.info("Inside HelloWorld");

		

		return "{\"result\": \"Hello CometBites\"}";
	}

}
