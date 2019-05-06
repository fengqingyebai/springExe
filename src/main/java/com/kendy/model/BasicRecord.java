package com.kendy.model;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;

/**
 * 新版本战绩记录（统一） 备注：保险是整个牌局的保险合计的总和 而保险合计只是单个玩家单条记录的保险
 *
 * @author 林泽涛
 * @time 2018年7月7日 下午9:10:43
 */
public class BasicRecord {

  @MyExcelField(title = "牌局类型")
  private String jutype;

  @MyExcelField(title = "总手数")
  private String sumhandscount;

  @MyExcelField(title = "玩家ID")
  private String playerid;

  @MyExcelField(title = "玩家昵称")
  private String playerName;

  @MyExcelField(title = "俱乐部ID")
  private String clubid;

  @MyExcelField(title = "俱乐部")
  private String clubName;

  @MyExcelField(title = "保险合计#彩池合计")
  private String singleinsurance = "0";

  @MyExcelField(title = "俱乐部保险#俱乐部彩池")
  private String clubinsurance= "0";

  @MyExcelField(title = "保险#总彩池")
  private String currenttableinsurance = "0";

  @MyExcelField(title = "原始战绩#战绩")
  private String yszj="0";

  @MyExcelField(title = "联盟币扣减")
  private String lmbKoujian = "0";

  @MyExcelField(title = "保险再分配")
  private String baoxianZaifenpei = "0";

  @MyExcelField(title = "俱乐部再分配")
  private String clubZaifenpei = "0";

  @MyExcelField(title = "是否庄家位")
  private String isZhuangwei = "0";

  @MyExcelField(title = "结束时间")
  private String finishedTime;

  @MyExcelField(title = "玩家手数")
  private String wanjiaShoushu="0";

  @MyExcelField(title = "玩家总加注")
  private String wanjiaZongJiazhu="0";

  @MyExcelField(title = "玩家总下注")
  private String wanjiaZongXiazhu="0";

  @MyExcelField(title = "俱乐部分成")
  private String clubFencheng="0";

  @MyExcelField(title = "联盟分成")
  private String lianmengFencheng="0";

  @MyExcelField(title = "所属代理")
  private String suoshuProxy="0";

  @MyExcelField(title = "代理返水")
  private String proxyFanshui="0";



  public BasicRecord() {
    super();
  }


  public String getJutype() {
    return jutype;
  }

  public void setJutype(String jutype) {
    this.jutype = jutype;
  }

  public String getSumhandscount() {
    return sumhandscount;
  }

  public void setSumhandscount(String sumhandscount) {
    this.sumhandscount = sumhandscount;
  }

  public String getPlayerid() {
    return playerid;
  }

  public void setPlayerid(String playerid) {
    this.playerid = playerid;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getClubid() {
    return clubid;
  }

  public void setClubid(String clubid) {
    this.clubid = clubid;
  }

  public String getClubName() {
    return clubName;
  }

  public void setClubName(String clubName) {
    this.clubName = clubName;
  }

  public String getSingleinsurance() {
    return singleinsurance;
  }

  public void setSingleinsurance(String singleinsurance) {
    this.singleinsurance = singleinsurance;
  }

  public String getClubinsurance() {
    return clubinsurance;
  }

  public void setClubinsurance(String clubinsurance) {
    this.clubinsurance = clubinsurance;
  }

  public String getCurrenttableinsurance() {
    return currenttableinsurance;
  }

  public void setCurrenttableinsurance(String currenttableinsurance) {
    this.currenttableinsurance = currenttableinsurance;
  }

  public String getYszj() {
    return yszj;
  }

  public void setYszj(String yszj) {
    this.yszj = yszj;
  }

  public String getLmbKoujian() {
    return lmbKoujian;
  }

  public void setLmbKoujian(String lmbKoujian) {
    this.lmbKoujian = lmbKoujian;
  }

  public String getBaoxianZaifenpei() {
    return baoxianZaifenpei;
  }

  public void setBaoxianZaifenpei(String baoxianZaifenpei) {
    this.baoxianZaifenpei = baoxianZaifenpei;
  }

  public String getClubZaifenpei() {
    return clubZaifenpei;
  }

  public void setClubZaifenpei(String clubZaifenpei) {
    this.clubZaifenpei = clubZaifenpei;
  }

  public String getIsZhuangwei() {
    return isZhuangwei;
  }

  public void setIsZhuangwei(String isZhuangwei) {
    this.isZhuangwei = isZhuangwei;
  }

  public String getFinishedTime() {
    return finishedTime;
  }

  public void setFinishedTime(String finishedTime) {
    this.finishedTime = finishedTime;
  }

  public String getWanjiaShoushu() {
    return wanjiaShoushu;
  }

  public void setWanjiaShoushu(String wanjiaShoushu) {
    this.wanjiaShoushu = wanjiaShoushu;
  }

  public String getWanjiaZongJiazhu() {
    return wanjiaZongJiazhu;
  }

  public void setWanjiaZongJiazhu(String wanjiaZongJiazhu) {
    this.wanjiaZongJiazhu = wanjiaZongJiazhu;
  }

  public String getWanjiaZongXiazhu() {
    return wanjiaZongXiazhu;
  }

  public void setWanjiaZongXiazhu(String wanjiaZongXiazhu) {
    this.wanjiaZongXiazhu = wanjiaZongXiazhu;
  }

  public String getClubFencheng() {
    return clubFencheng;
  }

  public void setClubFencheng(String clubFencheng) {
    this.clubFencheng = clubFencheng;
  }

  public String getLianmengFencheng() {
    return lianmengFencheng;
  }

  public void setLianmengFencheng(String lianmengFencheng) {
    this.lianmengFencheng = lianmengFencheng;
  }

  public String getSuoshuProxy() {
    return suoshuProxy;
  }

  public void setSuoshuProxy(String suoshuProxy) {
    this.suoshuProxy = suoshuProxy;
  }

  public String getProxyFanshui() {
    return proxyFanshui;
  }

  public void setProxyFanshui(String proxyFanshui) {
    this.proxyFanshui = proxyFanshui;
  }
}
