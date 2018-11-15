package com.nineplusten.app.view;

import com.nineplusten.app.App;
import com.nineplusten.app.service.LoadUserService;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

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

public class AccountManagementController {

  TableView<User> userTable;
  LoadUserService userService;

  private App mainApp;

  @FXML
  private void initialize() {

    userTable = new TableView<User>();

    TableColumn<User, String> userIDColumn = new TableColumn<>("User ID");
    TableColumn<User, String> emailColumn = new TableColumn<>("Email");
    TableColumn<User, String> roleColumn = new TableColumn<>("Role");

    userIDColumn.setCellValueFactory(new PropertyValueFactory("userID"));
    emailColumn.setCellValueFactory(new PropertyValueFactory("email"));
    roleColumn.setCellValueFactory(new PropertyValueFactory("role"));

    userTable.getColumns().setAll(userIDColumn, emailColumn, roleColumn);

    userService = new LoadUserService();
    userService.start();
    userService.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
      @Override 
      public void handle(WorkerStateEvent e) { 
        initializeTable();
      } 
    });
    
  }

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  private ObservableList<User> parseUsers(){
    List<String[]> userData = userService.getValue();
    
    List<User> userList = new ArrayList<>();

    for (int i = 0; i < userData.size(); i++){
        String[] tempArray = userData.get(i);
        User tempUser = new User(tempArray[0], tempArray[1], tempArray[2]);
        userList.add(tempUser);
    }

   
    return toObservableList(userList);
  }

  private ObservableList<User> toObservableList(List<User> list){
    ObservableList<User> observableList = FXCollections.observableArrayList();

    for (int i = 0; i < list.size(); i++){
      observableList.add(list.get(i));
    }

    return observableList;
  }

  private void initializeTable(){
    userTable.getItems().addAll(parseUsers());
  }

}
