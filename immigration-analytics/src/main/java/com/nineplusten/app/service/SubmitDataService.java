package com.nineplusten.app.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nineplusten.app.constant.Routes;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SubmitDataService extends Service<Boolean> {
  private ObjectProperty<ObservableList<TemplateData>> data;
  private Gson gson;

  public SubmitDataService(ObjectProperty<ObservableList<TemplateData>> data) {
    this.data = data;
    this.gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithoutExposeAnnotation().create();
  }

  @Override
  protected Task<Boolean> createTask() {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws Exception {
        String dataJson = gson.toJson(data.get());
        return RestDbIO.post(Routes.TEMPLATES_DATA, dataJson);
      }
    };
  }
}
