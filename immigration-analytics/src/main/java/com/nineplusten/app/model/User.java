package com.nineplusten.app.model;

import java.util.ArrayList;
import java.util.List;

public class User {
  private String _id;
  private String userId;
  private String userPw;
  private String salt;
  private String email;
  private boolean active;
  private List<UserRole> userRole;
  private List<Agency> agency;

  public String getId() {
    return _id;
  }

  public void setId(String id) {
    this._id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserPw() {
    return userPw;
  }

  public void setUserPw(String userPw) {
    this.userPw = userPw;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public UserRole getUserRole() {
    return userRole != null ? userRole.get(0) : null;
  }

  public void setUserRole(UserRole role) {
    if (userRole != null) {
      this.userRole.set(0, role);
    } else {
      userRole = new ArrayList<UserRole>();
      userRole.add(role);
    }
    
  }

  public Agency getAgency() {
    return agency != null ? agency.get(0) : null;
  }

  public void setAgency(Agency agency) {
    if (this.agency != null) {
      this.agency.set(0, agency);
    } else {
      this.agency = new ArrayList<Agency>();
      this.agency.add(agency);
    }
  }

}
