package com.cometbites.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.cometbites.rest.Checkout;
import com.cometbites.rest.FoodJoints;
import com.cometbites.rest.Orders;
import com.cometbites.rest.Register;
import com.cometbites.rest.Token;
import com.cometbites.rest.Transactions;
import com.cometbites.rest.Users;

@Configuration
@ApplicationPath("api/v1/cometbites")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CorsFilter.class);
		register(FoodJoints.class);
		register(Users.class);
		register(Checkout.class);
		register(Transactions.class);
		register(Orders.class);
		register(Token.class);
		register(Register.class);

	}

}
