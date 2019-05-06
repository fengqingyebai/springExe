package com.kendy.excel;

import com.kendy.db.entity.Player;
import com.kendy.entity.CurrentMoneyInfo;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.kendy.constant.DataConstans;
import com.kendy.controller.LMController;
import com.kendy.controller.MyController;
import com.kendy.entity.Huishui;
import com.kendy.excel.myExcel4j.MyExcelUtils;
import com.kendy.model.CombineID;
import com.kendy.model.GameRecordModel;
import com.kendy.other.Wrap;
import com.kendy.service.MoneyService;
import com.kendy.util.CollectUtil;
import com.kendy.util.FileUtil;
import com.kendy.util.FilterUtf8mb4;
import com.kendy.util.NumUtil;
import com.kendy.util.PathUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;

/**
 * 读取Excel
 *
 * @author 林泽涛
 * @time 2017年10月7日 下午3:54:05
 */
@Component
public class ExcelReaderUtil {

  @Autowired
  private MyController myController;

  @Autowired
  private MoneyService moneyService;

  @Autowired
  private DataConstans dataConstans;

  @Autowired
  private LMController lmController;

  // public static ExcelReaderUtil instance = new ExcelReaderUtil();

  private Logger log = LoggerFactory.getLogger(ExcelReaderUtil.class);

  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * 测试本工具类
   */
  public static void main(String[] args) throws Exception {

    String excelPath = PathUtil.getUserDeskPath() + "/战绩导出-24-299.xls";
    List<GameRecordModel> basicRecords =
        MyExcelUtils.getInstance().readExcel2Objects(excelPath, GameRecordModel.class, 1, 0);

    // log.info("finishes..." + basicRecords.size());
  }


  /**
   * 创建 Excel 2003 或2007工作簿对象
   *
   * @time 2017年10月7日
   */
  public Workbook getWeebWork(String filename) throws Exception {
    Workbook workbook = null;
    if (null != filename) {
      String fileType = filename.substring(filename.lastIndexOf("."));
      FileInputStream fileStream = new FileInputStream(new File(filename));
      if (".xls".equals(fileType.trim().toLowerCase())) {
        workbook = new HSSFWorkbook(fileStream);// 创建 Excel 2003 工作簿对象
      } else if (".xlsx".equals(fileType.trim().toLowerCase())) {
        workbook = new XSSFWorkbook(fileStream);// 创建 Excel 2007 工作簿对象
        // workbook = WorkbookFactory.create(fileStream);
      }
    }
    return workbook;
  }

  /**
   * 导入人员名单
   *
   * @time 2017年10月7日
   */
  public Map<String, Player> readMembersRecord(File file) throws Exception {

    Map<String, Player> membersMap = new LinkedHashMap<>();
    Player player;
    long start = System.currentTimeMillis();
    log.info("读取人员名单开始...");
    try (FileInputStream is = new FileInputStream(file)) {
      Workbook workbook = getWeebWork(file.getAbsolutePath());
      Sheet sheet = workbook.getSheetAt(0);

      Iterator<Row> rowIterator = sheet.iterator();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        if (row.getRowNum() != 0) {
          // 游戏id
          String gameId = getCellValue(row.getCell(0));
          gameId = StringUtil.nvl(gameId, "");
          // 玩家名称
          String playerName = getCellValue(row.getCell(1));
          playerName = StringUtil.nvl(playerName, "");
          // 股东名称
          String gudong = getCellValue(row.getCell(2));
          gudong = StringUtil.nvl(gudong, "");
          // 团队名称
          String team = getCellValue(row.getCell(3));
          team = StringUtil.nvl(team, "");
          // 额度
          String edu = getCellValue(row.getCell(4));
          edu = StringUtil.nvl(edu, "0");
          // 是否父ID
          String isParent = getCellValue(row.getCell(5));
          isParent = StringUtil.nvl(isParent, "0");
          // 回保
          String huibao = getCellValue(row.getCell(6));
          huibao = StringUtil.nvl(huibao, "");
          huibao = StringUtils.equals("0", huibao) ? "0%" : huibao;
          // 回水
          String huishui = getCellValue(row.getCell(7));
          huishui = StringUtil.nvl(huishui);
          huishui = StringUtils.equals("0", huishui) ? "0%" : huishui;
          if (!StringUtil.isBlank(gameId)) {
            try {
              gameId = new BigDecimal(gameId).toPlainString();// 不要科学记数法
            } catch (Exception e) {
              gameId = getCellValue(row.getCell(0));
            }
            gudong = StringUtil.isBlank(gudong) ? "W" : gudong;// 股东为空的默认设置为W
            player = new Player(gameId, team, gudong, playerName, edu, isParent, huibao, huishui);
            membersMap.put(gameId, player);
          }
        }
      }
    }
    log.info("读取人员名单结束! size:{}, cost:{}", membersMap.size(), (System.currentTimeMillis() - start));
    return membersMap;
  }

  private String getCellValue(Cell cell) {
    if (cell == null) {
      return "";
    } else {
      cell.setCellType(CellType.STRING);
      return cell.toString();
    }
  }

  /**
   * 导入回水
   */
  public Wrap readHuishuiRecord(File file) {

    Map<String, Huishui> huishuiMap = new HashMap<>();
    Wrap wrap = new Wrap();
    try {
      FileInputStream is = new FileInputStream(file);
      log.info("读取团队回水开始...");

      Workbook workbook = getWeebWork(file.getAbsolutePath());
      Sheet sheet = workbook.getSheetAt(0);// 回水对应的sheet

      Iterator<Row> rowIterator = sheet.iterator();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        if (row.getRowNum() >= 2) {
          // 团队id
          String teamId = "";
          // 团队名称
          String teamName = "";
          // 回水比例
          String huishuiRate = "";
          // 备注
          String beizhu = "";
          // 保险比例
          String insuranceRate = "";
          // 股东
          String gudong = "";
          // 是否代理
          String zjManage = "";
          // 代理回水比例
          String proxyHsRate = "";
          // 代理回保比例
          String proxyHbRate = "";
          // 代理服务费
          String proxyFWF = "";
          // 积分比例
          String jfPercent = "";
          for (int i = 1; i < 11; i++) {
            Cell cell = row.getCell(i);
            if (cell == null) {
              log.error("读取回水表发现NPE, 列：{}", i);
              break;
            }
            cell.setCellType(CellType.STRING);
            String value = cell.getStringCellValue();
            if (i == 1) {
              // 团ID====="+value);
              if (!StringUtil.isBlank(value)) {
                value = value.toUpperCase();
              }
              teamId = value;
            } else if (i == 2) {
              // 团队名字====="+value);
              teamName = StringUtil.isBlank(value) ? "" : value;
            } else if (i == 3) {
              if (!StringUtil.isBlank(value) && value.contains("E")) {
                value = Double.valueOf(value) + "";
              }
              // 回水比例====="+value);
              if (value.contains("%")) {
                value = NumUtil.digit4(NumUtil.getNumByPercent(value) + "");
              }
              huishuiRate = value;
            } else if (i == 4) {
              // 保险比例====="+value);
              if (value.contains("%")) {
                value = NumUtil.getNumByPercent(value) + "";
              }
              insuranceRate = StringUtil.isBlank(value) ? "0" : NumUtil.digit4(value);
            } else if (i == 5) {
              // 股东====="+value);
              gudong = value;
            } else if (i == 6) {
              // 战绩是否代理管====="+value);
              zjManage = StringUtil.isBlank(value) ? "否" : value;
            } else if (i == 7) {
              // 备注====="+value);
              beizhu = value;
            } else if (i == 8) {
              if (StringUtil.isBlank(value)) {
                value = "0";
              }
              if (value.contains("%")) {
                proxyHsRate = value;
              } else {
                // 回水比例====="+NumUtil.getPercentStr(Double.valueOf(value)));
                proxyHsRate = NumUtil.getPercentStr(Double.valueOf(value));
              }
            } else if (i == 9) {
              if (StringUtil.isBlank(value)) {
                value = "0";
              }
              if (value.contains("%")) {
                proxyHbRate = value;
              } else {
                // 回保比例====="+NumUtil.getPercentStr(Double.valueOf(value)));
                proxyHbRate = NumUtil.getPercentStr(Double.valueOf(value));
              }
            } else if (i == 10) {
              // 服务费====="+value);
              proxyFWF = value;
            } else if (i == 11) {
              // 积分比例====="+value);
              if (StringUtil.isBlank(value)) {
                value = "0";
              }
              jfPercent = NumUtil.getPercentStr(Double.valueOf(value));
            }
          }
          if (!StringUtil.isBlank(teamId)) {
            huishuiMap.put(teamId, new Huishui(teamId, teamName, huishuiRate, insuranceRate, gudong,
                zjManage, beizhu, proxyHsRate, proxyHbRate, proxyFWF));
          }
        }
      }
      is.close();
      wrap = new Wrap(true, huishuiMap);
      log.info("读取团队回水结束!size:" + huishuiMap.size());
    } catch (Exception e) {
      wrap = new Wrap();
      e.printStackTrace();
    }
    return wrap;
  }

  /**
   * 读取新-旧战绩
   *
   * @time 2018年1月10日
   */
  public List<GameRecordModel> readZJRecord(String excelFilePath, String userClubId, String LMType,
      int versionType) throws Exception {
    if (0 == versionType) {
      return null;
    } else {
      return readZJRecord_NewVersion(excelFilePath, userClubId, LMType);
    }
  }


  /*
   * 判断总手数为0
   */
  private void judgeSumHandsCount(List<GameRecordModel> gameRecordModels) {
    boolean isSumHandsCountZero =
        gameRecordModels.stream().allMatch(record -> "0".equals(record.getSumhandscount()));
    if (isSumHandsCountZero || CollectUtil.isEmpty(gameRecordModels)) {
      ShowUtil.show("提示：总手数为0！");
    }
  }

  /*
   * 获取级别
   */
  private String getLevel(String pathString) {
    String level = "";
    try {
      String split = "\\";
      if (!pathString.contains(split)) {
        split = "/";
      }
      String fileaName =
          pathString.substring(pathString.lastIndexOf(split) + 1, pathString.lastIndexOf("-"));
      fileaName = FilterUtf8mb4.filterUtf8mb4(fileaName);
      level = fileaName.substring(fileaName.indexOf("-") + 1);
      if (level.contains("战绩导出")) {
        level = fileaName.substring(fileaName.indexOf("战绩导出") + 5);
      }
      if (level.length() > 18) {
        throw new Exception("获取级别出错，战绩名称是：" + pathString);
      }
    } catch (Exception e) {
      log.error("通过{}获取级别失败！", pathString);
    }
    return level;
  }

  /**
   * 导入战绩Excel
   *
   * 两个功能： A:导入到场次信息 B:导入到联盟Tab
   */
  public List<GameRecordModel> readZJRecord_NewVersion(String excelFilePath, String userClubId,
      String LMType) throws Exception {// 新增了联盟类型

    // 获取所有记录
    List<GameRecordModel> gameRecordModels =
        MyExcelUtils.getInstance().readExcel2Objects(excelFilePath, GameRecordModel.class, 1, 0);

    if (CollectUtil.isHaveValue(gameRecordModels)) {
      Optional<GameRecordModel> noneExistPlayer =
          gameRecordModels.stream().filter(e -> e.getClubid().equals(myController.getClubId()))
              .filter(e -> dataConstans.membersMap.get(e.getPlayerid()) == null).findFirst();
      if (noneExistPlayer.isPresent()) {
        GameRecordModel gameRecordModel = noneExistPlayer.get();
        throw new Exception(" 玩家【" + gameRecordModel.getPlayerName() + "】，ID【"
            + gameRecordModel.getPlayerid() + "】不存在于系统中，请先建立该玩家信息！");
      }
    }
    String tableId = FileUtil.getTableId(excelFilePath);
    // 补全每条记录的值
    moneyService.fillGameRecords(gameRecordModels, tableId, getLevel(excelFilePath), LMType);
    // 判断总手数为0
    judgeSumHandsCount(gameRecordModels);

    // TODO 添加所有记录到联盟对帐表final?
    lmController.currentRecordList = gameRecordModels;

    // 只返回当前俱乐部的记录
    List<GameRecordModel> _gameRecordModels = gameRecordModels.stream()
        .filter(e -> e.getClubid().equals(userClubId)).collect(Collectors.toList());

    // 存储数据 {场次=infoList...}
    dataConstans.zjMap.put(tableId, _gameRecordModels);

    return _gameRecordModels;
  }


  /**
   * 导入昨日留底Excel
   */
  public Map<String, String> readPreDataRecord(File file) throws Exception {

    // 昨日留底总数据结构
    Map<String, String> preDataMap = new HashMap<>();

    FileInputStream is = null;

    if (!file.exists() || !file.isFile()) {
      return new HashMap<>();
    }
    log.info("开始----------导入昨日留底Excel");

    is = new FileInputStream(file);

    // 获取excel sheet
    Workbook workbook = getWeebWork(file.getAbsolutePath());
    Sheet sheet = workbook.getSheetAt(0);

    /** 开始遍历Excel各范围数据 **/
    // 资金
    Map<String, String> zijinMap = getMapByPosition(sheet, 3, 3, 9);
    // 实时开销(可以实时金额那里拿)
    Map<String, String> presentPayoutMap = getMapByPosition(sheet, 13, 3, 40);// 不知够不够
    // 实时金额(单独开来)
    // Map<String,String> presentMoneyMap = getMapByPosition(sheet,3,6,1500);//不知1000够不够
    Map<String, String> presentMoneyMap = getMapByPosition_SSJE(sheet, 3, 6, 4000);
    log.info("导入实时金额总共记录数：" + presentMoneyMap.size());

    // 昨日利润
    Map<String, String> yesterdayProfitMap = getMapByPosition(sheet, 3, 11, 11);// 增加了总服务费、小游戏利润
    // 联盟对帐
    Map<String, String> LMMap = getMapByPosition(sheet, 15, 11, 16);

    /** 缓存到昨日留底总Map中 **/
    preDataMap.put("资金", JSON.toJSONString(zijinMap));
    preDataMap.put("实时开销", JSON.toJSONString(presentPayoutMap));
    preDataMap.put("实时金额", JSON.toJSONString(presentMoneyMap));
    preDataMap.put("昨日利润", JSON.toJSONString(yesterdayProfitMap));// 注意，不是整数LM
    preDataMap.put("联盟对帐", JSON.toJSONString(LMMap));//

    is.close();
    log.info("结束----------导入昨日留底Excel" + " ==size:" + presentMoneyMap.size());
    return preDataMap;
  }


  @SuppressWarnings("unused")
  private boolean isDuplicate = false;

  /**
   * 根据位置获取相应领域的Map值
   *
   * @param sheet 昨日留底的电子表
   * @param x 起始行（都0开始）
   * @param y 超始列
   * @param x2 结束行
   */
  private Map<String, String> getMapByPosition(Sheet sheet, int x, int y, int x2) throws Exception {
    Map<String, String> resultMap = new LinkedHashMap<>();
    String key = "";
    for (int i = x; i <= x2; i++) {
      Row row = sheet.getRow(i);
      if (row == null) {// 针对实时金额Map有读取到row为空的情况，这里是直接退出循环，不影响结果。后面要分析。
        break;
      }
      Cell keyCell = row.getCell(y);
      if (keyCell == null) {
        continue;
      }
      key = keyCell.toString();
      Cell valueCell = row.getCell(y + 1);
      if (valueCell == null) {
        if (resultMap.get(key) != null) {
          log.error("重复===================================" + key);
          throw new Exception("重复项：" + key + ",请检查！");
        }
        resultMap.put(key, "0");
        continue;
      }

      valueCell.setCellType(CellType.STRING);
      // 添加
      if (!StringUtil.isBlank(key) && !StringUtil.isBlank(key.trim())) {
        if (key.endsWith(".0")) {// 过滤诸如123被读成123.0的情况
          key = key.substring(0, key.lastIndexOf("."));
        }
        // if(key.endsWith("'")) {//过滤诸如123被读成123.0的情况
        // key = key.substring(0, key.lastIndexOf("'"));
        // }
        if (resultMap.get(key) != null) {
          log.error("重复===================================" + key);
          throw new Exception("重复项：" + key + ",请检查！");
        }
        resultMap.put(key, valueCell.getStringCellValue());
      }
    }
    return resultMap;
  }


  /**
   * 根据位置获取相应领域的Map值(实时金额)
   *
   * @param sheet 昨日留底的电子表
   * @param x 起始行（都0开始）
   * @param y 超始列
   * @param x2 结束行
   */
  private Map<String, String> getMapByPosition_SSJE(Sheet sheet, int x, int y, int x2)
      throws Exception {
//      Map<String,String> stringMap=new HashMap<>();
//      return stringMap;
    Map<String, String> resultMap = new HashMap<>();
    String playerName = "";
    String playerId = "";
    for (int i = x; i <= x2; i++) {
      Row row = sheet.getRow(i);
      if (row == null ) {// 针对实时金额Map有读取到row为空的情况，这里是直接退出循环，不影响结果。后面要分析。
        log.info("实时资金读取终止，行数：" + i);
        break;
      }
      Cell playerNameCell = row.getCell(y);
      if (playerNameCell == null) {
        continue;
      }
      playerName = playerNameCell.toString();
      Cell moneyCell = row.getCell(y + 1);
      if (moneyCell == null) {
        if (resultMap.get(playerName) != null) {
          log.error("重复===================================" + playerName);
          throw new Exception("重复项：" + playerName + ",请检查！");
        }
        CurrentMoneyInfo cmi = new CurrentMoneyInfo();
        cmi.setCreator("");
        cmi.setMingzi(playerName);
        cmi.setShishiJine("0");
        cmi.setWanjiaId("");
        cmi.setCmiEdu("");
        cmi.setColor("");
        cmi.setCmSuperIdSum("");
        cmi.setCmiLmb("0");
        resultMap.put(playerName, JSON.toJSONString(cmi));
        continue;
      }
      moneyCell.setCellType(CellType.STRING);

      String cmiLmb = "0";
      Cell lmbCell = row.getCell(y + 2);// ID列，即第I列
      if (lmbCell == null) {
        cmiLmb = "0";
      } else {
        lmbCell.setCellType(CellType.STRING);
        cmiLmb = StringUtils.defaultString(lmbCell.getStringCellValue(), "0");
      }

      Cell idCell = row.getCell(y + 3);// ID列，即第J列
      if (idCell == null) {
        playerId = "";
      } else {
        idCell.setCellType(CellType.STRING);
        playerId = idCell.getStringCellValue();
      }
      // 添加
      if (StringUtils.isNotBlank(playerName) && StringUtils.isNotBlank(playerName.trim())) {
        if (playerName.endsWith(".0")) {// 过滤诸如123被读成123.0的情况
          playerName = playerName.substring(0, playerName.lastIndexOf("."));
        }
        if (resultMap.get(playerName) != null) {
          log.error("重复===================================" + playerName);
          throw new Exception("重复项：" + playerName + ",请检查！");
        }
        String nameAndIdKey = playerName;
        if (StringUtils.isNotBlank(playerId)) {
          nameAndIdKey = playerName + "###" + playerId;
        }
        Player player = dataConstans.membersMap.get(playerId);
        String cmiEdu = "0";
        if (player != null) {
          cmiEdu = NumUtil.digit(player.getEdu());
        }
        CurrentMoneyInfo cmi = new CurrentMoneyInfo();
        cmi.setCreator("");
        cmi.setMingzi(playerName);
        cmi.setShishiJine(NumUtil.digit(moneyCell.getStringCellValue()));
        cmi.setWanjiaId(playerId);
        cmi.setCmiEdu(cmiEdu);
        cmi.setColor("");
        cmi.setCmSuperIdSum("");
        cmi.setCmiLmb(cmiLmb);
        resultMap.put(nameAndIdKey, JSON.toJSONString(cmi));
      } else {
        // 不做操作
      }
    }
    log.info("昨日留底实时金额数据数：" + resultMap.size());
    return resultMap;
  }


  /**
   * 导入合并ID模板Excel
   */
  public List<CombineID> readCombineIdRecord(File file) throws Exception {

    // 昨日留底总数据结构
    List<CombineID> combinIds = new ArrayList<>();

    if (!file.exists() || !file.isFile()) {
      return combinIds;
    }

    try (FileInputStream is = new FileInputStream(file)) {
      log.info("开始导入合并ID模板Excel");
      List<List<String>> originalList = MyExcelUtils.getInstance().readExcel2List(is, 1);
      if (CollectUtil.isEmpty(originalList)) {
        return combinIds;
      }

      // 转换成想要的数据结构
      Predicate<List<String>> filterBlank = e -> {
        return e != null && StringUtil.isAllNotBlank(e.get(0), e.get(1));
      };
      final String SPLIT = "#";
      originalList.stream().filter(filterBlank).forEach(e -> {
        String parentId = e.get(0);
        Set<String> subIdSet = new HashSet<>(Arrays.asList(e.get(1).split(SPLIT)));
        combinIds.add(new CombineID(parentId, subIdSet));
      });

      log.info("结束导入合并ID模板Excel" + " ==size:" + combinIds.size());
    } catch (Exception e) {
      throw e;
    }

    return combinIds;
  }


}
