package com.nineplusten.app.view;

import java.util.stream.Collectors;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.service.CreateUserService;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AccountCreationController {

  // These items are for the combox set up
  @FXML
  private ComboBox<String> comboBox;
  @FXML
  private Label comboBoxLabel;
  @FXML
  private TextField AgencyNameText;
  @FXML
  private TextField UserNameText;
  @FXML
  private PasswordField PasswordText;
  @FXML
  private TextField EmailText;
  @FXML
  private VBox vboxholder;
  @FXML
  private Button submitButton;
  @FXML
  private Button clearButton;
  @FXML
  private ProgressIndicator submitProgress;

  private CreateUserService create;

  public AccountCreationController() {}

  /**
   * This will show text for agency only
   *
   */
  @FXML
  private void comboBoxWasUpdate() {
    if (comboBox.getValue().toString() == "Agency") {
      vboxholder.setVisible(true);
    } else {
      vboxholder.setVisible(false);
    }

  }

  /**
   * this is action call for submit button
   * 
   * @param action
   */
  @FXML
  private void submitButtonAction(ActionEvent action) {
    // checks status of the user accounts service
    if (!create.isRunning()) {
      create.restart();
    }
  }

  /**
   * this is action call for cancel button
   */
  @FXML
  private void clearButtonAction(ActionEvent action) {
    clearForm();
  }

  private void clearForm() {
    UserNameText.clear();
    PasswordText.clear();
    EmailText.clear();
    comboBox.getSelectionModel().clearSelection();
    AgencyNameText.clear();
  }

  /**
   * This just atuo call for the ui
   */
  @FXML
  private void initialize() {
    // this is for configuring the comboBox
    comboBox.getItems().addAll(
        Cache.userRoles.stream().map(role -> role.getRoleName()).collect(Collectors.toList()));
    vboxholder.managedProperty().bind(vboxholder.visibleProperty());
    vboxholder.setVisible(false);

    // creates a new user account service
    create = new CreateUserService(AgencyNameText.textProperty(), UserNameText.textProperty(),
        PasswordText.textProperty(), EmailText.textProperty(), comboBox.valueProperty());
    submitProgress.visibleProperty().bind(create.runningProperty());
    submitButton.visibleProperty().bind(create.runningProperty().not());
    vboxholder.visibleProperty().bind(comboBox.valueProperty().isEqualTo("Agency"));
    vboxholder.visibleProperty().addListener((src, wasVisible, isVisible) -> {
      AgencyNameText.clear();
    });
    configureServiceHandlers();
  }

  private void configureServiceHandlers() {
    create.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        clearForm();
      }
    });
    create.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = create.getException();
        throwable.printStackTrace();
      }
    });

  }
}
