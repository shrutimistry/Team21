package com.nineplusten.app.util;

public class StringUtil {
  public static String toSnakeCase(String text) {
    if (text != null) {
      return text.toLowerCase().trim().replace(" ", "_");
    }
    return null;
  }
}
