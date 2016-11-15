package store.cometbites.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;



import store.cometbites.rest.HelloWorld;
import store.cometbites.rest.Users;
import store.cometbites.rest.FoodJoints;



@Configuration
@ApplicationPath("api/v1/cometbites")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CorsFilter.class);
		register(HelloWorld.class);
		register(FoodJoints.class);
		register(Users.class);	
	}

}
