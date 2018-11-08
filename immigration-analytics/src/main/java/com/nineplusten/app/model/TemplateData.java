package com.nineplusten.app.model;

import java.util.Map;

public class TemplateData {
  private String _id;
  private String clientId;
  private Map<String, String> fieldData;
  
  public TemplateData(String _id, Template template, Agency agency, String clientId,
      Map<String, String> fieldData) {
    super();
    this._id = _id;
    this.clientId = clientId;
    this.fieldData = fieldData;
  }
  
  public TemplateData(Map<String, String> fieldData) {
    this.fieldData = fieldData;
  }

  public String get_id() {
    return _id;
  }
  public void set_id(String _id) {
    this._id = _id;
  }
  public String getClientId() {
    return clientId;
  }
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
  public Map<String, String> getFieldData() {
    return fieldData;
  }
  public void setFieldData(Map<String, String> fieldData) {
    this.fieldData = fieldData;
  }

}
