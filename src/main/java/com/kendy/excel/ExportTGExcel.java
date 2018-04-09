package com.kendy.excel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.kendy.entity.TGExcelModel;

/**
 * 导出托管Excel
 * 
 * @author 林泽涛
 * @time 2018年3月18日 下午11:03:18
 */
public class ExportTGExcel  {
	
	private static Logger log = Logger.getLogger(ExportTGExcel.class);
	
	private String out = "D:/";
	
	private List<TGExcelModel> excelList ;
	
    
	/**
	 * @param excelList
	 */
	public ExportTGExcel(List<TGExcelModel> excelList) {
		super();
		this.excelList = excelList;
	}
	
	
	
    /*
     * 导出数据
     * */
    @SuppressWarnings("deprecation")
	public void export() throws Exception{
        	
            HSSFWorkbook workbook = new HSSFWorkbook();                        // 创建工作簿对象
            
            for(TGExcelModel excelModel : excelList) {
            	
            	String sheetName = excelModel.getSheetName();
            	List<String> columnTitleList = excelModel.getColumnList();
            	List<Object[]> columnData = excelModel.getData();
            	
            	/******************************************************添加导出托管公司外债的信息******************************/
            	boolean isWaiZai = excelModel.getIsWaiZai();
            	
            	if(isWaiZai) {
            		setWaizhaiData(workbook, excelModel);
            		continue;
            	}
            	/************************************************************************************/
            	
            	HSSFSheet sheet = workbook.createSheet(sheetName);  
            
	            // 产生表格标题行
	            HSSFRow rowColumnName = sheet.createRow(0);
	            
	            // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展
	            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
	            HSSFCellStyle style = this.getStyle(workbook);                    //单元格样式对象
	            
	            /*************************************  标题栏  ****************************/
	            int columnNum = columnTitleList.size();
	            for(int n=0; n<columnNum; n++){
	                HSSFCell  cellName = rowColumnName.createCell(n);                //创建列头对应个数的单元格
	                cellName.setCellType(HSSFCell.CELL_TYPE_STRING);                //设置列头单元格的数据类型
	                HSSFRichTextString text = new HSSFRichTextString(columnTitleList.get(n));
	                cellName.setCellValue(text);                                    //设置列头单元格的值
	                cellName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
	            }
	            
	            
	            //将查询出的数据设置到sheet对应的单元格中
	            int dataSize = columnData.size();
	            for(int i=0; i<dataSize; i++){
	                
	                Object[] obj = columnData.get(i);//遍历每个对象
	                HSSFRow row = sheet.createRow(i+1);//创建所需的行数
	                
	                for(int j=0;  j<columnNum; j++){
	                    HSSFCell  cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
	                    if(!"".equals(obj[j]) && obj[j] != null){
	                        cell.setCellValue(obj[j].toString());                        //设置单元格的值
	                    }
	                    cell.setCellStyle(style);                                    //设置单元格样式
	                }
	            }
	            
	            /******************** 添加合计 ********/
	            List<String> columnSumList = excelModel.getColumnSumList();
	            List<Object[]> sumDataList = excelModel.getDataSum();
	        	
	        	int columnSumSize = sumDataList.size();
	            for(int i=0; i<columnSumSize; i++){
	                 
	            	 HSSFRow r = sheet.getRow(i+1);
	            	 if(r == null) {
	            		 r = sheet.createRow(i+1);
	            	 }
	                 HSSFCell type = r.createCell(columnNum+1,HSSFCell.CELL_TYPE_STRING);
	                 type.setCellStyle(style);
	                 type.setCellValue(sumDataList.get(i)[0].toString());
	                 
	                 HSSFCell sum = r.createCell(columnNum+2,HSSFCell.CELL_TYPE_STRING);
	                 sum.setCellStyle(style);
	                 sum.setCellValue(sumDataList.get(i)[1].toString());
	                 
	            }
	            
	            sheet.setColumnWidth(columnNum+1, 3500);
	            sheet.setColumnWidth(columnNum+2, 3500);
	            /****************************/
	            
	            
	            //让列宽随着导出的列长自动适应
	            for (int colNum = 0; colNum < columnNum; colNum++) {
	            	 sheet.setColumnWidth(colNum, 4000);
//	                if(colNum == 0){
//	                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
//	                }else{
//	                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
//	                }
	            }
	        }
            
	        if(workbook !=null){
	        	OutputStream out = null;
	            try{
	            	File file = new File(this.out+System.currentTimeMillis()+".xls");
	                out = new FileOutputStream(file);
	                workbook.write(out);
	                java.awt.Desktop.getDesktop().open(file);
	                System.out.println("==================================finishes...");
	            }catch (IOException e){
	                e.printStackTrace();
	            }finally {
	            	if(out != null) out.close();
	            }
	        }
        
    }
    
    /**
     * 设置外债的Excel数据
     * @time 2018年3月20日
     * @param workbook
     * @param excelModel
     */
    private void setWaizhaiData( HSSFWorkbook workbook, TGExcelModel excelModel) {
    	  HSSFSheet sheet = workbook.createSheet(excelModel.getSheetName());  
    	  Map<String, List<Object[]>> waizhaiMap = excelModel.getWaizhaiMap();
          
          HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
          HSSFCellStyle style = this.getStyle(workbook);                    //单元格样式对象
          
          
          int index = 1;//第几个表
    	  for(Map.Entry<String, List<Object[]>> entry : waizhaiMap.entrySet()) {
    		  
    		  HSSFRow r = sheet.getRow(1);
    		  if(r == null)  r = sheet.createRow(1);
    		  int start = (index -1) * 3;
    		  int end = (index -1) * 3 +1;
    		  //标题
    		  String teamString = entry.getKey();
    		  HSSFCell  cellName = r.createCell((index -1) * 3);                
              cellName.setCellType(HSSFCell.CELL_TYPE_STRING);               
              HSSFRichTextString text = new HSSFRichTextString(teamString.split("#")[0]);
              cellName.setCellValue(text);                                   
              cellName.setCellStyle(columnTopStyle);                       
              
    		  cellName = r.createCell((index -1) * 3 + 1);               
              cellName.setCellType(HSSFCell.CELL_TYPE_STRING);              
              text = new HSSFRichTextString(teamString.split("#")[1]);
              cellName.setCellValue(text);                                   
              cellName.setCellStyle(columnTopStyle);                        
    		  
    		  
    		  //外债明细 
              int i=1;
              List<Object[]> waizhaiList = entry.getValue();
              for(Object[] obj : waizhaiList) {
            	  HSSFRow row = sheet.getRow(i+1);
        		  if(row == null) {
        			  row = sheet.createRow(i+1);
        			  row.setHeight((short)400);
        		  }
            	  HSSFCell type = row.createCell(start  ,HSSFCell.CELL_TYPE_STRING);
            	  type.setCellStyle(style);
            	  type.setCellValue(obj[0].toString());
            	  
            	  HSSFCell sum = row.createCell(start+1, HSSFCell.CELL_TYPE_STRING);
            	  sum.setCellStyle(style);
            	  sum.setCellValue(obj[1].toString());
            	  
            	  sheet.setColumnWidth((i -1) * 3, 3500);
            	  sheet.setColumnWidth((i -1) * 3 + 1, 3500);
            	  i+=1;
              }
              index += 1;
    		  
    	  }
               
    }
    
    /* 
     * 列头单元格样式
     */    
      public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
          
            // 设置字体
          HSSFFont font = workbook.createFont();
          //设置字体大小
          font.setFontHeightInPoints((short)11);
          //字体加粗
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          //设置字体名字 
//          font.setFontName("Courier New");
          //设置样式; 
          HSSFCellStyle style = workbook.createCellStyle();
          //设置底边框; 
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
          //设置底边框颜色;  
          style.setBottomBorderColor(HSSFColor.BLACK.index);
          //设置左边框;   
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
          //设置左边框颜色; 
          style.setLeftBorderColor(HSSFColor.BLACK.index);
          //设置右边框; 
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);
          //设置右边框颜色; 
          style.setRightBorderColor(HSSFColor.BLACK.index);
          //设置顶边框; 
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);
          //设置顶边框颜色;  
          style.setTopBorderColor(HSSFColor.BLACK.index);
          //在样式用应用设置的字体;  
          style.setFont(font);
          //设置自动换行; 
          style.setWrapText(false);
          //设置水平对齐的样式为居中对齐;  
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
          //设置垂直对齐的样式为居中对齐; 
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
          
          return style;
          
      }
      
      /*  
     * 列数据信息单元格样式
     */  
      public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
            // 设置字体
            HSSFFont font = workbook.createFont();
            //设置字体大小
            //font.setFontHeightInPoints((short)10);
            //字体加粗
            //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //设置字体名字 
//            font.setFontName("Courier New");
            //设置样式; 
            HSSFCellStyle style = workbook.createCellStyle();
            //设置底边框; 
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            //设置底边框颜色;  
            style.setBottomBorderColor(HSSFColor.BLACK.index);
            //设置左边框;   
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //设置左边框颜色; 
            style.setLeftBorderColor(HSSFColor.BLACK.index);
            //设置右边框; 
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            //设置右边框颜色; 
            style.setRightBorderColor(HSSFColor.BLACK.index);
            //设置顶边框; 
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            //设置顶边框颜色;  
            style.setTopBorderColor(HSSFColor.BLACK.index);
            //在样式用应用设置的字体;  
            style.setFont(font);
            //设置自动换行; 
            style.setWrapText(false);
            //设置水平对齐的样式为居中对齐;  
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //设置垂直对齐的样式为居中对齐; 
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            return style;
      }


      /**
       * 程序测试入口
       * 
       * @time 2018年3月18日
       * @param strings
       * @throws Exception
       */
      public static void main(String... strings) throws Exception{
      	List<TGExcelModel> exportList = new ArrayList<>();
      	TGExcelModel excelModel = new TGExcelModel();
      	
      	List<String>  sumTitleList = new ArrayList<>();
      	sumTitleList.add("类型");
      	sumTitleList.add("值");
      	List<Object[]> sumDataList = new ArrayList<>();
      	Object[] sumDataObj = new Object[2];
      	sumDataObj[0] = "类型1";
      	sumDataObj[1] = "值1";
      	sumDataList.add(sumDataObj);
      	
      	Object[] sumDataObj2 = new Object[2];
      	sumDataObj2[0] = "类型2";
      	sumDataObj2[1] = "值2";
      	sumDataList.add(sumDataObj2);
      	excelModel.setColumnSumList(sumTitleList);
      	excelModel.setDataSum(sumDataList);
      	
      	
      	List<String>  columnList = new ArrayList<>();
      	columnList.add("列标题 ");
      	List<Object[]> dataList = new ArrayList<>();
      	Object[] obj = new Object[1];
      	obj[0] = "1";
      	dataList.add(obj);
      	excelModel.setSheetName("测试");
      	excelModel.setColumnList(columnList);
      	excelModel.setData(dataList);
      	exportList.add(excelModel);
      	
      	excelModel = new TGExcelModel();
      	columnList = new ArrayList<>();
      	columnList.add("列标题2 ");
      	dataList = new ArrayList<>();
      	obj = new Object[1];
      	obj[0] = "1";
      	dataList.add(obj);
      	excelModel.setSheetName("测试2");
      	excelModel.setColumnList(columnList);
      	excelModel.setData(dataList);
      	exportList.add(excelModel);
      	
      	
      	ExportTGExcel excelCreator = new ExportTGExcel(exportList);
      	excelCreator.export();
      	
      }
      
      
}