package store.cometbites.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import store.cometbites.rest.HelloWorld;



@Configuration
@ApplicationPath("api/v1/etrack")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(CorsFilter.class);
		register(HelloWorld.class);
			
	}

}
