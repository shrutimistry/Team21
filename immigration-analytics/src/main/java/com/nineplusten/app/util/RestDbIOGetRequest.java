package com.nineplusten.app.util;

import org.json.JSONObject;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;

public class RestDbIOGetRequest extends GetRequest {
  public RestDbIOGetRequest(HttpMethod method, String url) {
    super(method, url);
  }

  public HttpRequest queryString(String name, Object value) {
    String queryString = QueryUtil.getQueryString(name, value);
    this.url += queryString;
    return this;
  }

  public HttpRequest queryString(JSONObject jsonQuery) {
    String queryString = QueryUtil.getQueryString(jsonQuery);
    this.url += queryString;
    return this;
  }
}
