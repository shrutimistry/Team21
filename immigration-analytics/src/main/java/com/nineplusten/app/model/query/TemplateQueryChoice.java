package com.nineplusten.app.model.query;

import com.nineplusten.app.model.Template;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class TemplateQueryChoice {
  private final ObjectProperty<Template> template;
  private final StringProperty templateName;
  private final ObjectProperty<CheckBox> selectedCheck;
  private final BooleanProperty selected;
  
  public TemplateQueryChoice(Template template) {
    this.template = new SimpleObjectProperty<>(template);
    this.templateName = new  SimpleStringProperty(template.getTemplateName());
    CheckBox cb = new CheckBox();
    this.selectedCheck = new SimpleObjectProperty<>(cb);
    this.selected = new SimpleBooleanProperty(false);
    Bindings.bindBidirectional(this.selected, selectedCheck.get().selectedProperty());
  }
  
  public ObjectProperty<Template> templateProperty() {
    return template;
  }
  
  public Template getTemplate() {
    return template.get();
  }
  
  public void setTemplate(Template val) {
    template.set(val);
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
}
