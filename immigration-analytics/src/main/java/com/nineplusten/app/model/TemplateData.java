package com.nineplusten.app.model;

import java.util.Map;
import com.google.gson.annotations.Expose;

public class TemplateData {
  @Expose()
  private String _id;
  @Expose()
  private String clientId;
  @Expose()
  private Map<String, String> fieldData;
  @Expose(deserialize=false)
  private Template template;
  @Expose(deserialize=false)
  private Agency agency;
  
  public TemplateData(String _id, Template template, Agency agency, String clientId,
      Map<String, String> fieldData) {
    super();
    this._id = _id;
    this.clientId = clientId;
    this.fieldData = fieldData;
    this.template = template;
    this.agency = agency;
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

  public Template getTemplate() {
    return template;
  }

  public void setTemplate(Template template) {
    this.template = template;
  }

  public Agency getAgency() {
    return agency;
  }

  public void setAgency(Agency agency) {
    this.agency = agency;
  }

}
