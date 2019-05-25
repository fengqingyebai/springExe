package com.kendy.service;

import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.ChangciController;
import com.kendy.controller.LMBController;
import com.kendy.controller.MyController;
import com.kendy.db.DBUtil;
import com.kendy.db.service.CurrentMoneyService;
import com.kendy.db.service.GameRecordService;
import com.kendy.db.service.PlayerService;
import com.kendy.entity.WanjiaInfo;
import com.kendy.model.GameRecordModel;
import com.kendy.util.NumUtil;
import com.kendy.util.TimeUtil;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author linzt
 * @date 2019-04-26
 */
@Component
public class LittleGameService {

  private Logger log = LoggerFactory.getLogger(MoneyService.class);

  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Autowired
  public MyController myController;
  @Autowired
  LMBController lmbController;
  @Autowired
  MoneyService moneyService;

  @Autowired
  ChangciController changciController;

  @Resource
  GameRecordService gameRecordService;

  @Resource
  CurrentMoneyService currentMoneyService;

  @Resource
  PlayerService playerService;


  /**
   * 补全单条记录的值（小游戏）
   */
  public void setSingleGameRecord0(GameRecordModel r, String tableId, String level, String LMType) {

    String teamId = moneyService.getTeamId(r.getPlayerid());
    // 计算收回险
    String yszj = r.getYszj();
    String shishou = getShiShou0(r);
    String baoxian = "0";
    String chuHuishui = "0";
    String shuihouxian = "0";
    String shouHuishui = "0";
    String huiBao = "0";

    // 初始名称
    r.setBeginplayername(r.getPlayerName());
    // 获取软件时间
    r.setSoftTime(dataConstants.Date_Str);
    // 设置桌号
    r.setTableid(tableId);
    // 设置联盟
    r.setLmtype(LMType);
    // 设置团队ID
    r.setTeamId(teamId);
    // 级别
    r.setLevel(level);
    // 导入时间
    r.setImporttime(TimeUtil.getDateTime());
    // 实收
    r.setShishou(shishou);
    // 出回水
    r.setChuhuishui(chuHuishui);
    // 回保
    r.setHuibao(huiBao);
    // 水后险
    r.setShuihouxian(shuihouxian);
    // 收回水
    r.setShouhuishui(shouHuishui);

    // 个人回水、回保
    setPersonalHsHb0(r);

    // 合利润
    r.setHelirun(NumUtil.digit2(getHeLirun0(r)));

  }

  public void setPersonalHsHb0(GameRecordModel r) {
    r.setHshbType("0"); // 默认为团队类型
    r.setPersonalHuibao("0");
    r.setPersonalHuishui("0");
  }


  /**
   * 计算小游戏的合利润
   * 加勒比合利润公式 = 实收 = （原始战绩 - 保险） * 80% * (-1)
   * 德州牛仔合利润公式 = 实收 = 俱乐部分成列
   * @param r
   * @return
   */
  private String getHeLirun0(GameRecordModel r) {
    if (isJLBH(r) || isDeZhou(r)) {
      return r.getShishou();
    }
    return "0";
  }

  /**
   * 计算小游戏的实收
   * 加勒比实收 = （原始战绩 - 保险） * 80% * (-1)
   * 德州牛仔实收 = 俱乐部分成列
   * @param r
   * @return
   */
  private String getShiShou0(GameRecordModel r) {
    if (isJLBH(r)) {
      double dif = NumUtil.getNum(r.getYszj()) - NumUtil.getNum(r.getSingleinsurance());
      return NumUtil.digit(dif * Constants.LITTLE_GAME_RATE_MY * (-1));
    }
    if (isDeZhou(r)) {
      return NumUtil.digit(NumUtil.getNum(r.getClubFencheng()));
    }
    return "0";
  }


  public boolean isLittleGame(GameRecordModel record) {
    return lmbController.isLittleGame(record);
  }

  public boolean isLittleGame(String juType) {
    return lmbController.isLittleGame(juType);
  }


  public boolean isJLBH(GameRecordModel gameRecordModel) {
    return lmbController.isJLBH(gameRecordModel);
  }

  public boolean isDeZhou(GameRecordModel gameRecordModel) {
    return lmbController.isDeZhou(gameRecordModel);
  }


  public WanjiaInfo getWanjiaInfo0(GameRecordModel r, String playerId, String playerName, String shishou) {
    WanjiaInfo wanjia;
    wanjia = new WanjiaInfo();
    String lmb = moneyService.getYicunJifen(playerId); // 联盟币代替已存积分
    String heji = NumUtil.digit0(NumUtil.getNum(lmb) + NumUtil.getNum(r.getYszj()) + "");
    wanjia.setPaiju(r.getTableid());
    wanjia.setWanjiaName(playerName);
    wanjia.setYicunJifen(lmb);
    wanjia.setWanjiaId(playerId);
    wanjia.setHeji(heji);
    // 小游戏场次信息表中的牌局表，战绩取原始战绩
    wanjia.setZhangji(r.getYszj());
    return wanjia;
  }
}
