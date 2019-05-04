package com.kendy.service;

import com.jfoenix.controls.JFXButton;
import com.kendy.controller.ChangciController;
import com.kendy.controller.MyController;
import com.kendy.customize.MyTable;
import com.kendy.db.service.GameRecordService;
import com.kendy.entity.PersonalDetailInfo;
import com.kendy.model.GameRecordModel;
import com.kendy.util.ButtonUtil;
import com.kendy.util.ColumnUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.TimeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 双击个人累计回水版块弹框显示个人回水详情服务
 */
@Component
public class PersonalService extends BasicService {

  @Autowired
  MyController myController;
  @Autowired
  ChangciController changciController;
  @Autowired
  GameRecordService gameRecordService;

  private Stage stage; // 查看视图
  /**
   * 打开个人回水详情列表
   */
  public void openPersonalInfoDetailailView(String playerName, String playerId) {
    String clubId = myController.getClubId();
    String softDate = changciController.getSoftDate();
    List<GameRecordModel> records = gameRecordService.getRecordsByPlayerId(softDate, clubId, playerId);
    List<PersonalDetailInfo> detailList = getPersonalDataList(records);
    addPersonalSum(detailList);
    String TITLE = playerName + "的回水详情";
    MyTable<PersonalDetailInfo> table = new MyTable<>();

    setPersonalDeailColumns(table);
    table.setEditable(false);

    // 获取值
    table.getItems().addAll(detailList);
    table.getSelectionModel().clearSelection();

    // 导出按钮
    table.setEntityClass(PersonalDetailInfo.class);
    table.setExcelName(TITLE + TimeUtil.getDateTime());
    JFXButton exportBtn = ButtonUtil.getDownloadButn(table);

    StackPane stackPane = new StackPane();
    stackPane.getChildren().addAll(table, exportBtn);
    stackPane.setAlignment(Pos.BOTTOM_CENTER);

    if (stage == null) { // 共享一个舞台
      stage = new Stage();
    }
    // stage.setResizable(Boolean.FALSE); // 可手动放大缩小
    ShowUtil.setIcon(stage);
    stage.setTitle(TITLE);
    stage.setWidth(1050);
    stage.setHeight(450);

    Scene scene = new Scene(stackPane);
    stage.setScene(scene);
    stage.show();
  }

  private void addPersonalSum(List<PersonalDetailInfo> detailList) {
    if (CollectionUtils.isNotEmpty(detailList)) {
      PersonalDetailInfo firstInfo = detailList.get(0);
      PersonalDetailInfo sumInfo = new PersonalDetailInfo();
      sumInfo.setClubId(firstInfo.getClubId());
      sumInfo.setClubName(firstInfo.getClubName());
      sumInfo.setTeamId(firstInfo.getTeamId());
      sumInfo.setPlayerId(firstInfo.getPlayerId());
      sumInfo.setPlayerName(firstInfo.getPlayerName());
      sumInfo.setJuType("-");
      sumInfo.setTableId("合计");
      double jifen = 0;
      double shishou = 0;
      double baoxiao = 0;
      double chuhuishui = 0;
      double huibao = 0;
      for (PersonalDetailInfo detail : detailList) {
        jifen += NumUtil.getNum(detail.getJifen());
        shishou += NumUtil.getNum(detail.getShishou());
        baoxiao += NumUtil.getNum(detail.getBaoxian());
        chuhuishui += NumUtil.getNum(detail.getChuHuishui());
        huibao += NumUtil.getNum(detail.getHuibao());
      }
      sumInfo.setJifen(NumUtil.digit(jifen));
      sumInfo.setShishou(NumUtil.digit(shishou));
      sumInfo.setBaoxian(NumUtil.digit(baoxiao));
      sumInfo.setChuHuishui(NumUtil.digit(chuhuishui));
      sumInfo.setHuibao(NumUtil.digit(huibao));
      detailList.add(sumInfo);
    }
  }

  private List<PersonalDetailInfo> getPersonalDataList(List<GameRecordModel> records) {
    List<PersonalDetailInfo> personalDetailInfos = new ArrayList<>();
    for (GameRecordModel record : records) {
      PersonalDetailInfo info = new PersonalDetailInfo();
      info.setClubId(record.getClubid());
      info.setClubName(record.getClubName());
      info.setTeamId(record.getTeamId());
      info.setPlayerId(record.getPlayerid());
      info.setPlayerName(record.getBeginplayername());
      info.setJifen(record.getYszj());
      info.setShishou(record.getShishou());
      info.setBaoxian(record.getSingleinsurance());
      info.setChuHuishui(record.getPersonalHuishui());
      info.setHuibao(NumUtil.digit(record.getPersonalHuibao()));
      info.setJuType(record.getJutype());
      info.setTableId(record.getTableid());
      personalDetailInfos.add(info);
    }
    return personalDetailInfos;
  }

  private void setPersonalDeailColumns(MyTable<PersonalDetailInfo> table) {
    List<TableColumn<PersonalDetailInfo, String>> cols = Arrays.asList(
        getTableColumn("俱乐部ID", "clubId"),
        getTableColumn("俱乐部名称", "clubName"),
        getTableColumn("团队ID", "teamId"),
        getTableColumn("玩家ID", "playerId"),
        getTableColumn("玩家名称", "playerName"),
        getTableColumn("类型", "juType"),
        getTableColumn("牌局", "tableId"),
        getTableColumn("计分", "jifen"),
        getTableColumn("实收", "shishou"),
        getTableColumn("保险", "baoxian"),
        getTableColumn("出回水", "chuHuishui"),
        getTableColumn("回保", "huibao")
    );
    table.getColumns().addAll(cols);
  }


  /**
   * 获取动态数据表的列
   */
  private <T> TableColumn<T, String> getTableColumn(String colName,
      String colVal) {
    T t = (T) (new PersonalDetailInfo());
    return ColumnUtil.getTableRedColumn(colName, colVal, t);
  }
}
