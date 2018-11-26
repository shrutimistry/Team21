package com.nineplusten.app.view;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineplusten.app.App;
import com.nineplusten.app.model.User;
import com.nineplusten.app.service.LoginService;
import com.nineplusten.app.util.AnimationUtil;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LoginController {
  @FXML
  private TextField username;
  @FXML
  private PasswordField password;
  @FXML
  private Button loginButton;
  @FXML
  private Label messageText;
  @FXML
  private HBox messageContainer;
  @FXML
  private ProgressIndicator lsRunning;

  private Gson gson;
  private LoginService ls;
  private App mainApp;

  public LoginController() {
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  @FXML
  private void initialize() {
    ls = new LoginService(username.textProperty(), password.textProperty(), gson);
    lsRunning.visibleProperty().bind(ls.runningProperty());
    lsRunning.managedProperty().bind(lsRunning.visibleProperty());
    messageContainer.managedProperty().bind(messageContainer.visibleProperty());
    loginButton.disableProperty().bind(ls.runningProperty());
    configureBehaviours();
  }

  @FXML
  private void loginAction(ActionEvent e) {
    messageContainer.setVisible(false);
    messageText.setText("");
    if (!ls.isRunning()) {
      ls.restart();
    }
  }

  private void configureBehaviours() {
    ls.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        User user = ls.getValue();
        if (user != null) {
          mainApp.getSession().sessionUserProperty().set(user);
          mainApp.showLandingView();
        } else {
          // technically won't need this
          mainApp.getSession().invalidateSession();

          messageContainer.getStyleClass().remove("alert-box-success");
          messageContainer.getStyleClass().add("alert-box-fail");
          messageText.setText("Authentication failure.");
          messageContainer.setVisible(true);
          Platform.runLater(AnimationUtil.getAlertTimeline(messageContainer)::play);
        }
      }
    });
    ls.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = ls.getException();
        throwable.printStackTrace();
        mainApp.getSession().invalidateSession();

        messageContainer.getStyleClass().remove("alert-box-success");
        messageContainer.getStyleClass().add("alert-box-fail");
        messageText.setText("Failed to connect. Please verify connection.");
        messageContainer.setVisible(true);
        Platform.runLater(AnimationUtil.getAlertTimeline(messageContainer)::play);
      }
    });
  }

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }



}
