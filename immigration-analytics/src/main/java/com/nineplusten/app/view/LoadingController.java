package com.nineplusten.app.view;

import com.nineplusten.app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class LoadingController {
  @FXML
  private Label loadingText;
  @FXML
  private ProgressIndicator loadingIndicator;
  
  private App mainApp;
  
  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

}
