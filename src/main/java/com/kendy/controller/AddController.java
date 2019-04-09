package com.kendy.controller;

import com.kendy.enums.MoneyCreatorEnum;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import com.kendy.application.SpringFxmlLoader;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.Huishui;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.Player;
import com.kendy.service.JifenService;
import com.kendy.service.MoneyService;
import com.kendy.service.ShangmaService;
import com.kendy.service.TeamProxyService;
import com.kendy.service.TgWaizhaiService;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 添加团队回水的控制器
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:54:46
 */
@Component
public class AddController extends BaseController implements Initializable {

  private static Logger log = Logger.getLogger(AddController.class);
  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public MyController myController;
  @Autowired
  public TeamProxyController teamProxyController; // 代理控制类
  @Autowired
  public TGController tgController; // 托管控制类
  @Autowired
  public GDController gdController; // 股东控制类
  @Autowired
  public JifenService jifenService; // 积分控制类
  @Autowired
  public ShangmaService shangmaService; // 上码控制类
  @Autowired
  public TeamProxyService teamProxyService; // 配帐控制类
  @Autowired
  public TgWaizhaiService tgWaizhaiService; // 配帐控制类
  @Autowired
  public MoneyService moneyService; // 配帐控制类
  @Autowired
  public DataConstans dataConstants; // 数据控制类

  @Autowired
  ChangciController changciController;

  // =====================================================================新增团队回水对话框
  @FXML
  private TextField gudong2Field; // 股东
  @FXML
  private TextField beizhu2Field; // 股东
  @FXML
  private TextField teamIdField; // 新增团队ID
  @FXML
  private TextField teamNameField; // 新增团队名称
  @FXML
  private TextField huishui; // 新增团队回水
  @FXML
  private TextField insuranceRate; // 新增保险比例
  // =====================================================================新增人员名单对话框
  @FXML
  private TextField gameIdField;// 团ID
  @FXML
  private TextField gudongField;// 股东
  @FXML
  private TextField teamField;// 团队名称
  @FXML
  private TextField playerNameField;// 玩家名称
  @FXML
  private TextField beizhuField;// 备注
  @FXML
  private TextField huibaoField;// 个人回保
  @FXML
  private TextField huishuiField;// 个人回水


  // =====================================================================新增实时开销对话框
  @FXML
  private TextField kaixiaoTypes;// 名称
  @FXML
  private TextField kaixiaoMoneys;// 开销金额
  @FXML
  private ChoiceBox<String> gudongChoice;// 股东下拉框
  @FXML
  private CheckBox needComputeBox;// 是否需要纳入股东贡献值
  // =====================================================================新增实时金额对话框
  @FXML
  private TextField cmName;// 名称
  @FXML
  private TextField cmMoney;// 实时金额
  @FXML
  private TextField cmPlayerId;
  @FXML
  private TextField cmEdu;// 额度

  public static final String ALL_COMPANY = "全公司参与";

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // 初始化股东列表
    ObservableList<String> gudongList = myController.getGudongList();
    ObservableList<String> copyGudongList = FXCollections.observableArrayList();
    for (String gudong : gudongList) {
      copyGudongList.add(gudong);
    }
    if (!copyGudongList.contains(ALL_COMPANY)) {
      copyGudongList.add(ALL_COMPANY);
    }
    if (gudongChoice != null) {
      gudongChoice.setItems(copyGudongList);
    }


  }


  /**
   * 增加团队回水
   */
  public void addHuishuiOKAction(ActionEvent event) {

    Huishui hs = new Huishui();
    hs.setTeamId(teamIdField.getText());
    hs.setTeamName(teamNameField.getText());
    hs.setGudong(gudong2Field.getText());
    hs.setBeizhu(beizhu2Field.getText());
    String _insuranceRate = insuranceRate.getText();
    if (StringUtil.isBlank(_insuranceRate)) {
      _insuranceRate = "0";
    } else {
      _insuranceRate = NumUtil.digit4(_insuranceRate);
    }
    hs.setInsuranceRate(_insuranceRate);

    String _huishui = huishui.getText();
    if (StringUtil.isBlank(_huishui)) {
      _huishui = "0";
    } else {
      _huishui = NumUtil.digit4(_huishui);
    }
    hs.setHuishuiRate(_huishui);

    hs.setZjManaged("是");

    if (!StringUtil.isBlank(hs.getTeamId()) && !StringUtil.isBlank(hs.getHuishuiRate())) {
      dataConstants.huishuiMap.put(hs.getTeamId(), hs);
      // 入库
      dbUtil.addTeamHS(hs);
      log.info("已经添加该团队回水:" + hs.toString());
      ShowUtil.show("已经添加该团队回水", 2);
      // add 2017-10-23
      // 添加回水后往实时上码表添加团ID按钮
      shangmaService.initShangmaButton();
      // 添加回水后往代理查询中添加新团 队ID
      teamProxyService.addNewTeamId(hs.getTeamId());
      // 积分查询添加新团队ID jfTeamIDCombox 小胖有反馈时再添加吧
      jifenService.addNewTeamId(hs.getTeamId());

    }
    // 获取到新增回水窗口的实例
    Stage huishuiStage = dataConstants.framesNameMap.get(Constants.ADD_TEAM_HUISHUI_FRAME);
    dataConstants.framesNameMap.remove(Constants.ADD_TEAM_HUISHUI_FRAME);
    huishuiStage.close();
  }

  /**
   * 增加人员名单
   */
  public void addNewPlayerOKAction(ActionEvent event) {

    Player player = new Player();
    player.setGameId(StringUtils.defaultString(gameIdField.getText()).trim());
    player.setGudong(StringUtils.defaultString(gudongField.getText()).trim());
    player.setTeamName(StringUtils.defaultString(teamField.getText()).trim());
    player.setPlayerName(StringUtils.defaultString(playerNameField.getText()).trim());
    player.setEdu(StringUtils.defaultString(beizhuField.getText()).trim());
    player.setHuibao(StringUtils.defaultString(huibaoField.getText()).trim());
    player.setHuishui(StringUtils.defaultString(huishuiField.getText()).trim());
    if (!StringUtil.isBlank(player.getgameId()) && !StringUtil.isBlank(player.getTeamName())) {
      dataConstants.membersMap.put(player.getgameId(), player);
      dataConstants.refresh_SM_Detail_Map();
      // dataConstants.refreshTeamIdAndPlayerId();
      dbUtil.addMember(player);
      log.info("新增玩家信息:" + player);
      ShowUtil.show("已经添加该人员", 2);
    }
    // 获取到新增人员窗口的实例
    Stage addNewPlayerStage = dataConstants.framesNameMap.get(Constants.ADD_NEWPALYER_FRAME);
    dataConstants.framesNameMap.remove(Constants.ADD_NEWPALYER_FRAME);
    addNewPlayerStage.close();
  }

  /**
   * 增加开销
   */
  public void addKaixiaoOKAction(ActionEvent event) {

    if (!StringUtil.isBlank(kaixiaoTypes.getText())) {
      dataConstants.kaixiaoMap.put(kaixiaoTypes.getText(), kaixiaoMoneys.getText());
      ShowUtil.show("添加开销成功", 2);
      // 更新到表
      if (myController != null) {
        String kxGudong = gudongChoice.getSelectionModel().getSelectedItem();
        boolean isGudongEmpty = StringUtil.isBlank(kxGudong);
        String kaixiaoID = isGudongEmpty ? "" : UUID.randomUUID().toString().replace("-", "");
        String kaixiaoTime = StringUtil.nvl(dataConstants.Date_Str, "2017-01-01");
        String kxType = kaixiaoTypes.getText();
        String kxMoney = StringUtil.nvl(kaixiaoMoneys.getText(), "");
        KaixiaoInfo kaixiaoInfo =
            new KaixiaoInfo(kaixiaoID, kxType, kxMoney, kxGudong, kaixiaoTime);
        // 添加到场次信息中的开销表(若股东为空，则ID为空)
        changciController.updateKaixiaoTable(kaixiaoInfo);
        // 添加到数据库中（如果股东不为空）
        if (!StringUtil.isAnyBlank(kxMoney, kxGudong)) {
          if (ALL_COMPANY.equals(kxGudong)) {
            // N个股东平摊（包括银河股东）
            ObservableList<String> gudongList = myController.getGudongList();
            String averageKaixiaoMoney = getAverageKaixiaoMoney(kxMoney,
                gudongList.size());// 股东平摊的开销值
            // 有几个股东就保存几条开销记录进数据库
            int i = 0;
            for (String gudongName : gudongList) {
              kaixiaoID = kaixiaoID + "#" + (i++); // 共用一个开销ID, 从0编写到N
              KaixiaoInfo averageInfo =
                  new KaixiaoInfo(kaixiaoID, kxType, averageKaixiaoMoney, gudongName, kaixiaoTime);
              dbUtil.saveOrUpdate_gudong_kaixiao(averageInfo);
            }

          } else {
            dbUtil.saveOrUpdate_gudong_kaixiao(kaixiaoInfo);
          }
          // 缓存？ TODO
        }
      } else {
        log.info("===========================mc = null ");
      }

    }
    // 获取到新增人员窗口的实例
    Stage addNewPlayerStage = dataConstants.framesNameMap.get(Constants.ADD_KAIXIAO_FRAME);
    dataConstants.framesNameMap.remove(Constants.ADD_KAIXIAO_FRAME);
    addNewPlayerStage.close();
  }

  /**
   * 获取股东平摊的值
   *
   * @time 2018年2月22日
   */
  private String getAverageKaixiaoMoney(String kxMoney, Integer gudongSize) {
    Double money = NumUtil.getNum(kxMoney);
    Double averageKaixiaoMoney = NumUtil.getNumDivide(money, Double.valueOf(gudongSize.toString()));
    return NumUtil.digit2(averageKaixiaoMoney.toString());
  }

  /**
   * 增加人员的实时金额
   */
  public void add_SSJE_person_Action(ActionEvent event) {
    // 先对玩家ID进行判断
    String playerId = cmPlayerId.getText();
    Player player;
    if (StringUtil.isBlank(playerId)) {
      ShowUtil.show("操作失败！请详看系统说明！");
      return;
    } else {
      playerId = playerId.trim();
      player = dataConstants.membersMap.get(playerId);// 从人员表查找相关记录
      if (player == null) {
        ShowUtil.show("操作失败！人员表中无此ID,请先添加该人员！");
        return;
      }
      // 重复判断
      if (moneyService.isExistIn_SSJE_Table_byId(playerId)) {
        ShowUtil.show("操作失败！实时金额表中已经存在此ID,请查检！");
        return;
      }
    }
    // 添加实时金额
    CurrentMoneyInfo tempMoneyInfo = new CurrentMoneyInfo(player.getPlayerName(), cmMoney.getText(),
        player.getgameId(), player.getEdu(), MoneyCreatorEnum.DEFAULT.getCreatorName(), "0");
    moneyService.addInfo(tempMoneyInfo);
    ShowUtil.show("添加成功", 2);
    moneyService.flush_SSJE_table();
    moneyService.scrolById(playerId);
    logger.info("手动修改实时金额数据记录(新增人员)：名称：{}，ID:{}, 金额：{}"
        , player.getPlayerName(),player.getgameId(), cmMoney.getText() );

    // 获取到新增人员窗口的实例
    Stage addNewPlayerStage = dataConstants.framesNameMap.get(Constants.ADD_CURRENT_MONEY_FRAME);
    dataConstants.framesNameMap.remove(Constants.ADD_CURRENT_MONEY_FRAME);
    addNewPlayerStage.close();
  }

  /**
   * 增加临时实时金额（非人员金额）
   *
   * @time 2017年11月9日
   */
  public void add_SSJE_other_Action(ActionEvent event) {
    // 先对名称进行判断
    String name = cmName.getText();
    if (StringUtil.isBlank(name)) {
      ShowUtil.show("操作失败！请详看系统说明！");
      return;
    }
    // 重复判断
    if (moneyService.isExistIn_SSJE_Table_byName(name)) {
      ShowUtil.show("操作失败！实时金额表中已经存在此名称,请查检！");
      return;
    }
    // 添加实时金额
    CurrentMoneyInfo tempMoneyInfo = new CurrentMoneyInfo(name, cmMoney.getText(), "", "", MoneyCreatorEnum.USER.getCreatorName(), "");
    moneyService.addInfo(tempMoneyInfo);
    ShowUtil.show("添加成功", 2);
    moneyService.flush_SSJE_table();
    moneyService.scrolByName(name);
    logger.info("手动修改实时金额数据记录(新增其他)：名称：{}，金额：{}", name, cmMoney.getText() );

    // 获取到新增人员窗口的实例
    Stage addNewPlayerStage = dataConstants.framesNameMap.get(Constants.ADD_CURRENT_MONEY_FRAME);
    dataConstants.framesNameMap.remove(Constants.ADD_CURRENT_MONEY_FRAME);
    addNewPlayerStage.close();
  }


  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
