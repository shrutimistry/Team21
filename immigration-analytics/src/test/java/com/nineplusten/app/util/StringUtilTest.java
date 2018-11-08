package com.nineplusten.app.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StringUtilTest {
  @Test
  @DisplayName(".toSnakeCase(): null")
  void toSnakeCase_null() {
    String a = null;
    assertNull(StringUtil.toSnakeCase(a));
  }
  
  @Test
  @DisplayName(".toSnakeCase(): empty string")
  void toSnakeCase_empty() {
    String a = "";
    
    String actual = StringUtil.toSnakeCase(a);
    String expected = "";
    assertEquals(expected, actual);
  }
  
  @Test
  @DisplayName(".toSnakeCase(): one word")
  void toSnakeCase_oneWord() {
    String a = "test";
    
    String actual = StringUtil.toSnakeCase(a);
    String expected = "test";
    assertEquals(expected, actual);
  }
  
  @Test
  @DisplayName(".toSnakeCase(): one word caps")
  void toSnakeCase_oneWordCaps() {
    String a = "TEST";
    
    String actual = StringUtil.toSnakeCase(a);
    String expected = "test";
    assertEquals(expected, actual);
  }
  
  @Test
  @DisplayName(".toSnakeCase(): multiple words")
  void toSnakeCase_multipleWords() {
    String a = "Test Case";
    
    String actual = StringUtil.toSnakeCase(a);
    String expected = "test_case";
    assertEquals(expected, actual);
  }
  
  @Test
  @DisplayName(".toSnakeCase(): already snake_case")
  void toSnakeCase_alreadySnakeCase() {
    String a = "test_case";
    
    String actual = StringUtil.toSnakeCase(a);
    String expected = "test_case";
    assertEquals(expected, actual);
  }
}
