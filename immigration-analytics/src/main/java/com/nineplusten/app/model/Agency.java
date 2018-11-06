package com.nineplusten.app.model;

import java.util.Objects;

public class Agency implements Comparable<Agency>{
  private String _id;
  private String agencyName;
  
  public Agency(String agencyName) {
    this.agencyName = agencyName;
  }
  
  public String get_id() {
    return _id;
  }
  public void set_id(String _id) {
    this._id = _id;
  }
  public String getRoleName() {
    return agencyName;
  }
  public void setRoleName(String agencyName) {
    this.agencyName = agencyName;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof UserRole) {
      return o.equals(agencyName);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(agencyName);
  }
  @Override
  public int compareTo(Agency o) {
    return this.agencyName.compareTo(o.agencyName);
  }
  
  @Override
  public String toString() {
    return _id;
  }
}
