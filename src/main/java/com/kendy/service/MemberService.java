package com.kendy.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kendy.constant.DataConstans;
import com.kendy.db.DBUtil;
import com.kendy.entity.MemberZJInfo;
import com.kendy.entity.Player;
import com.kendy.model.GameRecord;
import com.kendy.util.NumUtil;
import com.kendy.util.StringUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * 会员查询服务类
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:51:39
 */
@Component
public class MemberService {

  @Autowired
  public DBUtil dbUtil;
  @Autowired
  public DataConstans dataConstants; // 数据控制类

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void initMemberQuery(ListView<String> memberListView,
      TableView<MemberZJInfo> tableMemberZJ, Label memberDateStr, Label memberPlayerId,
      Label memberPlayerName, Label memberSumOfZJ, Label memberTotalZJ) {
    // ListView变化时自动更新右边的信息
    memberListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
      @Override
      public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        /**** 会员当天战绩区域赋值 ***/
        // 清空原有数据
        memberDateStr.setText("2017-00-00");
        memberPlayerId.setText("");
        memberPlayerName.setText("");
        memberSumOfZJ.setText("");
        tableMemberZJ.setItems(null);
        if (!StringUtil.isBlank(dataConstants.Date_Str)) {
          memberDateStr.setText(dataConstants.Date_Str);
        }
        String newVal = (String) newValue;
        if (!StringUtil.isBlank(newVal)) {
          // 玩家ID和玩家名称赋值
          String wanjiaName = getNameFromStr(newVal);
          memberPlayerId.setText(getIdFromStr(newVal));
          memberPlayerName.setText(wanjiaName);
          // 根据玩家名称去查询数据
          updateMemberTable(tableMemberZJ, memberPlayerId.getText(), memberSumOfZJ);
        }

        /**** 会员历史战绩区域赋值 ***/
        String totalZJ = dbUtil.getTotalZJByPId(memberPlayerId.getText());
        memberTotalZJ.setText(totalZJ);
      }
    });
  }


  /**
   * 获取玩家ID 备注：名称可能含有空格，名称与ID也是用空格隔开。所以单独成方法。
   *
   * @param nameAndId 名称和ID 传过来肯定是不为空的，固不作空判断
   */
  public String getIdFromStr(String nameAndId) {
    String[] arr = nameAndId.split(" ");
    return arr[arr.length - 1];
  }

  public String getNameFromStr(String nameAndId) {
    return nameAndId.substring(0, nameAndId.lastIndexOf(" "));
  }

  /**
   * 更新人员表
   *
   * @time 2017年10月28日
   */
  public void updateMemberTable(TableView<MemberZJInfo> tableMemberZJ, String wanjiaId,
      Label memberSumOfZJ) {
    // 根据名称获取团队ID
    Player wanjia = dataConstants.membersMap.get(wanjiaId);
    String teamId = "";
    if (wanjia != null && !StringUtil.isBlank(wanjia.getTeamName())) {
      teamId = wanjia.getTeamName();
    }
    ObservableList<MemberZJInfo> obList = FXCollections.observableArrayList();
    String zj = "";// 战绩
    Double sumOfZJ = 0d;// 战绩总和
    if (dataConstants.Total_Team_Huishui_Map.size() != 0) {
      List<GameRecord> list = dataConstants.Total_Team_Huishui_Map.get(teamId);
      if (list != null) {
        for (GameRecord info : list) {
          if (info.getPlayerId().equals(wanjiaId)) {
            zj = info.getShishou();
            obList.add(new MemberZJInfo(info.getTableId(), zj));
            sumOfZJ += NumUtil.getNum(zj);
          }
        }
      }
    }
    // 赋值总和
    memberSumOfZJ.setText(NumUtil.digit0(sumOfZJ));
    // 刷新表
    tableMemberZJ.setItems(obList);
    tableMemberZJ.refresh();


  }

  public void setResult2ListView(TextField memberNameField,
      ListView<String> memberListView) {
    String name = memberNameField.getText();
    Set<Player> set = new HashSet<>();
    if (!StringUtil.isBlank(name)) {
      dataConstants.membersMap.forEach((mId, mPlayer) -> {
        if (mPlayer.getPlayerName().contains(name)) {
          set.add(mPlayer);
        }
      });
    }
    ObservableList<String> list = FXCollections.observableArrayList();
    memberListView.setItems(list);
    if (set.size() > 0) {
      set.forEach(play -> {
        memberListView.getItems().add(play.getPlayerName() + " " + play.getgameId());
      });
    }
    memberListView.refresh();
  }
}
