package com.nineplusten.app.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class TemplateViewController {

  // These items are for the combox set up
  @FXML
  private TableView<String> templateTable;
  
  @FXML
  private ChoiceBox<String> templateSelector;

  @FXML
  private void initialize() {
    configureTemplateTable();
  }

  

  private void configureTemplateTable() {
    templateTable.setPlaceholder(new Label(""));
  }

/*  private double getTextWidth(String str) {
    Text text = new Text(str);
    text.setFont(new Font(16));
    return text.getLayoutBounds().getWidth() + 2d;
  }*/
}
