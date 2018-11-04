package com.kendy.entity;

import com.kendy.excel.myExcel4j.annotation.MyExcelField;
import com.kendy.interfaces.Entity;
import javafx.beans.property.SimpleStringProperty;

/**
 * 游戏记录
 *
 * @author linzt
 */
public class GameRecordInfo implements Entity {

  private SimpleStringProperty shishou;// 实收

  private SimpleStringProperty chuHuishui;// 出回水

  private SimpleStringProperty huiBao;// 保回

  private SimpleStringProperty shuihouxian;// 水后险

  private SimpleStringProperty shouHuishui;// 收回水

  private SimpleStringProperty heLirun;// 合利润

  private SimpleStringProperty softDate; // 软件时间,以自动下载的时间为准

  private SimpleStringProperty tableId; // 桌号,如第X局

  private SimpleStringProperty isJiesuaned = new SimpleStringProperty("0"); // 是否已经结算过 0：未结算 1：已结算

  private SimpleStringProperty teamId; // 团队ID( 请注意：关联最新的人员表获取最新

  /*
   * 联盟类型
   */
  private SimpleStringProperty lmType;

  /*
   * 级别
   */
  private SimpleStringProperty level;

  /*
   * 是否已经清空过 0：未清空 1：已清空
   */
  private SimpleStringProperty isCleared = new SimpleStringProperty("0");


  @MyExcelField(title = "总手数", order = 8)
  private SimpleStringProperty sumHandsCount;

  @MyExcelField(title = "玩家ID", order = 9)
  private SimpleStringProperty playerId;

  @MyExcelField(title = "玩家昵称", order = 10)
  private SimpleStringProperty playerName;

  @MyExcelField(title = "俱乐部ID", order = 11)
  private SimpleStringProperty clubId;

  @MyExcelField(title = "俱乐部", order = 12)
  private SimpleStringProperty clubName;

  @MyExcelField(title = "保险合计", order = 17)
  private SimpleStringProperty sinegleInsurance;

  @MyExcelField(title = "俱乐部保险", order = 18)
  private SimpleStringProperty clubInsurance;

  @MyExcelField(title = "保险", order = 19)
  private SimpleStringProperty currentTableInsurance;

  @MyExcelField(title = "战绩", order = 20)
  private SimpleStringProperty yszj;

  @MyExcelField(title = "结束时间", order = 21)
  private SimpleStringProperty finisedTime;


}
