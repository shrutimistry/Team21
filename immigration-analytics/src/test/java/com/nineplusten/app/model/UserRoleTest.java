package com.nineplusten.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRoleTest {
  @Test
  @DisplayName(".equals(): Two User Roles with same name")
  void equals_sameUserRoleName() {
    UserRole a = new UserRole("Test");
    UserRole b = new UserRole("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two User Roles with same name with different casing")
  void equals_sameUserRoleDiffCasingName() {
    UserRole a = new UserRole("tEst");
    UserRole b = new UserRole("Test");
    assertEquals(a, b);
  }
  
  @Test
  @DisplayName(".equals(): Two User Roles with different names")
  void equals_differentUserRoleName() {
    UserRole a = new UserRole("Test");
    UserRole b = new UserRole("Test1");
    assertNotEquals(a, b);
  }
  
  @Test
  @DisplayName(".hashCode(): Object hashed by UserRole name")
  void contains_UserRoleInHashSet() {
    UserRole a = new UserRole("Test");
    HashSet<UserRole> set = new HashSet<>();
    set.add(a);
    
    UserRole b = new UserRole("Test");
    assertTrue(set.contains(b));
  }
  
  @Test
  @DisplayName(".hashCode(): Object not in set")
  void contains_UserRoleNotInHashSet() {
    UserRole a = new UserRole("Test");
    HashSet<UserRole> set = new HashSet<>();
    set.add(a);
    
    UserRole b = new UserRole("Test123");
    assertFalse(set.contains(b));
  }
}
