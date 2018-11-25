package com.nineplusten.app.service;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.constant.Routes;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import com.nineplusten.app.util.QueryUtil;
import com.nineplusten.app.util.RestDbIO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SubmitDataService extends Service<Boolean> {
  private ObjectProperty<ObservableList<TemplateData>> data;
  private Agency agency;
  private ReadOnlyObjectProperty<Template> template;
  private Gson gson;

  public SubmitDataService(ObjectProperty<ObservableList<TemplateData>> data, Agency agency,
      ReadOnlyObjectProperty<Template> template) {
    this.data = data;
    this.gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithoutExposeAnnotation().create();
    this.agency = agency;
    this.template = template;
  }

  @Override
  protected Task<Boolean> createTask() {
    return new Task<Boolean>() {
      @Override
      protected Boolean call() throws Exception {
        Set<TemplateData> existingData = queryExistingData();
        List<TemplateData> submissionData = data.get().stream()
            .filter(data -> !existingData.contains(data)).collect(Collectors.toList());
        Type listType = new TypeToken<List<TemplateData>>() {}.getType();
        String dataJson = gson.toJson(submissionData, listType);
        return RestDbIO.post(Routes.TEMPLATES_DATA, dataJson);
      }
    };
  }

  private Set<TemplateData> queryExistingData() {
    Set<TemplateData> existingData;
    Type templateDataType = new TypeToken<Collection<TemplateData>>() {}.getType();
    try {
      existingData = new HashSet<TemplateData>(gson.fromJson(
              RestDbIO.get(Routes.TEMPLATES_DATA,
                  QueryUtil.buildTemplateJsonQuery(template.get(), agency)).toString(),
              templateDataType));
    } catch (UnirestException e) {
      e.printStackTrace();
      existingData = null;
    }
    return existingData;
  }
}
