package com.kendy.excel.excel4j.sheet.wrapper;

import java.util.List;
import java.util.Map;

/**
 * <p>基于模板、注解的Map数据导出的sheet包装类</p>
 * author : Crab2Died
 * date : 2015/5/1  10:35
 */
public class MapSheetWrapper {

    /**
     * sheet序号
     */
    private int sheetIndex;

    /**
     * 表格行数据
     */
    private Map<String, List<?>> data;

    /**
     * 扩展数据
     */
    private Map<String, String> extendMap;

    /**
     * 注解的class
     */
    private Class clazz;

    /**
     * 是否写表头
     */
    private boolean isWriteHeader;

    public MapSheetWrapper(Map<String, List<?>> data, Class clazz) {
        this.data = data;
        this.clazz = clazz;
    }

    public MapSheetWrapper(int sheetIndex, Map<String, List<?>> data, Class clazz) {
        this.sheetIndex = sheetIndex;
        this.data = data;
        this.clazz = clazz;
    }

    public MapSheetWrapper(Map<String, List<?>> data, Map<String, String> extendMap, Class clazz) {
        this.data = data;
        this.extendMap = extendMap;
        this.clazz = clazz;
    }

    public MapSheetWrapper(int sheetIndex, Map<String, List<?>> data, Map<String, String> extendMap, Class clazz,
                           boolean isWriteHeader) {
        this.sheetIndex = sheetIndex;
        this.data = data;
        this.extendMap = extendMap;
        this.clazz = clazz;
        this.isWriteHeader = isWriteHeader;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Map<String, List<?>> getData() {
        return data;
    }

    public void setData(Map<String, List<?>> data) {
        this.data = data;
    }

    public Map<String, String> getExtendMap() {
        return extendMap;
    }

    public void setExtendMap(Map<String, String> extendMap) {
        this.extendMap = extendMap;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isWriteHeader() {
        return isWriteHeader;
    }

    public void setWriteHeader(boolean writeHeader) {
        isWriteHeader = writeHeader;
    }
}
