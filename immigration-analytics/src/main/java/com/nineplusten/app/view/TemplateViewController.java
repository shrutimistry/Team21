package com.nineplusten.app.view;

import java.util.List;
import java.util.stream.Collectors;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.util.TextUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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
            t.setPrefWidth(TextUtil.getTextWidth(name));
            return t;
          }).collect(Collectors.toList());
      templateTable.getColumns().addAll(columns);
    });
    templateTable.getItems().add("");
    templateTable.setSelectionModel(null);
  }
}
