package com.nineplusten.app.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.nineplusten.app.constant.Routes;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.TemplateData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class QueryUtil {
  public static String getQueryString(String name, Object value) {
    StringBuilder queryString = new StringBuilder();
    queryString.append("?q=");
    try {
      queryString.append(URLEncoder.encode("{\"" + name + "\"", "UTF-8")).append(":").append(
          URLEncoder.encode((value == null) ? "" : "\"" + value.toString() + "\"}", "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    return queryString.toString();
  }

  public static String getQueryString(JSONObject jsonQuery) {
    StringBuilder queryString = new StringBuilder();
    if (jsonQuery != null && jsonQuery.length() > 0) {
      queryString.append("?q=");
      try {
        queryString.append(URLEncoder.encode(jsonQuery.toString(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
    return queryString.toString();
  }

  public static JSONObject buildTemplateJsonQuery(Template template, Agency agency) {
    JSONObject jsQuery = new JSONObject();
    JSONObject templateSubQuery = new JSONObject();
    JSONObject agencySubQuery = new JSONObject();

    templateSubQuery.put("_id", template.get_id());
    agencySubQuery.put("_id", agency.get_id());
    jsQuery.put("template", templateSubQuery);
    jsQuery.put("agency", agencySubQuery);
    return jsQuery;
  }

  public static ObservableList<TemplateData> fetchTemplateData(Gson gson, Template template,
      Agency agency) {
    ObservableList<TemplateData> result;
    try {
      Type templateDataType = new TypeToken<Collection<TemplateData>>() {}.getType();
      List<TemplateData> resultList = gson.fromJson(
          RestDbIO.get(Routes.TEMPLATES_DATA, buildTemplateJsonQuery(template, agency)).toString(),
          templateDataType);
      result = FXCollections.observableArrayList(resultList);
    } catch (UnirestException e) {
      e.printStackTrace();
      result = null;
    }
    return result;
  }

  public static ObservableList<TemplateData> fetchTemplateData(Gson gson, JSONObject query) {
    ObservableList<TemplateData> result;
    try {
      Type templateDataType = new TypeToken<Collection<TemplateData>>() {}.getType();
      List<TemplateData> resultList =
          gson.fromJson(RestDbIO.get(Routes.TEMPLATES_DATA, query).toString(), templateDataType);
      result = FXCollections.observableArrayList(resultList);
    } catch (UnirestException e) {
      e.printStackTrace();
      result = null;
    }
    return result;
  }
}
