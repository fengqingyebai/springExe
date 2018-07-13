package com.kendy.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import com.kendy.entity.ProxySumInfo;
import com.kendy.entity.ProxyTeamInfo;
import com.kendy.service.TeamProxyService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * 团队代理查询控制类 TODO
 * <P>
 * 单独出来，原先是在MyController中
 * </P>
 * 
 * 
 * @author linzt
 * @time 2018年2月14日 上午9:32:23
 */
@Controller
public class TeamProxyController extends BaseController implements Initializable {

  // ===============================================================代理查询Tab
  @FXML public TableView<ProxyTeamInfo> tableProxyTeam;
  @FXML public HBox proxySumHBox;// 每列上的总和
  @FXML public TableColumn<ProxyTeamInfo, String> proxyTeamId;// 团ID
  @FXML public TableColumn<ProxyTeamInfo, String> proxyPlayerId;// 玩家ID
  @FXML public TableColumn<ProxyTeamInfo, String> proxyPlayerName;// 玩家名称
  @FXML public TableColumn<ProxyTeamInfo, String> proxyYSZJ;// 原始战绩
  @FXML public TableColumn<ProxyTeamInfo, String> proxyZJ;// 战绩
  @FXML public TableColumn<ProxyTeamInfo, String> proxyBaoxian;// 保险
  @FXML public TableColumn<ProxyTeamInfo, String> proxyHuishui;// 回水
  @FXML public TableColumn<ProxyTeamInfo, String> proxyHuiBao;// 回保
  @FXML public TableColumn<ProxyTeamInfo, String> proxyTableId;// 场次
  @FXML public ComboBox<String> teamIDCombox;// 团队ID下拉框
  @FXML public CheckBox isZjManage;// 团对应的战绩是否被管理
  @FXML public CheckBox hasTeamBaoxian;// 导出是否团队无保险
  @FXML public Label proxyDateLabel;
  @FXML public TableView<ProxySumInfo> tableProxySum;
  @FXML public TableColumn<ProxySumInfo, String> proxySumType;
  @FXML public TableColumn<ProxySumInfo, String> proxySum;
  @FXML public TextField proxyHSRate;// 回水比例
  @FXML public TextField proxyHBRate;// 回保比例
  @FXML public TextField proxyFWF;// 服务费大于多少有效


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // 绑定代理查询表（团队当天查询）
    bindCellValueByTable(new ProxyTeamInfo(), tableProxyTeam);
    // 绑定代理查询中的合计表
    bindCellValueByTable(new ProxySumInfo(), tableProxySum);

    // 代理查询中的团队回水选择
    TeamProxyService.initTeamProxy(tableProxyTeam, proxySumHBox, teamIDCombox, isZjManage,
        proxyDateLabel, tableProxySum, proxyHSRate, proxyHBRate, proxyFWF, hasTeamBaoxian);
    // 代理查询中的团队回水选择
    TeamProxyService.initTeamSelectAction(teamIDCombox, isZjManage, tableProxyTeam, proxySumHBox);
  }

  /**
   * 代理查询导出为Excel
   */
  public void exportExcelAction(ActionEvent e) {
    TeamProxyService.exportExcel();
  }

  /**
   * 代理查询一键导出为Excel
   */
  @FXML public void exportExcelBatchAction(Event e) {
    TeamProxyService.exportExcel();
  }

  /**
   * 代理查询刷新按钮
   */
  @FXML public void proxyRefreshAction(Event e) {
    TeamProxyService.proxyRefresh();
  }

  public void teamIDSelectedAction(Event e) {

  }

  /**
   * 隐藏今日无数据的团队
   * 
   * @time 2018年1月1日
   * @param event
   */
  @FXML public void proxyHideNoDataTeamAction() {
    TeamProxyService.proxyHideNoDataTeam();
  }



}
