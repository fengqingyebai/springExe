package com.kendy.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 实时上码导出
 * 
 * @author 林泽涛
 * @time 2017年12月20日 下午9:02:55
 */
public class ExportShangmaExcel {

	private static Logger log = Logger.getLogger(ExportShangmaExcel.class);

	// 标题
	private String title;
	// 列名
	private String[] rowName;
	// 输出
	private String out;
	// 数据
	private List<Object[]> dataList = new ArrayList<Object[]>();

	/**
	 * 构造方法
	 * 传入要导出的数据
	 * 
	 * @param title 标题
	 * @param rowName 列名数组
	 * @param dataList 数据源
	 * @param out 输出
	 */
	public ExportShangmaExcel(String title, String[] rowName, List<Object[]> dataList, String out) {
		this.title = title;
		this.dataList = dataList;
		this.rowName = rowName;
		this.out = out;
	}

	/*
	 * 导出数据
	 */
	public void export() throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
			HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

			//产生表格标题行
			 HSSFRow rowm = sheet.createRow(0);
			 HSSFCell cellTiltle = rowm.createCell(0);
			
			//// sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展
			HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
			HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象
			/************************************* 正文 ****************************/

			// 定义所需列数
			int columnNum = rowName.length;
			HSSFRow rowRowName = sheet.createRow(1); // 在索引2的位置创建行(最顶端的行开始的第二行)

			/************************************* 标题栏 ****************************/
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell(n ); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}

			/********将查询出的数据设置到sheet对应的单元格中*******************/
			for (int i = 0; i < dataList.size(); i++) {

				Object[] obj = dataList.get(i);// 遍历每个对象
				HSSFRow row = sheet.createRow(i + 2);// 创建所需的行数

				HSSFCell cell = null; // 设置单元格的数据类型
				for (int j = 0; j < obj.length; j++) {
					cell = row.createCell(j , HSSFCell.CELL_TYPE_STRING);
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
				switch(colNum) {
				case 0 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 1 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 2 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 3 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 4 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 5 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				case 6 :sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				default:sheet.setColumnWidth(colNum, (columnWidth + 1) * 400);break;
				}
			}

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
					if (out != null)
						out.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 列头单元格样式
	 */
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		// font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;

	}

	/*
	 * 列数据信息单元格样式
	 */
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		// font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

}