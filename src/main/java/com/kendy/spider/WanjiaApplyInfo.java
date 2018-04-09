package com.kendy.spider;

import com.alibaba.fastjson.JSON;

/**
 * 玩家申请相关
 * 
 * @author 林泽涛
 * @time 2018年3月24日 上午10:55:37
 */
public class WanjiaApplyInfo {
	
	private String showId;
	private String strNick;
	private String strSmallCover;
	private Long gameRoomId;
	private String gameRoomName;
	private Long buyStack;
	private Long uuid;
	private Long totalBuyin;
	private Long totalProfit;
	
	public static void main(String... strings) {
		String json = "{\"showId\":\"71258550\",\"strNick\":\"leon668\",\"strSmallCover\":\"http://info.pokermate.net/data/2018/2/4/39101_1517736974551.png\",\"gameRoomId\":28288654,\"gameRoomName\":\"🏧510-122\",\"buyStack\":1000,\"uuid\":39101,\"totalBuyin\":3500,\"totalProfit\":-83}";
//		json = "{}";
		WanjiaApplyInfo parseObject = JSON.parseObject(json, WanjiaApplyInfo.class);
		System.out.println(parseObject.toString());
	}

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	public String getStrNick() {
		return strNick;
	}

	public void setStrNick(String strNick) {
		this.strNick = strNick;
	}

	public String getStrSmallCover() {
		return strSmallCover;
	}

	public void setStrSmallCover(String strSmallCover) {
		this.strSmallCover = strSmallCover;
	}

	public Long getGameRoomId() {
		return gameRoomId;
	}

	public void setGameRoomId(Long gameRoomId) {
		this.gameRoomId = gameRoomId;
	}

	public String getGameRoomName() {
		return gameRoomName;
	}

	public void setGameRoomName(String gameRoomName) {
		this.gameRoomName = gameRoomName;
	}

	public Long getBuyStack() {
		return buyStack;
	}

	public void setBuyStack(Long buyStack) {
		this.buyStack = buyStack;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public Long getTotalBuyin() {
		return totalBuyin;
	}

	public void setTotalBuyin(Long totalBuyin) {
		this.totalBuyin = totalBuyin;
	}

	public Long getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Long totalProfit) {
		this.totalProfit = totalProfit;
	}

	@Override
	public String toString() {
		return "WanjiaApplyInfo [showId=" + showId + ", strNick=" + strNick + ", strSmallCover=" + strSmallCover
				+ ", gameRoomId=" + gameRoomId + ", gameRoomName=" + gameRoomName + ", buyStack=" + buyStack + ", uuid="
				+ uuid + ", totalBuyin=" + totalBuyin + ", totalProfit=" + totalProfit + "]";
	}
	
	
	
	
}
