package com.nineplusten.app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.skin.TableColumnHeader;

public class TemplateEditorModel {
  private BooleanProperty modified;
  private ObjectProperty<TableColumnHeader> selectedHeader;
  private ObjectProperty<Template> referenceTemplate;
  private MapProperty<TableColumn<String, ?>, String> columnIdMap;

  public TemplateEditorModel() {
    selectedHeader = new SimpleObjectProperty<TableColumnHeader>(null);
    referenceTemplate = new SimpleObjectProperty<Template>(null);
    modified = new SimpleBooleanProperty(false);
    columnIdMap =
        new SimpleMapProperty<TableColumn<String, ?>, String>(FXCollections.observableHashMap());
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

  public MapProperty<TableColumn<String, ?>, String> columnIdMapProperty() {
    return columnIdMap;
  }
  
  public String getColumnId(TableColumn<String,?> column) {
    return columnIdMap.get(column);
  }
  
  @SuppressWarnings("unchecked")
  public <T extends TableColumnBase<?, ?>> void putColumnId(T column, String id) {
    if(columnIdMap.containsValue(id)) {
      id = getNewId(id);
    }
    columnIdMap.put((TableColumn<String, ?>) column, id);
  }
  
  public <T extends TableColumnBase<?, ?>> void putColumnId(TableColumn<String, ?> column, String id) {
    if(columnIdMap.containsValue(id)) {
      id = getNewId(id);
    }
    columnIdMap.put(column, id);
  }
  
  public ObjectProperty<TableColumnHeader> selectedHeaderProperty() {
    return selectedHeader;
  }
  
  public TableColumnHeader getSelectedHeader() {
    return selectedHeader.get();
  }
  
  public void setSelectedHeader(TableColumnHeader header) {
    selectedHeader.set(header);
  }
  
  public ObjectProperty<Template> referenceTemplateProperty() {
    return referenceTemplate;
  }
  
  public Template getReferenceTemplate() {
    return referenceTemplate.get();
  }
  
  public void setReferenceTemplate(Template t) {
    referenceTemplate.set(t);
  }
  
  private String getNewId(String id) {
    int incrementingVal = 1;
    String newId = id + "_" + incrementingVal;
    while (columnIdMap.containsValue(newId)) {
      incrementingVal++;
      newId = id + "_" + incrementingVal;
    }
    id = newId;
    return id;
  }
}
