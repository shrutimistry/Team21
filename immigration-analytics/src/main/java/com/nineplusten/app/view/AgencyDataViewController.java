package com.nineplusten.app.view;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.AgencyDataViewModel;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.model.User;
import com.nineplusten.app.service.ModifyDataService;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.TextUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
  @FXML
  private ButtonBar editControls;
  @FXML
  private Button editButton;
  @FXML
  private Button saveButton;

  private TemplateDataRetrievalService dataService;
  // TODO initialize
  private ModifyDataService modifyDataService;
  private AgencyDataViewModel dataModel;

  @FXML
  private void initialize() {
    loadResources();
    dataModel = new AgencyDataViewModel();
    templateSelector.getItems().addAll(Cache.templates);
    dataTable.setPlaceholder(new Label("No data found for this template."));


    configureBindings();
  }

  private void loadResources() {
    Image editImg =
        new Image(getClass().getClassLoader().getResourceAsStream("edit_24_0_000000_none.png"));
    editButton.setText("");
    editButton.setGraphic(new ImageView(editImg));
  }

  public void initDataService(User user) {
    dataModel.setAgency(user.getAgency());
    dataService = new TemplateDataRetrievalService(dataModel.agencyProperty(),
        templateSelector.valueProperty());
    modifyDataService = new ModifyDataService(dataModel.tableDataProperty(), dataModel.getAgency(),
        templateSelector.valueProperty());
    dataTable.disableProperty()
        .bind(dataService.runningProperty().or(modifyDataService.runningProperty()));
    dataServiceProgressIndicator.visibleProperty().bind(dataService.runningProperty());
    editButton.disableProperty().bind(dataService.runningProperty());
    editControls.disableProperty().bind(modifyDataService.runningProperty());
    configureListeners();
  }

  private void configureBindings() {
    Bindings.bindBidirectional(dataModel.tableDataProperty(), dataTable.itemsProperty());
    templateNameText.textProperty().bind(templateSelector.valueProperty().asString());
    dataContainer.visibleProperty().bind(templateSelector.valueProperty().isNotNull());
    editButton.visibleProperty().bind(dataModel.editModeProperty().not());
    editControls.visibleProperty().bind(dataModel.editModeProperty());
    saveButton.disableProperty().bind(dataModel.modifiedProperty().not());
  }

  private void configureListeners() {
    dataTable.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
      if (dataModel.isEditMode()) {
        if (e.getCode() == KeyCode.DELETE && !e.isConsumed()) {
          deleteRows();
        }
      }
      e.consume();
    });
    dataModel.editModeProperty().addListener((src, oldVal, newVal) -> {
      if (newVal) {
        dataTable.setEditable(true);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      } else {
        dataTable.setEditable(false);
        dataTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      }
    });
    dataService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        configureTable();
        dataModel.setTableData(dataService.getValue());
        dataModel.setEditMode(false);
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
    modifyDataService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        dataModel.setEditMode(false);
      }
    });
    modifyDataService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = modifyDataService.getException();
        throwable.printStackTrace();
      }
    });

    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      dataModel.setEditMode(false);
      dataService.restart();
    });
  }

  private void deleteRows() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Delete Confirmation");
    alert.setHeaderText("Delete selected row(s)?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      dataModel.getTableData().removeAll(dataTable.getSelectionModel().getSelectedItems());
      dataTable.getSelectionModel().clearAndSelect(0);
      dataModel.setModified(true);
    }
  }

  @FXML
  private void edit() {
    dataModel.setEditMode(true);
  }

  @FXML
  private void discard() {
    dataService.restart();
  }
  
  @FXML
  private void delete() {
    deleteRows();
  }

  @FXML
  private void save() {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Upload Confirmation");
    alert.setHeaderText(
        "Save changes to \"" + templateSelector.getValue().getTemplateName() + "\" on the server?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      if (!modifyDataService.isRunning()) {
        modifyDataService.restart();
      }
    }
  }

  private void configureTable() {
    List<TableColumn<TemplateData, String>> tableColumns =
        templateSelector.getValue().getColumns().entrySet().stream().map(e -> {
          TableColumn<TemplateData, String> column = new TableColumn<>(e.getValue());
          column.setPrefWidth(TextUtil.getTextWidth(e.getValue()));
          column.setResizable(false);
          column.setCellValueFactory(cellData -> new SimpleStringProperty(
              cellData.getValue().getFieldData().get(e.getKey())));
          column.setCellFactory(TextFieldTableCell.<TemplateData>forTableColumn());
          column.setOnEditCommit(cmt -> {
            TemplateData t = cmt.getRowValue();
            if (!cmt.getNewValue().equals(t.getFieldData().get(e.getKey()))) {
              dataModel.setModified(true);
              t.getFieldData().put(e.getKey(), cmt.getNewValue());
            }
          });
          return column;
        }).collect(Collectors.toList());
    dataTable.getColumns().setAll(tableColumns);
  }
}
