package com.nineplusten.app.model;

import java.util.Map;
import java.util.Objects;
import com.google.gson.annotations.Expose;

public class Template implements Comparable<Template> {
  @Expose
  private String _id;
  @Expose
  private String templateName;
  // Map column_id to column_name
  @Expose
  private Map<String, String> columns;
  
  public Template(String templateName) {
    this.templateName = templateName;
  }
  
  public Template(String _id, String templateName, Map<String, String> columns) {
    this._id = _id;
    this.templateName = templateName;
    this.columns = columns;
  }
  
  public String get_id() {
    return _id;
  }
  public void set_id(String _id) {
    this._id = _id;
  }
  public String getTemplateName() {
    return templateName;
  }
  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }
  public Map<String, String> getColumns() {
    return columns;
  }
  public void setColumns(Map<String, String> columns) {
    this.columns = columns;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof Template) {
      return ((Template) o).getTemplateName().equalsIgnoreCase(templateName);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.templateName);
  }
  
  @Override
  public int compareTo(Template o) {
    return this.templateName.compareTo(o.getTemplateName());
  }
  
  @Override
  public String toString() {
    return templateName;
  }
  
}
