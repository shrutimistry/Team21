package com.nineplusten.app.view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AccountCreationController {
  
  //These items are for the combox set up
  @FXML private ComboBox<String> comboBox;
  @FXML private Label comboBoxLbael;
  @FXML private TextField AgencyNameText;
  @FXML private TextField UserNameText;
  @FXML private TextField PasswordText;
  @FXML private TextField EmailText;
  
  public AccountCreationController() {
  }
  /**
   * This will show text for agency only
   *
   */
  private void comboBoxWasUpdate() {
    if (comboBox.getValue().toString() == "Agency") {
      this.comboBoxLbael.setVisible(true);
      this.AgencyNameText.setVisible(true);
    } else {
      this.comboBoxLbael.setVisible(false);
      this.AgencyNameText.setVisible(false);
    }
    
  }
  /**
   * this is action call for submit button
   * @param action
   */
  private void submitButtonAction(ActionEvent action) {
    String s = "add action here";
  }
  
  /**
   * this is action call for cancel button
   */
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
    comboBoxLbael.setVisible(false);
    AgencyNameText.setVisible(false);
    
  }
}
