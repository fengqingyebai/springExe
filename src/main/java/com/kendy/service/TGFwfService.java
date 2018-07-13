package com.kendy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import com.kendy.constant.Constants;
import com.kendy.controller.MyController;
import com.kendy.controller.tgController.TGController;
import com.kendy.db.DBUtil;
import com.kendy.entity.ProxyTeamInfo;
import com.kendy.entity.TGCompanyModel;
import com.kendy.entity.TGFwfinfo;
import com.kendy.entity.TGTeamInfo;
import com.kendy.entity.TGTeamModel;
import com.kendy.entity.TypeValueInfo;
import com.kendy.util.CollectUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


/**
 * 托管公司的服务费
 * 
 * @author 林泽涛
 * @time 2018年3月13日 下午6:58:18
 */
@Service
public class TGFwfService {

  public void setFwfDetail(String tgCompany, TableView<TGFwfinfo> tableTGFwf,
      TableView<TypeValueInfo> tableTGFwfSum) {
    if (StringUtil.isBlank(tgCompany)) {
      ShowUtil.show("请选择托管公司");
      // return;
    }

    TGController tgController = MyController.tgController;
    List<TGCompanyModel> tgCompanys = DBUtil.get_all_tg_company();

    Set<String> teamSet = new HashSet<>();
    if (CollectUtil.isHaveValue(tgCompanys)) {
      teamSet = tgCompanys.stream().filter(info -> tgCompany.equals(info.getTgCompanyName()))
          .flatMap((TGCompanyModel info) -> Stream.of(info.getTgTeamsStr().split("#")))
          .collect(Collectors.toSet());
    }
    if (CollectUtil.isNullOrEmpty(teamSet)) {
      ShowUtil.show("没有托管团队！", 2);
      return;
    }

    List<TGTeamInfo> companyProxyTeamInfo = new ArrayList<>();
    for (String teamId : teamSet) {
      // 获取代理查询的团队数据
      final List<ProxyTeamInfo> proxyTeamInfoList = tgController.getProxyTeamInfoList(teamId);
      companyProxyTeamInfo.addAll(convert2TGTeamInfo(proxyTeamInfoList));
    }
    int renci = companyProxyTeamInfo.size();

    if (CollectUtil.isNullOrEmpty(companyProxyTeamInfo)) {
      ShowUtil.show("没有代理数据！", 2);
      return;
    }

    // 转化为托管公司的团队数据
    Map<String, List<TGTeamInfo>> teamProxys =
        companyProxyTeamInfo.stream().collect(Collectors.groupingBy(TGTeamInfo::getTgTeamId));

    List<TGFwfinfo> tgFwfInfoList = new ArrayList<>();
    teamProxys.forEach((teamID, tgTeamInfoList) -> {

      // 1 战绩2.5%：
      double zjRate25Sum =
          tgTeamInfoList.stream().mapToDouble(info -> NumUtil.getNum(info.getTgZJ25())).sum();

      // 2 战绩未知
      double zjRateUnknowSum =
          tgTeamInfoList.stream().mapToDouble(info -> NumUtil.getNum(info.getTgZJUnknow())).sum();

      // 3 保险
      double zjBaoxianSum =
          tgTeamInfoList.stream().mapToDouble(info -> NumUtil.getNum(info.getTgBaoxian())).sum()
              * (-Constants.CURRENT_HS_RATE);

      // 4 回保
      double zjHuibaoSum =
          tgTeamInfoList.stream().mapToDouble(info -> NumUtil.getNum(info.getTgHuiBao())).sum();

      // 5 总和
      double zjProfitSum = zjRate25Sum - zjRateUnknowSum + zjBaoxianSum - zjHuibaoSum;

      double huibaoDouble = zjBaoxianSum * tgController.getTgTeamHuibaoRate(teamID);
      double teamFwfRate = tgController.getTgTeamFwfRate(teamID);

      TGFwfinfo fwfInfo =
          new TGFwfinfo(tgCompany, teamID, NumUtil.digit2(zjRate25Sum - zjRateUnknowSum + ""), // 服务回水
                                                                                               // =
                                                                                               // 战绩2.5%
                                                                                               // -
                                                                                               // 战绩未知
              NumUtil.digit2(huibaoDouble + ""), // 服务回保 = 保险 - 回保
              NumUtil.digit2(zjProfitSum + ""), // 单个总利润
              NumUtil.digit2(zjRateUnknowSum + ""), // 服务返水
              NumUtil.digit2(zjHuibaoSum + ""), // 服务返保
              NumUtil.digit2(zjRate25Sum + ""), // 服务全水
              NumUtil.digit2(zjBaoxianSum + ""), // 服务全保
              NumUtil.digit2((zjRate25Sum + zjBaoxianSum) * teamFwfRate + "") // 服务合计
      );
      tgFwfInfoList.add(fwfInfo);

    });

    // 排序
    sort(tgFwfInfoList);
    // 设值
    tableTGFwf.setItems(FXCollections.observableArrayList(tgFwfInfoList));
    // 设总和表
    setTableTGFwfSumData(tableTGFwf, tableTGFwfSum, renci);
  }

  /**
   * 排序
   * 
   * @time 2018年3月15日
   * @param tgFwfInfoList
   */
  private void sort(List<TGFwfinfo> tgFwfInfoList) {
    try {
      if (CollectUtil.isHaveValue(tgFwfInfoList)) {
        Collections.sort(tgFwfInfoList, new Comparator<TGFwfinfo>() {
          @Override
          public int compare(TGFwfinfo x, TGFwfinfo y) {
            String a = x.getTgFwfTeamId().replaceFirst("[a-zA-Z]+", "");
            String b = y.getTgFwfTeamId().replaceFirst("[a-zA-Z]+", "");
            if (StringUtil.isAnyBlank(a, b)) {
              return -1000;
            } else {
              return Integer.valueOf(a).compareTo(Integer.valueOf(b));
            }
          }
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 设置总和表
   * 
   * @time 2018年3月15日
   * @param tableTGFwf
   * @param tableTGFwfSum
   */
  private void setTableTGFwfSumData(TableView<TGFwfinfo> tableTGFwf,
      TableView<TypeValueInfo> tableTGFwfSum, int renci) {
    ObservableList<TGFwfinfo> items = tableTGFwf.getItems();
    if (CollectUtil.isHaveValue(items)) {
      double sumFanbao =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfFanbao())).sum();
      double sumFanshui =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfFanshui())).sum();
      double sumHeji =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfHeji())).sum();
      double sumHuibao =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfHuiBao())).sum();
      double sumHuishui =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfHuishui())).sum();
      double sumProfit =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfProfit())).sum();
      double sumQuanbao =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfQuanbao())).sum();
      double sumQuanshui =
          items.stream().mapToDouble(info -> NumUtil.getNum(info.getTgFwfQuanshui())).sum();
      List<TypeValueInfo> list =
          new ArrayList<>(Arrays.asList(new TypeValueInfo("总回水", NumUtil.digit2(sumHuishui + "")),
              new TypeValueInfo("总回保", NumUtil.digit2(sumHuibao + "")),
              new TypeValueInfo("总利润", NumUtil.digit2(sumProfit + "")),
              new TypeValueInfo("总返水返保", NumUtil.digit2(sumFanshui + sumFanbao + "")),
              new TypeValueInfo("总全水全保", NumUtil.digit2(sumQuanshui + sumQuanbao + "")),
              new TypeValueInfo("总服务费合计", NumUtil.digit2(sumHeji + "")),
              new TypeValueInfo("总人次", renci + "")));
      tableTGFwfSum.setItems(FXCollections.observableArrayList(list));
    }
  }



  /**
   * 代理查询中的数据转成托管中的团队信息数据
   * 
   * @time 2018年3月7日
   * @param teamId
   * @param proxyTeamInfoList
   * @return
   */
  private List<TGTeamInfo> convert2TGTeamInfo(List<ProxyTeamInfo> proxyTeamInfoList) {
    List<TGTeamInfo> list = new ArrayList<>();
    TGController tgController = MyController.tgController;
    Map<String, TGTeamModel> tgTeamRateMap = tgController.getTgTeamModelMap();

    if (CollectUtil.isHaveValue(proxyTeamInfoList)) {
      list = proxyTeamInfoList.stream().map(info -> {
        String yszj = info.getProxyYSZJ();
        TGTeamInfo tgTeam = new TGTeamInfo();
        tgTeam.setTgPlayerId(info.getProxyPlayerId());
        tgTeam.setTgPlayerName(info.getProxyPlayerName());
        tgTeam.setTgYSZJ(info.getProxyYSZJ());
        tgTeam.setTgBaoxian(info.getProxyBaoxian());
        tgTeam.setTgChangci(info.getProxyTableId());
        // 设置战绩未知%
        String teamId = info.getProxyTeamId();
        TGTeamModel tgTeamModel = tgTeamRateMap.get(teamId);
        String teamUnknowValue = tgTeamModel == null ? "0.0" : tgTeamModel.getTgHuishui();
        String teamUnknowStr =
            MyController.tgController.getLirunByYSZJ_TG(yszj, teamUnknowValue, null, 2);
        tgTeam.setTgZJUnknow(teamUnknowStr);
        // 设置战绩2.5% ,即满水
        String percent25Str = MyController.tgController.getLirunByYSZJ_TG(info.getProxyYSZJ(),
            teamUnknowValue, null, 1);
        tgTeam.setTgZJ25(percent25Str);
        // 设置回保
        String teamHuibaoRateValue = tgTeamModel == null ? "0.0" : tgTeamModel.getTgHuiBao();
        String teamHuibaoRateStr = NumUtil.digit2(-Constants.CURRENT_HS_RATE
            * NumUtil.getNumTimes(tgTeam.getTgBaoxian(), teamHuibaoRateValue) + "");
        if (tgTeam.getTgBaoxian().equals("0")) {
          teamHuibaoRateStr = "0";
        }
        tgTeam.setTgHuiBao(teamHuibaoRateStr);
        // 设置利润
        String profit = tgController.getRecordProfit(tgTeam);
        tgTeam.setTgProfit(profit);
        // 设置团队
        tgTeam.setTgTeamId(teamId);

        return tgTeam;
      }).collect(Collectors.toList());
    }
    return list;
  }

}
