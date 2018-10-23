package com.nineplusten.app.service;

// check imports before commit
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.model.User;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.security.SecureRandom;

public class CreateUserService /*extends Service<User>*/{
	
	private StringProperty agencyName;
	private StringProperty username;
	private StringProperty password;
	private StringProperty email;
	private String userRole;
	
	public CreateUserService(StringProperty agencyName, StringProperty username, 
			StringProperty password, StringProperty email, String userRole) {
		this.agencyName = agencyName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.userRole = userRole;
	}

	/*@Override
	protected Task<User> createTask() {
		// TODO Auto-generated method stub
		return new Task<User>() {
			@Override
			protected User call() throws Exception {
				// TODO Auto-generated method stub
				createUserInstance();
				return null;
			}
		};
		
	}*/
	
	public void createUserInstance() {
		boolean result = false;
		
		User user = new User();
		user.setId(this.agencyName.get());
		user.setUserId(this.username.get());
		user.setUserPw(this.password.get());
		user.setEmail(this.email.get());
		user.setRole(this.userRole);
		
		//String dbPassword = makePassword(this.password);
		
		JSONObject user_obj = new JSONObject();
		user_obj.put("user_id", this.username.get());
		user_obj.put("user_role", this.userRole);
		user_obj.put("user_pw", this.password.get());
		user_obj.put("email", this.email.get());
		
		try {
			result = RestDbIO.post("/users", user_obj);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result);
	}
	
	/*private String makePassword(String currPassword) {
		String password; 
		password = Hashing.sha256().hashString(currPassword, StandardCharsets.UTF_8).toString();
		//password = 
	}*/

}
