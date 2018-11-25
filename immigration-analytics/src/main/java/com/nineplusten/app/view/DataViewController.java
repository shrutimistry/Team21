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
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import static j2html.TagCreator.*;
import org.w3c.dom.Document;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Reports;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.service.DoubleBarGraph;
import com.nineplusten.app.service.PieChart_AWT;
import com.nineplusten.app.service.TemplateDataRetrievalService;
import com.nineplusten.app.util.ReportUtil;
import com.nineplusten.app.util.TextUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import j2html.tags.ContainerTag;
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

  private String generateHTML() {
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
	  		"            <h2 class='heading'>" + templateNameText.getText() + "</h2>\n" +
	  		"        </div>";
	  String table = "<div class='graphic-data'>\n" + 
	  		"            <img src=\"src/main/resources/barChart.png\" alt=\"Bar Chart\">\n" + 
	  		"        </div>\n"
	  		+ "</body>"
	  		+ "</html>";
	  String html = header + body + table;
		    
	  return html;
	  
  }
  
  private void generatej2html() {
	 ContainerTag html =  html(
			    head(
			        title("ICARE Reports"),
			        link().withRel("stylesheet").withHref("styles.css")
			    ),
			    body(
			    	div(attrs(".header")).with(
			            h1(templateNameText.getText()).withClass("title")
			    			),
			    	div(attrs(".template")).with(
				            h2(templateNameText.getText() + "'s Reffered vs Recieved").withClass("heading")
				    			),
			    	div(attrs(".graphic-data")).with(img().withSrc("src/main/resources/barChart.png").withAlt("Bar Chart portryaing Services Recieved and Referred"))
			    )
			);
	 
	 System.out.println(html.render());
  }
  
  	private CategoryDataset createDataset() {
      
      final String series1 = "Referred";
      final String series2 = "Recieved";

      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      dataset.addValue(this.getTemplateData().get(0), "Percentage", series1);
      dataset.addValue(this.getTemplateData().get(1), "Percentage", series2);
      return dataset;
      
  }
  /*private DefaultPieDataset createPieDataset() {
	  DefaultPieDataset target_data = new DefaultPieDataset();
	  target_data.setValue("Referred", this.getTemplateData().get(0));
	  target_data.setValue("Recieved", this.getTemplateData().get(1));
	  return target_data;
  }*/

  private List<Double> getTemplateData() {
	  Reports report = new Reports(dataTable);
	  double referred = 0;
	  double recieved = 0;
	  List<Double> data = new ArrayList<>();
	  if (templateNameText.getText().equalsIgnoreCase("Information and Orientation")) {
		  referred = report.getNonEmptyCellCount("service_referred_by_id");
		  recieved = report.getNonEmptyCellCount("orientation_service_id");
	  
	  } else if (templateNameText.getText().equalsIgnoreCase("Needs Assessment and Referrals Service")){
		  referred = report.getNonEmptyCellCount("assessment_referral_id");
		  recieved = report.getNonEmptyCellCount("support_received_ind");
		  
	  } else if (templateNameText.getText().equalsIgnoreCase("Community Connections")) {
		  referred = report.getNonEmptyCellCount("assessment_referral_id");
		  recieved = report.getNonEmptyCellCount("community_service_id");
		  
	  } else if (templateNameText.getText().equalsIgnoreCase("Employment Related Services")) {
		  referred = report.getNonEmptyCellCount("assessment_referral_id");
		  recieved = report.getNonEmptyCellCount("support_received_ind");
	  }
	  System.out.println("recieved percent: " + recieved);
	  System.out.println("ref percent: " + referred);
	  data.add(referred);
	  data.add(recieved);
	  
	  return data;
  }

  @FXML
  private void generateReport() throws IOException {
	  this.generatej2html();
	//PieChart_AWT pie = new PieChart_AWT();
	  DoubleBarGraph bar = new DoubleBarGraph();
	bar.createChart(this.createDataset(), templateNameText.getText());
	FileChooser chooser = new FileChooser();
    chooser.setTitle("Save Report File");
    chooser.setInitialFileName("*.pdf");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files (.pdf)", "*.pdf"));
    File selectedFile = chooser.showSaveDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      // TODO: generate HTML
      String path = (selectedFile.getAbsolutePath());
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
