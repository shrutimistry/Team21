package com.nineplusten.app.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;

import javafx.fxml.Initializable;

import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.service.LoadExcelService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Window;



public class DataEntryViewController {
	 @FXML
	  private ChoiceBox<Template> templateSelector;
	  private Desktop desktop = Desktop.getDesktop();
	  private static App mainApp = new App();
	  //final FileChooser fileChooser = new FileChooser();
	  private Button fileupload;
	  private VBox dataentryVbox;
	 @FXML
	  private void initialize() {
	    templateSelector.getItems().addAll(Cache.templates);
	    
	 }
	 
	 @FXML
	  private void uploadExcel(ActionEvent e) {
	    FileChooser chooser = new FileChooser();
	    chooser.setTitle("Select ICARE Template");
	    chooser.getExtensionFilters().addAll(new ExtensionFilter("Excel Files (.xlsx)", "*.xlsx"));
	    File selectedFile = chooser.showOpenDialog(mainApp.getPrimaryStage());
	    if (selectedFile != null) {
	    	/* openFile(selectedFile);
	    	String excelPath = (selectedFile.getAbsolutePath());
	    	LoadExcelService excelparser = new LoadExcelService(excelPath);
	    	Workbook wb;
	    	wb = excelparser.loadExcel(excelPath); */
	    }
	    
	  }
	 
	 public void setMainApp(App mainApp) {
		   this.mainApp = mainApp;
	 }
	
	private void openFile(File file) {
	   try {
	      desktop.open(file);
	   } catch (IOException ex) {
	      Logger.getLogger(
	      DataEntryViewController.class.getName()).log(
	      Level.SEVERE, null, ex
	       );
	  }
	}




}
