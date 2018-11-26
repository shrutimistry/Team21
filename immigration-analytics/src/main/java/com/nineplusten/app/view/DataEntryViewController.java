package com.nineplusten.app.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.DataUploadModel;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.service.LoadExcelService;
import com.nineplusten.app.service.SubmitDataService;
import com.nineplusten.app.util.AnimationUtil;
import com.nineplusten.app.util.TextUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;



public class DataEntryViewController {
  @FXML
  private ChoiceBox<Template> templateSelector;
  @FXML
  private Button browseButton;
  @FXML
  private VBox dataUploadPreview;
  @FXML
  private TableView<TemplateData> previewTable;
  @FXML
  private Label templateNameText;
  @FXML
  private ProgressBar loadProgress;
  @FXML
  private ProgressIndicator submitIndicator;
  @FXML
  private Button submitButton;
  @FXML
  private Button downloadButton;
  @FXML
  private HBox messageContainer;
  @FXML
  private Label messageText;

  private DataUploadModel dataModel;
  private LoadExcelService loadExcelService;
  private SubmitDataService submitDataService;



  private App mainApp;

  @FXML
  private void initialize() {
    loadResources();
    dataModel = new DataUploadModel();
    templateSelector.getItems().addAll(Cache.templates);
    dataUploadPreview.visibleProperty().bind(dataModel.excelLoadedProperty());
    templateNameText.textProperty().bind(templateSelector.valueProperty().asString());
    previewTable.setPlaceholder(new Label("No content found for selected file."));
    configureListeners();
  }

  private void loadResources() {
    Image dlImage =
        new Image(getClass().getClassLoader().getResourceAsStream("download_16_0_000000_none.png"));
    Image ulImage =
        new Image(getClass().getClassLoader().getResourceAsStream("upload_16_0_000000_none.png"));
    downloadButton.setGraphic(new ImageView(dlImage));
    downloadButton.setTooltip(new Tooltip("Download ICARE template"));
    browseButton.setGraphic(new ImageView(ulImage));
    browseButton.setTooltip(new Tooltip("Upload ICARE template with data"));
  }

  @FXML
  private void uploadExcel(ActionEvent e) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Select Data File");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("Excel Files (.xlsx)", "*.xlsx"));
    File selectedFile = chooser.showOpenDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      String excelPath = (selectedFile.getAbsolutePath());
      dataModel.setExcelPath(excelPath);
      if (!loadExcelService.isRunning()) {
        loadExcelService.restart();
      }
    }
  }

  @FXML
  private void downloadExcel(ActionEvent e) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Download Template");
    chooser.setInitialFileName(templateSelector.getValue().getTemplateFileName() + ".xlsx");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("Excel Files (.xlsx)", "*.xlsx"));
    File selectedFile = chooser.showSaveDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      try {
        Files.copy(getClass().getClassLoader().getResourceAsStream(
            templateSelector.getValue().getTemplateFilePath()), selectedFile.toPath());
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  @FXML
  private void submitData(ActionEvent e) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Upload Confirmation");
    alert.setHeaderText(
        "Upload \"" + templateSelector.getValue().getTemplateName() + "\" data to the server?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      if (!submitDataService.isRunning()) {
        submitDataService.restart();
      }
    }
  }


  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  private void configureListeners() {
    templateSelector.valueProperty().addListener((src, oldVal, newVal) -> {
      dataModel.setExcelLoaded(false);
    });
  }

  public void configureServices() {
    configureExcelLoadService();
    configureSubmitDataService();
  }

  private void configureExcelLoadService() {
    loadExcelService = new LoadExcelService(dataModel.excelPathProperty(),
        mainApp.getSession().getUser().getAgency(), templateSelector.valueProperty());
    loadExcelService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        ObservableList<TemplateData> result = loadExcelService.getValue();
        configureTable();
        dataModel.setExcelData(result);
        previewTable.setItems(result);
        dataModel.setExcelLoaded(true);

      }
    });
    loadExcelService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = loadExcelService.getException();
        throwable.printStackTrace();
      }
    });
    browseButton.disableProperty()
        .bind(loadExcelService.runningProperty().or(templateSelector.valueProperty().isNull()));
    downloadButton.disableProperty()
        .bind(loadExcelService.runningProperty().or(templateSelector.valueProperty().isNull()));
    loadProgress.visibleProperty().bind(loadExcelService.runningProperty());
    loadProgress.progressProperty().bind(loadExcelService.progressProperty());
  }

  private void configureSubmitDataService() {
    submitDataService = new SubmitDataService(dataModel.excelDataProperty(),
        mainApp.getSession().getUser().getAgency(), templateSelector.valueProperty());

    submitDataService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        if (submitDataService.getValue()) {
          messageContainer.getStyleClass().remove("alert-box-fail");
          messageContainer.getStyleClass().add("alert-box-success");
          messageText.setText("Data uploaded successfully.");
        } else {
          messageContainer.getStyleClass().remove("alert-box-success");
          messageContainer.getStyleClass().add("alert-box-fail");
          messageText.setText("Failed to upload data.");
        }
        messageContainer.setVisible(true);

        Platform.runLater(AnimationUtil.getAlertTimeline(messageContainer)::play);
      }
    });
    submitDataService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = submitDataService.getException();
        throwable.printStackTrace();
        messageContainer.getStyleClass().remove("alert-box-success");
        messageContainer.getStyleClass().add("alert-box-fail");
        messageText.setText("Failed to upload data.");
        messageContainer.setVisible(true);
        Platform.runLater(AnimationUtil.getAlertTimeline(messageContainer)::play);
      }
    });
    submitIndicator.visibleProperty().bind(submitDataService.runningProperty());
    submitButton.visibleProperty().bind(submitDataService.runningProperty().not());
    submitButton.managedProperty().bind(submitButton.visibleProperty());
    submitIndicator.managedProperty().bind(submitIndicator.visibleProperty());

  }

  private void configureTable() {
    previewTable.getColumns().clear();
    List<TableColumn<TemplateData, String>> tableColumns =
        templateSelector.getValue().getColumns().entrySet().stream().map(e -> {
          TableColumn<TemplateData, String> column = new TableColumn<>(e.getValue());
          column.setPrefWidth(TextUtil.getTextWidth(e.getValue()));
          column.setResizable(false);
          column.setCellValueFactory(cellData -> new SimpleStringProperty(
              cellData.getValue().getFieldData().get(e.getKey())));
          return column;
        }).collect(Collectors.toList());
    previewTable.getColumns().addAll(tableColumns);
  }

}
