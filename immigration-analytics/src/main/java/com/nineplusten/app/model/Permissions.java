package com.nineplusten.app.model;

import java.util.ArrayList;
import java.util.List;
import com.nineplusten.app.constant.Component;
import com.nineplusten.app.constant.Right;

public class Permissions {
  private String _id;
  private List<UserRole> role;
  private List<Right> rights;
  private Component permission;
  
  public String get_id() {
    return _id;
  }
  public void set_id(String _id) {
    this._id = _id;
  }
  public UserRole getRole() {
    return role != null ? role.get(0) : null;
  }
  public void setRole(UserRole role) {
    if (role != null) {
      this.role.set(0, role);
    } else {
      this.role = new ArrayList<UserRole>();
      this.role.add(role);
    }
  }
  public List<Right> getRights() {
    return rights;
  }
  public void setRights(List<Right> rights) {
    this.rights = rights;
  }
  public Component getPermission() {
    return permission;
  }
  public void setPermission(Component permission) {
    this.permission = permission;
  }
}
