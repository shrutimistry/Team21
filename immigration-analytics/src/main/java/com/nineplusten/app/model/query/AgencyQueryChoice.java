package com.nineplusten.app.model.query;

import com.nineplusten.app.model.Agency;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class AgencyQueryChoice {
  private final ObjectProperty<Agency> agency;
  private final StringProperty agencyName;
  private final ObjectProperty<CheckBox> selectedCheck;
  private final BooleanProperty selected;
  
  public AgencyQueryChoice(Agency agency) {
    this.agency = new SimpleObjectProperty<>(agency);
    this.agencyName = new  SimpleStringProperty(agency.getAgencyName());
    CheckBox cb = new CheckBox();
    this.selectedCheck = new SimpleObjectProperty<>(cb);
    this.selected = new SimpleBooleanProperty(false);
    Bindings.bindBidirectional(this.selected, selectedCheck.get().selectedProperty());
  }
  
  public ObjectProperty<Agency> agencyProperty() {
    return agency;
  }
  
  public Agency getAgency() {
    return agency.get();
  }
  
  public void setAgency(Agency val) {
    agency.set(val);
  }
  
  public ObjectProperty<CheckBox> selectedCheckProperty() {
    return selectedCheck;
  }
  
  public CheckBox getSelectedCheck() {
    return selectedCheck.get();
  }
  
  public void setSelectedCheck(CheckBox val) {
    selectedCheck.set(val);
  }
  
  public BooleanProperty selectedProperty() {
    return selected;
  }
  
  public boolean isSelected() {
    return selected.get();
  }
  
  public void setSelcted(boolean val) {
    selected.set(val);
  }
  
  public StringProperty agencyNameProperty() {
    return agencyName;
  }
  
  public String getAgencyName() {
    return agencyName.get();
  }
  
  public void setAgencyName(String val) {
    agencyName.set(val);
  }
}
