package com.nineplusten.app.util;

public class EqualUtil {
  public static boolean isEmpty(String s) {
    if (s == null) {
      return true;
    } else {
      return s.trim().length() == 0;
    }
    
  }
}
