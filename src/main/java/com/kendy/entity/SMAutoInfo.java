package com.kendy.entity;

import com.kendy.interfaces.Entity;

import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Id;

/**
 * 自动上码日志记录
 *
 * @author 林泽涛
 * @time 2018年3月26日 下午1:37:33
 */
public class SMAutoInfo implements Entity {

  /*
   * 记录日期，精确到时分秒
   */
  private SimpleStringProperty smAutoDate = new SimpleStringProperty();
  /*
   * 团队ID
   */
  private SimpleStringProperty smAutoTeamId = new SimpleStringProperty();
  /*
   * 玩家ID
   */
  private SimpleStringProperty smAutoPlayerId = new SimpleStringProperty();
  /*
   * 玩家名称
   */
  private SimpleStringProperty smAutoPlayerName = new SimpleStringProperty();
  /*
   * 申请数量
   */
  private SimpleStringProperty smAutoApplyAccount = new SimpleStringProperty();
  /*
   * 实时金额
   */
  private SimpleStringProperty smAutoCurrentMoney = new SimpleStringProperty();
  /*
   * 可用额度
   */
  private SimpleStringProperty smAutoAvailabelEdu = new SimpleStringProperty();
  /*
   * 判断是否同意审核
   */
  private SimpleStringProperty smAutoIsAgree = new SimpleStringProperty();
  /*
   * 程序审核结果
   */
  private SimpleStringProperty smAutoIsAgreeSuccess = new SimpleStringProperty();
  /*
   * 购买联盟币失败原因
   */
  private SimpleStringProperty smAutoFailDescription = new SimpleStringProperty();


  public SMAutoInfo() {
    super();
  }


  /**
   * 构造方法
   */
  public SMAutoInfo(String smAutoDate,
      String smAutoTeamId, String smAutoPlayerId,
      String smAutoPlayerName,
      String smAutoApplyAccount,
      String smAutoCurrentMoney,
      String smAutoAvailabelEdu,
      String smAutoIsAgree,
      String smAutoIsAgreeSuccess,
      String smAutoFailDescription) {
    this.smAutoDate = new SimpleStringProperty(smAutoDate);
    this.smAutoTeamId = new SimpleStringProperty(smAutoTeamId);
    this.smAutoPlayerId = new SimpleStringProperty(smAutoPlayerId);
    this.smAutoPlayerName = new SimpleStringProperty(smAutoPlayerName);
    this.smAutoApplyAccount = new SimpleStringProperty(smAutoApplyAccount);
    this.smAutoIsAgree = new SimpleStringProperty(smAutoIsAgree);
    this.smAutoIsAgreeSuccess = new SimpleStringProperty(smAutoIsAgreeSuccess);
    this.smAutoCurrentMoney = new SimpleStringProperty(smAutoCurrentMoney);
    this.smAutoAvailabelEdu = new SimpleStringProperty(smAutoAvailabelEdu);
    this.smAutoFailDescription = new SimpleStringProperty(smAutoFailDescription);
  }

  /*****************************************************************smAutoDate***/
  public SimpleStringProperty smAutoDateProperty() {
    return this.smAutoDate;
  }

  public String getSmAutoDate() {
    return this.smAutoDateProperty().get();
  }

  public void setSmAutoDate(final String smAutoDate) {
    this.smAutoDateProperty().set(smAutoDate);
  }

  /*****************************************************************smAutoPlayerId***/
  public SimpleStringProperty smAutoPlayerIdProperty() {
    return this.smAutoPlayerId;
  }

  public String getSmAutoPlayerId() {
    return this.smAutoPlayerIdProperty().get();
  }

  public void setSmAutoPlayerId(final String smAutoPlayerId) {
    this.smAutoPlayerIdProperty().set(smAutoPlayerId);
  }

  /*****************************************************************smAutoPlayerName***/
  public SimpleStringProperty smAutoPlayerNameProperty() {
    return this.smAutoPlayerName;
  }

  public String getSmAutoPlayerName() {
    return this.smAutoPlayerNameProperty().get();
  }

  public void setSmAutoPlayerName(final String smAutoPlayerName) {
    this.smAutoPlayerNameProperty().set(smAutoPlayerName);
  }

  /*****************************************************************smAutoApplyAccount***/
  public SimpleStringProperty smAutoApplyAccountProperty() {
    return this.smAutoApplyAccount;
  }

  public String getSmAutoApplyAccount() {
    return this.smAutoApplyAccountProperty().get();
  }

  public void setSmAutoApplyAccount(final String smAutoApplyAccount) {
    this.smAutoApplyAccountProperty().set(smAutoApplyAccount);
  }


  /*****************************************************************smAutoIsAgree***/
  public SimpleStringProperty smAutoIsAgreeProperty() {
    return this.smAutoIsAgree;
  }

  public String getSmAutoIsAgree() {
    return this.smAutoIsAgreeProperty().get();
  }

  public void setSmAutoIsAgree(final String smAutoIsAgree) {
    this.smAutoIsAgreeProperty().set(smAutoIsAgree);
  }

  /*****************************************************************smAutoIsAgreeSuccess***/
  public SimpleStringProperty smAutoIsAgreeSuccessProperty() {
    return this.smAutoIsAgreeSuccess;
  }

  public String getSmAutoIsAgreeSuccess() {
    return this.smAutoIsAgreeSuccessProperty().get();
  }

  public void setSmAutoIsAgreeSuccess(final String smAutoIsAgreeSuccess) {
    this.smAutoIsAgreeSuccessProperty().set(smAutoIsAgreeSuccess);
  }


  //***********************************************
  public String getSmAutoTeamId() {
    return smAutoTeamId.get();
  }

  public SimpleStringProperty smAutoTeamIdProperty() {
    return smAutoTeamId;
  }

  public void setSmAutoTeamId(String smAutoTeamId) {
    this.smAutoTeamId.set(smAutoTeamId);
  }


  public String getSmAutoCurrentMoney() {
    return smAutoCurrentMoney.get();
  }

  public SimpleStringProperty smAutoCurrentMoneyProperty() {
    return smAutoCurrentMoney;
  }

  public void setSmAutoCurrentMoney(String smAutoCurrentMoney) {
    this.smAutoCurrentMoney.set(smAutoCurrentMoney);
  }

  public String getSmAutoAvailabelEdu() {
    return smAutoAvailabelEdu.get();
  }

  public SimpleStringProperty smAutoAvailabelEduProperty() {
    return smAutoAvailabelEdu;
  }

  public void setSmAutoAvailabelEdu(String smAutoAvailabelEdu) {
    this.smAutoAvailabelEdu.set(smAutoAvailabelEdu);
  }

  public String getSmAutoFailDescription() {
    return smAutoFailDescription.get();
  }

  public SimpleStringProperty smAutoFailDescriptionProperty() {
    return smAutoFailDescription;
  }

  public void setSmAutoFailDescription(String smAutoFailDescription) {
    this.smAutoFailDescription.set(smAutoFailDescription);
  }
}
