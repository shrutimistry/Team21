package com.nineplusten.app.view;

import java.util.List;
import java.util.stream.Collectors;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Template;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn;

public class TemplateViewController {

  // These items are for the combox set up
  @FXML
  private TableView<String> templateTable;

  @FXML
  private ChoiceBox<Template> templateSelector;

  @FXML
  private void initialize() {
    templateSelector.getItems().addAll(Cache.templates);
    configureTemplateTable();
  }



  private void configureTemplateTable() {
    templateTable.setPlaceholder(new Label(""));
    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      templateTable.getColumns().clear();
      List<TableColumn<String, String>> columns =
          newVal.getColumns().values().stream().map(name -> {
            TableColumn<String, String> t = new TableColumn<>(name);
            t.setResizable(false);
            t.setSortable(false);
            t.setPrefWidth(getTextWidth(name));
            return t;
          }).collect(Collectors.toList());
      templateTable.getColumns().addAll(columns);
    });
    templateTable.getItems().add("");
    templateTable.setSelectionModel(null);
  }


  private double getTextWidth(String str) {
    Text text = new Text(str);
    text.setFont(new Font(16));
    Double val = text.getLayoutBounds().getWidth() + 2d;
    if (str.length() <= 5) {
      val += 5d;
    }
    return val;
  }

}
