package com.nineplusten.app.model;

import java.util.Objects;

public class UserRole implements Comparable<UserRole>{
  private String _id;
  private String roleName;
  
  public UserRole(String roleName) {
    this.roleName = roleName;
  }
  
  public String get_id() {
    return _id;
  }
  public void set_id(String _id) {
    this._id = _id;
  }
  public String getRoleName() {
    return roleName;
  }
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof UserRole) {
      return ((UserRole) o).getRoleName().equals(roleName);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(roleName);
  }
  @Override
  public int compareTo(UserRole o) {
    return this.roleName.compareTo(o.roleName);
  }
  
  @Override
  public String toString() {
    return roleName;
  }
}
