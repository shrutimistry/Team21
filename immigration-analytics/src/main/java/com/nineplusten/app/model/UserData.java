package com.nineplusten.app.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserData {

    private StringProperty userID;
    private StringProperty email;
    private StringProperty role;
    
    public UserData(String userID, String email, String role){
      userIDProperty().setValue(userID);
      emailProperty().setValue(email);
      roleProperty().setValue(role);
    }
  
    public StringProperty userIDProperty() {
      if (userID == null){
        userID = new SimpleStringProperty(this, "userID");
      }
  
      return userID;
    }
  
    public StringProperty emailProperty() {
      if (email == null){
        email = new SimpleStringProperty(this, "email");
      }
      return email;
    }
  
    public StringProperty roleProperty() {
      if (role == null){
        role = new SimpleStringProperty(this, "role");
      }
      return role;
    }
  }