package com.nineplusten.app.view;

import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.UserRole;
import com.nineplusten.app.service.CreateUserService;
import com.nineplusten.app.util.AnimationUtil;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class AccountCreationController {

  // These items are for the combox set up
  @FXML
  private ChoiceBox<UserRole> comboBox;
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
  private GridPane vboxholder;
  @FXML
  private Button submitButton;
  @FXML
  private Button clearButton;
  @FXML
  private ProgressIndicator submitProgress;
  @FXML
  private HBox messageContainer;
  @FXML
  private Label messageText;

  private CreateUserService create;

  public AccountCreationController() {}

  /**
   * this is action call for submit button
   * 
   * @param action
   */
  @FXML
  private void submitButtonAction(ActionEvent action) {
    // checks status of the user accounts service
    messageContainer.setVisible(false);
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
    comboBox.getItems().addAll(Cache.userRoles);
    vboxholder.managedProperty().bind(vboxholder.visibleProperty());
    vboxholder.setVisible(false);

    // creates a new user account service
    create = new CreateUserService(AgencyNameText.textProperty(), UserNameText.textProperty(),
        PasswordText.textProperty(), EmailText.textProperty(), comboBox.valueProperty());
    submitProgress.visibleProperty().bind(create.runningProperty());
    submitButton.visibleProperty().bind(create.runningProperty().not());
    vboxholder.visibleProperty().bind(comboBox.valueProperty().asString().isEqualTo("Agency"));
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
        if (create.getValue()) {
          messageContainer.getStyleClass().remove("alert-box-fail");
          messageContainer.getStyleClass().add("alert-box-success");
          messageText.setText("Account created successfully.");
        } else {
          messageContainer.getStyleClass().remove("alert-box-success");
          messageContainer.getStyleClass().add("alert-box-fail");
          messageText.setText("Failed to create account. Please verify your input.");
        }
        messageContainer.setVisible(true);
        
        Platform.runLater(AnimationUtil.getAlertTimeline(messageContainer)::play);
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
