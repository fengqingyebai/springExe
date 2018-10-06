package com.kendy.excel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import com.kendy.util.StringUtil;

/**
 * 导出Excel公共方法
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:52:22
 */
public class ExportLMExcel {

  private static Logger log = Logger.getLogger(ExportLMExcel.class);

  // //显示的导出表的标题
  private String title;
  // 导出表的列名
  private String[] rowName;

  // 导出表的列名
  private String[] rowName2;

  private String out;


  private List<Object[]> dataList = new ArrayList<Object[]>();

  private List<Object[]> dataList2 = new ArrayList<Object[]>();
  private String YES = "√";

  private String NO = "✘";


  // 构造方法，传入要导出的数据
  public ExportLMExcel(String title, String[] rowName, List<Object[]> dataList, String outPath,
      String[] rowName2, List<Object[]> dataList2) {
    this.title = title;
    this.dataList = dataList;
    this.rowName = rowName;
    this.rowName2 = rowName2;
    this.dataList2 = dataList2;
    this.out = handleOutPath(outPath);
  }

  private static String handleOutPath(String outPath) {
    if (StringUtil.isNotBlank(outPath)) {
      outPath = outPath.replace("/", "").replace(":", ":/").replace("?", "");
      return outPath;
    }
    log.error("传入的Excel路径为空");
    return "D:/";
  }

  /*
   * 导出数据
   */
  @SuppressWarnings("deprecation")
  public void export() throws Exception {

    HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
    String _title = title.replace("/", "").replace("?", "");
    HSSFSheet sheet = workbook.createSheet(_title); // 创建工作表

    // 产生表格标题行
    HSSFRow rowm = sheet.createRow(0);
    HSSFCell cellTiltle = rowm.createCell(0);

    // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展
    CellStyle columnTopStyle = ExcelCss.getColumnTopStyle(workbook);// 获取列头样式对象
    CellStyle style = ExcelCss.getStyle(workbook); // 单元格样式对象

    sheet.addMergedRegion(new CellRangeAddress(0, 3, 0, (rowName.length - 1)));
    cellTiltle.setCellStyle(columnTopStyle);
    cellTiltle.setCellValue(title);
    /************************************* 正文 ****************************/
    // 时间：
    HSSFRow timeRow = sheet.createRow(1);
    HSSFCell timeLabel = timeRow.createCell(0);
    cellTiltle.setCellStyle(style);
    // 团队ID
    HSSFRow teamRow = sheet.createRow(2);

    // 定义所需列数
    int columnNum = rowName.length;
    HSSFRow rowRowNames = sheet.createRow(3); // 在索引2的位置创建行(最顶端的行开始的第二行)
    HSSFRow rowRowName = sheet.createRow(4); // 在索引2的位置创建行(最顶端的行开始的第二行)

    /************************************* 标题栏 ****************************/
    for (int n = 0; n < columnNum; n++) {
      HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
      cellRowName.setCellType(CellType.STRING); // 设置列头单元格的数据类型
      HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
      cellRowName.setCellValue(text); // 设置列头单元格的值
      cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
    }

    // 将查询出的数据设置到sheet对应的单元格中
    for (int i = 0; i < dataList.size(); i++) {

      Object[] obj = dataList.get(i);// 遍历每个对象
      HSSFRow row = sheet.createRow(i + 5);// 创建所需的行数

      for (int j = 0; j < obj.length; j++) {
        HSSFCell cell = null; // 设置单元格的数据类型
        cell = row.createCell(j, CellType.STRING);
        if (!"".equals(obj[j]) && obj[j] != null) {
          cell.setCellValue(obj[j].toString()); // 设置单元格的值
        }
        cell.setCellStyle(style); // 设置单元格样式
      }
    }

    /******************** 添加合计 ********/
    HSSFRow rr = sheet.getRow(1);
    HSSFCell cellHeji = rr.createCell(rowName.length + 1, CellType.STRING); // 创建列头对应个数的单元格
    HSSFRichTextString cellHejiVal = new HSSFRichTextString(rowName2[0]);
    cellHeji.setCellValue(cellHejiVal); // 设置列头单元格的值
    cellHeji.setCellStyle(columnTopStyle);
    HSSFCell cellSum = rr.createCell(rowName.length + 2, CellType.STRING); // 创建列头对应个数的单元格
    HSSFRichTextString sumVal = new HSSFRichTextString(rowName2[1]);
    cellSum.setCellValue(sumVal); // 设置列头单元格的值
    cellSum.setCellStyle(columnTopStyle);

    HSSFCell cellInsure = rr.createCell(rowName.length + 3, CellType.STRING); // 创建列头对应个数的单元格
    HSSFRichTextString sumInsure = new HSSFRichTextString(rowName2[2]);
    cellInsure.setCellValue(sumInsure); // 设置列头单元格的值
    cellInsure.setCellStyle(columnTopStyle);

    HSSFCell cellCount = rr.createCell(rowName.length + 4, CellType.STRING); // 创建列头对应个数的单元格
    HSSFRichTextString sumCount = new HSSFRichTextString(rowName2[3]);
    cellCount.setCellValue(sumCount); // 设置列头单元格的值
    cellCount.setCellStyle(columnTopStyle);

    for (int i = 0; i < dataList2.size(); i++) {

      HSSFRow r = sheet.getRow(i + 2);
      if (r == null) {
        log.error("excel行为空");
        continue;
      }
      HSSFCell type = r.createCell(rowName.length + 1, CellType.STRING);
      type.setCellStyle(style);
      type.setCellValue(dataList2.get(i)[0].toString());

      HSSFCell sum = r.createCell(rowName.length + 2, CellType.STRING);
      sum.setCellStyle(style);
      sum.setCellValue(dataList2.get(i)[1].toString());

      HSSFCell insure = r.createCell(rowName.length + 3, CellType.STRING);
      insure.setCellStyle(style);
      insure.setCellValue(dataList2.get(i)[2].toString());

      HSSFCell personCount = r.createCell(rowName.length + 4, CellType.STRING);
      personCount.setCellStyle(style);
      personCount.setCellValue(dataList2.get(i)[3].toString());
    }

    sheet.setColumnWidth(rowName.length + 1, 3500);
    sheet.setColumnWidth(rowName.length + 2, 3500);
    /****************************/

    if (workbook != null) {
      OutputStream out = null;
      try {
        File file = new File(this.out + ".xls");
        out = new FileOutputStream(file);
        workbook.write(out);
        java.awt.Desktop.getDesktop().open(file);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (out != null) {
          out.close();
        }
      }
    }

  }


}
