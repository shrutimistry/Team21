package com.nineplusten.app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataUploadModel {
  private BooleanProperty excelLoaded;
  private ObjectProperty<ObservableList<TemplateData>> excelData;
  private StringProperty excelPath;
  
  public DataUploadModel() {
    excelLoaded = new SimpleBooleanProperty(false);
    excelData = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    excelPath = new SimpleStringProperty("");
  }
  
  public BooleanProperty excelLoadedProperty() {
    return excelLoaded;
  }
  
  public boolean isExcelLoaded() {
    return excelLoaded.get();
  }
  
  public void setExcelLoaded(boolean val) {
    excelLoaded.set(val);
  }
  
  public ObjectProperty<ObservableList<TemplateData>> excelDataProperty() {
    return excelData;
  }
  
  public ObservableList<TemplateData> getExcelData() {
    return excelData.get();
  }
  
  public void setExcelData(ObservableList<TemplateData> excelData) {
    this.excelData.set(excelData);
  }
  
  public StringProperty excelPathProperty() {
    return excelPath;
  }
  
  public String getExcelPath() {
    return excelPath.get();
  }
  
  public void setExcelPath(String path) {
    excelPath.set(path);
  }
}
