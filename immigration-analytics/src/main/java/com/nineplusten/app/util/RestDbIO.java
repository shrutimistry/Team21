package com.nineplusten.app.util;

import java.util.Map;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestDbIO {
  private static final String API_KEY = "bf284180feb47ea0014bbcad417065cf43285";
  private static final String API_URL = "https://imgrnsvc-000a.restdb.io/rest";

  public static HttpResponse<JsonNode> getResponse(String route) throws UnirestException {
    HttpResponse<JsonNode> response = Unirest.get(API_URL + "{route}").routeParam("route", route)
        .header("x-apikey", API_KEY).header("cache-control", "no-cache").asJson();
    return response;
  }

  public static HttpResponse<JsonNode> getResponse(String route,
      Map<String, Object> queryParameters) throws UnirestException {
    HttpResponse<JsonNode> response =
        Unirest.get(API_URL + "{route}").routeParam("route", route).header("x-apikey", API_KEY)
            .header("cache-control", "no-cache").queryString(queryParameters).asJson();
    return response;
  }

  public static HttpResponse<JsonNode> getResponse(String route, String queryField,
      Object queryValue) throws UnirestException {
    HttpResponse<JsonNode> response =
        Unirest.get(API_URL + "{route}").routeParam("route", route).header("x-apikey", API_KEY)
            .header("cache-control", "no-cache").queryString(queryField, queryValue).asJson();
    return response;
  }

  public static JSONArray get(String route) throws UnirestException {
    JSONArray result = null;
    HttpResponse<JsonNode> response = getResponse(route);
    // Check for OK (works because not async)
    if (response.getStatus() == HttpStatus.SC_OK) {
      JsonNode body = response.getBody();
      if (body != null) {
        result = body.getArray();
      }
    }
    return result;
  }

  public static JSONArray get(String route, Map<String, Object> queryParameters)
      throws UnirestException {
    JSONArray result = null;
    HttpResponse<JsonNode> response = getResponse(route, queryParameters);
    // Check for OK (works because not async)
    if (response.getStatus() == HttpStatus.SC_OK) {
      JsonNode body = response.getBody();
      if (body != null) {
        result = body.getArray();
      }
    }
    return result;
  }

  public static JSONArray get(String route, String queryField, Object queryValue)
      throws UnirestException {
    JSONArray result = null;
    HttpResponse<JsonNode> response = getResponse(route, queryField, queryValue);
    // Check for OK (works because not async)
    if (response.getStatus() == HttpStatus.SC_OK) {
      JsonNode body = response.getBody();
      if (body != null) {
        result = body.getArray();
      }
    }
    return result;
  }

  public static HttpResponse<JsonNode> postResponse(String route, Object body)
      throws UnirestException {
    HttpResponse<JsonNode> response;
    if (body instanceof JSONObject) {
      response = Unirest.post(API_URL + "{route}").routeParam("route", route)
          .header("content-type", "application/json").header("x-apikey", API_KEY)
          .header("cache-control", "no-cache").body((JSONObject) body).asJson();
    } else if (body instanceof JSONArray) {
      response = Unirest.post(API_URL + "{route}").routeParam("route", route)
          .header("content-type", "application/json").header("x-apikey", API_KEY)
          .header("cache-control", "no-cache").body((JSONArray) body).asJson();
    } else {
      // requires object wrapper
      response = Unirest.post(API_URL + "{route}").routeParam("route", route)
          .header("content-type", "application/json").header("x-apikey", API_KEY)
          .header("cache-control", "no-cache").body(body).asJson();
    }
    return response;
  }

  public static boolean post(String route, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = postResponse(route, body);
    return response.getStatus() == HttpStatus.SC_CREATED;
  }

  public static HttpResponse<JsonNode> putResponse(String route, String objectId, Object body)
      throws UnirestException {
    HttpResponse<JsonNode> response;
    if (body instanceof JSONObject) {
      response = Unirest.put(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body((JSONObject) body)
          .asJson();
    } else if (body instanceof JSONArray) {
      response = Unirest.put(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body((JSONArray) body)
          .asJson();
    } else {
      // requires object wrapper
      response = Unirest.put(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body(body).asJson();
    }
    return response;
  }

  public static boolean put(String route, String objectId, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = putResponse(route, objectId, body);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  public static HttpResponse<JsonNode> patchResponse(String route, String objectId, Object body)
      throws UnirestException {
    HttpResponse<JsonNode> response;
    if (body instanceof JSONObject) {
      response = Unirest.patch(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body((JSONObject) body)
          .asJson();
    } else if (body instanceof JSONArray) {
      response = Unirest.patch(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body((JSONArray) body)
          .asJson();
    } else {
      // requires object wrapper
      response = Unirest.patch(API_URL + "{route}/{ID}").routeParam("route", route)
          .routeParam("ID", objectId).header("content-type", "application/json")
          .header("x-apikey", API_KEY).header("cache-control", "no-cache").body(body).asJson();
    }
    return response;
  }

  public static boolean patch(String route, String objectId, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = patchResponse(route, objectId, body);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  public static HttpResponse<JsonNode> deleteResponse(String route, String objectId)
      throws UnirestException {
    HttpResponse<JsonNode> response = Unirest.delete(API_URL + "{route}/{ID}")
        .routeParam("route", route).routeParam("ID", objectId).header("x-apikey", API_KEY)
        .header("cache-control", "no-cache").asJson();
    return response;
  }

  public static HttpResponse<JsonNode> deleteResponse(String route, String[] objectIds)
      throws UnirestException {
    HttpResponse<JsonNode> response = Unirest.delete(API_URL + "{route}/*").routeParam("route", route)
        .header("content-type", "application/json").header("x-apikey", API_KEY)
        .header("cache-control", "no-cache").body(new JSONArray(objectIds)).asJson();
    return response;
  }

  public static boolean delete(String route, String objectId) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, objectId);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  public static boolean delete(String route, String[] objectIds) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, objectIds);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  public static JSONObject singleResultToJSONObject(JSONArray jsonResponse) {
    JSONObject jsonObj = null;
    if (jsonResponse != null && jsonResponse.length() == 1) {
      jsonObj = jsonResponse.getJSONObject(0);
    } else {
      System.out.println("Input was null or contained multiple JSON objects.");
    }
    return jsonObj;
  }
}
