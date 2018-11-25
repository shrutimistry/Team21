package com.nineplusten.app.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Document;
import com.nineplusten.app.App;
import com.nineplusten.app.util.ReportUtil;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class QueryViewController {
  @FXML
  private VBox queryContainer;
  @FXML
  private CheckBox selectAllAgencies;
  @FXML
  private CheckBox selectAllTemplates;
  @FXML
  private CheckBox selectAllColumns;
  
  private App mainApp;
  
  @FXML
  private void initialize() {
    queryContainer.setVisible(false);
  }
  
  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
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
}
