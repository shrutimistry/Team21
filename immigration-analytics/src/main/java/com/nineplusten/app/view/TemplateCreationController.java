package com.nineplusten.app.view;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class TemplateCreationController {
  
  //These items are for the combox set up
  @FXML private ComboBox<String> comboBox;
  @FXML private Label comboBoxLabel;
  @FXML private TextField AgencyNameText;
  @FXML private ListView<String> colList;
  @FXML private TextField newColName;

  
  
  public TemplateCreationController() {
  }
  /**
   * This will show text for agency only
   *
   */
  @FXML
  private void comboBoxWasUpdate() {
    if (comboBox.getValue().toString() == "Agency") {
      this.comboBoxLabel.setVisible(true);
      this.AgencyNameText.setVisible(true);
    } else {
      this.comboBoxLabel.setVisible(false);
      this.AgencyNameText.setVisible(false);
    }
    
  }
  /**
   * this is action call for submit button
   * @param action
   */
  @FXML
  private void submitButtonAction(ActionEvent action) {
    String s = "add action here";
  }
  
  /**
   * this is action call for cancel button
   */
  @FXML
  private void cancelButtonAction(ActionEvent action) {
    String s = "add action here";
  }
  
  
  /**
   * This will remove button for listview
   */
  @FXML
  private void listviewRemove() {
    ObservableList<String> selectedItem = colList.getSelectionModel().getSelectedItems();
    for(String selected : selectedItem) {
      colList.getItems().remove(selected);
    }
  }
  
  /**
   * This will add one addtional entry to the listview
   */
  @FXML
  private void listviewAdd() {

    colList.getItems().add(newColName.getText());
  }
  
  /**
   * This just atuo call for the ui
   */
  @FXML
  private void initialize() {
    // this is for configuring the comboBox
    comboBox.getItems().addAll("TEQ", "Agency", "All");
    comboBoxLabel.setVisible(false);
    AgencyNameText.setVisible(false);
    colList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    
    
  }
}
