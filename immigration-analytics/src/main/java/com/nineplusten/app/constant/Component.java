package com.nineplusten.app.constant;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Component {

  ACCOUNT(new Right[] {Right.CREATE, Right.VIEW_SELF}, new String[] {"Account Creation", "Account Information"}),
  TEMPLATE(new Right[] {Right.MODIFY, Right.VIEW_ALL}, new String[] {"Template Editor", "Template Browser"}),
  DATA(new Right[] {Right.VIEW_ALL, Right.VIEW_SELF}, new String[] {"Data Browser", "My Data"});

  private final String[] tabTitles;
  private final Right[] rights;
  private final Map<Right, String> rightsMap;

  Component(Right[] rights, String[] tabTitles) {
    this.tabTitles = tabTitles;
    this.rights = rights;
    this.rightsMap = Collections.unmodifiableMap(initializeMapping());
  }

  private String[] tabTitles() {
    return tabTitles;
  }

  private Right[] rights() {
    return rights;
  }
  
  public Map<Right, String> permissions() {
    return rightsMap;
  }

  private Map<Right, String> initializeMapping() {
    Map<Right, String> rMap = new LinkedHashMap<>();
    for (int i = 0; i < this.rights().length; i++) {
      rMap.put(this.rights()[i], this.tabTitles()[i]);
    }
    return rMap;
  }
}
