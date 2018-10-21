package com.nineplusten.app.model;

public class User {
  private String _id;
  private String userId;
  private String userPw;
  private String salt;
  private String email;

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

}
