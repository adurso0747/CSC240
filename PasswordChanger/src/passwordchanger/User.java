package passwordchanger;

/**
 *
 * @author Alex Durso <ad901965@wcupa.edu>
 */

//User class allows organization of usernames and passwords as User objects that can be put into an arraylist
public class User {
	private String username;
	private String password;
	
	public User (String u, String p){
		username = u;
		password = p;
	}
	
	//Getters and setters
	public String getUsername(){
		return username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
}
