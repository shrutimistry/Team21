package com.nineplusten.app.util;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * The wrapper class for handling HTTP requests to restdb.io. Use this to write GET, POST, PUT,
 * PATCH, DELETE requests. Please reference https://restdb.io/docs/rest-api#restdb for more detailed
 * information about each request. Methods include abstracted versions of response (ie 'get()') and
 * methods which return the response (ie 'getResponse()'). Depending on your usage, you may or may
 * not need the entire response body.
 */
public class RestDbIO {

  /** The API_KEY. */
  private static final String API_KEY = "bf284180feb47ea0014bbcad417065cf43285";

  /** The API_URL. */
  private static final String API_URL = "https://imgrnsvc-000a.restdb.io/rest";

  /**
   * Makes a GET request to restdb.io. Returns the full HTTP response.
   * 
   * Usage: getResponse("/users");
   *
   * @param route the API path appended to host URL
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> getResponse(String route) throws UnirestException {
    HttpResponse<JsonNode> response = Unirest.get(API_URL + "{route}").routeParam("route", route)
        .header("x-apikey", API_KEY).header("cache-control", "no-cache").asJson();
    return response;
  }

  /**
   * Makes a GET request to restdb.io. Returns the full HTTP response.
   * 
   * Usage: getResponse("/users", {object_id});
   *
   * @param route the API path appended to host URL
   * @param objectId the object _id value
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> getResponse(String route, String objectId)
      throws UnirestException {
    HttpResponse<JsonNode> response =
        Unirest.get(API_URL + "{route}/{ID}").routeParam("route", route).routeParam("ID", objectId)
            .header("x-apikey", API_KEY).header("cache-control", "no-cache").asJson();
    return response;
  }

  /**
   * Makes a GET request to restdb.io using a restdb.io query (see
   * https://restdb.io/docs/querying-with-the-api#restdb). Returns the full HTTP response.
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "admin"); getResponse("/users", o);
   * 
   * @param route the API path appended to host URL
   * @param jsonQuery a restdb.io query (see https://restdb.io/docs/querying-with-the-api#restdb)
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> getResponse(String route, JSONObject jsonQuery)
      throws UnirestException {
    HttpResponse<JsonNode> response = ((RestDbIOGetRequest) UnirestQuery.get(API_URL + "{route}")
        .routeParam("route", route).header("x-apikey", API_KEY).header("cache-control", "no-cache"))
            .queryString(jsonQuery).asJson();
    return response;
  }

  /**
   * Makes a GET request to restdb.io using a simplified restdb.io query. Specify only the query
   * field and query parameter. Returns the full HTTP response.
   * 
   * Usage:
   * 
   * getResponse("/users", "user_id", "admin");
   *
   * @param route the API path appended to host URL
   * @param queryField the column/field to query by
   * @param queryValue the query value to match
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> getResponse(String route, String queryField,
      Object queryValue) throws UnirestException {
    HttpResponse<JsonNode> response = ((RestDbIOGetRequest) UnirestQuery.get(API_URL + "{route}")
        .routeParam("route", route).header("x-apikey", API_KEY).header("cache-control", "no-cache"))
            .queryString(queryField, queryValue).asJson();
    return response;
  }

  /**
   * Makes a GET request to restdb.io. Returns a JSONArray consisting of JSONObjects.
   * 
   * Usage: get("/users");
   *
   * @param route the API path appended to host URL
   * @return the JSONArray object consisting of all items in the collection
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
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

  /**
   * Makes a GET request to restdb.io. Returns a JSONArray consisting of JSONObjects.
   * 
   * Usage: get("/users", {object_id});
   *
   * @param route the API path appended to host URL
   * @return the JSONArray object consisting of all items in the collection
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static JSONArray get(String route, String objectId) throws UnirestException {
    JSONArray result = null;
    HttpResponse<JsonNode> response = getResponse(route, objectId);
    // Check for OK (works because not async)
    if (response.getStatus() == HttpStatus.SC_OK) {
      JsonNode body = response.getBody();
      if (body != null) {
        result = body.getArray();
      }
    }
    return result;
  }

  /**
   * Makes a GET request to restdb.io using a restdb.io query (see
   * https://restdb.io/docs/querying-with-the-api#restdb). Returns a JSONArray consisting of
   * JSONObjects.
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "admin"); get("/users", o);
   *
   * @param route the API path appended to host URL
   * @param jsonQuery a restdb.io query (see https://restdb.io/docs/querying-with-the-api#restdb)
   * @return the JSONArray object consisting of all items returned by the query result
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static JSONArray get(String route, JSONObject jsonQuery) throws UnirestException {
    JSONArray result = null;
    HttpResponse<JsonNode> response = getResponse(route, jsonQuery);
    // Check for OK (works because not async)
    if (response.getStatus() == HttpStatus.SC_OK) {
      JsonNode body = response.getBody();
      if (body != null) {
        result = body.getArray();
      }
    }
    return result;
  }

  /**
   * Makes a GET request to restdb.io using a simplified restdb.io query. Specify only the query
   * field and query parameter. Returns a JSONArray consisting of JSONObjects.
   * 
   * Usage:
   * 
   * get("/users", "user_id", "admin");
   *
   * @param route the API path appended to host URL
   * @param queryField the column/field to query by
   * @param queryValue the query value to match
   * @return the JSONArray object consisting of all items returned by the query result
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
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

  /**
   * Makes a POST request to restdb.io with a specified body (see API docs for body format). Returns
   * the full HTTP response.
   *
   * Usage:
   * 
   * JSONObject body = new JSONObject(); o.put("user_id", "test123"); o.put("email",
   * "test123@example.com"); o.put("active", true); postResponse("/users", body);
   * 
   * @param route the API path appended to host URL
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
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
    } else if (body instanceof String) {
      response = Unirest.post(API_URL + "{route}").routeParam("route", route)
          .header("content-type", "application/json").header("x-apikey", API_KEY)
          .header("cache-control", "no-cache").body((String) body).asJson();
    } else {
      // requires object wrapper
      response = Unirest.post(API_URL + "{route}").routeParam("route", route)
          .header("content-type", "application/json").header("x-apikey", API_KEY)
          .header("cache-control", "no-cache").body(body).asJson();
    }
    return response;
  }

  /**
   * Makes a POST request to restdb.io with a specified body (see API docs for body format). Returns
   * true if the request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * JSONObject body = new JSONObject(); o.put("user_id", "test123"); o.put("email",
   * "test123@example.com"); o.put("active", true); post("/users", body);
   *
   * @param route the API path appended to host URL
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean post(String route, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = postResponse(route, body);
    return response.getStatus() == HttpStatus.SC_CREATED;
  }

  /**
   * Makes a PUT request to restdb.io with a specified body (see API docs for body format). Returns
   * the full HTTP response.
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test321"); o.put("email",
   * "test123@example.com"); o.put("active", true); putResponse("/users", {object_id}, o);
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
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

  /**
   * Makes a PUT request to restdb.io with a specified body (see API docs for body format). Returns
   * true if the request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test321"); o.put("email",
   * "test123@example.com"); o.put("active", true); put("/users", {object_id}, o);
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean put(String route, String objectId, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = putResponse(route, objectId, body);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Makes a PATCH request to restdb.io with a specified body (see API docs for body format).
   * Returns the full HTTP response.
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test123"); o.put("email",
   * "test123@example.com"); o.put("active", true); patchResponse("/users", {object_id}, o);
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
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

  /**
   * Makes a PATCH request to restdb.io with a specified body (see API docs for body format).
   * Returns true if the request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test123"); o.put("email",
   * "test123@example.com"); o.put("active", true); patch("/users", {object_id}, o);
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @param body the request body. Must be either JSONObject, JSONArray, or Object (with wrapper)
   *        types
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean patch(String route, String objectId, Object body) throws UnirestException {
    HttpResponse<JsonNode> response = patchResponse(route, objectId, body);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Makes a DELETE request to restdb.io and deletes the object with the specified id. Returns the
   * full HTTP response.
   * 
   * Usage:
   * 
   * deleteResponse("/users", {object_id});
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> deleteResponse(String route, String objectId)
      throws UnirestException {
    HttpResponse<JsonNode> response = Unirest.delete(API_URL + "{route}/{ID}")
        .routeParam("route", route).routeParam("ID", objectId).header("x-apikey", API_KEY)
        .header("cache-control", "no-cache").asJson();
    return response;
  }

  /**
   * Makes a DELETE request to restdb.io and deletes all objects with the specified ids. Returns the
   * full HTTP response.
   * 
   * Usage:
   * 
   * String [] ids = {object_id_1, object_id_2}; deleteResponse("/users", ids);
   *
   * @param route the API path appended to host URL
   * @param objectIds the object ids ("_id") to delete (as a String array)
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> deleteResponse(String route, String[] objectIds)
      throws UnirestException {
    HttpResponse<JsonNode> response =
        Unirest.delete(API_URL + "{route}/*").routeParam("route", route)
            .header("content-type", "application/json").header("x-apikey", API_KEY)
            .header("cache-control", "no-cache").body(new JSONArray(objectIds)).asJson();
    return response;
  }

  /**
   * Makes a DELETE request to restdb.io using a simplified query (key/value). Returns the full HTTP
   * response.
   * 
   * Usage:
   * 
   * deleteResponse("/users", "user_id", "test123");
   *
   * @param route the API path appended to host URL
   * @param name the query value to match
   * @param value the column/field to query by
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> deleteResponse(String route, String name, Object value)
      throws UnirestException {
    HttpResponse<JsonNode> response =
        ((RestDbIOBodyRequest) UnirestQuery.delete(API_URL + "{route}/*").routeParam("route", route)
            .header("content-type", "application/json").header("x-apikey", API_KEY)
            .header("cache-control", "no-cache")).queryString(name, value).asJson();
    return response;
  }

  /**
   * Makes a DELETE request to restdb.io using a restdb.io query (see
   * https://restdb.io/docs/querying-with-the-api#restdb). Returns the full HTTP response.
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test123"); deleteResponse("/users", o);
   *
   * @param route the API path appended to host URL
   * @param jsonQuery a restdb.io query (see https://restdb.io/docs/querying-with-the-api#restdb)
   * @return the HTTP response from restdb.io server
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static HttpResponse<JsonNode> deleteResponse(String route, JSONObject jsonQuery)
      throws UnirestException {
    HttpResponse<JsonNode> response =
        ((RestDbIOBodyRequest) UnirestQuery.delete(API_URL + "{route}/*").routeParam("route", route)
            .header("content-type", "application/json").header("x-apikey", API_KEY)
            .header("cache-control", "no-cache")).queryString(jsonQuery).asJson();
    return response;
  }

  /**
   * Makes a DELETE request to restdb.io and deletes the object with the specified id. Returns true
   * if the request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * delete("/users", {object_id});
   * 
   * (where {object_id} refers to the specific ID to modify)
   *
   * @param route the API path appended to host URL
   * @param objectId the _id of the object being modified
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean delete(String route, String objectId) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, objectId);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Makes a DELETE request to restdb.io and deletes all objects with the specified ids. Returns
   * true if the request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * String [] ids = {object_id_1, object_id_2}; delete("/users", ids);
   *
   * @param route the API path appended to host URL
   * @param objectIds the object ids ("_id") to delete (as a String array)
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean delete(String route, String[] objectIds) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, objectIds);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Makes a DELETE request to restdb.io using a simplified query (key/value). Returns true if the
   * request is successful (HTTP 200).
   * 
   * Usage:
   * 
   * deleteResponse("/users", "user_id", "test123");
   *
   * @param route the API path appended to host URL
   * @param name the query value to match
   * @param value the column/field to query by
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean delete(String route, String name, Object value) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, name, value);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Makes a DELETE request to restdb.io using a restdb.io query (see
   * https://restdb.io/docs/querying-with-the-api#restdb). Returns true if the request is successful
   * (HTTP 200).
   * 
   * Usage:
   * 
   * JSONObject o = new JSONObject(); o.put("user_id", "test123"); deleteResponse("/users", o);
   *
   * @param route the API path appended to host URL
   * @param jsonQuery a restdb.io query (see https://restdb.io/docs/querying-with-the-api#restdb)
   * @return true, if request is successful (HTTP 200)
   * @throws UnirestException Java lib exception (usually for poorly formed requests)
   */
  public static boolean delete(String route, JSONObject jsonQuery) throws UnirestException {
    HttpResponse<JsonNode> response = deleteResponse(route, jsonQuery);
    return response.getStatus() == HttpStatus.SC_OK;
  }

  /**
   * Utility helper method to automatically convert JSONArray to JSONObject iff the JSONArray length
   * == 1. If the requirement is not met, null is returned.
   *
   * @param jsonResponse the JSONArray object with length == 1
   * @return the JSON object
   */
  public static JSONObject singleResultToJSONObject(JSONArray jsonResponse) {
    JSONObject jsonObj = null;
    if (jsonResponse != null && jsonResponse.length() == 1) {
      jsonObj = jsonResponse.getJSONObject(0);
    }
    return jsonObj;
  }
}
