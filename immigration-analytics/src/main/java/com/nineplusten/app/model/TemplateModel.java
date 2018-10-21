package com.nineplusten.app.model;

import java.util.ArrayList;

public class TemplateModel {
  private ArrayList<String> colNames = new ArrayList<>();
  
  public ArrayList<String> getColList(){
    return this.colNames;
  }
  
  public boolean addCol(String colName) {
    if (!colNames.contains(colName)) {
      colNames.add(colName);
      return true;
    }

    return false;
  }
  
  public boolean removeCol(String colName) {
    return colNames.remove(colName);
  }
  

}
