package com.nineplusten.app.service;

// check imports before commit
import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.User;
import com.nineplusten.app.model.UserRole;
import com.nineplusten.app.util.EqualUtil;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.security.SecureRandom;
import java.util.Collections;

public class CreateUserService extends Service<Void> {

  private StringProperty agencyName;
  private StringProperty username;
  private StringProperty password;
  private StringProperty email;
  private ObjectProperty<String> userRole;
  private Gson gson;

  public CreateUserService(StringProperty agencyName, StringProperty username,
      StringProperty password, StringProperty email, ObjectProperty<String> userRole) {
    // initialize class variables
    this.agencyName = agencyName;
    this.username = username;
    this.password = password;
    this.email = email;
    this.userRole = userRole;
  }

  @Override
  protected Task<Void> createTask() {
    return new Task<Void>() {
      @Override
      protected Void call() throws Exception {
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
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    // temp var to store result of posting the user into the database
    boolean result = false;

    // generate the encrypted password and identify the user role for the DB
    String salt = generateSalt();
    String dbPassword = makePassword(this.password.get(), salt);

    User user = new User();
    user.setUserId(this.username.get());
    user.setUserPw(dbPassword);
    user.setEmail(this.email.get());
    int roleIndex = Collections.binarySearch(Cache.userRoles, new UserRole(this.userRole.get()));
    if (roleIndex != -1) {
      user.setUserRole(Cache.userRoles.get(roleIndex));
    }
    if (agencyName.get() != null && !EqualUtil.isEmpty(agencyName.get())) {
      Agency agency = new Agency(this.agencyName.get());
      int agencyIndex = Collections.binarySearch(Cache.agencies, agency);
      if (agencyIndex >= 0) {
        user.setAgency(Cache.agencies.get(agencyIndex));
      } else {
        Agency createdAgency = createNewAgency(agency);
        if (createdAgency != null) {
          Cache.agencies.add(-agencyIndex - 1, createdAgency);
          user.setAgency(createdAgency);
        }
      }
    }

    user.setActive(true);
    user.setSalt(salt);
    String user_obj = gson.toJson(user);

    // post the JSON object into the database
    try {
      result = RestDbIO.post("/users", user_obj);
    } catch (UnirestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // DEBUGGING
    System.out.println(result);
  }

  /**
   * Encrypts the desired password using a hashing algorithm and prepending a salt for storage in
   * the database
   * 
   * @param currPassword is the users' desired password
   * @param salt is the salt generated to prepend onto the hashed password
   * @return the encrypted password
   */
  private String makePassword(String currPassword, String salt) {
    String password;
    password = salt + currPassword;
    password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    return password;
  }

  /**
   * Generates a random salt to prepend onto the hashed password
   * 
   * @return the salt
   */
  private String generateSalt() {
    SecureRandom saltGenerator = new SecureRandom();
    byte[] salt = new byte[16];
    saltGenerator.nextBytes(salt);
    // DEBUGGING
    System.out.println(salt);
    return new String(salt, StandardCharsets.UTF_8);
  }

  private Agency createNewAgency(Agency agency) {
    Agency created;
    String agencyJson = gson.toJson(agency);
    try {
      HttpResponse<JsonNode> response = RestDbIO.postResponse("/agencies", agencyJson);
      created = gson.fromJson(response.getBody().toString(), Agency.class);
    } catch (UnirestException e) {
      e.printStackTrace();
      created = null;
    }
    return created;
  }
}
