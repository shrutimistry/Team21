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
import com.nineplusten.app.constant.Routes;
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

public class CreateUserService extends Service<Boolean> {

  private StringProperty agencyName;
  private StringProperty username;
  private StringProperty password;
  private StringProperty email;
  private ObjectProperty<UserRole> userRole;
  private Gson gson;

  public CreateUserService(StringProperty agencyName, StringProperty username,
      StringProperty password, StringProperty email, ObjectProperty<UserRole> userRole) {
    // initialize class variables
    this.agencyName = agencyName;
    this.username = username;
    this.password = password;
    this.email = email;
    this.userRole = userRole;
  }

  @Override
  protected Task<Boolean> createTask() {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws Exception {
        // creates a user in the database
        return createUserInstance();
      }
    };

  }

  /**
   * Instantiates a new user object and JSON object and posts it onto the database
   * 
   */
  public boolean createUserInstance() {
    boolean userCreated;
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    // generate the encrypted password and identify the user role for the DB
    String salt = generateSalt();
    String dbPassword = makePassword(this.password.get(), salt);

    User user = new User();
    user.setUserId(this.username.get().toLowerCase());
    user.setUserPw(dbPassword);
    user.setEmail(this.email.get());
    user.setUserRole(this.userRole.get());
    if (agencyName.get() != null && !EqualUtil.isEmpty(agencyName.get())) {
      Agency agency = new Agency(this.agencyName.get());
      Agency createdAgency = createNewAgency(agency);
      if (createdAgency != null) {
        user.setAgency(createdAgency);
        Cache.agencies.add(createdAgency);
      }
    }

    user.setActive(true);
    user.setSalt(salt);
    String user_obj = gson.toJson(user);

    // post the JSON object into the database
    try {
      userCreated = RestDbIO.post(Routes.USERS, user_obj);
    } catch (UnirestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      userCreated = false;
    }
    return userCreated;

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
    return new String(salt, StandardCharsets.UTF_8);
  }

  private Agency createNewAgency(Agency agency) {
    Agency created;
    String agencyJson = gson.toJson(agency);
    try {
      HttpResponse<JsonNode> response = RestDbIO.postResponse(Routes.AGENCIES, agencyJson);
      created = gson.fromJson(response.getBody().toString(), Agency.class);
    } catch (UnirestException e) {
      e.printStackTrace();
      created = null;
    }
    return created;
  }
}
