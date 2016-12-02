package com.cometbites.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

@Component
@Path("pay")
@Produces(MediaType.APPLICATION_JSON)
public class Checkout {

	private static BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "4pnfw7bk3cwpygw8",
			"y632bwgb8d2ssg77", "3e5f7f7033cfa2fc91c7025b947f772a");

	@GET
	@Path("/client_token")
	public String getClientToken() {
		String token = "";
		try {
			token = gateway.clientToken().generate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return token;
	}

}
