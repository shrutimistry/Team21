package com.nineplusten.app.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EqualUtilTest {
  @Test
  @DisplayName(".isEmpty(): null")
  void isEmpty_null() {
    String a = null;
    assertTrue(EqualUtil.isEmpty(a));
  }
  
  @Test
  @DisplayName(".isEmpty(): empty String")
  void isEmpty_emptyString() {
    String a = "";
    assertTrue(EqualUtil.isEmpty(a));
  }
  
  @Test
  @DisplayName(".isEmpty(): String with spaces")
  void isEmpty_stringWithSpaces() {
    String a = "                    ";
    assertTrue(EqualUtil.isEmpty(a));
  }
  
  @Test
  @DisplayName(".isEmpty(): String with tabs")
  void isEmpty_stringWithTabs() {
    String a = "            ";
    assertTrue(EqualUtil.isEmpty(a));
  }
  
  @Test
  @DisplayName(".isEmpty(): Non-empty")
  void isEmpty_nonEmptyString() {
    String a = "test";
    assertFalse(EqualUtil.isEmpty(a));
  }
}
