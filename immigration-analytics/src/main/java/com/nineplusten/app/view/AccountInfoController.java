package com.nineplusten.app.view;

import com.nineplusten.app.App;
import com.nineplusten.app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AccountInfoController {

  // These items are for the combox set up
  @FXML
  private TextField AgencyNameText;
  @FXML
  private TextField UsernameText;
  @FXML
  private TextField EmailText;

  private App mainApp;
  private User currUser;

  public AccountInfoController() {}

  /**
   * This just auto call for the UI
   */
  @FXML
  private void initialize() {
	  
  }
  
  

  
  public void setMainApp(App mainApp) {
	  this.mainApp = mainApp;
	  
	  this.currUser = mainApp.getSession().sessionUserProperty().get();
  }
}
