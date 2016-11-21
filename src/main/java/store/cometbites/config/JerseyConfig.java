package store.cometbites.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import store.cometbites.rest.Checkout;
import store.cometbites.rest.FoodJoints;
import store.cometbites.rest.HelloWorld;
import store.cometbites.rest.Orders;
import store.cometbites.rest.Transactions;
import store.cometbites.rest.Users;



@Configuration
@ApplicationPath("api/v1/cometbites")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CorsFilter.class);
		register(HelloWorld.class);
		register(FoodJoints.class);
		register(Users.class);	
		register(Checkout.class);
		register(Transactions.class);
		register(Orders.class);
	}

}
