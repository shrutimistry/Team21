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
    String queryString = QueryStringUtil.getQueryString(name, value);
    this.url += queryString;
    return this;
  }

  public HttpRequest queryString(JSONObject jsonQuery) {
    String queryString = QueryStringUtil.getQueryString(jsonQuery);
    this.url += queryString;
    return this;
  }
}
