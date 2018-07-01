package com.kendy.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import com.kendy.excel.excel4j.ExcelUtils;
import com.kendy.util.PathUtil;


public class ExportExcelTest {

	public static void main(String[] args) throws Exception {
		testObject2Excel();
		System.out.println("finishes...");
	}
	
    public static void testObject2Excel() throws Exception {

        List<Student1> list = new ArrayList<>();
        list.add(new Student1("1010001", "盖伦", "六年级三班"));
        list.add(new Student1("1010002", "古尔丹", "一年级三班"));
        list.add(new Student1("1010003", "蒙多(被开除了)", "六年级一班449646546549846531654"));
        list.add(new Student1("1010004", "萝卜特", "三年级二班"));
        list.add(new Student1("1010005", "奥拉基", "三年级二班"));
        list.add(new Student1("1010006", "得嘞", "四年级二班"));
        list.add(new Student1("1010007", "瓜娃子", "五年级一班"));
        list.add(new Student1("1010008", "战三", "二年级一班"));
        list.add(new Student1("1010009", "李四", "一年级一班"));
        Map<String, String> data = new HashMap<>();
        data.put("title", "战争学院花名册");
        data.put("info", "学校统一花名册");
        // 不基于模板导出Excel
        String userDeskPath = PathUtil.getUserDeskPath();
        ExcelUtils.getInstance().exportObjects2Excel(list, Student1.class, true, null, true, userDeskPath + "a4.xlsx");
        
        System.out.println("finises...");
    }

}
