package com.kendy.constant;

/**
 * 配置一些常量
 * 
 * @author 林泽涛
 * @time 2018年1月1日 下午10:55:22
 */
public class Constants {

  public static final String TITLE = "财务软件";
  public static final String VERSION = "V3.0";

  public static final String ADD_TEAM_HUISHUI_FRAME = "addTeamHuishui";
  public static final String ADD_NEWPALYER_FRAME = "newPlayer";
  public static final String MEMBER_ZJ_QUERY_FRAME = "memberZjQuery";
  public static final String ADD_KAIXIAO_FRAME = "addKaixiao";
  public static final String DEL_MEMBER_FRAME = "delMember";
  public static final String ADD_CURRENT_MONEY_FRAME = "addCurrentMoney";
  public static final String COMBINE_ID_FRAME = "combineId";

  public static final String ADD_COMPANY_FRAME = "newTGCompany";
  public static final String ADD_TG_KAIXIAAO_FRAME = "newTGCompany";

  public static final String GU_DONG = "gudong";

  public static final String CSS_CENTER_BOLD = "-fx-alignment: CENTER;-fx-font-weight: bold;";
  public static final String CSS_CENTER = "-fx-alignment: CENTER;";


  public static final String ZERO = "0";
  
  public static  final javafx.scene.image.Image icon =
      new javafx.scene.image.Image(Constants.class.getResourceAsStream("/images/icon.png"));

  public static final double FINAL_HS_RATE_095 = 0.95; // 回水比例，这个是固定值
  public static final double FINAL_HS_RATE_0975 = 0.975; // 回水比例，这个是固定值
  public static double CURRENT_HS_RATE = 0.95;// 默认的回水比例，会根据选择而变化



}
