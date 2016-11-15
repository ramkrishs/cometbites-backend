package store.cometbites.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Component
public class DatabaseManager {

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        // Set credentials      
        MongoCredential credential = MongoCredential.createCredential("root", "cometbites", ("github123").toCharArray());
        ServerAddress serverAddress = new ServerAddress("candidate.61.mongolayer.com", 11093);

        // Mongo Client
        MongoClient mongoClient = new MongoClient(serverAddress,Arrays.asList(credential)); 

        // Mongo DB Factory
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
                mongoClient, "cometbites");

        return simpleMongoDbFactory;
    }

    /**
     * Template ready to use to operate on the database
     * 
     * @return Mongo Template ready to use
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

}