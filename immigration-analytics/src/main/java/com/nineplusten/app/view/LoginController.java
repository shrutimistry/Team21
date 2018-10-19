package com.nineplusten.app.view;

import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.common.hash.Hashing;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.util.RestDbIO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
  @FXML
  private TextField username;
  @FXML
  private PasswordField password;

  @FXML
  private void loginAction(ActionEvent e) {
    System.out.println(verifyUserCredentials(username.getText(), password.getText()));
  }

  private boolean verifyUserCredentials(String username, String password) {
    boolean verified = false;
    JSONArray userResult;
    try {
      // Retrieve JSONarray for login user, query by unique id
      userResult = RestDbIO.get("/users", "user_id", username);
    } catch (UnirestException e) {
      // Connection failure
      System.out.println("Failed to reach database: " + e.getStackTrace());
      return false;
    }
    // Attempt to retrieve single object from JSON array
    // If lookup failed, user == null
    JSONObject user = RestDbIO.singleResultToJSONObject(userResult);
    // If user exists, verify password
    if (user != null) {
      String salt = user.getString("salt");
      password = salt + password;
      String pwdLocalHash =
          Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
      String pwdDBHash = user.getString("user_pw");
      verified = pwdLocalHash.equalsIgnoreCase(pwdDBHash);
    }
    return verified;
  }

}
