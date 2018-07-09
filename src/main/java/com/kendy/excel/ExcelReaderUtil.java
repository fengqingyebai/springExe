package com.kendy.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.kendy.controller.LMController;
import com.kendy.entity.Huishui;
import com.kendy.entity.Player;
import com.kendy.excel.excel4j.ExcelUtils;
import com.kendy.model.GameRecord;
import com.kendy.other.Wrap;
import com.kendy.service.MoneyService;
import com.kendy.util.CollectUtil;
import com.kendy.util.FileUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.PathUtil;
import com.kendy.util.ShowUtil;
import com.kendy.util.StringUtil;

import application.DataConstans;

/**
 * 读取Excel
 * @author 林泽涛
 * @time 2017年10月7日 下午3:54:05
 */
public class ExcelReaderUtil {
	
	private static Logger log = Logger.getLogger(ExcelReaderUtil.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 测试本工具类
	 */
	public static void main(String[] args) throws IOException, Exception{
		
		String excelPath = PathUtil.getUserDeskPath() + "/战绩导出-24-299.xls";
		List<GameRecord> basicRecords = ExcelUtils.getInstance().readExcel2Objects(excelPath, GameRecord.class, 1, 0);
		
		
		
		
        log.info("finishes..." + basicRecords.size());
	}
	
	
	/**
	 * 创建 Excel 2003 或2007工作簿对象
	 * @time 2017年10月7日
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWeebWork(String filename) throws Exception {
		Workbook workbook = null;
		if (null != filename) {
			String fileType = filename.substring(filename.lastIndexOf("."),
					filename.length());
			FileInputStream fileStream = new FileInputStream(new File(filename));
			if (".xls".equals(fileType.trim().toLowerCase())) {
				workbook = new HSSFWorkbook(fileStream);// 创建 Excel 2003 工作簿对象
			} else if (".xlsx".equals(fileType.trim().toLowerCase())) {
				workbook = new XSSFWorkbook(fileStream);// 创建 Excel 2007 工作簿对象
				//workbook = WorkbookFactory.create(fileStream);
			}
		}
		return workbook;
	}
	
	/**
	 * 导入人员名单
	 * 
	 * @time 2017年10月7日
	 * @param file
	 * @return
	 */
	public static Map<String,Player> readMembersRecord(File file) throws Exception{
		
		Map<String,Player> membersMap = new LinkedHashMap<>();
		Player player ;
		log.info("读取人员名单开始...");
		try(
			FileInputStream is = new FileInputStream(file)
		){
            Workbook workbook = getWeebWork(file.getAbsolutePath());
            Sheet sheet = workbook.getSheetAt(0);
 
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if(row.getRowNum()!=0){
                	//游戏id
                	String gameId = getCellValue(row.getCell(0));
                	gameId = StringUtil.nvl(gameId,"");
                	//股东名称
                	String gudong = getCellValue(row.getCell(1));
                	gudong = StringUtil.nvl(gudong,"");
                	//团队名称
                	String team = getCellValue(row.getCell(2));
                	team = StringUtil.nvl(team,"");
                	//玩家名称
                	String playerName = getCellValue(row.getCell(3));
                	playerName = StringUtil.nvl(playerName,"");
                	//备注
                	String beizhu = getCellValue(row.getCell(4));
                	if(!StringUtil.isBlank(gameId)){
                		try {
							gameId =  new BigDecimal(gameId).toPlainString();//不要科学记数法
						} catch (Exception e) {
							gameId = getCellValue(row.getCell(0));
						}
                		gudong = StringUtil.isBlank(gudong) ? "W" : gudong;//股东为空的默认设置为W
                		player = new Player(gameId,team,gudong,playerName,beizhu);
                		membersMap.put(gameId, player);
                	}
                }
            }
        } 
		log.info("读取人员名单结束! size:"+membersMap.size());
		return membersMap;
	}
	
	private static String getCellValue(Cell cell){
		if(cell == null){
			return "";
		}else{
			cell.setCellType(CellType.STRING);
			return cell.toString();
		}
	}
	
	/**
	 * 导入回水
	 * @param file
	 * @return
	 */
	public static Wrap readHuishuiRecord(File file){
		
		Map<String,Huishui> huishuiMap = new HashMap<>();
		Wrap wrap = new Wrap();
		try{
			FileInputStream is = new FileInputStream(file);
			log.info("读取团队回水开始...");
			
			Workbook workbook = getWeebWork(file.getAbsolutePath());
			Sheet sheet = workbook.getSheetAt(0);//回水对应的sheet
			
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if(row.getRowNum()>=2){
					//团队id
					String teamId = "";
					//团队名称
					String teamName = "";
					//回水比例
					String huishuiRate = "";
					//备注
					String beizhu = "";
					//保险比例
					String insuranceRate = "";
					//股东
					String gudong = "";
					//是否代理
					String zjManage = "";
					//代理回水比例
					String proxyHsRate = "";
					//代理回保比例
					String proxyHbRate = "";
					//代理服务费
					String proxyFWF = "";
					//积分比例
					String jfPercent = "";
					for(int i=1;i<11;i++){
						Cell cell = row.getCell(i);
						cell.setCellType(CellType.STRING);
						String value = cell.getStringCellValue();
						if(i==1){
							//团ID====="+value);
							if(!StringUtil.isBlank(value))
								value = value.toUpperCase();
							teamId = value;
						}else if(i==2){
							//团队名字====="+value);
							teamName = StringUtil.isBlank(value) ? "" : value;
						}else if(i==3){
							if(!StringUtil.isBlank(value) && value.contains("E")) {
								value = Double.valueOf(value)+"";
							}
							//回水比例====="+value);
							if(value.contains("%")) {
								value = NumUtil.digit4(NumUtil.getNumByPercent(value)+"");
							}
							huishuiRate = value;
						}else if(i==4){
							//保险比例====="+value);
							if(value.contains("%")) {
								value = NumUtil.getNumByPercent(value)+"";
							}
							insuranceRate = StringUtil.isBlank(value) ? "0" : NumUtil.digit4(value);
						}else if(i==5){
							//股东====="+value);
							gudong = value;
						}else if(i==6){
							//战绩是否代理管====="+value);
							zjManage = StringUtil.isBlank(value) ? "否" : value;
						}else if(i==7){
							//备注====="+value);
							beizhu = value;
						}else if(i==8) {
							if(StringUtil.isBlank(value)) {
								value = "0";
							}
							if(value.contains("%")) {
								proxyHsRate = value;
							}else {
								//回水比例====="+NumUtil.getPercentStr(Double.valueOf(value)));
								proxyHsRate = NumUtil.getPercentStr(Double.valueOf(value));
							}
						}else if(i==9) {
							if(StringUtil.isBlank(value)) {
								value = "0";
							}
							if(value.contains("%")) {
								proxyHbRate = value;
							}else {
								//回保比例====="+NumUtil.getPercentStr(Double.valueOf(value)));
								proxyHbRate = NumUtil.getPercentStr(Double.valueOf(value));
							}
						}else if(i==10) {
							//服务费====="+value);
							proxyFWF = value;
						}
						else if(i==11) {
							//积分比例====="+value);
							if(StringUtil.isBlank(value)) {
								value = "0";
							}
							jfPercent = NumUtil.getPercentStr(Double.valueOf(value));
						}
					}
					if(!StringUtil.isBlank(teamId))
						huishuiMap.put(teamId, new Huishui(teamId,teamName,huishuiRate,insuranceRate,gudong,zjManage,beizhu,proxyHsRate,proxyHbRate,proxyFWF));
				}
			}
			is.close();
			wrap = new Wrap(true,huishuiMap);
			log.info("读取团队回水结束!size:"+huishuiMap.size());
		} catch (Exception e) {
			wrap = new Wrap();
			e.printStackTrace();
		}
		return wrap;
	}
	
	/**
	 * 读取新-旧战绩
	 * @time 2018年1月10日
	 * @param file
	 * @param userClubId
	 * @param LMType
	 * @param versionType
	 * @return
	 */
	public static List<GameRecord> readZJRecord(String excelFilePath,String userClubId,String LMType, int versionType) throws Exception{
		if (0 == versionType) {
			return null ; 
		} else {
			return readZJRecord_NewVersion(excelFilePath, userClubId,LMType);
		}
	}
	
	
	
	
	/*
	 * 判断总手数为0
	 */
	private static void judgeSumHandsCount(List<GameRecord> gameRecords) {
		boolean isSumHandsCountZero = gameRecords.stream().allMatch(record -> "0".equals(record.getSumHandsCount()));
		if (isSumHandsCountZero || CollectUtil.isNullOrEmpty(gameRecords)) {
			ShowUtil.show("提示：总手数为0！");
		}
	}
	
	/**
	 * 导入战绩Excel
	 * 
	 * 两个功能：
	 * 	A:导入到场次信息
	 * 	B:导入到联盟Tab
	 */
	public static List<GameRecord> readZJRecord_NewVersion(String excelFilePath, String userClubId, String LMType) throws Exception{//新增了联盟类型
		
		//获取所有记录
		List<GameRecord> gameRecords = ExcelUtils.getInstance().readExcel2Objects(excelFilePath, GameRecord.class, 1, 0);
		String tableId = FileUtil.getTableId(excelFilePath);
		//补全每条记录的值
		MoneyService.fillGameRecords(gameRecords, tableId, LMType);
		//判断总手数为0
		judgeSumHandsCount(gameRecords);
		
		// TODO 添加所有记录到联盟对帐表final?
		LMController.currentRecordList = gameRecords;
		
		//只返回当前俱乐部的记录
		List<GameRecord> _gameRecords = gameRecords.stream().filter(e->e.getClubId().equals(userClubId)).collect(Collectors.toList());
		
		// 存储数据  {场次=infoList...}
		DataConstans.zjMap.put(tableId, _gameRecords);
		
		return _gameRecords;
	}
	
	
	
	
	
	/**
	 * 导入昨日留底Excel
	 * @throws IOException,Exception 
	 */
	public static Map<String,String> readPreDataRecord(File file) throws IOException,Exception{
		
		//昨日留底总数据结构
		Map<String,String> preDataMap = new HashMap<>();
		
		FileInputStream is = null;
			
		if(!file.exists() || !file.isFile() ){
			return new HashMap<>();
		}
		log.info("开始----------导入昨日留底Excel");
		
		is = new FileInputStream(file);
		
		//获取excel sheet
		Workbook workbook = getWeebWork(file.getAbsolutePath());
		Sheet sheet = workbook.getSheetAt(0);
		
		/**开始遍历Excel各范围数据**/
		//资金
		Map<String,String> zijinMap = getMapByPosition(sheet,3,3,9);
		//实时开销(可以实时金额那里拿)
		Map<String,String> presentPayoutMap = getMapByPosition(sheet,13,3,40);//不知够不够
		//实时金额(单独开来)
//		Map<String,String> presentMoneyMap = getMapByPosition(sheet,3,6,1500);//不知1000够不够
		Map<String,String> presentMoneyMap = getMapByPosition_SSJE(sheet,3,6,1500);
		
		//昨日利润
		Map<String,String> yesterdayProfitMap = getMapByPosition(sheet,3,11,10);//增加了总服务费
		//联盟对帐
		Map<String,String> LMMap = getMapByPosition(sheet,15,11,16);
		
		/**缓存到昨日留底总Map中**/
		preDataMap.put("资金", JSON.toJSONString(zijinMap));
		preDataMap.put("实时开销", JSON.toJSONString(presentPayoutMap));
		preDataMap.put("实时金额", JSON.toJSONString(presentMoneyMap));
		preDataMap.put("昨日利润", JSON.toJSONString(yesterdayProfitMap));//注意，不是整数LM
		preDataMap.put("联盟对帐", JSON.toJSONString(LMMap));//
		
		is.close();
        log.info("结束----------导入昨日留底Excel"+" ==size:"+preDataMap.size());
		return preDataMap;
	}
	
	
	@SuppressWarnings("unused")
	private static boolean isDuplicate = false;
	/**
	 * 根据位置获取相应领域的Map值
	 * @param sheet 昨日留底的电子表
	 * @param x 起始行（都0开始）
	 * @param y 超始列
	 * @param x2 结束行 
	 * @throws Exception 
	 * 
	 */
	private static Map<String,String> getMapByPosition(Sheet sheet,int x,int y,int x2) throws Exception{
		Map<String,String> resultMap = new LinkedHashMap<>();
		String key = "";
		for(int i=x; i<=x2; i++){
			Row row = sheet.getRow(i);
			if(row == null){//针对实时金额Map有读取到row为空的情况，这里是直接退出循环，不影响结果。后面要分析。
				break;
			}
			Cell keyCell = row.getCell(y);
			if(keyCell == null) {
				continue;
			}
			key = keyCell.toString();
			Cell valueCell = row.getCell(y+1);
			if(valueCell ==  null) {
				if(resultMap.get(key) != null ) {
					log.error("重复==================================="+key);
					throw new Exception("重复项："+key+",请检查！");
				}
				resultMap.put(key, "0");
				continue;
			}
		
			valueCell.setCellType(CellType.STRING);
			//添加
			if(!StringUtil.isBlank(key) && !StringUtil.isBlank(key.trim())){
				if(key.endsWith(".0")) {//过滤诸如123被读成123.0的情况
					key = key.substring(0, key.lastIndexOf("."));
				}
//				if(key.endsWith("'")) {//过滤诸如123被读成123.0的情况
//					key = key.substring(0, key.lastIndexOf("'"));
//				}
				if(resultMap.get(key) != null ) {
					log.error("重复==================================="+key);
					throw new Exception("重复项："+key+",请检查！");
				}
				resultMap.put(key, valueCell.getStringCellValue());
			}
		}
		return resultMap;
	}
	
	
	
	/**
	 * 根据位置获取相应领域的Map值(实时金额)
	 * @param sheet 昨日留底的电子表
	 * @param x 起始行（都0开始）
	 * @param y 超始列
	 * @param x2 结束行 
	 * @throws Exception 
	 * 
	 */
	private static Map<String,String> getMapByPosition_SSJE(Sheet sheet,int x,int y,int x2) throws Exception{
		Map<String,String> resultMap = new LinkedHashMap<>();
		String key = "";
		String playerId = "";
		for(int i=x; i<=x2; i++){
			Row row = sheet.getRow(i);
			if(row == null){//针对实时金额Map有读取到row为空的情况，这里是直接退出循环，不影响结果。后面要分析。
				break;
			}
			Cell keyCell = row.getCell(y);
			if(keyCell == null) {
				continue;
			}
			key = keyCell.toString();
			Cell valueCell = row.getCell(y+1);
			if(valueCell ==  null) {
				if(resultMap.get(key) != null ) {
					log.error("重复==================================="+key);
					throw new Exception("重复项："+key+",请检查！");
				}
				resultMap.put(key, "0");
				continue;
			}
			valueCell.setCellType(CellType.STRING);
			
			Cell idCell = row.getCell(y+3);//ID列，即第J列
			if(idCell == null) {
				playerId = "";
			}else {
				idCell.setCellType(CellType.STRING);
				playerId = idCell.getStringCellValue();
			}
			//添加
			if(!StringUtil.isBlank(key) && !StringUtil.isBlank(key.trim())){
				if(key.endsWith(".0")) {//过滤诸如123被读成123.0的情况
					key = key.substring(0, key.lastIndexOf("."));
				}
//				if(key.endsWith("'")) {//过滤诸如123被读成123.0的情况
//					key = key.substring(0, key.lastIndexOf("'"));
//				}
				if(resultMap.get(key) != null ) {
					log.error("重复==================================="+key);
					throw new Exception("重复项："+key+",请检查！");
				}
				if(!StringUtil.isBlank(playerId)) {
					key = key + "###" + playerId;
				}
				resultMap.put(key, valueCell.getStringCellValue());
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 导入合并ID模板Excel
	 * @throws IOException,Exception 
	 */
	public static Map<String,String> readCombineIdRecord(File file) throws IOException,Exception{
		
		//昨日留底总数据结构
		Map<String,String> combineIdMap = new LinkedHashMap<>();
		
		FileInputStream is = null;
			
		if(!file.exists() || !file.isFile() ){
			return new HashMap<>();
		}
		log.info("开始----------导入合并ID模板Excel");
		
		is = new FileInputStream(file);
		
		//获取excel sheet
		Workbook workbook = getWeebWork(file.getAbsolutePath());
		Sheet sheet = workbook.getSheetAt(0);
		
		//检验是否合并ID模板
		Row firstRow = sheet.getRow(0);
		if(firstRow == null || firstRow.getCell(0) == null 
				|| !"父ID".equals(firstRow.getCell(0).getStringCellValue())) {
			return combineIdMap;
		}
		
		/**遍历Excel开始**/
		Row row ;
		for(int i=1; i< 1000; i++){
			
			row = sheet.getRow(i);
			if(row == null) break;
			
			
			if(row.getCell(0) == null || StringUtil.isBlank(row.getCell(0).toString()) 
					|| row.getCell(1) == null || StringUtil.isBlank(row.getCell(1).toString()))
				new Exception("父ID或子ID列表不能为空，请检查！");
			
			row.getCell(0).setCellType(CellType.STRING);
			row.getCell(1).setCellType(CellType.STRING);
			combineIdMap.put(row.getCell(0).toString(), row.getCell(1).toString());
		}
		
		/**遍历Excel结束**/
		
		is.close();
        log.info("结束----------导入合并ID模板Excel"+" ==size:"+combineIdMap.size());
		return combineIdMap;
	}
	
	

}
