package com.nineplusten.app.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SessionModel {
  private final ObjectProperty<User> sessionUser;
  
  public SessionModel() {
    this.sessionUser = new SimpleObjectProperty<User>();
  }
  
  public SessionModel(User user) {
    this.sessionUser = new SimpleObjectProperty<User>(user);
  }
  
  public ObjectProperty<User> sessionUserProperty() {
    return sessionUser;
  }
  
  public User getUser() {
    return sessionUser.get();
  }
  
  public void setUser(User user) {
    sessionUser.set(user);
  }
  
  public void invalidateSession() {
    sessionUser.setValue(null);;
  }
}
