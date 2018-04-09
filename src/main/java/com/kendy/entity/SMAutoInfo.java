package com.kendy.entity;

import com.kendy.interfaces.Entity;

import javafx.beans.property.SimpleStringProperty;

/**
 * 自动上码日志记录
 * 
 * @author 林泽涛
 * @time 2018年3月26日 下午1:37:33
 */
public class SMAutoInfo implements Entity{
	
	/*
	 * 记录日期，精确到时分秒
	 */
	private SimpleStringProperty smAutoDate = new SimpleStringProperty();
	/*
	 * 玩家ID
	 */
	private SimpleStringProperty smAutoPlayerId = new SimpleStringProperty();
	/*
	 * 玩家名称 
	 */
	private SimpleStringProperty smAutoPlayerName = new SimpleStringProperty();
	/*
	 * 牌局
	 */
	private SimpleStringProperty smAutoPaiju = new SimpleStringProperty();
	/*
	 * 申请数量
	 */
	private SimpleStringProperty smAutoApplyAccount = new SimpleStringProperty();
	/*
	 * 计算额度
	 */
	private SimpleStringProperty  smAutoAvailabel= new SimpleStringProperty();
	/*
	 * 是否勾选团队可上码
	 */
	private SimpleStringProperty smAutoIsTeamAvailabel = new SimpleStringProperty();
	/*
	 * 是否今日上码
	 */
	private SimpleStringProperty smAutoIsCurrentDay = new SimpleStringProperty();
	/*
	 * 是否次日上码
	 */
	private SimpleStringProperty smAutoIsNextDay = new SimpleStringProperty();
	/*
	 * 判断是否同意审核
	 */
	private SimpleStringProperty smAutoIsAgree = new SimpleStringProperty();
	/*
	 * 程序审核结果
	 */
	private SimpleStringProperty smAutoIsAgreeSuccess = new SimpleStringProperty();
	/*
	 * 团队的可上码
	 */
	private SimpleStringProperty smAutoTeamTotalAvailabel = new SimpleStringProperty();

	
	
	public SMAutoInfo() {
		super();
	}
	
	
	


	/**
	 *  构造方法
	 *  
	 * @param smAutoDate
	 * @param smAutoPlayerId
	 * @param smAutoPlayerName
	 * @param smAutoPaiju
	 * @param smAutoApplyAccount
	 * @param smAutoTeamTotalAvailabel
	 * @param smAutoAvailabel
	 * @param smAutoIsTeamAvailabel
	 * @param smAutoIsCurrentDay
	 * @param smAutoIsNextDay
	 * @param smAutoIsAgree
	 * @param smAutoIsAgreeSuccess
	 */
	public SMAutoInfo(
			String smAutoDate, 
			String smAutoPlayerId,
			String smAutoPlayerName, 
			String smAutoPaiju,
			String smAutoApplyAccount, 
			String smAutoTeamTotalAvailabel, 
			String smAutoAvailabel,
			String smAutoIsTeamAvailabel,
			String smAutoIsCurrentDay, 
			String smAutoIsNextDay, 
			String smAutoIsAgree, 
			String smAutoIsAgreeSuccess) {
		super();
		this.smAutoDate = new SimpleStringProperty(smAutoDate);
		this.smAutoPlayerId = new SimpleStringProperty(smAutoPlayerId);
		this.smAutoPlayerName = new SimpleStringProperty(smAutoPlayerName);
		this.smAutoPaiju = new SimpleStringProperty(smAutoPaiju);
		this.smAutoApplyAccount = new SimpleStringProperty(smAutoApplyAccount);
		this.smAutoTeamTotalAvailabel = new SimpleStringProperty(smAutoTeamTotalAvailabel);
		this.smAutoAvailabel = new SimpleStringProperty(smAutoAvailabel);
		this.smAutoIsTeamAvailabel = new SimpleStringProperty(smAutoIsTeamAvailabel);
		this.smAutoIsCurrentDay = new SimpleStringProperty(smAutoIsCurrentDay);
		this.smAutoIsNextDay = new SimpleStringProperty(smAutoIsNextDay);
		this.smAutoIsAgree = new SimpleStringProperty(smAutoIsAgree);
		this.smAutoIsAgreeSuccess = new SimpleStringProperty(smAutoIsAgreeSuccess);
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
	/*****************************************************************smAutoPaiju***/
	public SimpleStringProperty smAutoPaijuProperty() {
		return this.smAutoPaiju;
	}
	public String getSmAutoPaiju() {
		return this.smAutoPaijuProperty().get();
	}
	public void setSmAutoPaiju(final String smAutoPaiju) {
		this.smAutoPaijuProperty().set(smAutoPaiju);
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
	/*****************************************************************smAutoIsTeamAvailabel***/
	public SimpleStringProperty smAutoIsTeamAvailabelProperty() {
		return this.smAutoIsTeamAvailabel;
	}
	public String getSmAutoIsTeamAvailabel() {
		return this.smAutoIsTeamAvailabelProperty().get();
	}
	public void setSmAutoIsTeamAvailabel(final String smAutoIsTeamAvailabel) {
		this.smAutoIsTeamAvailabelProperty().set(smAutoIsTeamAvailabel);
	}
	/*****************************************************************smAutoIsCurrentDay***/
	public SimpleStringProperty smAutoIsCurrentDayProperty() {
		return this.smAutoIsCurrentDay;
	}
	public String getSmAutoIsCurrentDay() {
		return this.smAutoIsCurrentDayProperty().get();
	}
	public void setSmAutoIsCurrentDay(final String smAutoIsCurrentDay) {
		this.smAutoIsCurrentDayProperty().set(smAutoIsCurrentDay);
	}

	/*****************************************************************smAutoIsNextDay***/
	public SimpleStringProperty smAutoIsNextDayProperty() {
		return this.smAutoIsNextDay;
	}
	public String getSmAutoIsNextDay() {
		return this.smAutoIsNextDayProperty().get();
	}
	public void setSmAutoIsNextDay(final String smAutoIsNextDay) {
		this.smAutoIsNextDayProperty().set(smAutoIsNextDay);
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
	
	//==========================================
	public SimpleStringProperty smAutoAvailabelProperty() {
		return this.smAutoAvailabel;
	}

	public String getSmAutoAvailabel() {
		return this.smAutoAvailabelProperty().get();
	}

	public void setSmAutoAvailabel(final String smAutoAvailabel) {
		this.smAutoAvailabelProperty().set(smAutoAvailabel);
	}
	//***********************************************
	public SimpleStringProperty smAutoTeamTotalAvailabelProperty() {
		return this.smAutoTeamTotalAvailabel;
	}
	public String getSmAutoTeamTotalAvailabel() {
		return this.smAutoTeamTotalAvailabelProperty().get();
	}
	public void setSmAutoTeamTotalAvailabel(final String smAutoTeamTotalAvailabel) {
		this.smAutoTeamTotalAvailabelProperty().set(smAutoTeamTotalAvailabel);
	}


}
