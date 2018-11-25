package com.nineplusten.app.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONObject;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Template;

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
}
