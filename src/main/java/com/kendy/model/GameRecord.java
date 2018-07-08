package com.kendy.model;

public class GameRecord extends BasicRecord{
	
	
	private String shishou;//实收
	
	private String chuHuishui;//出回水
	
	private String huiBao;//保回
	
	private String shuihouxian;//水后险
	
	private String shouHuishui;//收回水
	
	private String heLirun;//合利润
	
	private String softDate; // 软件时间,以自动下载的时间为准
	
	private String tableId; // 桌号,如第X局
	
	private String isJiesuaned = "0"; // 是否已经结算过 0：未结算  1：已结算
	
	private String teamId;
	
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


	public String getShishou() {
		return shishou;
	}


	public void setShishou(String shishou) {
		this.shishou = shishou;
	}


	public String getChuHuishui() {
		return chuHuishui;
	}


	public void setChuHuishui(String chuHuishui) {
		this.chuHuishui = chuHuishui;
	}


	public String getHuiBao() {
		return huiBao;
	}


	public void setHuiBao(String huiBao) {
		this.huiBao = huiBao;
	}


	public String getShuihouxian() {
		return shuihouxian;
	}


	public void setShuihouxian(String shuihouxian) {
		this.shuihouxian = shuihouxian;
	}


	public String getShouHuishui() {
		return shouHuishui;
	}


	public void setShouHuishui(String shouHuishui) {
		this.shouHuishui = shouHuishui;
	}


	public String getHeLirun() {
		return heLirun;
	}


	public void setHeLirun(String heLirun) {
		this.heLirun = heLirun;
	}


	public String getTeamId() {
		return teamId;
	}


	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	
	
	

}
