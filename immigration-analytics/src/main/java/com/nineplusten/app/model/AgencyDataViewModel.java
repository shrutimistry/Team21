package com.nineplusten.app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AgencyDataViewModel {
  private final BooleanProperty editMode;
  private final ListProperty<TemplateData> tableData;
  private ObjectProperty<Agency> agency;
  private BooleanProperty modified;
  
  public AgencyDataViewModel() {
    editMode = new SimpleBooleanProperty(false);
    tableData = new SimpleListProperty<>(FXCollections.observableArrayList());
    agency = new SimpleObjectProperty<Agency>();
    modified = new SimpleBooleanProperty(false);
  }
  
  public BooleanProperty editModeProperty() {
    return editMode;
  }
  
  public boolean isEditMode() {
    return editMode.get();
  }
  
  public void setEditMode(boolean val) {
    editMode.set(val);
  }

  public ObservableList<TemplateData> getTableData() {
    return tableData.get();
  }
  public void setTableData(ObservableList<TemplateData> val) {
    tableData.set(val);
  }
  
  public ListProperty<TemplateData> tableDataProperty() {
    return tableData;
  }
  
  public ObjectProperty<Agency> agencyProperty() {
    return agency;
  }
  
  public Agency getAgency() {
    return agency.get();
  }
  
  public void setAgency(Agency val) {
    agency.set(val);
  }
  
  public BooleanProperty modifiedProperty() {
    return modified;
  }
  
  public boolean isModified() {
    return modified.get();
  }
  
  public void setModified(boolean val) {
    modified.set(val);
  }
}
