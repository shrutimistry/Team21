package com.nineplusten.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TemplateTest {
  @Test
  @DisplayName(".equals(): Two Templates with same name")
  void equals_sameTemplateName() {
    Template a = new Template("Test");
    Template b = new Template("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two Templates with same name with different casing")
  void equals_sameTemplateDiffCasingName() {
    Template a = new Template("tEst");
    Template b = new Template("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two Templates with different names")
  void equals_differentTemplateName() {
    Template a = new Template("Test");
    Template b = new Template("Test1");
    assertNotEquals(a, b);
  }
  
  @Test
  @DisplayName(".hashCode(): Object hashed by Template name")
  void contains_TemplateInHashSet() {
    Template a = new Template("Test");
    HashSet<Template> set = new HashSet<>();
    set.add(a);
    
    Template b = new Template("Test");
    assertTrue(set.contains(b));
  }
  
  @Test
  @DisplayName(".hashCode(): Object not in set")
  void contains_TemplateNotInHashSet() {
    Template a = new Template("Test");
    HashSet<Template> set = new HashSet<>();
    set.add(a);
    
    Template b = new Template("Test123");
    assertFalse(set.contains(b));
  }
}
