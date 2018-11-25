package com.nineplusten.app.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Document;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.ReportUtil;
import com.nineplusten.app.util.TextUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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

  @FXML
  private void generateReport() {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save Report File");
    chooser.setInitialFileName("*.pdf");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files (.pdf)", "*.pdf"));
    File selectedFile = chooser.showSaveDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      // TODO: generate HTML
      //String path = (selectedFile.getAbsolutePath());
      Document document;
      String pathURL;
      try {
        pathURL = getClass().getClassLoader().getResource("report.html").toString();
        document = ReportUtil.html5ParseDocument(pathURL, 0);
      } catch (IOException e) {
        e.printStackTrace();
        document = null;
        pathURL = null;
      }
      if (document != null) {
        try (OutputStream os = new FileOutputStream(selectedFile)) {
          PdfRendererBuilder builder = new PdfRendererBuilder();
          builder.withW3cDocument(document, pathURL);
          builder.toStream(os);
          builder.run();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      try {
        PDDocument doc = PDDocument.load(selectedFile);
        doc.removePage(doc.getNumberOfPages() - 1);
        doc.save(selectedFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
