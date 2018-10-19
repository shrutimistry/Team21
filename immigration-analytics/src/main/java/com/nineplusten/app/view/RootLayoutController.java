package com.nineplusten.app.view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RootLayoutController {

  
  
  /**
   * When this method is called, it will change to the Scene
   * @throws IOException 
   * 
   */
  public void changeScreentButtonPushed(ActionEvent event) throws IOException {
    Parent accountViewParent = FXMLLoader.load(getClass().getResource("AccountCreation.fxml"));
    Scene accountViewScene = new Scene(accountViewParent);
    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    window.setScene(accountViewScene);
    window.show();
  }
}
