package com.nineplusten.app.util;

import org.json.JSONObject;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.request.HttpRequestWithBody;

public class RestDbIOBodyRequest extends HttpRequestWithBody {

  public RestDbIOBodyRequest(HttpMethod method, String url) {
    super(method, url);
  }
  
  public HttpRequestWithBody queryString(String name, Object value) {
    String queryString = QueryStringUtil.getQueryString(name, value);
    this.url += queryString;
    return this;
  }

  public HttpRequestWithBody queryString(JSONObject jsonQuery) {
    String queryString = QueryStringUtil.getQueryString(jsonQuery);
    this.url += queryString;
    return this;
  }

}
