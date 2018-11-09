package com.nineplusten.app.view;

import com.nineplusten.app.App;
import javafx.fxml.FXML;

public class LandingController {
  @FXML
  private TemplateCreationController templateCreationController;
  @FXML
  private AccountInfoController accountInfoController;
  
  @FXML
  private void initialize() {
  }
  
  public void setMainApp(App mainApp) {
    templateCreationController.setMainApp(mainApp);
    accountInfoController.setMainApp(mainApp);
    
  }
    
}
