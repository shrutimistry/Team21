package com.nineplusten.app.view;

import java.util.List;
import java.util.stream.Collectors;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.TextUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class DataViewController {
  @FXML
  private TableView<TemplateData> dataTable;
  @FXML
  private ChoiceBox<Agency> agencySelector;
  @FXML
  private ChoiceBox<Template> templateSelector;
  @FXML
  private Label agencyNameText;
  @FXML
  private Label templateNameText;
  @FXML
  private Label selectTemplateText;
  @FXML
  private VBox dataContainer;
  @FXML
  private ProgressIndicator dataServiceProgressIndicator;

  private TemplateDataRetrievalService dataService;

  @SuppressWarnings("unused")
  private App mainApp;

  @FXML
  private void initialize() {
    agencySelector.getItems().addAll(Cache.agencies);
    templateSelector.getItems().addAll(Cache.templates);
    dataTable.setPlaceholder(new Label("No data found for this template."));
    dataService = new TemplateDataRetrievalService(agencySelector.valueProperty(),
        templateSelector.valueProperty());
    configureBindings();
    configureListeners();
  }


  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  private void configureBindings() {
    templateSelector.disableProperty().bind(agencySelector.valueProperty().isNull());
    agencyNameText.textProperty().bind(agencySelector.valueProperty().asString());
    templateNameText.textProperty().bind(templateSelector.valueProperty().asString());
    dataContainer.visibleProperty().bind(agencySelector.valueProperty().isNotNull()
        .and(templateSelector.valueProperty().isNotNull()));
    dataContainer.disableProperty().bind(dataService.runningProperty());
    dataServiceProgressIndicator.visibleProperty().bind(dataService.runningProperty());
  }

  private void configureListeners() {
    dataService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        configureTable();
        dataTable.setItems(dataService.getValue());
      }
    });
    dataService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = dataService.getException();
        throwable.printStackTrace();
      }
    });
    agencySelector.valueProperty().addListener((src, oldVal, newVal) -> {
      templateSelector.getSelectionModel().clearSelection();
    });
    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      if (newVal != null) {
        dataService.restart();
      }
    });

  }

  private void configureTable() {
    List<TableColumn<TemplateData, String>> tableColumns =
        templateSelector.getValue().getColumns().entrySet().stream().map(e -> {
          TableColumn<TemplateData, String> column = new TableColumn<>(e.getValue());
          column.setPrefWidth(TextUtil.getTextWidth(e.getValue()));
          column.setResizable(false);
          column.setCellValueFactory(cellData -> new SimpleStringProperty(
              cellData.getValue().getFieldData().get(e.getKey())));
          return column;
        }).collect(Collectors.toList());
    dataTable.getColumns().setAll(tableColumns);
  }
}
