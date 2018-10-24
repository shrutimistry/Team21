package com.nineplusten.app.service;

// check imports before commit
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import com.google.common.hash.Hashing;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.model.User;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.security.SecureRandom;

public class CreateUserService extends Service<User>{
	
	private StringProperty agencyName;
	private StringProperty username;
	private StringProperty password;
	private StringProperty email;
	private String userRole;
	
	public CreateUserService(StringProperty agencyName, StringProperty username, 
			StringProperty password, StringProperty email, String userRole) {
		// initialize class variables
		this.agencyName = agencyName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.userRole = userRole;
	}

	@Override
	protected Task<User> createTask() {
		return new Task<User>() {
			@Override
			protected User call() throws Exception {
				// creates a user in the database
				createUserInstance();
				return null;
			}
		};
		
	}
	
	/**
	 * Instantiates a new user object and JSON object and posts it onto the database
	 * 
	 */
	public void createUserInstance() {
		// temp var to store result of posting the user into the database
		boolean result = false;
		
		// *** not sure if necessary to create an actual user object
		User user = new User();
		user.setId(this.agencyName.get());
		user.setUserId(this.username.get());
		user.setUserPw(this.password.get());
		user.setEmail(this.email.get());
		//user.setRole(this.userRole);
		
		// generate the encrypted password and identify the user role for the DB
		byte[] salt = generateSalt();
		String dbPassword = makePassword(this.password.get(), salt);
		String dbRole = identifyRole(this.userRole);
		
		// create a new JSON object with the necessary information
		JSONObject user_obj = new JSONObject();
		user_obj.put("user_id", this.username.get());
		user_obj.put("user_role", dbRole);
		user_obj.put("user_pw", dbPassword);
		user_obj.put("email", this.email.get());
		user_obj.put("active", true);
		user_obj.put("salt", salt);
		
		// post the JSON object into the database
		try {
			result = RestDbIO.post("/users", user_obj);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//DEBUGGING
		System.out.println(result);
	}
	
	/**
	 * Encrypts the desired password using a hashing algorithm and prepending a salt for storage in the database
	 * 
	 * @param currPassword is the users' desired password
	 * @param salt is the salt generated to prepend onto the hashed password
	 * @return the encrypted password
	 */
	private String makePassword(String currPassword, byte[] salt) {
		String password; 
		password = Hashing.sha256().hashString(currPassword, StandardCharsets.UTF_8).toString();
		password = salt + password;
		return password;
	}
	
	/**
	 * Generates a random salt to prepend onto the hashed password
	 * 
	 * @return the salt 
	 */
	private byte[] generateSalt() {
		SecureRandom saltGenerator = new SecureRandom();
		byte[] salt = new byte[16];
		saltGenerator.nextBytes(salt);
		//DEBUGGING
		System.out.println(salt);
		return salt;
	}
	
	/**
	 * Identifies the corresponding role in the database to the role the user selected 
	 * 
	 * @param currRole is the role chosen by the user 
	 * @return the roles appropriate abbreviation as required by the database
	 */
	private String identifyRole(String currRole) {
		String result = "";
		if (currRole == "TEQ") {
			// need to figure out how to differ from higher levels of TEQ
			result = "TEQL";
		}
		else if (currRole == "UTSC") {
			result = "UTSC";
		}
		else if (currRole == "Agency") {
			result = "AGNT";
		}
		return result;
	}

}
