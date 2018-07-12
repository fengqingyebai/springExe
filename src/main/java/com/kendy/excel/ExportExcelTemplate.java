package com.kendy.excel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.kendy.util.TimeUtil;

/**
 * 导出Excel模板
 * 
 * @author 林泽涛
 * @time 2018年3月31日 下午12:32:36
 */
public class ExportExcelTemplate {

  private static Logger log = Logger.getLogger(ExportExcelTemplate.class);

  private String title; // 标题

  private String[] rowName;// 列头

  private List<Integer> columnWidths = new ArrayList<>(); // 列宽

  private List<Object[]> dataList = new ArrayList<Object[]>(); // 数据源

  private String out = "D:/"; // 导出路径



  /**
   * @param title
   * @param rowName
   * @param columnWidths
   * @param dataList
   * @param out
   */
  public ExportExcelTemplate(String title, String[] rowName, List<Integer> columnWidths,
      List<Object[]> dataList, String out) {
    super();
    this.title = title;
    this.rowName = rowName;
    this.columnWidths = columnWidths;
    this.dataList = dataList;
    this.out = out;
  }

  public ExportExcelTemplate(String title, String[] rowName, List<Integer> columnWidths,
      List<Object[]> dataList) {
    super();
    this.title = title;
    this.rowName = rowName;
    this.columnWidths = columnWidths;
    this.dataList = dataList;
  }



  /*
   * 导出数据
   */
  public void export() throws Exception {
    try {
      HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
      HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

      // // 产生表格标题行
      // HSSFRow rowm = sheet.createRow(0);
      // HSSFCell cellTiltle = rowm.createCell(0);
      //
      //// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展
      CellStyle columnTopStyle = ExcelCss.getColumnTopStyle(workbook);// 获取列头样式对象
      HSSFCellStyle style = (HSSFCellStyle) ExcelCss.getStyle(workbook); // 单元格样式对象
      //
      // sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (rowName.length-1)));
      // cellTiltle.setCellStyle(columnTopStyle);
      // cellTiltle.setCellValue(title);
      /************************************* 正文 ****************************/

      // 定义所需列数
      int columnNum = rowName.length;
      HSSFRow rowRowName = sheet.createRow(1); // 在索引2的位置创建行(最顶端的行开始的第二行)

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
        HSSFRow row = sheet.createRow(i + 2);// 创建所需的行数

        HSSFCell cell = null; // 设置单元格的数据类型
        for (int j = 0; j < obj.length; j++) {
          cell = row.createCell(j, CellType.STRING);
          if (!"".equals(obj[j]) && obj[j] != null) {
            cell.setCellValue(obj[j].toString()); // 设置单元格的值
          }
          cell.setCellStyle(style); // 设置单元格样式
        }
      }

      // 让列宽随着导出的列长自动适应
      for (int colNum = 0; colNum < columnNum; colNum++) {
        int columnWidth = sheet.getColumnWidth(colNum) / 256;
        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
          HSSFRow currentRow;
          // 当前行未被使用过
          if (sheet.getRow(rowNum) == null) {
            currentRow = sheet.createRow(rowNum);
          } else {
            currentRow = sheet.getRow(rowNum);
            currentRow.setHeight((short) 400);
          }
        }
        // if(colNum == 0){
        // sheet.setColumnWidth(colNum, (columnWidth+6) * 256);
        // }else{
        // sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
        // }
        sheet.setColumnWidth(colNum, columnWidths.get(colNum));
      }

      if (workbook != null) {
        OutputStream out = null;
        try {
          File file = new File(this.out + title + ".xls");
          out = new FileOutputStream(file);
          workbook.write(out);
          java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (out != null)
            out.close();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }



  public static void main(String[] args) throws Exception {
    // String title = Message.getString("manifestIExportTitle");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String title = "自动上码-" + TimeUtil.getDateTime();
    System.out.println(title);
    // 团队ID 团队名字 比例 保险比例 股东 战绩是否代管理 备注 回水比例 回保比例 服务费判定 服务费判定
    String[] rowsName = new String[] {"时间", "玩家ID", "玩家名称", "牌局", "申请数量", "团队可上码", "计算可上码", "勾选团队",
        "当天", "次日", "同意审核", "审核结果"};
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    // for (int i = 0; i < manifestIMainList.size(); i++) {
    // ManifestIMain man = manifestIMainList.get(i);
    // objs = new Object[rowsName.length];
    // objs[0] = i;
    // objs[1] = man.getTranNo();
    // objs[2] = man.getBillNo();
    // objs[3] = man.getStatusFlagCnName();
    // objs[4] = man.getLoginName();
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // String date = df.format(man.getModiDate());
    // objs[5] = date;
    // dataList.add(objs);
    // }
    List<Integer> columnWidths =
        Arrays.asList(5000, 4000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3000, 3500);
    ExportExcelTemplate ex = new ExportExcelTemplate(title, rowsName, columnWidths, dataList);
    ex.export();
    System.out.println("finises..");
  }
}
