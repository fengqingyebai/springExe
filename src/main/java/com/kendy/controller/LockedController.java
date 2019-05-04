/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kendy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kendy.constant.Constants;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.DangjuInfo;
import com.kendy.entity.JiaoshouInfo;
import com.kendy.entity.KaixiaoInfo;
import com.kendy.entity.PersonalInfo;
import com.kendy.entity.PingzhangInfo;
import com.kendy.entity.ProfitInfo;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.TotalInfo;
import com.kendy.entity.WanjiaInfo;
import com.kendy.entity.ZijinInfo;
import com.kendy.interfaces.Entity;
import com.kendy.model.GameRecordModel;
import com.kendy.service.MoneyService;
import com.kendy.service.PersonalService;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockedController extends BaseController implements Initializable {

  @Autowired
  DBUtil dbUtil;
  @Autowired
  MoneyService moneyService;
  @Autowired
  DataConstans dataConstants;
  @Autowired
  MyController myController;
  @Autowired
  PersonalService personalService;
  @Autowired
  ChangciController changciController;

  // =================================================第一个tableView
  @FXML
  public TableView<TotalInfo> tableTotalInfo;
  @FXML
  public TableColumn<TotalInfo, String> tuan;// 团
  @FXML
  public TableColumn<TotalInfo, String> wanjiaId;// ID
  @FXML
  public TableColumn<TotalInfo, String> wanjia;// 玩家
  @FXML
  public TableColumn<TotalInfo, String> jifen;// 计分
  @FXML
  public TableColumn<TotalInfo, String> shishou;// 实收
  @FXML
  public TableColumn<TotalInfo, String> baoxian;// 保险
  @FXML
  public TableColumn<TotalInfo, String> chuHuishui;// 出回水
  @FXML
  public TableColumn<TotalInfo, String> baohui;// 保回
  @FXML
  public TableColumn<TotalInfo, String> shuihouxian;// 水后险
  @FXML
  public TableColumn<TotalInfo, String> shouHuishui;// 收回水
  @FXML
  public TableColumn<TotalInfo, String> heLirun;// 合利润

  // =================================================实时金额表tableView
  @FXML
  public TableView<CurrentMoneyInfo> tableCurrentMoneyInfo;
  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmSuperIdSum;// 总和
  @FXML
  public TableColumn<CurrentMoneyInfo, String> mingzi;// 名字
  @FXML
  public TableColumn<CurrentMoneyInfo, String> shishiJine;// 实时金额
  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmiEdu;// 实时金额
  @FXML
  public TableColumn<CurrentMoneyInfo, String> cmiLmb;// 用户剩余联盟币

  // =================================================资金表tableView
  @FXML
  public TableView<ZijinInfo> tableZijin;
  @FXML
  public TableColumn<ZijinInfo, String> zijinType;
  @FXML
  public TableColumn<ZijinInfo, String> zijinAccount;
  // =================================================利润表tableView
  @FXML
  public TableView<ProfitInfo> tableProfit;
  @FXML
  public TableColumn<ProfitInfo, String> profitType;
  @FXML
  public TableColumn<ProfitInfo, String> profitAccount;
  // =================================================开销表tableView
  @FXML
  public TableView<KaixiaoInfo> tableKaixiao;
  @FXML
  public TableColumn<KaixiaoInfo, String> kaixiaoType;
  @FXML
  public TableColumn<KaixiaoInfo, String> kaixiaoMoney;
  // =================================================当局表tableView
  @FXML
  public TableView<DangjuInfo> tableDangju;
  @FXML
  public TableColumn<DangjuInfo, String> type;
  @FXML
  public TableColumn<DangjuInfo, String> money;
  // =================================================交收表tableView
  @FXML
  public TableView<JiaoshouInfo> tableJiaoshou;
  @FXML
  public TableColumn<JiaoshouInfo, String> jiaoshouType;
  @FXML
  public TableColumn<JiaoshouInfo, String> jiaoshouMoney;
  // =================================================交收表tableView
  @FXML
  public TableView<PingzhangInfo> tablePingzhang;

  @FXML
  public TableColumn<PingzhangInfo, String> pingzhangType;
  @FXML
  public TableColumn<PingzhangInfo, String> pingzhangMoney;

  // =================================================牌局表tableView
  @FXML
  public TableView<WanjiaInfo> tablePaiju;

  @FXML
  public TableColumn<WanjiaInfo, String> paiju;// 名字
  @FXML
  public TableColumn<WanjiaInfo, String> wanjiaName;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> zhangji;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> yicunJifen;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> heji;// 实时金额
  @FXML
  public TableColumn<WanjiaInfo, String> pay;// 支付
  @FXML
  public TableColumn<WanjiaInfo, String> copy;// 复制

  // =================================================团队累计表tableView
  @FXML
  public TableView<TeamInfo> tableTeam;
  @FXML
  public TableColumn<TeamInfo, String> teamID;// 团ID
  @FXML
  public TableColumn<TeamInfo, String> teamZJ;// 团战绩
  @FXML
  public TableColumn<TeamInfo, String> teamHS;// 团回水
  @FXML
  public TableColumn<TeamInfo, String> teamBS;// 团保险
  @FXML
  public TableColumn<TeamInfo, String> teamSum;// 行总和
  @FXML
  public TableColumn<TeamInfo, String> teamJiesuan;// 结算按钮

  // =================================================个人累计表
  @FXML
  public TableView<PersonalInfo> tablePersonal;
  @FXML
  public TableColumn<PersonalInfo, String> personalPlayerId;// 团ID
  @FXML
  public TableColumn<PersonalInfo, String> personalPlayerName;// 团战绩
  @FXML
  public TableColumn<PersonalInfo, String> personalSumZJ;// 团回水
  @FXML
  public TableColumn<PersonalInfo, String> personalSumHS;// 团保险
  @FXML
  public TableColumn<PersonalInfo, String> personalSumHB;// 行总和
  @FXML
  public TableColumn<PersonalInfo, String> personalPay;// 结算按钮

  // ===========================================联盟对帐
  @FXML
  public Label LMLabel;

  // ===========================================分页控件
  @FXML
  public TextField pageInput;
  @FXML
  public Label lockedLabel;
  @FXML
  public Label indexLabel;// 第几局
  @FXML
  public TextField searchText;//
  @FXML
  public StackPane stackPane;

  private final String ZERO = "0";
  public final String INDEX_ZERO = "第0局";

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    // 绑定玩家信息表
    bindCellValueByTable(new TotalInfo(), tableTotalInfo);
    // 行双击
    tableTotalInfo.setOnMouseClicked(
        e -> changciController.setTableTotalInfoDialog(stackPane, tableTotalInfo));

    // 绑定牌局表
    bindCellValueByTable(new WanjiaInfo(), tablePaiju);

    // 绑定实时金额表
    bindCellValueByTable(new CurrentMoneyInfo(), tableCurrentMoneyInfo);
    cmSuperIdSum.setStyle(Constants.CSS_CENTER_BOLD);

    // 绑定资金表
    bindCellValueByTable(new ZijinInfo(), tableZijin);

    // 绑定利润表
    bindCellValueByTable(new ProfitInfo(), tableProfit);
    // 绑定实时开销表
    bindCellValueByTable(new KaixiaoInfo(), tableKaixiao);
    // 绑定实时当局表
    bindCellValueByTable(new DangjuInfo(), tableDangju);
    // 绑定交收表
    bindCellValueByTable(new JiaoshouInfo(), tableJiaoshou);
    // 绑定平帐表
    bindCellValueByTable(new PingzhangInfo(), tablePingzhang);

    // 绑定团队表
    bindCellValueByTable(new TeamInfo(), tableTeam);
    teamJiesuan.setStyle(Constants.CSS_CENTER);
    // 绑定个人表
    bindCellValueByTable(new PersonalInfo(), tablePersonal);

//    LMLabel.setTextFill(Color.web("#CD3700"));
//    indexLabel.setTextFill(Color.web("#0076a3"));// 设置Label 的文本颜色。
//    indexLabel.setFont(new Font("Arial", 30));
  }


  /**
   * kendy:绑定数据域
   *
   * @param colums TableColumn 可变参数
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void bindCellValue(TableColumn<? extends Entity, String>... colums) {
    try {
      for (TableColumn column : colums) {
        String fxId = column.getId();
        column.setCellValueFactory(new PropertyValueFactory<Entity, String>(fxId));
        column.setStyle("-fx-alignment: CENTER;");
        column.setSortable(false);// 禁止排序
      }
    } catch (Exception e) {
      throw new RuntimeException("小林：绑定列值失败");
    }
  }

  private void importExcelData(String tableId, List<GameRecordModel> gameRecordModels) {
    // 1 填充总信息表
    moneyService.fillTablerAfterImportZJ(tableTotalInfo, tablePaiju, tableDangju, tableJiaoshou,
        tableTeam, tablePersonal, gameRecordModels, tableId);
    // 2填充当局表和交收表和团队表的总和
    moneyService.setTotalNumOnTable(tableDangju, dataConstants.SumMap.get("当局"));
    moneyService.setTotalNumOnTable(tableJiaoshou, dataConstants.SumMap.get("交收"));
    tableTeam.getColumns().get(4)
        .setText(moneyService.digit0(dataConstants.SumMap.get("团队回水及保险总和")));
  }


  /**
   * 搜索按钮
   */
  public void searchRowAction(ActionEvent event) {
    String keyWord = searchText.getText();
    moneyService.searchRowAction(tableCurrentMoneyInfo, keyWord);
  }

  public void searchRowByEnterEvent(KeyEvent event) {
    String keyWord = searchText.getText();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(keyWord)) {
      moneyService.searchRowAction(tableCurrentMoneyInfo, keyWord);
    }
  }


  @SuppressWarnings({"unchecked", "rawtypes"})
  public void clearData(TableView tableTotalInfo, TableView tablePaiju, TableView tableTeam,
      TableView tableDangju, TableView tableJiaoshou, TableView tablePingzhang) {
    // 清空相关界面表数据
    tableTotalInfo.setItems(null);
    tablePaiju.setItems(null);
    tableDangju.setItems(null);
    tableJiaoshou.setItems(null);
    tablePingzhang.setItems(null);
    // 清空相关界面表总数据
    moneyService.setTotalNumOnTable(tableDangju, 0d);
    moneyService.setTotalNumOnTable(tableJiaoshou, 0d);
    moneyService.setTotalNumOnTable(tablePingzhang, 0d);

    // 清空缓存中的数据
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public void clear10Tables(TableView tableTotalInfo, TableView tablePaiju, TableView tableTeam,
      TableView tableDangju, TableView tableJiaoshou, TableView tablePingzhang) {
    // 清空相关界面表数据
    tableTotalInfo.setItems(null);
    tablePaiju.setItems(null);
    tableDangju.setItems(null);
    tableJiaoshou.setItems(null);
    tablePingzhang.setItems(null);
    // 清空相关界面表总数据
    moneyService.setTotalNumOnTable(tableDangju, 0d);
    moneyService.setTotalNumOnTable(tableJiaoshou, 0d);
    moneyService.setTotalNumOnTable(tablePingzhang, 0d);

    if ("2017-01-01".equals(dbUtil.Load_Date)) {
      tableTeam.setItems(null);
      moneyService.setTotalNumOnTable(tableTeam, 0d, 4);
    }
  }


  /********************************************************** 自定义 分页控件代码 开始 *********/
  // 每一场锁定时添加一个页
  public void pagePaneOpration() {
    int index = dataConstants.Paiju_Index.get();
    pageInput.setText(index + "");
  }

  // 第一页按钮
  public void pageFirstAction(ActionEvent event) {
    pageInput.setText("1");
    // 调用
    getResultByPage(1);
  }

  // 前一页按钮
  public void pagePreAction(ActionEvent event) {
    String page = pageInput.getText();
    int pageIndex = getPageIndex(page, false);
    pageInput.setText(pageIndex + "");
    // 调用
    if (dataConstants.Index_Table_Id_Map.size() > 0 && pageIndex >= 1) {
      getResultByPage(pageIndex);
    }
  }

  // 下一页按钮
  public void pageNextAction(ActionEvent event) {
    String page = pageInput.getText();
    int pageIndex = getPageIndex(page, true);
    pageInput.setText(pageIndex + "");

    // 调用
    if (dataConstants.Index_Table_Id_Map.size() > 0 && pageIndex >= 1) {
      getResultByPage(pageIndex);
    }
  }

  // 最后一页页按钮
  public void pageLastAction(ActionEvent event) {
    pageInput.setText(dataConstants.Paiju_Index.get() + "");
    // 调用
    getResultByPage(dataConstants.Paiju_Index.get());
  }

  // 输入页码
  public void turn2PageEnterAction(KeyEvent event) {
    String pageText = pageInput.getText();
    int pageMax = dataConstants.Paiju_Index.get();
    if (KeyCode.ENTER == event.getCode() && !StringUtil.isBlank(pageText)) {
      try {
        int page = Integer.valueOf(pageText.trim());
        if (page > pageMax || page < 0) {
          ShowUtil.show("值必须界于1到" + pageMax + "之间", 1);
          return;
        }
        // 调用
        getResultByPage(page);

      } catch (Exception e) {
        ShowUtil.show("您输入的不是一个数值", 1);
        pageInput.setText("");
      }
    }
  }

  // 根据用户输入的页码或不断点击前后页时获取可用的页数
  public int getPageIndex(String oldPage, boolean addOrDel) {
    int newPage = 1;
    int currentMaxPage = dataConstants.Paiju_Index.get();
    try {
      if (!StringUtil.isBlank(oldPage)) {
        if (addOrDel) {// 加
          newPage = Integer.valueOf(oldPage) + 1;
          if (newPage > currentMaxPage) {
            newPage--;
          }
        } else {// 减
          newPage = Integer.valueOf(oldPage) - 1;
          if (newPage < 1) {
            newPage++;
          }
        }
      } else {
        newPage = currentMaxPage - 1;
      }
    } catch (Exception e) {// 出现异常默认跳到最后一页
      newPage = currentMaxPage - 1;
    }
    return newPage;
  }

  /**
   * 根据页码查询
   *
   * @time 2017年10月17日
   */
  public void getResultByPage(int pageIndex) {
    try {
      // add 2017-11-11 若是开始新的一天则最后一场和第一场不做重新加载
      if (dataConstants.Index_Table_Id_Map.size() == 0) {
        return;
      }

      // 对于已缓存的数据的查询
      int cacheMaxPage = dataConstants.Paiju_Index.get();
      if (pageIndex > 0 && pageIndex < cacheMaxPage) {
        // 恢复十个表数据
        reCovery10TablesByPage(pageIndex);
        // 获取对应的场次
        // 锁定的数据不能再被修改
        indexLabel.setVisible(true);
        logger.debug("dataConstants.Index_Table_Id_Map.get(pageIndex):"
            + dataConstants.Index_Table_Id_Map.get(pageIndex + ""));
        indexLabel.setText(dataConstants.Index_Table_Id_Map.get(pageIndex + ""));

      } else {// 对于缓存之外的数据查询（属于新增）
        // 恢复锁定的数据
        // 加载前一场数据
        pageIndex -= 1;
        reCovery10TablesByPage(pageIndex);// 恢复十个表数据

        // 清空相关表数据（保留类似昨日留底的表数据）
        clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou,
            tablePingzhang);
        // tableCurrentMoneyInfo,tableZijin,tableKaixiao,tableProfit
        indexLabel.setText(INDEX_ZERO);
      }
      // 如果尾页有改动数据，需要同步缓存
      // reCoveryRelatedCache();

    } catch (Exception e) {
      ShowUtil.show("查询失败！！", 1);
      e.printStackTrace();
    }
  }

  public void clear10Tables() {
    // 清空相关表数据（保留类似昨日留底的表数据）
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang,
        tablePersonal);
    // tableCurrentMoneyInfo,tableZijin,tableKaixiao,tableProfit
    indexLabel.setText(INDEX_ZERO);
  }

  // 恢复十个表数据
  public void reCovery10TablesByPage(int pageIndex) throws Exception {

    moneyService.reCovery10TablesByPage(tableTotalInfo, tablePaiju, tableTeam,
        tableCurrentMoneyInfo, tableZijin, tableKaixiao, tableProfit, tableDangju, tableJiaoshou,
        tablePingzhang, tablePersonal, LMLabel, pageIndex);
  }

  /********************************************************** 分页控件代码 结束 *********/


  public int getCurrentPage() {
    return Integer.parseInt(pageInput.getText());
  }


  /**
   * 把渲染表格的数据加载出来，要判断是否2017-01-01，分成两步
   */
  public void fillTables(TableView<CurrentMoneyInfo> table, TableView<ZijinInfo> tableZijin,
      TableView<ProfitInfo> tableProfit, TableView<KaixiaoInfo> tableKaixiao, Label LMLabel) {
    // 清空十个表数据
    clearData(tableTotalInfo, tablePaiju, tableTeam, tableDangju, tableJiaoshou, tablePingzhang);
    indexLabel.setText(INDEX_ZERO);

    if (dbUtil.isPreData2017VeryFirst()) {
      moneyService.fillTableCurrentMoneyInfo(tableCurrentMoneyInfo, tableZijin, tableProfit,
          tableKaixiao, LMLabel);
    } else {
      moneyService.fillTableCurrentMoneyInfo2(tableTeam, tableCurrentMoneyInfo, tableZijin,
          tableProfit, tableKaixiao, LMLabel);
      // 缓存战绩文件夹中多份excel中的数据 {团队ID=List<GameRecord>...}这个可能会被修改，用在展示每场的tableTeam信息
      Map<String, String> map = dbUtil.getLastLockedData();
      if (map != null && map.size() > 0) {
        dataConstants.Team_Huishui_Map = JSON.parseObject(map.get("Team_Huishui_Map"),
            new TypeReference<Map<String, List<GameRecordModel>>>() {
            });
      }
    }
  }

  /**
   * 导出实时金额表
   *
   * @time 2017年10月28日
   */
  public void exportSSJEAction(ActionEvent event) {
    try {
      moneyService.exportSSJEAction(tableCurrentMoneyInfo);
      ShowUtil.show("导出实时金额表Excel成功", 2);
    } catch (Exception e) {
      ShowUtil.show("导出实时金额表Excel失败，原因：" + e.getMessage());
    }
  }


  @Override
  public Class<?> getSubClass() {
    return getClass();
  }


}
