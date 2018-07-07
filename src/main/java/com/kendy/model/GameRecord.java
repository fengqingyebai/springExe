package com.kendy.model;

public class GameRecord extends BasicRecord{
	
	/*
	 * 软件时间,以自动下载的时间为准
	 */
	private String softDate;
	
	/*
	 * 桌号,如第X局
	 */
	private String tableId;
	
	/*
	 * 是否已经结算过
	 * 0：未结算  1：已结算
	 */
	private String isJiesuaned = "0";
	
	/*
	 * 联盟类型
	 */
	private String lmType;
	
	/*
	 * 临时数据？？？
	 */
	private String personCount;
	
	
	/**
	 * 
	 */
	public GameRecord() {
		super();
	}
	

	/**
	 * @param sumHandsCount
	 * @param playerId
	 * @param playerName
	 * @param clubId
	 * @param clubName
	 * @param insuranceHiji
	 * @param clubInsurance
	 * @param insurance
	 * @param yszj
	 * @param finisedTime
	 */
	public GameRecord(String sumHandsCount, String playerId, String playerName, String clubId, String clubName,
			String insuranceHiji, String clubInsurance, String insurance, String yszj, String finisedTime) {
		super(sumHandsCount, playerId, playerName, clubId, clubName, insuranceHiji, clubInsurance, insurance, yszj,
				finisedTime);
	}

	public String getSoftDate() {
		return softDate;
	}

	public void setSoftDate(String softDate) {
		this.softDate = softDate;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getIsJiesuaned() {
		return isJiesuaned;
	}

	public void setIsJiesuaned(String isJiesuaned) {
		this.isJiesuaned = isJiesuaned;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}
	
	public String getPersonCount() {
		return personCount;
	}

	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}
	

}
