package com.nineplusten.app.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QueryStringUtilTest {
  final String ENC = "UTF-8";

  @Test
  @DisplayName(".getQueryString: Key/Value functionality")
  void getQueryString_keyValue() throws UnsupportedEncodingException {
    String key = "Name";
    String value = "Dennis";
    String actual = QueryStringUtil.getQueryString(key, value);
    String expected =
        "?q=" + URLEncoder.encode("{\"Name\"", ENC) + ":" + URLEncoder.encode("\"Dennis\"}", ENC);
    assertEquals(expected, actual);
  }
  
  @Test
  @DisplayName(".getQueryString: JSONObject functionality")
  void getQueryString_JSON() throws UnsupportedEncodingException {
    String key = "Name";
    String value = "Dennis";
    HashMap<String, String> map = new HashMap<>();
    map.put(key, value);
    JSONObject o = new JSONObject(map);
    
    String actual = QueryStringUtil.getQueryString(o);
    String expected =
        "?q=" + URLEncoder.encode("{\"Name\":\"Dennis\"}", ENC);
    System.out.println(expected);
    System.out.println(actual);
    assertEquals(expected, actual);
  }
}
