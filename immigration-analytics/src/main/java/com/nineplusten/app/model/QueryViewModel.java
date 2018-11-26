package com.nineplusten.app.model;

import org.json.JSONObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class QueryViewModel {
  private final BooleanProperty anyAgencySelected;
  private final BooleanProperty templateSelected;
  private final BooleanProperty anyColumnSelected;
  private final BooleanProperty anySelected;
  private final ObjectProperty<Template> selectedTemplate;
  private final ObjectProperty<JSONObject> queryObject;

  public QueryViewModel() {
    anyAgencySelected = new SimpleBooleanProperty(false);
    templateSelected = new SimpleBooleanProperty(false);
    anyColumnSelected = new SimpleBooleanProperty(false);
    anySelected = new SimpleBooleanProperty(false);
    anySelected.bind(anyAgencySelected.and(templateSelected).and(anyColumnSelected));
    selectedTemplate = new SimpleObjectProperty<>();
    queryObject = new SimpleObjectProperty<>();
  }

  public BooleanProperty anyAgencySelectedProperty() {
    return anyAgencySelected;
  }

  public BooleanProperty templateSelectedProperty() {
    return templateSelected;
  }

  public BooleanProperty anyColumnSelectedProperty() {
    return anyColumnSelected;
  }

  public boolean isAnyAgencySelected() {
    return anyAgencySelected.get();
  }

  public void setAnyAgencySelected(boolean anyAgencySelected) {
    this.anyAgencySelected.set(anyAgencySelected);
  }

  public boolean isTemplateSelected() {
    return templateSelected.get();
  }

  public void setTemplateSelected(boolean templateSelected) {
    this.templateSelected.set(templateSelected);
  }

  public boolean isAnyColumnSelected() {
    return anyColumnSelected.get();
  }

  public void setAnyColumnSelected(boolean anyColumnSelected) {
    this.anyColumnSelected.set(anyColumnSelected);
  }
  
  public BooleanProperty anySelectedProperty() {
    return anySelected;
  }
  
  public boolean isAnySelected() {
    return anySelected.get();
  }
  
  public ObjectProperty<Template> selectedTemplateProperty() {
    return selectedTemplate;
  }
  
  public Template getSelectedTemplate() {
    return selectedTemplate.get();
  }
  
  public void setSelectedTemplate(Template template) {
    selectedTemplate.set(template);
  }
  
  public ObjectProperty<JSONObject> queryObjectProperty() {
    return queryObject;
  }
  
  public JSONObject getQueryObject() {
    return queryObject.get();
  }
  
  public void setQueryObject(JSONObject query) {
    queryObject.set(query);
  }
}
