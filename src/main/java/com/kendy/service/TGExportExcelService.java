package com.kendy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.kendy.controller.tgController.TGController;
import com.kendy.entity.CurrentMoneyInfo;
import com.kendy.entity.TGCommentInfo;
import com.kendy.entity.TGExcelModel;
import com.kendy.entity.TGFwfinfo;
import com.kendy.entity.TGKaixiaoInfo;
import com.kendy.entity.TGLirunInfo;
import com.kendy.entity.TGTeamInfo;
import com.kendy.entity.TypeValueInfo;
import com.kendy.excel.ExportTGExcel;
import com.kendy.util.CollectUtil;
import com.kendy.util.ErrorUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

/**
 * 导出TGExcel
 *
 * @author linzt
 * @time 2018年3月19日 下午2:49:16
 */
@Component
public class TGExportExcelService {

  private TGController tgController;

  /**
   * 构造方法
   */
  public TGExportExcelService(TGController tgController) {
    this.tgController = tgController;
  }

  /**
   * 导出Excel
   */
  public void exportExcel() {
    try {
      List<TGExcelModel> excelList = new ArrayList<>();
      excelList.add(getKaixiaoExcelModel()); // 开销
      excelList.add(getPlayerCommentExcelModel()); // 玩家备注
      excelList.add(getMonthLirunExcelModel());// 月利润
      excelList.add(getTGWaizhaiExcelModel()); // 外债表
      excelList.add(getFwfExcelModel()); // 服务费明细
      excelList.addAll(getTGTeamZhanjiExcelModels()); // 战绩表
      ExportTGExcel excelCreator = new ExportTGExcel(excelList);

      excelCreator.export();
      ShowUtil.show("导出成功", 2);
    } catch (Exception e) {
      ErrorUtil.err("导出托管公司所有数据失败", e);
      e.printStackTrace();
    }

  }

  /**
   * 开销ExcelModel
   *
   * @time 2018年3月18日
   */
  private TGExcelModel getKaixiaoExcelModel() {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();

    titleList = new LinkedList<>(Arrays.asList("日期", "玩家名称", "支出项目", "开销金额", "托管公司"));
    ObservableList<TGKaixiaoInfo> items = tgController.tableTGKaixiao.getItems();
    if (CollectUtil.isHaveValue(items)) {
      data = items.stream().map(info -> {
        Object[] obj = new Object[5];
        obj[0] = info.getTgKaixiaoDate();
        obj[1] = info.getTgKaixiaoPlayerName();
        obj[2] = info.getTgKaixiaoPayItem();
        obj[3] = info.getTgKaixiaoMoney();
        obj[4] = info.getTgKaixiaoCompany();
        return obj;
      }).collect(Collectors.toList());
    }
    ObservableList<String> sumItems = tgController.tgKaixiaoSumView.getItems();
    if (CollectUtil.isHaveValue(sumItems)) {
      dataSum = sumItems.stream().map(info -> {
        Object[] obj = new Object[2];
        obj[0] = info.split(":")[0];
        obj[1] = info.split(":")[1];
        return obj;
      }).collect(Collectors.toList());
    }

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName("开销");
    return excelModel;
  }

  /**
   * 玩家备注ExcelModel
   *
   * @time 2018年3月18日
   */
  private TGExcelModel getPlayerCommentExcelModel() {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();

    titleList =
        new LinkedList<>(Arrays.asList("日期", "玩家ID", "玩家名称", "项目类型", "ID", "名称", "备注", "托管公司"));
    ObservableList<TGCommentInfo> items = tgController.tableTGComment.getItems();
    if (CollectUtil.isHaveValue(items)) {
      data = items.stream().map(info -> {
        Object[] obj = new Object[8];
        obj[0] = info.getTgCommentDate();
        obj[1] = info.getTgCommentPlayerId();
        obj[2] = info.getTgCommentPlayerName();
        obj[3] = info.getTgCommentType();
        obj[4] = info.getTgCommentId();
        obj[5] = info.getTgCommentName();
        obj[6] = info.getTgCommentBeizhu();
        obj[7] = info.getTgCommentCompany();
        return obj;
      }).collect(Collectors.toList());
    }
    ObservableList<String> sumItems = tgController.tgCommentSumView.getItems();
    if (CollectUtil.isHaveValue(sumItems)) {
      dataSum = sumItems.stream().map(info -> {
        Object[] obj = new Object[2];
        obj[0] = info.split(":")[0];
        obj[1] = info.split(":")[1];
        return obj;
      }).collect(Collectors.toList());
    }

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName("玩家备注");
    return excelModel;
  }

  /**
   * 月利润ExcelModel
   *
   * @time 2018年3月19日
   */
  private TGExcelModel getMonthLirunExcelModel() {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();

    titleList = new LinkedList<>(
        Arrays.asList("日期", "托管公司", "总利润", "总开销", "合计", "公司占股", "托管公司占股", "团队利润", "托管合计"));
    ObservableList<TGLirunInfo> items = tgController.tableTGLirun.getItems();
    if (CollectUtil.isHaveValue(items)) {
      data = items.stream().map(info -> {
        Object[] obj = new Object[9];
        obj[0] = info.getTgLirunDate();
        obj[1] = info.getTgLirunCompanyName();
        obj[2] = info.getTgLirunTotalProfit();
        obj[3] = info.getTgLirunTotalKaixiao();
        obj[4] = info.getTgLirunRestHeji();
        obj[5] = info.getTgLirunATMCompany();
        obj[6] = info.getTgLirunTGCompany();
        obj[7] = info.getTgLirunTeamProfit();
        obj[8] = info.getTgLirunHeji();
        return obj;
      }).collect(Collectors.toList());
    }

    // add 新增合计
    String yajin = StringUtil.nvl(tgController.tgCompanyYajin.getText()); // 托管公司押金
    String edu = StringUtil.nvl(tgController.tgCompanyEdu.getText()); // 托管公司额度
    String yifenhong = StringUtil.nvl(tgController.tgYifenhong.getText()); // 托管公司已分红
    String waizhai = StringUtil.nvl(tgController.totalWaizhai.getText()); // 总外债
    String profit = StringUtil.nvl(tgController.tgTotalProfit.getText()); // 总利润
    String availabel = StringUtil.nvl(tgController.tgAvailable.getText()); // 总可分配

    Object[] obj = new Object[2];
    obj[0] = "押金";
    obj[1] = yajin;
    dataSum.add(obj);
    obj = new Object[2];
    obj[0] = "额度";
    obj[1] = edu;
    dataSum.add(obj);
    obj = new Object[2];
    obj[0] = "已分红";
    obj[1] = yifenhong;
    dataSum.add(obj);
    obj = new Object[2];
    obj[0] = "总外债";
    obj[1] = waizhai;
    dataSum.add(obj);
    obj = new Object[2];
    obj[0] = "总利润";
    obj[1] = profit;
    dataSum.add(obj);
    obj = new Object[2];
    obj[0] = "可分配";
    obj[1] = availabel;
    dataSum.add(obj);

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName("月利润");
    return excelModel;
  }

  /**
   * 导出服务费明细
   */
  private TGExcelModel getFwfExcelModel() {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();

    titleList = new LinkedList<>(
        Arrays.asList("托管公司", "托管团队", "回水", "回保", "总利润合计", "返水", "返保", "全水", "全保", "合计"));
    ObservableList<TGFwfinfo> items = tgController.tableTGFwf.getItems();
    if (CollectUtil.isHaveValue(items)) {
      data = items.stream().map(info -> {
        Object[] obj = new Object[10];
        obj[0] = info.getTgFwfCompany();
        obj[1] = info.getTgFwfTeamId();
        obj[2] = info.getTgFwfHuishui();
        obj[3] = info.getTgFwfHuiBao();
        obj[4] = info.getTgFwfProfit();
        obj[5] = info.getTgFwfFanshui();
        obj[6] = info.getTgFwfFanbao();
        obj[7] = info.getTgFwfQuanshui();
        obj[8] = info.getTgFwfQuanbao();
        obj[9] = info.getTgFwfHeji();
        return obj;
      }).collect(Collectors.toList());
    }
    ObservableList<TypeValueInfo> sumItems = tgController.tableTGFwfSum.getItems();
    if (CollectUtil.isHaveValue(sumItems)) {
      dataSum = sumItems.stream().map(info -> {
        Object[] obj = new Object[2];
        obj[0] = info.getType();
        obj[1] = info.getValue();
        return obj;
      }).collect(Collectors.toList());
    }

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName("服务费");
    return excelModel;
  }

  /**
   * 导出战绩明细
   */
  private TGExcelModel getTeamZJExcelModel(String teamId, String hsRate) {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();

    titleList = new LinkedList<>(
        Arrays.asList("玩家ID", "玩家名称", "原始战绩", "保险", "满水", "战绩" + hsRate, "回保", "利润", "场次"));
    ObservableList<TGTeamInfo> items = tgController.tableTGZhanji.getItems();
    if (CollectUtil.isHaveValue(items)) {
      data = items.stream().map(info -> {
        Object[] obj = new Object[9];
        obj[0] = info.getTgPlayerId();
        obj[1] = info.getTgPlayerName();
        obj[2] = info.getTgYSZJ();
        obj[3] = info.getTgBaoxian();
        obj[4] = info.getTgZJ25();
        obj[5] = info.getTgZJUnknow();
        obj[6] = info.getTgHuiBao();
        obj[7] = info.getTgProfit();
        obj[8] = info.getTgChangci();
        return obj;
      }).collect(Collectors.toList());
    }
    ObservableList<TypeValueInfo> sumItems = tgController.tableZJSum.getItems();
    if (CollectUtil.isHaveValue(sumItems)) {
      dataSum = sumItems.stream().map(info -> {
        Object[] obj = new Object[2];
        obj[0] = info.getType();
        obj[1] = info.getValue();
        return obj;
      }).collect(Collectors.toList());
    }

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName(teamId);
    return excelModel;
  }


  /**
   * 导出战绩明细(所有)
   */
  private List<TGExcelModel> getTGTeamZhanjiExcelModels() {
    List<TGExcelModel> excelLsit = new ArrayList<>();
    ObservableList<Node> teamBtns = tgController.TG_Team_VBox.getChildren();
    if (CollectUtil.isEmpty(teamBtns)) {
      return excelLsit;
    }

    for (Node node : teamBtns) {
      Button teamBtn = (Button) node;
      String teamId = teamBtn.getText();
      teamBtn.fire();
      String hsRate = StringUtil.nvl(tgController.tgTeamHSRate.getText(), "0%");
      TGExcelModel teamZJExcelModel = getTeamZJExcelModel(teamId, hsRate);
      excelLsit.add(teamZJExcelModel);

    }

    return excelLsit;
  }


  /**
   * 导出托管公司的团队外债与团队外债明细
   */
  @SuppressWarnings("unchecked")
  private TGExcelModel getTGWaizhaiExcelModel() {
    TGExcelModel excelModel = new TGExcelModel();
    List<String> titleList = new LinkedList<>();
    List<Object[]> data = new LinkedList<>();
    List<String> titleSumList = new LinkedList<>();
    List<Object[]> dataSum = new LinkedList<>();
    // 取值
    Map<String, List<Object[]>> teamMap = new LinkedHashMap<>();// 传给Excel的外债数据
    ObservableList<Node> items = tgController.tgWZTeamHBox.getChildren();
    // 存放总和表
    ObservableList<TypeValueInfo> sumItems = tgController.tgWZTeam.getItems();
    if (CollectUtil.isHaveValue(sumItems)) {
      List<Object[]> teamList = tgController.tgWZTeam.getItems().stream().map(info -> {
        Object[] obj = new Object[2];
        obj[0] = info.getType();
        obj[1] = info.getValue();
        return obj;
      }).collect(Collectors.toList());
      String key = tgController.tgWZTeam.getColumns().get(0).getText() + "#"
          + tgController.tgWZTeam.getColumns().get(1).getText();
      teamMap.put(key, teamList);
    }
    // 存放明细表
    if (CollectUtil.isHaveValue(items)) {
      for (Node node : items) {
        TableView<CurrentMoneyInfo> table = (TableView<CurrentMoneyInfo>) node;
        List<Object[]> teamList = table.getItems().stream().map(info -> {
          Object[] obj = new Object[2];
          obj[0] = info.getMingzi();
          obj[1] = info.getShishiJine();
          return obj;
        }).collect(Collectors.toList());
        String key =
            table.getColumns().get(0).getText() + "#" + table.getColumns().get(1).getText();
        teamMap.put(key, teamList);
      }
    }

    excelModel.setColumnList(titleList);
    excelModel.setData(data);
    excelModel.setColumnSumList(titleSumList);
    excelModel.setDataSum(dataSum);
    excelModel.setSheetName("外债");
    // 设值
    excelModel.setWaiZai(true);
    excelModel.setWaizhaiMap(teamMap);
    return excelModel;
  }


}
