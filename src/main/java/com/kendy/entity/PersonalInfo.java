package com.kendy.entity;

import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

public class PersonalInfo implements Entity {

  private SimpleStringProperty personalPlayerId = new SimpleStringProperty();
  private SimpleStringProperty personalPlayerName = new SimpleStringProperty();
  private SimpleStringProperty personalSumZJ = new SimpleStringProperty();
  private SimpleStringProperty personalSumHS = new SimpleStringProperty();
  private SimpleStringProperty personalSumHB = new SimpleStringProperty();
  //
  private SimpleStringProperty personalSum = new SimpleStringProperty("0");
  private SimpleStringProperty hasJiesuaned = new SimpleStringProperty("0");


  public PersonalInfo() {
    super();
  }



  public PersonalInfo(String personalPlayerId,
      String personalPlayerName,
      String personalSumZJ, String personalSumHS,
      String personalSumHB, String personalSum) {
    this.personalPlayerId = new SimpleStringProperty(personalPlayerId);
    this.personalPlayerName = new SimpleStringProperty(personalPlayerName);
    this.personalSumZJ = new SimpleStringProperty(personalSumZJ);
    this.personalSumHS = new SimpleStringProperty(personalSumHS);
    this.personalSumHB = new SimpleStringProperty(personalSumHB);
    this.personalSum = new SimpleStringProperty(personalSum);
  }

  public String getPersonalPlayerId() {
    return personalPlayerId.get();
  }

  public SimpleStringProperty personalPlayerIdProperty() {
    return personalPlayerId;
  }

  public void setPersonalPlayerId(String personalPlayerId) {
    this.personalPlayerId.set(personalPlayerId);
  }

  public String getPersonalPlayerName() {
    return personalPlayerName.get();
  }

  public SimpleStringProperty personalPlayerNameProperty() {
    return personalPlayerName;
  }

  public void setPersonalPlayerName(String personalPlayerName) {
    this.personalPlayerName.set(personalPlayerName);
  }

  public String getPersonalSumZJ() {
    return personalSumZJ.get();
  }

  public SimpleStringProperty personalSumZJProperty() {
    return personalSumZJ;
  }

  public void setPersonalSumZJ(String personalSumZJ) {
    this.personalSumZJ.set(personalSumZJ);
  }

  public String getPersonalSumHS() {
    return personalSumHS.get();
  }

  public SimpleStringProperty personalSumHSProperty() {
    return personalSumHS;
  }

  public void setPersonalSumHS(String personalSumHS) {
    this.personalSumHS.set(personalSumHS);
  }

  public String getPersonalSumHB() {
    return personalSumHB.get();
  }

  public SimpleStringProperty personalSumHBProperty() {
    return personalSumHB;
  }

  public void setPersonalSumHB(String personalSumHB) {
    this.personalSumHB.set(personalSumHB);
  }

  public String getPersonalSum() {
    return personalSum.get();
  }

  public SimpleStringProperty personalSumProperty() {
    return personalSum;
  }

  public void setPersonalSum(String personalSum) {
    this.personalSum.set(personalSum);
  }

  public String getHasJiesuaned() {
    return hasJiesuaned.get();
  }

  public SimpleStringProperty hasJiesuanedProperty() {
    return hasJiesuaned;
  }

  public void setHasJiesuaned(String hasJiesuaned) {
    this.hasJiesuaned.set(hasJiesuaned);
  }
}
