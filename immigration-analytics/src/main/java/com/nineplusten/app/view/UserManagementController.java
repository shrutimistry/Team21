package com.nineplusten.app.view;

import com.nineplusten.app.App;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

class User {

  StringProperty userID;
  StringProperty email;
  StringProperty role;

  public User(String userID, String email, String role){
    userIDProperty().setValue(userID);
    emailProperty().setValue(userID);
    userIDProperty().setValue(userID);
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

public class UserManagementController {

  TableView<User> userTable;

  @FXML
  private void initialize() {
    
    userTable = new TableView<User>();
  }

  public void setMainApp(App mainApp) {
  }

}
