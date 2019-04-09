package com.kendy.model;

/**
 * 游戏记录
 * <p>
 * 记录了一个玩家一场记录的相关金额信息
 * 注意：GameRecord实体每新增一个字段，都需要在此模型域中添加
 * </p>
 *
 * @author linzt
 * @time 2018年8月1日
 */
public class GameRecordModel extends BasicRecord {


  private String shishou;// 实收

  private String chuhuishui;// 出回水

  private String huibao;// 保回

  private String shuihouxian;// 水后险

  private String shouhuishui;// 收回水

  private String helirun;// 合利润

  private String softTime; // 软件时间,以自动下载的时间为准

  private String tableid; // 桌号,如第X局

  private String isjiesuaned = "0"; // 是否已经结算过 0：未结算 1：已结算

  private String teamId; // 团队ID( 请注意：关联最新的人员表获取最新

  /*
   * 联盟类型
   */
  private String lmtype;

  /*
   * 临时数据
   */
  private String personCount;

  /*
   * 级别
   */
  private String level;

  /*
   * 是否已经清空过 0：未清空 1：已清空
   */
  private String iscleared = "0";

  /*
   * 导入的时间
   */
  private String importtime ;

  /**
   * 个人回保
   */
  private String personalHuibao = "0%";

  /**
   * 个人回水=个人回水比例 * 负数战绩
   */
  private String personalHuishui = "0%";

  /**
   * 个人是否已经结算
   */
  private String personalJiesuan = "0";

  /**
   * 原始名称
   */
  private String beginplayername = "";
  /**
   * 自定义回水回保类型：0团队  1个人
   */
  private String hshbType = "0";


  /**
   *
   */
  public GameRecordModel() {
    super();
  }

  public String getShishou() {
    return shishou;
  }

  public void setShishou(String shishou) {
    this.shishou = shishou;
  }

  public String getChuhuishui() {
    return chuhuishui;
  }

  public void setChuhuishui(String chuhuishui) {
    this.chuhuishui = chuhuishui;
  }

  public String getHuibao() {
    return huibao;
  }

  public void setHuibao(String huibao) {
    this.huibao = huibao;
  }

  public String getShuihouxian() {
    return shuihouxian;
  }

  public void setShuihouxian(String shuihouxian) {
    this.shuihouxian = shuihouxian;
  }

  public String getShouhuishui() {
    return shouhuishui;
  }

  public void setShouhuishui(String shouhuishui) {
    this.shouhuishui = shouhuishui;
  }

  public String getHelirun() {
    return helirun;
  }

  public void setHelirun(String helirun) {
    this.helirun = helirun;
  }

  public String getSoftTime() {
    return softTime;
  }

  public void setSoftTime(String softTime) {
    this.softTime = softTime;
  }

  public String getTableid() {
    return tableid;
  }

  public void setTableid(String tableid) {
    this.tableid = tableid;
  }

  public String getIsjiesuaned() {
    return isjiesuaned;
  }

  public void setIsjiesuaned(String isjiesuaned) {
    this.isjiesuaned = isjiesuaned;
  }

  public String getTeamId() {
    return teamId;
  }

  public void setTeamId(String teamId) {
    this.teamId = teamId;
  }

  public String getLmtype() {
    return lmtype;
  }

  public void setLmtype(String lmtype) {
    this.lmtype = lmtype;
  }

  public String getPersonCount() {
    return personCount;
  }

  public void setPersonCount(String personCount) {
    this.personCount = personCount;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getIscleared() {
    return iscleared;
  }

  public void setIscleared(String iscleared) {
    this.iscleared = iscleared;
  }

  public String getImporttime() {
    return importtime;
  }

  public void setImporttime(String importtime) {
    this.importtime = importtime;
  }

  public String getPersonalHuibao() {
    return personalHuibao;
  }

  public void setPersonalHuibao(String personalHuibao) {
    this.personalHuibao = personalHuibao;
  }

  public String getPersonalHuishui() {
    return personalHuishui;
  }

  public void setPersonalHuishui(String personalHuishui) {
    this.personalHuishui = personalHuishui;
  }

  public String getPersonalJiesuan() {
    return personalJiesuan;
  }

  public void setPersonalJiesuan(String personalJiesuan) {
    this.personalJiesuan = personalJiesuan;
  }

  public String getBeginplayername() {
    return beginplayername;
  }

  public void setBeginplayername(String beginplayername) {
    this.beginplayername = beginplayername;
  }

  public String getHshbType() {
    return hshbType;
  }

  public void setHshbType(String hshbType) {
    this.hshbType = hshbType;
  }
}
