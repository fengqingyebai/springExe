package com.kendy.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kendy.constant.DataConstans;
import com.kendy.controller.BaseController;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBUtil;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.Player;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TeamInfo;
import com.kendy.entity.TypeValueInfo;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;


/**
 * 托管外债
 * 
 * @author 林泽涛
 * @time 2018年3月8日 下午8:49:07
 */
@Component
public class TgWaizhaiService{

  private Logger log = Logger.getLogger(TgWaizhaiService.class);
  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public DataConstans dataConstants; // 数据控制类
  @Autowired
  public TGController tgController ;
  @Autowired
  public BaseController baseController ;
  

  private final String UNKNOW_TG_TEAM = "未知托管团队";
  public DecimalFormat df = new DecimalFormat("#.00");


  /**
   * 获取托管团队ID
   * 
   * @time 2018年3月8日
   * @return
   */
  public Set<String> getTGTeamId() {
    Set<String> tgTeamSet = new HashSet<>();
    try {
      TGCompanyModel currentCompany =
          dbUtil.get_tg_company_by_id(tgController.getCurrentTGCompany());
      String tgTeamsStr = currentCompany.getTgTeamsStr();
      if (StringUtil.isNotBlank(tgTeamsStr)) {
        tgTeamSet = Stream.of(tgTeamsStr.split("#")).collect(Collectors.toSet());
      }
    } catch (Exception e) {
      ErrorUtil.err("获取托管团队ID失败", e);
    }
    log.info("托管团队：" + tgTeamSet);
    return tgTeamSet;
  }


  /**
   * 自动生成外债信息表
   * 
   * @param tableWaizhai
   * @param hbox
   * @param tableCurrentMoneyInfo
   * @param tableTeam
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void generateWaizhaiTables(TableView<TypeValueInfo> tableWaizhai, HBox hbox,
      TableView<CurrentMoneyInfo> tableCurrentMoneyInfo, TableView<TeamInfo> tableTeam) {
    // //获取托管团队ID
    // Set<String> tgTeamIdSet = getTGTeamId();

    // 清空数据
    ObservableList<Node> allTables = hbox.getChildren();
    if (allTables != null && allTables.size() > 0)
      hbox.getChildren().remove(0, allTables.size());

    if (dataConstants.Index_Table_Id_Map.size() == 0) {
      ShowUtil.show("你当前还未锁定任意一局，查询没有数据!", 2);
      return;
    }
    ObservableList<CurrentMoneyInfo> CurrentMoneyInfo_OB_List = FXCollections.observableArrayList();
    Map<String, List<CurrentMoneyInfo>> tgTeamIdMap = get_SSJE_Gudong_Map(tableCurrentMoneyInfo);
    Map<String, String> sumMap = getSum(tgTeamIdMap);


    int tgTeamIdMapSize = tgTeamIdMap.size();
    if (tgTeamIdMapSize == 0) {
      ShowUtil.show("股东列表为空或实时金额为空！");
      return;
    }

    TableView<CurrentMoneyInfo> table;

    for (Map.Entry<String, List<CurrentMoneyInfo>> entry : tgTeamIdMap.entrySet()) {
      String tgTeamName = entry.getKey();
      List<CurrentMoneyInfo> list = entry.getValue();
      table = new TableView<CurrentMoneyInfo>();

      // 设置列
      TableColumn firstNameCol = new TableColumn("团队" + tgTeamName);
      firstNameCol.setSortable(false);// 禁止排序
      firstNameCol.setPrefWidth(80);
      firstNameCol
          .setCellValueFactory(new PropertyValueFactory<CurrentMoneyInfo, String>("mingzi"));

      TableColumn lastNameCol = new TableColumn(sumMap.get(tgTeamName));
      lastNameCol.setSortable(false);// 禁止排序
      lastNameCol.setStyle("-fx-alignment: CENTER;");
      lastNameCol.setPrefWidth(65);
      lastNameCol
          .setCellValueFactory(new PropertyValueFactory<CurrentMoneyInfo, String>("shishiJine"));
      lastNameCol.setCellFactory(baseController.getColorCellFactory(new CurrentMoneyInfo()));
      table.setPrefWidth(150);
      table.getColumns().addAll(firstNameCol, lastNameCol);

      // 设置数据
      CurrentMoneyInfo_OB_List = FXCollections.observableArrayList();
      for (CurrentMoneyInfo info : list) {
        CurrentMoneyInfo_OB_List.add(info);
      }
      table.setItems(CurrentMoneyInfo_OB_List);

      hbox.setSpacing(5);
      hbox.setPadding(new Insets(0, 0, 0, 0));
      hbox.getChildren().addAll(table);
    }

    // 设置债务表
    ObservableList<TypeValueInfo> obList = FXCollections.observableArrayList();
    for (Map.Entry<String, String> entry : sumMap.entrySet()) {
      obList.add(new TypeValueInfo(entry.getKey(), entry.getValue()));
    }
    tableWaizhai.setItems(obList);
    setWaizhaiSum(tableWaizhai);
  }

  /**
   * 设置外债信息总和
   * 
   * @time 2017年10月28日
   * @param tableWaizhai
   */
  public void setWaizhaiSum(TableView<TypeValueInfo> tableWaizhai) {
    Double sum = 0d;
    ObservableList<TypeValueInfo> list = tableWaizhai.getItems();
    if (list != null && list.size() > 0) {
      for (TypeValueInfo info : list) {
        sum += NumUtil.getNum(info.getValue());
      }
    } else {
      sum = 0d;
    }
    tableWaizhai.getColumns().get(1).setText(NumUtil.digit0(sum));
  }

  /**
   * 计算每个股东的外债总和
   * 
   * @param tgTeamIdMap
   * @return
   */
  public Map<String, String> getSum(Map<String, List<CurrentMoneyInfo>> tgTeamIdMap) {
    final Map<String, String> map = new HashMap<>();
    if (tgTeamIdMap != null && tgTeamIdMap.size() > 0) {
      for (Map.Entry<String, List<CurrentMoneyInfo>> entry : tgTeamIdMap.entrySet()) {
        Double sum = 0d;
        for (CurrentMoneyInfo info : entry.getValue()) {
          sum += NumUtil.getNum(info.getShishiJine());
        }
        map.put(entry.getKey(), NumUtil.digit0(sum));
      }
    }
    return map;
  }

  /**
   * 获取每个托管团队的实时金额 备注：不包括个人和存在于左边的团队
   * 
   */
  public Map<String, List<CurrentMoneyInfo>> get_SSJE_Gudong_Map(
      TableView<CurrentMoneyInfo> tableCurrentMoneyInfo) {
    int pageIndex = dataConstants.Index_Table_Id_Map.size();
    if (pageIndex < 0) {
      return new HashMap<>();
    }

    // 获取托管团队ID
    Set<String> tgTeamIdSet = getTGTeamId();

    // 获取实时金额数据
    List<CurrentMoneyInfo> CurrentMoneyInfoList = new ArrayList<>();
    for (CurrentMoneyInfo infos : tableCurrentMoneyInfo.getItems()) {
      CurrentMoneyInfoList.add(infos);
    }
    // 情况2：从最新的锁定表中获取数据
    // CurrentMoneyInfoList = JSON.parseObject(MoneyService.getJsonString(map,"实时金额"), new
    // TypeReference<List<CurrentMoneyInfo>>() {});

    // 添加只属于托管的团队和个人信息
    List<CurrentMoneyInfo> SSJE_obList = new LinkedList<>();
    for (CurrentMoneyInfo infos : CurrentMoneyInfoList) {
      if (!StringUtil.isAnyBlank(infos.getWanjiaId(), infos.getMingzi())) {
        String playerId = infos.getWanjiaId();
        Player player = dataConstants.membersMap.get(playerId);
        if (player == null || StringUtil.isBlank(player.getTeamName())
            || !tgTeamIdSet.contains(player.getTeamName().toUpperCase())) {
          continue;
        } else if (tgTeamIdSet.contains(player.getTeamName().toUpperCase())) {
          SSJE_obList.add(copyCurrentMoneyInfo(infos)); // 深层克隆
        }
      }
    }

    // 获取每个股东的实时金额数据

    // 托管团队的实时金额信息{ 托管团队：托管团队金额列表List}

    // 步骤1：添加玩家
    Map<String, List<CurrentMoneyInfo>> tgTeamCMIMap = SSJE_obList.stream().filter(infos -> {
      boolean isSuperId = dataConstants.Combine_Super_Id_Map.containsKey(infos.getWanjiaId());
      if (!isSuperId) {// 为解决联合ID的问题，在这里把父节点信息加了进来，后面会把父节点的联合额度为0或空的清除掉，问题：能否在此处就过滤过？？
        if (StringUtil.isBlank(infos.getShishiJine()) || "0".equals(infos.getShishiJine())
            || !infos.getShishiJine().contains("-")) {
          return false;
        }
      }
      return true;
    })
        // .map(info -> copyCurrentMoneyInfo(info)) //复制一份
        .collect(Collectors.groupingBy(info -> {
          CurrentMoneyInfo cmi = (CurrentMoneyInfo) info;
          Player p = dataConstants.membersMap.get(cmi.getWanjiaId());
          if (p == null) {
            return UNKNOW_TG_TEAM;
          } else {
            return p.getTeamName();
          }
        }));


    // 步骤2：处理个人外债和有联合额度的外债
    tgTeamCMIMap = getFinalTGTeamMap(SSJE_obList);
    return tgTeamCMIMap;
  }

  /**
   * 处理托管团队数据
   * 
   * @time 2018年3月8日
   * @param tgTeamMap
   * @return
   */
  @SuppressWarnings("unchecked")
  private Map<String, List<CurrentMoneyInfo>> getFinalTGTeamMap(
      List<CurrentMoneyInfo> SSJE_obList) {

    if (CollectUtil.isNullOrEmpty(SSJE_obList)) {
      return Collections.EMPTY_MAP;
    }
    // 1 获取非父非子节点 A
    List<CurrentMoneyInfo> not_supter_not_sub_list =
        SSJE_obList.stream().filter(info -> not_supter_not_sub(info)).collect(Collectors.toList());

    // 2 获取子节点
    // List<CurrentMoneyInfo> sub_list = SSJE_obList.stream()
    // .filter(info-> subList(info))
    // .collect(Collectors.toList());

    // 3 获取父节点
    List<CurrentMoneyInfo> super_list =
        SSJE_obList.stream().filter(info -> superList(info)).map(superCMI -> {
          superCMI.setShishiJine(superCMI.getCmSuperIdSum());
          return superCMI;
        })// 直接把联合总和赋值到父节点的实时金额
            .collect(Collectors.toList());

    // 4 把子节点赋值给父节点 {父ID : 子ID列表}
    // Map<String, Double> subSumMap = getSubSumMap(sub_list);
    // List<CurrentMoneyInfo> superComputedList = super_list.stream().map(superInfo -> {
    // Double subSum = subSumMap.getOrDefault(superInfo.getWanjiaId(), 0d);
    // superInfo.setShishiJine(NumUtil.getSum(superInfo.getShishiJine(), subSum + ""));
    // return superInfo;
    // }).collect(Collectors.toList());

    // 5 整合A+B
    List<CurrentMoneyInfo> totalList = new ArrayList<>();
    totalList.addAll(not_supter_not_sub_list);
    // totalList.addAll(superComputedList);
    totalList.addAll(super_list);

    Map<String, List<CurrentMoneyInfo>> finalList = totalList.stream()
        .filter(cmi -> cmi.getShishiJine().contains("-")).collect(Collectors.groupingBy(info -> {
          CurrentMoneyInfo cmi = (CurrentMoneyInfo) info;
          Player p = dataConstants.membersMap.get(cmi.getWanjiaId());
          if (p == null) {
            log.error("玩家" + cmi.getWanjiaId() + ",找不到！");
            return UNKNOW_TG_TEAM;
          }
          return p.getTeamName();
        }));


    return finalList;
  }

  private boolean not_supter_not_sub(CurrentMoneyInfo cmi) {
    boolean isSuperId = dataConstants.Combine_Super_Id_Map.containsKey(cmi.getWanjiaId());
    boolean isSubId = dataConstants.Combine_Sub_Id_Map.containsKey(cmi.getWanjiaId());
    return !isSuperId && !isSubId;
  }

  private boolean superList(CurrentMoneyInfo cmi) {
    boolean isSuperId = dataConstants.Combine_Super_Id_Map.containsKey(cmi.getWanjiaId());
    boolean isSubId = dataConstants.Combine_Sub_Id_Map.containsKey(cmi.getWanjiaId());
    return isSuperId && !isSubId;
  }

  /**
   * 复制一个实时金额表的记录
   * 
   * @time 2017年12月29日
   * @param info
   * @param tempSuperInfoMap
   * @return
   */
  private CurrentMoneyInfo copyCurrentMoneyInfo(CurrentMoneyInfo info) {
    CurrentMoneyInfo copyInfo = new CurrentMoneyInfo(info.getMingzi(), info.getShishiJine(),
        info.getWanjiaId(), info.getCmiEdu());
    copyInfo.setColor(info.getColor());
    copyInfo.setCmSuperIdSum(info.getCmSuperIdSum());
    return copyInfo;
  }

}
