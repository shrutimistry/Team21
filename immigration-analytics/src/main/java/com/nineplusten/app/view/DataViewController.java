package com.nineplusten.app.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import static j2html.TagCreator.*;
import org.w3c.dom.Document;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.service.PieChart_AWT;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.ReportUtil;
import com.nineplusten.app.util.TextUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

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

  private void generateHTML() {
	  ObservableList<TableColumn<TemplateData,?>> cols = dataTable.getColumns();
	   Callback<TableView<TemplateData>, TableRow<TemplateData>> rows = dataTable.getRowFactory();
	  String header = "<!DOCTYPE html>\n" + 
	  		"<html>\n" + 
	  		"\n" + 
	  		"<head>\n" + 
	  		"    <link rel=\"stylesheet\" href=\"styles.css\" />\n" + 
	  		"    <title>ICARE Report</title>\n" + 
	  		"</head>";
	  String body = "<body>\n" + 
	  		"    <div class='header'>\n" + 
	  		"        <h1 class='title'>ICARE Report</h1>\n" + 
	  		"    </div>\n" + 
	  		"    <div class='template'>\n" + 
	  		"        <div>\n" + 
	  		"            <h2 class='heading'>" + templateNameText + "</h2>\n" +
	  		"        </div>";
	  String table = "div class='graphic-data'>\n" + 
	  		"            <img src=\"chart.png\" />\n" + 
	  		"        </div>\n" + 
	  		"        <div class='table-container'>\n" + 
	  		"            <table class='table-data'>\n" + 
	  		"                <thead>\n" + 
	  		"                    <tr>\n" ;
	 String columns = null;
	  int i = 0;
	  while ( i < cols.size()) { 
		  columns = columns + "<th>" + (cols.get(i)).getText() + "</th>\n";
		  i++;
	  }
      
	String columnend = "                    </tr>\n" + 
	"                </thead>\n" + 
	"                <tbody>\n" + 
	"                    <tr>\n";
	
	
	  String html = header + body + table + columns + columnend;
	  System.out.print(html);
	  System.out.print(cols.size());
	  
  }
  
  private List<Object> getValuesforColumn(String columnID) {
	  //final String COLUMN_ID = "client_validation_type_id";
	    List<Object> rowCount = new ArrayList<>();
	    List<String> values = new ArrayList<>();
	    int total = 0;
	    for (TemplateData row: dataTable.getItems()) {
	      values.add(row.getFieldData().get(columnID));
	      total += 1;
	      
	    }
	    rowCount.add(total);
	    rowCount.add(values);
	    
	   return rowCount;
  }
  
  private double getTargetChildPercent() {
	  double total_rows = (Integer) getValuesforColumn("target_group_children_ind").get(0);
	  System.out.println("total rows" + total_rows);
	  List<String> target_child = (List<String>) getValuesforColumn("target_group_children_ind").get(1);
	  int i = 0;
	  int count_child = 0;
	  for (i = 0; i < target_child.size(); i++) {
		  if (target_child.get(i).equalsIgnoreCase("yes")) {
			  count_child += 1;
		  }
	  }
	  System.out.println("count child " + count_child);
	  double child_percent_decimal = count_child/total_rows;
	  double child_percent = child_percent_decimal * 100;
	  System.out.println("percent hin child " + child_percent_decimal);

	  return child_percent;
	  
  }
  
  private double getTargetYouthPercent() {
	  double total_rows = (Integer) getValuesforColumn("target_group_youth_ind").get(0);
	  List<String> target_youth = (List<String>) getValuesforColumn("target_group_youth_ind").get(1);
	  int i = 0;
	  int count_youth = 0;
	  for (i = 0; i < target_youth.size(); i++) {
		  if (target_youth.get(i).equalsIgnoreCase("yes")) {
			  count_youth += 1;
		  }
	  }
	  double youth_percent = (count_youth/total_rows)*100;
	  return youth_percent;
	  
  }
  private double getTargetSeniorPercent() {
	  double total_rows = (Integer) getValuesforColumn("target_group_senior_ind").get(0);
	  List<String> target_senior = (List<String>) getValuesforColumn("target_group_senior_ind").get(1);
	  int i = 0;
	  int count_senior = 0;
	  for (i = 0; i < target_senior.size(); i++) {
		  if (target_senior.get(i).equalsIgnoreCase("yes")) {
			  count_senior += 1;
		  }
	  }
	  double senior_percent = (count_senior/total_rows)*100;
	  return senior_percent;
	  
  }
  
  private DefaultPieDataset createDataset() {
	  DefaultPieDataset target_data = new DefaultPieDataset();
	  target_data.setValue("Senior", this.getTargetSeniorPercent());
	  target_data.setValue("Youth", this.getTargetYouthPercent());
	  target_data.setValue("Child", this.getTargetChildPercent());
	  return target_data;
  }

  

  @FXML
  private void generateReport() {
		System.out.println(this.getTargetChildPercent());

	PieChart_AWT pie = new PieChart_AWT();
	pie.createPiechart(this.createDataset());
	//FileChooser chooser = new FileChooser();
//    chooser.setTitle("Save Report File");
//    chooser.setInitialFileName("*.pdf");
//    chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files (.pdf)", "*.pdf"));
//    File selectedFile = chooser.showSaveDialog(mainApp.getPrimaryStage());
//    if (selectedFile != null) {
//      // TODO: generate HTML
//      //String path = (selectedFile.getAbsolutePath());
//      Document document;
//      String pathURL;
//      try {
//        pathURL = getClass().getClassLoader().getResource("report.html").toString();
//        document = ReportUtil.html5ParseDocument(pathURL, 0);
//      } catch (IOException e) {
//        e.printStackTrace();
//        document = null;
//        pathURL = null;
//      }
//      if (document != null) {
//        try (OutputStream os = new FileOutputStream(selectedFile)) {
//          PdfRendererBuilder builder = new PdfRendererBuilder();
//          builder.withW3cDocument(document, pathURL);
//          builder.toStream(os);
//          builder.run();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      }
//      try {
//        PDDocument doc = PDDocument.load(selectedFile);
//        doc.removePage(doc.getNumberOfPages() - 1);
//        doc.save(selectedFile);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
    //}
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
