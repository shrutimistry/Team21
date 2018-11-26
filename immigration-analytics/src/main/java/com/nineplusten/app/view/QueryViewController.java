package com.nineplusten.app.view;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.img;
import static j2html.TagCreator.link;
import static j2html.TagCreator.table;
import static j2html.TagCreator.td;
import static j2html.TagCreator.title;
import static j2html.TagCreator.tr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.QueryViewModel;
import com.nineplusten.app.model.Reports;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.model.query.AgencyQueryChoice;
import com.nineplusten.app.model.query.ColumnQueryChoice;
import com.nineplusten.app.model.query.TemplateQueryChoice;
import com.nineplusten.app.service.DoubleBarGraph;
import com.nineplusten.app.service.PieChart_AWT;
import com.nineplusten.app.service.QueryService;
import com.nineplusten.app.util.ReportUtil;
import com.nineplusten.app.util.TextUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import j2html.tags.ContainerTag;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class QueryViewController {
  @FXML
  private TableView<AgencyQueryChoice> agencySelectorTable;
  @FXML
  private TableView<TemplateQueryChoice> templateSelectorTable;
  @FXML
  private TableView<ColumnQueryChoice> columnSelectorTable;
  @FXML
  private TableColumn<TemplateQueryChoice, CheckBox> templateSelectedColumn;
  @FXML
  private TableColumn<TemplateQueryChoice, String> templateName;
  @FXML
  private TableColumn<AgencyQueryChoice, CheckBox> agencySelectedColumn;
  @FXML
  private TableColumn<AgencyQueryChoice, String> agencyName;
  @FXML
  private TableColumn<ColumnQueryChoice, CheckBox> columnSelectedColumn;
  @FXML
  private TableColumn<ColumnQueryChoice, String> columnName;
  @FXML
  private TableColumn<ColumnQueryChoice, String> columnId;
  @FXML
  private VBox queryContainer;
  @FXML
  private CheckBox selectAllAgencies;
  @FXML
  private CheckBox selectAllColumns;

  @FXML
  private Button queryButton;
  @FXML
  private Button generateReportButton;
  @FXML
  private ProgressBar queryRunningIndicator;
  @FXML
  private TableView<TemplateData> dataTable;

  private QueryService queryService;
  private QueryViewModel queryModel;

  private static final String Q_AND = "$and";
  private static final String Q_OR = "$or";
  private static final String Q_ID = "_id";


  private App mainApp;

  @FXML
  private void initialize() {
    queryContainer.setVisible(false);
    queryModel = new QueryViewModel();
    dataTable.setPlaceholder(new Label("No results found"));

    queryService = new QueryService(queryModel.queryObjectProperty());

    queryService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        configureTable();
        ObservableList<TemplateData> result = queryService.getValue();
        dataTable.setItems(result);
        generateReportButton.setDisable(result.size() == 0);
        queryContainer.setVisible(true);
      }
    });
    queryService.setOnFailed(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent event) {
        // DEBUG
        Throwable throwable = queryService.getException();
        throwable.printStackTrace();
      }
    });


    queryRunningIndicator.visibleProperty().bind(queryService.runningProperty());

    // Agency Table
    agencySelectedColumn
        .setCellValueFactory(cellData -> cellData.getValue().selectedCheckProperty());
    agencyName.setCellValueFactory(cellData -> cellData.getValue().agencyNameProperty());
    agencySelectorTable.getItems().addAll(Cache.agencies.stream()
        .map(agency -> new AgencyQueryChoice(agency)).collect(Collectors.toList()));


    // Template table
    templateSelectedColumn
        .setCellValueFactory(cellData -> cellData.getValue().selectedCheckProperty());
    templateName.setCellValueFactory(cellData -> cellData.getValue().templateNameProperty());
    templateSelectorTable.getItems().addAll(Cache.templates.stream()
        .map(template -> new TemplateQueryChoice(template)).collect(Collectors.toList()));

    // Column table
    columnSelectedColumn
        .setCellValueFactory(cellData -> cellData.getValue().selectedCheckProperty());
    columnName.setCellValueFactory(cellData -> cellData.getValue().columnNameProperty());
    columnId.setCellValueFactory(cellData -> cellData.getValue().columnIdProperty());
    columnSelectorTable.setPlaceholder(new Label("No templates selected"));

    startListeners();
  }

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
  }

  @FXML
  private void executeQuery() {
    queryModel.setQueryObject(new JSONObject().put(Q_AND,
        new JSONArray().put(new JSONObject().put(Q_OR, agencySelectorTable.getItems().stream()
            .filter(AgencyQueryChoice::isSelected).map(agency -> {
              JSONObject agencyJson = new JSONObject();
              agencyJson.put("agency", new JSONObject().put(Q_ID, agency.getAgency().get_id()));
              return agencyJson;
            }).collect(Collectors.toList()))).put(templateSelectorTable.getItems().stream()
                .filter(TemplateQueryChoice::isSelected).map(template -> {
                  queryModel.setSelectedTemplate(template.getTemplate());
                  JSONObject templateJson = new JSONObject();
                  templateJson.put("template",
                      new JSONObject().put(Q_ID, template.getTemplate().get_id()));
                  return templateJson;
                }).findFirst().orElse(new JSONObject()))));
    if (!queryService.isRunning()) {
      queryService.restart();
    }

  }

  private List<Double> getTemplateData() {
	  Reports report = new Reports(dataTable);
	  double referred = 0;
	  double recieved = 0;
	  List<Double> data = new ArrayList<>();
	  Map<String, String> columnIdName = columnSelectorTable.getItems().stream().filter(ColumnQueryChoice::isSelected).collect(
			  Collectors.toMap(ColumnQueryChoice::getColumnId, ColumnQueryChoice::getColumnName));
	  if(columnIdName.containsKey("assessment_referral_id") && columnIdName.containsKey("support_received_ind")) {
		  referred = report.getNonEmptyCellCount("assessment_referral_id");
		  recieved = report.getNonEmptyCellCount("support_received_ind");
	  }
	  if(columnIdName.containsKey("assessment_referral_id") && columnIdName.containsKey("community_service_id")) {
		  referred = report.getNonEmptyCellCount("assessment_referral_id");
		  recieved = report.getNonEmptyCellCount("community_service_id");
	  }
	  if(columnIdName.containsKey("service_referred_by_id") && columnIdName.containsKey("orientation_service_id")) {
		  referred = report.getNonEmptyCellCount("service_referred_by_id");
		  recieved = report.getNonEmptyCellCount("orientation_service_id");
	  }

	  data.add(referred * 100);
	  data.add(recieved * 100);

	  System.out.print("Reff" + data.get(0));
	  System.out.print("rec" + data.get(1));


	  return data;
  }
  
  private CategoryDataset createDataset() {
      
      final String series1 = "Referred";
      final String series2 = "Recieved";

      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

      dataset.addValue(this.getTemplateData().get(0), "Percentage", series1);
      dataset.addValue(this.getTemplateData().get(1), "Percentage", series2);
      return dataset;
      
  }
  
  private String generatej2html() {

	  ContainerTag html =  html(
			  head(
					  title("ICARE Reports"),
					  link().withRel("stylesheet").withHref("styles.css")
					  ),
			  body(
					  div(attrs(".header")).with(
							  h1("iCare Reports").withClass("title")
							  ),
					  div(attrs(".template"),
							  h2(queryModel.getSelectedTemplate().getTemplateName()).withClass("heading")
							  ,(img().withSrc("barChart.jpg").withClass(".graphic-data").withAlt("Bar Chart portryaing Services Recieved and Referred"))
							  ),
					  div(attrs(".table-container"),(table().withClass(".table-data").with(
							  tr().with(
									  td().withText(
											  "Refered: " + this.getTemplateData().get(0).toString() + "%"
											  ),
									  td().withText(
											  "Recieved: " + this.getTemplateData().get(1).toString() + "%"
											  )

									  )
							  ))),
						div(attrs(".template"),
								h2(queryModel.getSelectedTemplate().getTemplateName()).withClass("heading")
								,(img().withSrc("pieChart.jpg").withClass(".graphic-data").withAlt("Pie Chart illustrating the various age groups represented in the service"))
								)
						)
				);
			  

	  return html.render();
  }
  
  private void addHtmltoFile(String html) {
  File file = new File("./reports/report.html");
      FileWriter fr = null;
      try {
          fr = new FileWriter(file);
          fr.write(html);
      } catch (IOException e) {
          e.printStackTrace();
      }finally{
          //close resources
          try {
              fr.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }


  @FXML
  private void generateReport() throws IOException {
	PieChart_AWT pie = new PieChart_AWT();
	pie.createPieChart(this.getAgeReports());
	DoubleBarGraph bar = new DoubleBarGraph();
  	bar.createChart(this.createDataset(),"");
  	String html = this.generatej2html();
  	this.addHtmltoFile(html);
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save Report File");
    chooser.setInitialFileName("*.pdf");
    chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files (.pdf)", "*.pdf"));
    File selectedFile = chooser.showSaveDialog(mainApp.getPrimaryStage());
    if (selectedFile != null) {
      // TODO: generate HTML
      // String path = (selectedFile.getAbsolutePath());
      Document document;
      String pathURL;
      try {
        pathURL = new File("reports/report.html").toURI().toURL().toString();
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
    }
  }
  
  public DefaultPieDataset getAgeReports() {
	  Reports report = new Reports(dataTable);
	  report.sortAges();
	  double childAmount = report.getTargetChildPercent();
	  double seniorAmount = report.getTargetSeniorPercent();
	  double youthAmount = report.getTargetYouthPercent();
	  double adultAmount = 100 - (childAmount + seniorAmount + youthAmount);
	  DefaultPieDataset target_data = new DefaultPieDataset();
	  target_data.setValue("Children (0 - 14 yrs)", childAmount);
	  target_data.setValue("Youth (15 - 24 yrs)", youthAmount);
	  target_data.setValue("Adult (25 - 64 yrs)", adultAmount);
	  target_data.setValue("Senior (65+ yrs)", seniorAmount);
	  return target_data;
  }

  private void startListeners() {
    agencySelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
        .forEach(box -> box.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
          boolean allSelected = agencySelectorTable.getItems().stream()
              .map(item -> item.isSelected()).reduce(true, (a, b) -> a && b);
          boolean anySelected = agencySelectorTable.getItems().stream()
              .map(item -> item.isSelected()).reduce(false, (a, b) -> a || b);
          if (allSelected) {
            selectAllAgencies.setSelected(true);
            selectAllAgencies.setIndeterminate(false);
          }
          if (!anySelected) {
            selectAllAgencies.setSelected(false);
            selectAllAgencies.setIndeterminate(false);
          }
          if (anySelected && !allSelected) {
            selectAllAgencies.setSelected(false);
            selectAllAgencies.setIndeterminate(true);
          }
          queryModel.setAnyAgencySelected(anySelected);
        }));

    templateSelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
        .forEach(box -> box.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
          Optional<BooleanProperty> current = templateSelectorTable.getItems().stream()
              .filter(item -> item.isSelected() && box != item.getSelectedCheck())
              .map(choice -> choice.selectedProperty()).findFirst();
          if (current.isPresent()) {
            current.get().set(false);
          }
          box.setSelected(isSelected);

          boolean anySelectedTemplate = templateSelectorTable.getItems().stream()
              .map(item -> item.isSelected()).reduce(false, (a, b) -> a || b);
          queryModel.setTemplateSelected(anySelectedTemplate);
          if (!anySelectedTemplate) {
            selectAllColumns.setSelected(false);
          }


          columnSelectorTable.getItems().clear();
          templateSelectorTable.getItems().stream().filter(TemplateQueryChoice::isSelected)
              .forEach(item -> {
                item.getTemplate().getColumns().entrySet().stream().forEach(entry -> {
                  ColumnQueryChoice c = new ColumnQueryChoice(item.getTemplateName(),
                      entry.getValue(), entry.getKey());
                  c.getSelectedCheck().selectedProperty()
                      .addListener((obsI, wasSelectedI, isSelectedI) -> {
                        boolean allSelected = columnSelectorTable.getItems().stream()
                            .map(itemI -> itemI.isSelected()).reduce(true, (a, b) -> a && b);
                        boolean anySelected = columnSelectorTable.getItems().stream()
                            .map(itemI -> itemI.isSelected()).reduce(false, (a, b) -> a || b);
                        if (allSelected) {
                          selectAllColumns.setSelected(true);
                          selectAllColumns.setIndeterminate(false);
                        }
                        if (!anySelected) {
                          selectAllColumns.setSelected(false);
                          selectAllColumns.setIndeterminate(false);
                        }
                        if (anySelected && !allSelected) {
                          selectAllColumns.setSelected(false);
                          selectAllColumns.setIndeterminate(true);
                        }
                        queryModel.setAnyColumnSelected(anySelected);
                      });
                  columnSelectorTable.getItems().add(c);
                });
              });
        }));


    queryButton.disableProperty().bind(queryModel.anySelectedProperty().not());
    selectAllColumns.disableProperty().bind(queryModel.templateSelectedProperty().not());
  }

  @FXML
  private void selectAllAction(ActionEvent e) {
    if (e.getSource() == selectAllAgencies) {
      if (selectAllAgencies.isSelected()) {
        agencySelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
            .forEach(box -> box.setSelected(true));
      } else {
        agencySelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
            .forEach(box -> box.setSelected(false));
      }
    } else if (e.getSource() == selectAllColumns) {
      if (selectAllColumns.isSelected()) {
        columnSelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
            .forEach(box -> box.setSelected(true));
      } else {
        columnSelectorTable.getItems().stream().map(item -> item.getSelectedCheck())
            .forEach(box -> box.setSelected(false));
      }
    }
  }

  private void configureTable() {
    List<TableColumn<TemplateData, String>> tableColumns = new ArrayList<>();
    TableColumn<TemplateData, String> agencyNameColumn = new TableColumn<>("Agency");
    agencyNameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().getAgency().getAgencyName()));
    tableColumns.add(agencyNameColumn);
    tableColumns
        .addAll(queryModel.getSelectedTemplate().getColumns().entrySet().stream().filter(e -> {
          Set<String> selectedColumnIdSet =
              columnSelectorTable.getItems().stream().filter(ColumnQueryChoice::isSelected)
                  .map(ColumnQueryChoice::getColumnId).collect(Collectors.toSet());
          return selectedColumnIdSet.contains(e.getKey());
        }).map(e -> {
          TableColumn<TemplateData, String> column = new TableColumn<>(e.getValue());
          column.setPrefWidth(TextUtil.getTextWidth(e.getValue()));
          column.setResizable(false);
          column.setCellValueFactory(cellData -> new SimpleStringProperty(
              cellData.getValue().getFieldData().get(e.getKey())));
          return column;
        }).collect(Collectors.toList()));
    dataTable.getColumns().setAll(tableColumns);
  }
}
