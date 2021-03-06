package com.nineplusten.app.view;

import com.nineplusten.app.App;
import com.nineplusten.app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountInfoController {

  // These items are for the combox set up
  @FXML
  private Label agencyNameText;
  @FXML
  private Label usernameText;
  @FXML
  private Label emailText;
  @FXML
  private Label agencyPrompt;

  @SuppressWarnings("unused")
  private App mainApp;
  private User currUser;

  public AccountInfoController() {}

  /**
   * This just auto call for the UI
   */
  @FXML
  private void initialize() {
    agencyPrompt.setVisible(false);
    agencyNameText.setVisible(false);
    agencyPrompt.managedProperty().bind(agencyPrompt.visibleProperty());
    agencyNameText.managedProperty().bind(agencyNameText.visibleProperty());
  }

  private void getUserInfo(User currUser) {
    // agencyNameText.setText(currUser.getAgency().getAgencyName());
    usernameText.setText(currUser.getUserId());
    emailText.setText(currUser.getEmail());

    if (currUser.getAgency() != null) {
      agencyPrompt.setVisible(true);
      agencyNameText.setVisible(true);
      agencyNameText.setText(currUser.getAgency().getAgencyName());
    }
  }

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;

    this.currUser = mainApp.getSession().sessionUserProperty().get();
    getUserInfo(this.currUser);
  }
}
