package com.nineplusten.app.model.query;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class ColumnQueryChoice {
  private final StringProperty templateName;
  private final StringProperty columnName;
  private final StringProperty columnId;
  private final ObjectProperty<CheckBox> selectedCheck;
  private final BooleanProperty selected;
  
  public ColumnQueryChoice(String templateName, String columnName, String columnId) {
    this.templateName = new SimpleStringProperty(templateName);
    this.columnName = new SimpleStringProperty(columnName);
    this.columnId = new SimpleStringProperty(columnId);
    CheckBox cb = new CheckBox();
    this.selectedCheck = new SimpleObjectProperty<>(cb);
    this.selected = new SimpleBooleanProperty(false);
    Bindings.bindBidirectional(this.selected, selectedCheck.get().selectedProperty());
  }
  
  
  public ObjectProperty<CheckBox> selectedCheckProperty() {
    return selectedCheck;
  }
  
  public CheckBox getSelectedCheck() {
    return selectedCheck.get();
  }
  
  public void setSelectedCheck(CheckBox val) {
    selectedCheck.set(val);
  }
  
  public BooleanProperty selectedProperty() {
    return selected;
  }
  
  public boolean isSelected() {
    return selected.get();
  }
  
  public void setSelected(boolean val) {
    selected.set(val);
  }
  
  public StringProperty templateNameProperty() {
    return templateName;
  }
  
  public String getTemplateName() {
    return templateName.get();
  }
  
  public void setTemplateName(String val) {
    templateName.set(val);
  }
  
  public StringProperty columnNameProperty() {
    return columnName;
  }
  
  public String getColumnName() {
    return columnName.get();
  }
  
  public void setColumnName(String val) {
    columnName.set(val);
  }
  
  public StringProperty columnIdProperty() {
    return columnId;
  }
  
  public String getColumnId() {
    return columnId.get();
  }
  
  public void setColumnId(String val) {
    columnId.set(val);
  }
}
