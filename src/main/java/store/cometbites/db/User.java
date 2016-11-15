package store.cometbites.db;
import org.springframework.data.annotation.Id;


public class User {
	
	@Id
    public String id;

    public String firstName;
    public String lastName;
    public String netid;
    
    public User(){}
    
    public User(String firstName,String lastName,String netid){
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.netid = netid;
    }
    
    @Override
    public String toString() {
        return String.format(
                "User[id=%s, firstName='%s', lastName='%s',netid='%s']",
                id, firstName, lastName,netid);
    }

}
