package com.nineplusten.app.view;

import com.nineplusten.app.service.LoadUserDataService;
import com.nineplusten.app.service.DeleteUserService;
import com.nineplusten.app.model.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import java.util.List;
import javafx.event.ActionEvent;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AccountManagementController {

  @FXML
  private TableView<UserData> userTable;
  @FXML
  private TableColumn<UserData, String> userIDColumn;
  @FXML
  private TableColumn<UserData, String> emailColumn;
  @FXML
  private TableColumn<UserData, String> roleColumn;
  @FXML
  private Button refreshButton;
  @FXML
  private Button deleteButton;

  LoadUserDataService userService;

  DeleteUserService deleteService;

  @FXML
  private void refreshPage(ActionEvent event) {
    if (!userService.isRunning()) {
      userService.restart();
    }
  }

  @FXML
  private void deleteUser(ActionEvent event) {
    UserData selectedItem = userTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      String userID = selectedItem.userIDProperty().getValue();

      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Confirm");
      alert.setHeaderText("Deleting User");
      alert.setContentText("Are you sure you want to delete the user: " + userID + "?");

      alert.showAndWait();

      if (alert.getResult().getText().equals("OK")) {
        deleteService = new DeleteUserService(userID);
        deleteService.start();
        deleteService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
          @Override
          public void handle(WorkerStateEvent e) {
            userService.restart();
          }
        });
      }
    }
  }

  @SuppressWarnings("unchecked")
  @FXML
  private void initialize() {
    userService = new LoadUserDataService();
    userIDColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("userID"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("email"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("role"));

    userTable.getColumns().setAll(userIDColumn, emailColumn, roleColumn);
    userTable.setPlaceholder(new Label("No users found."));
    userTable.disableProperty().bind(userService.runningProperty());
    refreshButton.disableProperty().bind(userService.runningProperty());
    deleteButton.disableProperty()
        .bind(userService.runningProperty().or(userTable.getSelectionModel().selectedItemProperty().isNull()));

    userService.start();
    userService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent e) {
        userTable.setItems(parseUsers());
        userTable.getColumns().setAll(userIDColumn, emailColumn, roleColumn);
      }
    });
    userService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = userService.getException();
        throwable.printStackTrace();
      }
    });
  }

  private ObservableList<UserData> parseUsers() {
    List<String[]> userData = userService.getValue();

    List<UserData> userList = new ArrayList<>();

    for (int i = 0; i < userData.size(); i++) {
      String[] tempArray = userData.get(i);
      UserData tempUser = new UserData(tempArray[0], tempArray[1], tempArray[2]);
      userList.add(tempUser);
    }

    return toObservableList(userList);
  }

  private ObservableList<UserData> toObservableList(List<UserData> list) {
    return FXCollections.observableArrayList(list);
  }
}
