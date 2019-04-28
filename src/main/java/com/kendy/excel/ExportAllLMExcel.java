package com.kendy.excel;

import com.kendy.entity.Club;
import com.kendy.entity.LMSumInfo;
import com.kendy.util.ErrorUtil;
import com.kendy.util.TimeUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导出联盟总帐Excel
 *
 * @author simenen
 * @time 2017年11月30日 下午9:42:22
 */
public class ExportAllLMExcel {

  private static Logger log =  LoggerFactory.getLogger(ExportAllLMExcel.class);

  // 模板路径
  private static final String TEMPLE_PATH = "联盟总账模板.xls";

  // 输出路径
  private String alllm_output_path = "D:/联盟总账-";

  // 联盟总帐统计结果Map
  private Map<String, List<LMSumInfo>> allClubSumMap = null;

  // 俱乐部信息
  private Map<String, Club> allClubMap = null;

  // 合计桌费
  private Integer sumOfZF = 0;

  // 联盟
  private Map<String, String> sortMap = null;

  /**
   * 构造方法
   */
  public ExportAllLMExcel() {

  }

  /**
   * 初始化excel基础信息
   *
   * @param allClubSumMap 联盟总帐统计结果集
   * @param allClubMap 用于获取俱乐部名称
   * @param sumOfZF 合计桌费
   */
  public ExportAllLMExcel(Map<String, List<LMSumInfo>> allClubSumMap, Map<String, Club> allClubMap,
      Integer sumOfZF) {
    // 输出文件路径
    this.allClubMap = allClubMap;
    this.allClubSumMap = allClubSumMap;
    this.sumOfZF = sumOfZF;
  }

  /**
   * 初始化excel基础信息
   *
   * @param allClubSumMap 联盟总帐统计结果集
   * @param allClubMap 用于获取俱乐部名称
   * @param sumOfZF 合计桌费 param sortMap lmsumname排序
   */
  public ExportAllLMExcel(Map<String, List<LMSumInfo>> allClubSumMap, Map<String, Club> allClubMap,
      Integer sumOfZF, Map<String, String> sortMap) {
    // 输出文件路径
    this.allClubMap = allClubMap;
    this.allClubSumMap = allClubSumMap;
    this.sumOfZF = sumOfZF;
    this.sortMap = sortMap;
  }

  /**
   * 加载初始化表格数据联盟信息排序数据
   */
  @SuppressWarnings("serial")
  private void loadDefaultSort() {
    sortMap = new HashMap<String, String>() {
      {
        put("玩家战绩", "0");
        put("桌费", "1");
        put("当天总帐", "2");
      }
    };
  }

  /**
   * 导出excel
   *
   * @param allClubSumMap 联盟总帐统计结果集
   * @param allClubMap 用于获取俱乐部名称
   * @param sumOfZF 合计桌费
   */
  public boolean export(Map<String, List<LMSumInfo>> allClubSumMap, Map<String, Club> allClubMap,
      Integer sumOfZF) throws IOException {
    this.allClubMap = allClubMap;
    this.allClubSumMap = allClubSumMap;
    this.sumOfZF = sumOfZF;
    return export();
  }

  /**
   * 导出excel
   */
  public boolean export() {
    // 如果没有加载过联盟数据排序信息，则加载默认排序信息
    if (sortMap == null || sortMap.size() <= 0) {
      loadDefaultSort();
    }
    try {
      // 加载模板文件
      Workbook templatewb = loadTemple();
      // 加载数据Map
      Map<String, String> dataMap = loadData();
      // 生成excel
      templatewb = convertExcel(templatewb, dataMap);
      // 保存excel并打开
      ouputExcel(templatewb);

      return true;
    } catch (Exception e) {
      ErrorUtil.err("联盟总账导出Excel失败", e);
    }

    return false;
  }

  /**
   * 加载数据，将数据整合成模板所需的key值
   *
   * @return Map 用于替换Excel模板中的数据
   */
  private Map<String, String> loadData() {
    Map<String, String> map = new HashMap<String, String>();
    int count = 0;

    for (String clubId : allClubSumMap.keySet()) {
      // 联盟信息
      List<LMSumInfo> lmSumInfos = allClubSumMap.get(clubId);
      String clubName = allClubMap.containsKey(clubId) ? allClubMap.get(clubId).getName() : "";
      map.put("clubName" + count, clubName);
      // 联盟数据，根据排序sortMap顺序瓶装Excel所需的key值
      for (LMSumInfo lmSumInfo : lmSumInfos) {
        String infoCount = sortMap.get(lmSumInfo.getLmSumName());
        map.put("lmSumInsure_" + count + "_" + infoCount, lmSumInfo.getLmSumInsure());
        map.put("lmSumName_" + count + "_" + infoCount, lmSumInfo.getLmSumName());
        map.put("lmSumPersonCount_" + count + "_" + infoCount, lmSumInfo.getLmSumPersonCount());
        map.put("lmSumZJ_" + count + "_" + infoCount, lmSumInfo.getLmSumZJ());
      }
      count++;
    }
    map.put("total", String.valueOf(sumOfZF));
    log.info("加载联盟总帐Excel数据到Map完成");
    return map;
  }

  /**
   * 将workbook输出到Excel文件中并打开
   */
  private void ouputExcel(Workbook templatewb) throws IOException {
    alllm_output_path += TimeUtil.getDateTime() + ".xls";
    File file = new File(alllm_output_path);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream fos = new FileOutputStream(file);
    templatewb.write(fos);
    fos.close();
    templatewb.close();
    java.awt.Desktop.getDesktop().open(file);
    log.info("生成联盟总帐Excel文件并打开");
  }

  /**
   * 根据dataMap 填充 Workbook
   */
  private static Workbook convertExcel(Workbook templatewb, Map<String, String> dataMap) {
    log.info("开始填充联盟总帐Excel模板数据");
    Sheet sheet = templatewb.getSheetAt(0);
    // 总行数
    int trLength = sheet.getLastRowNum();
    // 得到Excel工作表的行
    Row row = sheet.getRow(0);
    // 总列数
    int tdLength = row.getLastCellNum();
    Pattern pattern = Pattern.compile("\\{[^\\}]{1,}\\}");
    Matcher matcher = null;
    Pattern numpattern = Pattern.compile("^[-\\+]?[\\d]*$");

    for (int i = 0; i < trLength; i++) {
      // 得到Excel工作表的行
      Row row1 = sheet.getRow(i);
      for (int j = 0; j < tdLength; j++) {
        // 得到Excel工作表指定行的单元格
        Cell cell = row1.getCell(j);

        if (cell == null) {
          log.info("================cell 为空，行：{}, 列：{}", i+"", j+"");
          continue;
        }


        if (cell.getStringCellValue() != null && cell.getStringCellValue().length() > 0) {
          matcher = pattern.matcher(cell.getStringCellValue());
          if (matcher.find()) {
            // 匹配到的字符串
            String regexStr = matcher.group();
            // 获取单元格数据
            String cellvalue = cell.getStringCellValue();
            // excel数据dataMap中的key值
            String key = regexStr.substring(1, regexStr.length() - 1);
            // 当前单元格应该填充的数据
            String value = dataMap.get(key);
            // 替换单元格数据
            cellvalue =
                cellvalue.replace(regexStr, (cellvalue == null || value == null) ? "" : value);
            if (cellvalue != null && cellvalue.length() > 0
                && numpattern.matcher(cellvalue).matches()) {
              cell.setCellValue(Double.parseDouble(cellvalue));
            } else {
              cell.setCellValue(cellvalue);
            }
          }
        }
      }
    }
    return templatewb;
  }

  public void setSortMap(Map<String, String> sortMap) {
    this.sortMap = sortMap;
  }

  /**
   * 加载Excel模板
   */
  private Workbook loadTemple() throws IOException {
    log.info("加载联盟总帐Excel模板...");
    InputStream is = ExportAllLMExcel.class.getResource("/excel/联盟总账模板.xls").openStream();
    Workbook templatewb = new HSSFWorkbook(is);
    return templatewb;
  }

  public static void main(String[] args) {
    log.info("加载联盟总帐Excel模板");
    try {
      InputStream is = ExportAllLMExcel.class.getResource("/excel/联盟总账模板.xls")
          .openStream();// 生产环境可用
      Workbook templatewb = new HSSFWorkbook(is);
      System.out.println("finishes..." + templatewb == null ? "失败" : "成功");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
