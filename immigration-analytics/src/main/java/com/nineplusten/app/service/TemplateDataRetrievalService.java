package com.nineplusten.app.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.QueryUtil;
import javafx.beans.property.ReadOnlyObjectProperty;
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
        ObservableList<TemplateData> templateData =
            QueryUtil.fetchTemplateData(gson, template.get(), agency.get());
        return templateData;
      }
    };
  }
}
