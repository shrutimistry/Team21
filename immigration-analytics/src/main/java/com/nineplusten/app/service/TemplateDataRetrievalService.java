package com.nineplusten.app.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.constant.Routes;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TemplateDataRetrievalService extends Service<ObservableList<TemplateData>> {
  private ReadOnlyObjectProperty<Agency> agency;
  private ReadOnlyObjectProperty<Template> template;
  private Gson gson;

  public TemplateDataRetrievalService(ReadOnlyObjectProperty<Agency> agency,
      ReadOnlyObjectProperty<Template> template) {
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation().create();
    this.agency = agency;
    this.template = template;
  }

  @Override
  protected Task<ObservableList<TemplateData>> createTask() {
    return new Task<ObservableList<TemplateData>>() {
      @Override
      protected ObservableList<TemplateData> call() throws Exception {
        ObservableList<TemplateData> templateData = fetchTemplateData();
        return templateData;
      }
    };
  }

  private ObservableList<TemplateData> fetchTemplateData() {
    ObservableList<TemplateData> result;
    try {
      Type templateDataType = new TypeToken<Collection<TemplateData>>() {}.getType();
      List<TemplateData> resultList = gson.fromJson(
          RestDbIO.get(Routes.TEMPLATES_DATA, buildTemplateJsonQuery(template.get(), agency.get()))
              .toString(),
          templateDataType);
      result = FXCollections.observableArrayList(resultList);
    } catch (UnirestException e) {
      e.printStackTrace();
      result = null;
    }
    return result;
  }

  private JSONObject buildTemplateJsonQuery(Template template, Agency agency) {
    JSONObject jsQuery = new JSONObject();
    JSONObject templateSubQuery = new JSONObject();
    JSONObject agencySubQuery = new JSONObject();

    templateSubQuery.put("_id", template.get_id());
    agencySubQuery.put("_id", agency.get_id());
    jsQuery.put("template", templateSubQuery);
    jsQuery.put("agency", agencySubQuery);
    return jsQuery;
  }

}
