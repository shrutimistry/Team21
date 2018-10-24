package com.nineplusten.app.view;
import com.nineplusten.app.service.CreateUserService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AccountCreationController {
  
  //These items are for the combox set up
  @FXML private ComboBox<String> comboBox;
  @FXML private Label comboBoxLabel;
  @FXML private TextField AgencyNameText;
  @FXML private TextField UserNameText;
  @FXML private TextField PasswordText;
  @FXML private TextField EmailText;
  @FXML private VBox vboxholder;
  @FXML private Button submitButton;
  
  private CreateUserService create;
  
  public AccountCreationController() {
  }
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
  private void cancelButtonAction(ActionEvent action) {
    String s = "add action here";
  }
  
  /**
   * This just atuo call for the ui
   */
  @FXML
  private void initialize() {
    // this is for configuring the comboBox
    comboBox.getItems().addAll("TEQ", "Agency", "UTSC");
    vboxholder.setVisible(false);
    
    // creates a new user account service
    create = new CreateUserService(AgencyNameText.textProperty(), UserNameText.textProperty(),
			  PasswordText.textProperty(), EmailText.textProperty(), comboBox.getValue());

  }
  
}
