package com.nineplusten.app.service;

import org.json.JSONObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.QueryUtil;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class QueryService extends Service<ObservableList<TemplateData>> {
  private ReadOnlyObjectProperty<JSONObject> query;
  private Gson gson;

  public QueryService(ReadOnlyObjectProperty<JSONObject> query) {
    gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .excludeFieldsWithoutExposeAnnotation().create();
    this.query = query;
  }

  @Override
  protected Task<ObservableList<TemplateData>> createTask() {
    return new Task<ObservableList<TemplateData>>() {
      @Override
      protected ObservableList<TemplateData> call() throws Exception {
        ObservableList<TemplateData> templateData = QueryUtil.fetchTemplateData(gson, query.get());
        return templateData;
      }
    };
  }
}
