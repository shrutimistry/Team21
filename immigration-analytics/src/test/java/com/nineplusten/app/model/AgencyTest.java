package com.nineplusten.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgencyTest {
  @Test
  @DisplayName(".equals(): Two Agencies with same name")
  void equals_sameAgencyName() {
    Agency a = new Agency("Test");
    Agency b = new Agency("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two Agencies with same name with different casing")
  void equals_sameAgencyDiffCasingName() {
    Agency a = new Agency("tEst");
    Agency b = new Agency("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two Agencies with different names")
  void equals_differentAgencyName() {
    Agency a = new Agency("Test");
    Agency b = new Agency("Test1");
    assertNotEquals(a, b);
  }
  
  @Test
  @DisplayName(".hashCode(): Object hashed by agency name")
  void contains_agencyInHashSet() {
    Agency a = new Agency("Test");
    HashSet<Agency> set = new HashSet<>();
    set.add(a);
    
    Agency b = new Agency("Test");
    assertTrue(set.contains(b));
  }
  
  @Test
  @DisplayName(".hashCode(): Object not in set")
  void contains_agencyNotInHashSet() {
    Agency a = new Agency("Test");
    HashSet<Agency> set = new HashSet<>();
    set.add(a);
    
    Agency b = new Agency("Test123");
    assertFalse(set.contains(b));
  }
}
