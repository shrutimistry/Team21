package com.nineplusten.app.service;

import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.model.User;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoginService extends Service<User> {

  private ReadOnlyStringProperty username;
  private ReadOnlyStringProperty password;
  private Gson gson;

  public LoginService(ReadOnlyStringProperty username, ReadOnlyStringProperty password, Gson gson) {
    this.username = username;
    this.password = password;
    this.gson = gson;
  }

  @Override
  protected Task<User> createTask() {
    return new Task<User>() {
      @Override
      protected User call() throws Exception {
        JSONObject userJson = retrieveUserJson();
        User user;
        if (userJson != null) {
          user = gson.fromJson(userJson.toString(), User.class);
          user = verifyUserCredentials(user) ? user : null;
        } else {
          user = null;
          System.out.println("User not found.");
        }
        return user;
      }
    };
  }

  private JSONObject retrieveUserJson() {
    JSONArray userResult;
    try {
      // Retrieve JSONarray for login user, query by unique id
      userResult = RestDbIO.get("/users", "user_id", username.get());
    } catch (UnirestException e) {
      // Connection failure
      System.out.println("Failed to reach database:");
      e.printStackTrace();
      return null;
    }
    // Attempt to retrieve single object from JSON array
    // If lookup failed, user == null
    JSONObject user = RestDbIO.singleResultToJSONObject(userResult);
    return user;
  }

  private boolean verifyUserCredentials(User user) {
    boolean verified = false;
    String localPassword = user.getSalt() + password.get();
    String pwdLocalHash =
        Hashing.sha256().hashString(localPassword, StandardCharsets.UTF_8).toString();
    String pwdDBHash = user.getUserPw();
    verified = pwdLocalHash.equalsIgnoreCase(pwdDBHash);
    return verified;
  }

}
