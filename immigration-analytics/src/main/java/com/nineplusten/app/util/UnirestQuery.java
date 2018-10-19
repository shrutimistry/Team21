package com.nineplusten.app.util;

import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public class UnirestQuery extends Unirest {
  public static GetRequest get(String url) {
    return new RestDbIOGetRequest(HttpMethod.GET, url);
  }

  public static HttpRequestWithBody delete(String url) {
    return new RestDbIOBodyRequest(HttpMethod.DELETE, url);
  }
}
