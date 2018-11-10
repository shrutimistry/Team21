package com.nineplusten.app.view;

import java.util.List;
import java.util.stream.Collectors;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.model.User;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.TextUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class AgencyDataViewController {
  @FXML
  private TableView<TemplateData> dataTable;
  @FXML
  private ChoiceBox<Template> templateSelector;
  @FXML
  private Label templateNameText;
  @FXML
  private VBox dataContainer;
  @FXML
  private ProgressIndicator dataServiceProgressIndicator;

  private TemplateDataRetrievalService dataService;
  private ObjectProperty<Agency> agency;

  @FXML
  private void initialize() {
    templateSelector.getItems().addAll(Cache.templates);
    dataTable.setPlaceholder(new Label("No data found for this template."));
    
    configureBindings();
  }
  
  public void initDataService(User user) {
    agency = new SimpleObjectProperty<>(user.getAgency());
    dataService = new TemplateDataRetrievalService(agency,
        templateSelector.valueProperty());
    dataTable.disableProperty().bind(dataService.runningProperty());
    dataServiceProgressIndicator.visibleProperty().bind(dataService.runningProperty());
    configureListeners();
  }

  private void configureBindings() {
    templateNameText.textProperty().bind(templateSelector.valueProperty().asString());
    dataContainer.visibleProperty().bind(templateSelector.valueProperty().isNotNull());
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
    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      dataService.restart();
    });
    
  }

  private void configureTable() {
    dataTable.getColumns().clear();
    List<TableColumn<TemplateData, String>> tableColumns =
        templateSelector.getValue().getColumns().entrySet().stream().map(e -> {
          TableColumn<TemplateData, String> column = new TableColumn<>(e.getValue());
          column.setPrefWidth(TextUtil.getTextWidth(e.getValue()));
          column.setResizable(false);
          column.setCellValueFactory(cellData -> new SimpleStringProperty(
              cellData.getValue().getFieldData().get(e.getKey())));
          return column;
        }).collect(Collectors.toList());
    dataTable.getColumns().addAll(tableColumns);
  }
}
