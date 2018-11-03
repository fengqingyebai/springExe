package com.kendy.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.kendy.excel.excel4j.exceptions.TimeMatchFormatException;

/**
 * Excel读取和导入工具类
 * <p>
 * POI 3.17
 * </p>
 *
 * @author delegation by linzt
 */
public enum ExcelUtil {

  INSTANCE;


  /**
   * 根据文件路径读取（指定从第几行读取内容）
   */
  public <T> List<T> readExcel2Objects(String excelPath, Class<T> clazz, int offsetLine)
      throws Exception {
    Workbook workbook = WorkbookFactory.create(new File(excelPath));
    return readExcel2ObjectsHandler(workbook, clazz, offsetLine);
  }

  /**
   * 直正的读取逻辑
   */
  private <T> List<T> readExcel2ObjectsHandler(Workbook workbook, Class<T> clazz, int offsetLine)
      throws Exception {

    Sheet sheet = workbook.getSheetAt(0);
    Row row = sheet.getRow(offsetLine);
    List<T> list = new ArrayList<>();
    Map<Integer, ExcelHeader> maps = Utils.getHeaderMap(row, clazz);
    if (maps == null || maps.size() <= 0) {
      throw new Exception(
          "The Excel format to read is not correct, and check to see if the appropriate rows are set");
    }
    long maxLine = sheet.getLastRowNum();

    for (int i = offsetLine + 1; i <= maxLine; i++) {
      row = sheet.getRow(i);
      if (null == row) {
        continue;
      }
      T obj;
      try {
        obj = clazz.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new Exception(e);
      }
      for (Cell cell : row) {
        int ci = cell.getColumnIndex();
        ExcelHeader header = maps.get(ci);
        if (null == header) {
          continue;
        }
        String val = Utils.getCellValue(cell);
        Object value;
        String filed = header.getFiled();
        // 默认转换
        value = Utils.str2TargetClass(val, header.getFiledClazz());
        Utils.copyProperty(obj, filed, value);
      }
      list.add(obj);
    }
    return list;
  }

  /*---------------------------------------2.读取Excel操作无映射-------------------------------------------------*/

  /**
   * 读取Excel表格数据,返回{@code List[List[String]]}类型的数据集合
   *
   * @param excelPath 待读取Excel的路径
   * @param offsetLine Excel表头行(默认是0)
   * @param limitLine 最大读取行数(默认表尾)
   * @param sheetIndex Sheet索引(默认0)
   * @return 返回{@code List<List<String>>}类型的数据集合
   * @throws IOException 异常
   * @throws InvalidFormatException 异常
   */
  public List<List<String>> readExcel2List(String excelPath, int offsetLine)
      throws IOException, InvalidFormatException {

    Workbook workbook = WorkbookFactory.create(new File(excelPath));
    return readExcel2ObjectsHandler(workbook, offsetLine);
  }

  /**
   * 读取Excel表格数据(从文件流)
   */
  public List<List<String>> readExcel2List(InputStream is, int offsetLine)
      throws Exception {

    Workbook workbook = WorkbookFactory.create(is);
    return readExcel2ObjectsHandler(workbook, offsetLine);
  }


  private List<List<String>> readExcel2ObjectsHandler(Workbook workbook, int offsetLine) {

    List<List<String>> list = new ArrayList<>();
    Sheet sheet = workbook.getSheetAt(0);
    long maxLine = sheet.getLastRowNum();
    for (int i = offsetLine; i <= maxLine; i++) {
      List<String> rows = new ArrayList<>();
      Row row = sheet.getRow(i);
      if (null == row) {
        continue;
      }
      for (Cell cell : row) {
        String val = Utils.getCellValue(cell);
        rows.add(val);
      }
      list.add(rows);
    }
    return list;
  }


  /*--------------------------------------基于注解导出---------------------------------------------------*/

  /**
   * 无模板、基于注解的数据导出
   *
   * @param data 待导出数据
   * @param clazz {@link com.github.crab2died.annotation.ExcelField}映射对象Class
   * @param isWriteHeader 是否写入表头
   * @param sheetName 指定导出Excel的sheet名称
   * @param isXSSF 导出的Excel是否为Excel2007及以上版本(默认是)
   * @param targetPath 生成的Excel输出全路径
   * @throws Exception 异常
   * @throws IOException 异常
   */
  public void exportObjects2Excel(List<?> data, Class<?> clazz, boolean isWriteHeader,
      String sheetName, boolean isXSSF, String targetPath) throws Exception, IOException {

    try (FileOutputStream fos = new FileOutputStream(targetPath)) {
      exportExcelNoTemplateHandler(data, clazz, isWriteHeader, sheetName, isXSSF).write(fos);
    }

  }


  /**
   * 无模板、基于注解的数据导出
   *
   * @param data 待导出数据
   * @param clazz {@link com.github.crab2died.annotation.ExcelField}映射对象Class
   * @param isWriteHeader 是否写入表头
   * @param targetPath 生成的Excel输出全路径
   * @throws Exception 异常
   * @throws IOException 异常
   */
  public void exportObjects2Excel(List<?> data, Class<?> clazz, boolean isWriteHeader,
      String targetPath) throws Exception, IOException {

    try (FileOutputStream fos = new FileOutputStream(targetPath)) {
      exportExcelNoTemplateHandler(data, clazz, isWriteHeader, null, true).write(fos);
    }
  }

  /**
   * 无模板、基于注解的数据导出
   *
   * @param data 待导出数据
   * @param clazz {@link com.github.crab2died.annotation.ExcelField}映射对象Class
   * @param targetPath 生成的Excel输出全路径
   * @throws Exception 异常
   * @throws IOException 异常
   */
  public void exportObjects2Excel(List<?> data, Class<?> clazz, String targetPath)
      throws Exception, IOException {

    try (FileOutputStream fos = new FileOutputStream(targetPath)) {
      exportExcelNoTemplateHandler(data, clazz, true, null, true).write(fos);
    }
  }

  // 单sheet数据导出
  private Workbook exportExcelNoTemplateHandler(List<?> data, Class<?> clazz, boolean isWriteHeader,
      String sheetName, boolean isXSSF) throws Exception {

    Workbook workbook = isXSSF ? new XSSFWorkbook() : new HSSFWorkbook();

    generateSheet(workbook, data, clazz, isWriteHeader, sheetName);

    return workbook;
  }


  // 生成sheet数据
  private void generateSheet(Workbook workbook, List<?> data, Class<?> clazz, boolean isWriteHeader,
      String sheetName) throws Exception {

    // 获取列头样式对象
    CellStyle columnTopStyle = ExcelCss.getColumnTopStyle(workbook);
    CellStyle style = ExcelCss.getStyle(workbook);

    Sheet sheet;
    if (null != sheetName && !"".equals(sheetName)) {
      sheet = workbook.createSheet(sheetName);
    } else {
      sheet = workbook.createSheet();
    }
    Row row = sheet.createRow(0);
    List<ExcelHeader> headers = Utils.getHeaderList(clazz);
    if (isWriteHeader) {
      // 写标题
      for (int i = 0; i < headers.size(); i++) {
        Cell cell = row.createCell(i);
        cell.setCellValue(headers.get(i).getTitle());
        cell.setCellStyle(columnTopStyle);
      }
    }
    // 写数据
    Object _data;
    for (int i = 0; i < data.size(); i++) {
      row = sheet.createRow(i + 1);
      _data = data.get(i);
      for (int j = 0; j < headers.size(); j++) {
        row.createCell(j).setCellValue(_data.toString());
        row.createCell(j).setCellValue(Utils.getProperty(_data,
            headers.get(j).getFiled()));
        row.getCell(j).setCellStyle(style);
      }
    }

    // 设置列宽(泽涛：256的倍数，最长为256 * 256)
    for (int colNum = 0; colNum < headers.size(); colNum++) {
      int columnWidth = headers.get(colNum).getColWidth();
      for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
        Row currentRow;
        // 当前行未被使用过
        if (sheet.getRow(rowNum) == null) {
          currentRow = sheet.createRow(rowNum);
        } else {
          currentRow = sheet.getRow(rowNum);
          currentRow.setHeight((short) 400);
        }
      }
      sheet.setColumnWidth(colNum, columnWidth * 256);
    }

  }
}


/**
 * 功能说明: 用来在对象的属性上加入的annotation，通过该annotation说明某个属性所对应的标题
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface ExcelField {

  /**
   * 属性的标题名称
   *
   * @return 表头名
   */
  String title();

  /**
   * 在excel的顺序
   *
   * @return 列表顺序
   */
  int order() default Integer.MAX_VALUE;


  /**
   * 在excel的列宽
   *
   * @return 列宽, 最长为 256 * 256
   */
  int colWidth() default 15;

}


/**
 * 基于注解导出的sheet包装类
 */
class SheetWrapper {

  /**
   * 待导出行数据
   */
  private List<?> data;

  /**
   * 基于注解的class
   */
  private Class<?> clazz;

  /**
   * 是否写入表头
   */
  private boolean isWriteHeader;

  /**
   * sheet名
   */
  private String sheetName;

  public SheetWrapper(List<?> data, Class<?> clazz) {
    this.data = data;
    this.clazz = clazz;
  }

  public SheetWrapper(List<?> data, Class<?> clazz, boolean isWriteHeader) {
    this.data = data;
    this.clazz = clazz;
    this.isWriteHeader = isWriteHeader;
  }

  public SheetWrapper(List<?> data, Class<?> clazz, boolean isWriteHeader, String sheetName) {
    this.data = data;
    this.clazz = clazz;
    this.isWriteHeader = isWriteHeader;
    this.sheetName = sheetName;
  }

  public List<?> getData() {
    return data;
  }

  public void setData(List<?> data) {
    this.data = data;
  }

  public Class getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  public boolean isWriteHeader() {
    return isWriteHeader;
  }

  public void setWriteHeader(boolean writeHeader) {
    isWriteHeader = writeHeader;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }
}


/**
 * 功能说明: 用来存储Excel标题的对象，通过该对象可以获取标题和方法的对应关系
 */
class ExcelHeader implements Comparable<ExcelHeader> {

  /**
   * excel的标题名称
   */
  private String title;

  /**
   * 每一个标题的顺序
   */
  private int order;

  /**
   * 注解域
   */
  private String filed;

  /**
   * 属性类型
   */
  private Class<?> filedClazz;

  /**
   * 每一个标题的宽度(自定义)
   */
  private int colWidth;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public String getFiled() {
    return filed;
  }

  public void setFiled(String filed) {
    this.filed = filed;
  }

  public Class<?> getFiledClazz() {
    return filedClazz;
  }

  public void setFiledClazz(Class<?> filedClazz) {
    this.filedClazz = filedClazz;
  }


  public int getColWidth() {
    return colWidth;
  }

  public void setColWidth(int colWidth) {
    this.colWidth = colWidth;
  }

  @Override
  public int compareTo(ExcelHeader o) {
    return order - o.order;
  }

  public ExcelHeader() {
    super();
  }

  public ExcelHeader(String title, int order, String filed, int colWidth, Class<?> filedClazz) {
    super();
    this.title = title;
    this.order = order;
    this.filed = filed;
    this.filedClazz = filedClazz;
    this.colWidth = colWidth;
  }
}


class Utils {

  /**
   * getter或setter枚举
   */
  public enum FieldAccessType {

    GETTER, SETTER
  }

  /**
   * <p>
   * 根据JAVA对象注解获取Excel表头信息
   * </p>
   *
   * @param clz 类型
   * @return 表头信息
   */
  public static List<ExcelHeader> getHeaderList(Class<?> clz) throws Exception {

    List<ExcelHeader> headers = new ArrayList<>();
    List<Field> fields = new ArrayList<>();
    for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    }
    for (Field field : fields) {
      // 是否使用ExcelField注解
      if (field.isAnnotationPresent(ExcelField.class)) {
        ExcelField er = field.getAnnotation(ExcelField.class);
        try {
          headers.add(new ExcelHeader(er.title(), er.order(), field.getName(), er.colWidth(),
              field.getType()));
        } catch (Exception e) {
          throw new Exception(e);
        }
      }
    }
    Collections.sort(headers);
    return headers;
  }

  /**
   * 获取excel列表头
   *
   * @param titleRow excel行
   * @param clz 类型
   * @return ExcelHeader集合
   * @throws Exception 异常
   */
  public static Map<Integer, ExcelHeader> getHeaderMap(Row titleRow, Class<?> clz)
      throws Exception {

    List<ExcelHeader> headers = getHeaderList(clz);
    Map<Integer, ExcelHeader> maps = new HashMap<>();
    for (Cell c : titleRow) {
      String title = c.getStringCellValue();
      for (ExcelHeader eh : headers) {
        if (eh.getTitle().equals(title.trim())) {
          maps.put(c.getColumnIndex(), eh);
          break;
        }
      }
    }
    return maps;
  }

  /**
   * 获取单元格内容
   *
   * @param c 单元格
   * @return 单元格内容
   */
  public static String getCellValue(Cell c) {
    String o;
    switch (c.getCellTypeEnum()) {
      case BLANK:
        o = "";
        break;
      case BOOLEAN:
        o = String.valueOf(c.getBooleanCellValue());
        break;
      case FORMULA:
        o = c.getStringCellValue();
        break;
      case NUMERIC:
        o = String.valueOf(c.getNumericCellValue());
        o = matchDoneBigDecimal(o);
        o = RegularUtils.converNumByReg(o);
        break;
      case STRING:
        o = c.getStringCellValue();
        break;
      default:
        o = null;
        break;
    }
    return o;
  }

  /**
   * 字符串转对象
   *
   * @param strField 字符串
   * @param clazz 待转类型
   * @return 转换后数据
   */
  public static Object str2TargetClass(String strField, Class<?> clazz) {
    if (null == strField || "".equals(strField)) {
      return null;
    }
    if ((Long.class == clazz) || (long.class == clazz)) {
      strField = matchDoneBigDecimal(strField);
      strField = RegularUtils.converNumByReg(strField);
      return Long.parseLong(strField);
    }
    if ((Integer.class == clazz) || (int.class == clazz)) {
      strField = matchDoneBigDecimal(strField);
      strField = RegularUtils.converNumByReg(strField);
      return Integer.parseInt(strField);
    }
    if ((Float.class == clazz) || (float.class == clazz)) {
      strField = matchDoneBigDecimal(strField);
      return Float.parseFloat(strField);
    }
    if ((Double.class == clazz) || (double.class == clazz)) {
      strField = matchDoneBigDecimal(strField);
      return Double.parseDouble(strField);
    }
    if ((Character.class == clazz) || (char.class == clazz)) {
      return strField.toCharArray()[0];
    }
    if ((Boolean.class == clazz) || (boolean.class == clazz)) {
      return Boolean.parseBoolean(strField);
    }
    if (Date.class == clazz) {
      return DateUtils.str2DateUnmatch2Null(strField);
    }
    return strField;
  }

  /**
   * 科学计数法数据转换
   *
   * @param bigDecimal 科学计数法
   * @return 数据字符串
   */
  private static String matchDoneBigDecimal(String bigDecimal) {
    // 对科学计数法进行处理
    boolean flg = Pattern.matches("^-?\\d+(\\.\\d+)?(E-?\\d+)?$", bigDecimal);
    if (flg) {
      BigDecimal bd = new BigDecimal(bigDecimal);
      bigDecimal = bd.toPlainString();
    }
    return bigDecimal;
  }

  /**
   * <p>
   * 根据java对象属性{@link Field}获取该属性的getter或setter方法名， 另对{@link boolean}及{@link Boolean}做了行管处理
   * </p>
   *
   * @param clazz 操作对象
   * @param fieldName 对象属性
   * @param methodType 方法类型，getter或setter枚举
   * @return getter或setter方法
   * @throws IntrospectionException 异常
   * @author Crab2Died
   */
  public static Method getterOrSetter(Class<?> clazz, String fieldName, FieldAccessType methodType)
      throws IntrospectionException {

    if (null == fieldName || "".equals(fieldName)) {
      return null;
    }

    BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
    PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
    for (PropertyDescriptor prop : props) {
      if (fieldName.equals(prop.getName())) {
        if (FieldAccessType.SETTER == methodType) {
          return prop.getWriteMethod();
        }
        if (FieldAccessType.GETTER == methodType) {
          return prop.getReadMethod();
        }
      }
    }
    throw new IntrospectionException("Can not get the getter or setter method");
  }

  /**
   * <p>
   * 根据对象的属性名{@code fieldName}获取某个java的属性{@link java.lang.reflect.Field}
   * </p>
   *
   * @param clazz java对象的class属性
   * @param fieldName 属性名
   * @return {@link java.lang.reflect.Field} java对象的属性
   * @author Crab2Died
   */
  private static Field matchClassField(Class<?> clazz, String fieldName) {

    List<Field> fields = new ArrayList<>();
    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
    }
    for (Field field : fields) {
      if (fieldName.equals(field.getName())) {
        return field;
      }
    }
    throw new IllegalArgumentException(
        "[" + clazz.getName() + "] can`t found field with [" + fieldName + "]");
  }

  /**
   * 根据属性名与属性类型获取字段内容
   *
   * @param bean 对象
   * @param fieldName 字段名
   * @param writeConvertible 写入转换器
   * @return 对象指定字段内容
   * @throws Exception 异常
   */
  public static String getProperty(Object bean, String fieldName) throws Exception {

    if (bean == null || fieldName == null) {
      throw new IllegalArgumentException("Operating bean or filed class must not be null");
    }
    Method method;
    Object object;
    try {
      method = getterOrSetter(bean.getClass(), fieldName, FieldAccessType.GETTER);
      object = method.invoke(bean);
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      throw new Exception(e);
    }
    return object == null ? "" : object.toString();
  }

  /**
   * 根据属性名与属性类型获取字段内容
   *
   * @param bean 对象
   * @param name 字段名
   * @param value 字段类型
   */
  public static void copyProperty(Object bean, String name, Object value) throws Exception {

    if (null == name || null == value) {
      return;
    }
    Field field = matchClassField(bean.getClass(), name);
    if (null == field) {
      return;
    }
    Method method;
    try {
      method = getterOrSetter(bean.getClass(), name, FieldAccessType.SETTER);

      if (value.getClass() == field.getType()) {
        method.invoke(bean, value);
      } else {
        method.invoke(bean, str2TargetClass(value.toString(), field.getType()));
      }
    } catch (Exception e) {
      throw new Exception(e);
    }

  }
}


/**
 * <p>
 * 时间处理工具类
 * </p>
 * date : 2017/5/23 10:35
 */
class DateUtils {

  public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
  public static final String DATE_FORMAT_DAY_2 = "yyyy/MM/dd";
  public static final String TIME_FORMAT_SEC = "HH:mm:ss";
  public static final String DATE_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT_MSEC = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String DATE_FORMAT_MSEC_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  public static final String DATE_FORMAT_MSEC_T_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static final String DATE_FORMAT_DAY_SIMPLE = "y/M/d";

  /**
   * 匹配yyyy-MM-dd
   */
  private static final String DATE_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
  /**
   * 匹配yyyy/MM/dd
   */
  private static final String DATE_REG_2 =
      "^[1-9]\\d{3}/(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[0-1])$";
  /**
   * 匹配y/M/d
   */
  private static final String DATE_REG_SIMPLE_2 =
      "^[1-9]\\d{3}/([1-9]|1[0-2])/([1-9]|[1-2][0-9]|3[0-1])$";
  /**
   * 匹配HH:mm:ss
   */
  private static final String TIME_SEC_REG = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
  /**
   * 匹配yyyy-MM-dd HH:mm:ss
   */
  private static final String DATE_TIME_REG =
      "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s"
          + "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
  /**
   * 匹配yyyy-MM-dd HH:mm:ss.SSS
   */
  private static final String DATE_TIME_MSEC_REG =
      "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s"
          + "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
  /**
   * 匹配yyyy-MM-dd'T'HH:mm:ss.SSS
   */
  private static final String DATE_TIME_MSEC_T_REG =
      "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T"
          + "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
  /**
   * 匹配yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
   */
  private static final String DATE_TIME_MSEC_T_Z_REG =
      "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T"
          + "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}Z$";

  /**
   * <p>
   * 将{@link Date}类型转换为指定格式的字符串
   * </p>
   * author : Crab2Died date : 2017年06月02日 15:32:04
   *
   * @param date {@link Date}类型的时间
   * @param format 指定格式化类型
   * @return 返回格式化后的时间字符串
   */
  public static String date2Str(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }

  /**
   * <p>
   * 将{@link Date}类型转换为默认为[yyyy-MM-dd HH:mm:ss]类型的字符串
   * </p>
   * author : Crab2Died date : 2017年06月02日 15:30:01
   *
   * @param date {@link Date}类型的时间
   * @return 返回格式化后的时间字符串
   */
  public static String date2Str(Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SEC);
    return sdf.format(date);
  }

  /**
   * <p>
   * 根据给出的格式化类型将时间字符串转为{@link Date}类型
   * </p>
   * author : Crab2Died date : 2017年06月02日 15:27:22
   *
   * @param strDate 时间字符串
   * @param format 格式化类型
   * @return 返回{@link java.util.Date}类型
   */
  public static Date str2Date(String strDate, String format) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    try {
      date = sdf.parse(strDate);
    } catch (ParseException e) {
      throw new DateTimeException("[" + strDate + "] parse to [" + format + "] exception", e);
    }
    return date;
  }

  /**
   * <p>
   * 字符串时间转为{@link Date}类型， 未找到匹配类型则抛出{@link TimeMatchFormatException}异常
   * </p>
   * <p>
   * 支持匹配类型列表：
   * </p>
   * <p>
   * yyyy-MM-dd
   * </p>
   * <p>
   * yyyy/MM/dd
   * </p>
   * <p>
   * HH:mm:ss
   * </p>
   * <p>
   * yyyy-MM-dd HH:mm:ss
   * </p>
   * <p>
   * yyyy-MM-dd'T'HH:mm:ss.SSS
   * </p>
   * <p>
   * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
   * </p>
   * <p>
   * author : Crab2Died date : 2017年06月02日 15:21:54
   *
   * @param strDate 时间字符串
   * @return Date {@link Date}时间
   * @throws ParseException 异常
   */
  public static Date str2Date(String strDate) throws Exception {

    strDate = strDate.trim();
    SimpleDateFormat sdf = null;
    if (RegularUtils.isMatched(strDate, DATE_REG)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
    }
    if (RegularUtils.isMatched(strDate, DATE_REG_2)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_DAY_2);
    }
    if (RegularUtils.isMatched(strDate, DATE_REG_SIMPLE_2)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_DAY_SIMPLE);
    }
    if (RegularUtils.isMatched(strDate, TIME_SEC_REG)) {
      sdf = new SimpleDateFormat(TIME_FORMAT_SEC);
    }
    if (RegularUtils.isMatched(strDate, DATE_TIME_REG)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_SEC);
    }
    if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_REG)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_MSEC);
    }
    if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_T_REG)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T);
    }
    if (RegularUtils.isMatched(strDate, DATE_TIME_MSEC_T_Z_REG)) {
      sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T_Z);
    }
    if (null != sdf) {
      return sdf.parse(strDate);
    }
    throw new Exception(String.format("[%s] can not matching right time format", strDate));
  }

  /**
   * <p>
   * 字符串时间转为{@link Date}类型，未找到匹配类型则返NULL
   * </p>
   * <p>
   * 支持匹配类型列表：
   * </p>
   * <p>
   * yyyy-MM-dd
   * </p>
   * <p>
   * yyyy/MM/dd
   * </p>
   * <p>
   * HH:mm:ss
   * </p>
   * <p>
   * yyyy-MM-dd HH:mm:ss
   * </p>
   * <p>
   * yyyy-MM-dTHH:mm:ss.SSS
   * </p>
   * <p>
   * author : Crab2Died date : 2017年06月02日 15:21:54
   *
   * @param strDate 时间字符串
   * @return Date {@link Date}时间
   */
  public static Date str2DateUnmatch2Null(String strDate) {
    Date date;
    try {
      date = str2Date(strDate);
    } catch (Exception e) {
      throw new DateTimeException("[" + strDate + "] date auto parse exception", e);
    }
    return date;
  }
}


/**
 * <p>
 * 正则匹配相关工具
 * </p>
 * author : Crab2Died date : 2017/5/24 9:43
 */
class RegularUtils {


  /**
   * <p>
   * 判断内容是否匹配
   * </p>
   * author : Crab2Died date : 2017年06月02日 15:46:25
   *
   * @param pattern 匹配目标内容
   * @param reg 正则表达式
   * @return 返回boolean
   */
  public static boolean isMatched(String pattern, String reg) {
    Pattern compile = Pattern.compile(reg);
    return compile.matcher(pattern).matches();
  }

  /**
   * <p>
   * 正则提取匹配到的内容
   * </p>
   * <p>
   * 例如：
   * </p>
   * <p>
   * author : Crab2Died date : 2017年06月02日 15:49:51
   *
   * @param pattern 匹配目标内容
   * @param reg 正则表达式
   * @param group 提取内容索引
   * @return 提取内容集合
   */
  public static List<String> match(String pattern, String reg, int group) {

    List<String> matchGroups = new ArrayList<>();
    Pattern compile = Pattern.compile(reg);
    Matcher matcher = compile.matcher(pattern);
    if (group > matcher.groupCount() || group < 0) {
      return Collections.EMPTY_LIST;
    }
    while (matcher.find()) {
      matchGroups.add(matcher.group(group));
    }
    return matchGroups;
  }

  /**
   * <p>
   * 正则提取匹配到的内容,默认提取索引为0
   * </p>
   * <p>
   * 例如：
   * </p>
   * <p>
   * author : Crab2Died date : 2017年06月02日 15:49:51
   *
   * @param pattern 匹配目标内容
   * @param reg 正则表达式
   * @return 提取内容集合
   */
  public static String match(String pattern, String reg) {

    String match = null;
    List<String> matches = match(pattern, reg, 0);
    if (null != matches && matches.size() > 0) {
      match = matches.get(0);
    }
    return match;
  }

  public static String converNumByReg(String number) {
    Pattern compile = Pattern.compile("^(\\d+)(\\.0*)?$");
    Matcher matcher = compile.matcher(number);
    while (matcher.find()) {
      number = matcher.group(1);
    }
    return number;
  }
}


/**
 * 自定义Excel单元格样式
 *
 * @author 林泽涛
 * @time 2018年7月1日 下午3:42:15
 */
class ExcelCss {

  private static short blackCode = HSSFColor.HSSFColorPredefined.BLACK.getIndex();


  /*
   * 列头单元格样式
   */
  public static CellStyle getColumnTopStyle(Workbook workbook) {

    // 设置字体
    Font font = workbook.createFont();
    // 设置字体大小
    font.setFontHeightInPoints((short) 11);
    // 字体加粗
    font.setBold(true);
    // 设置样式;
    CellStyle style = workbook.createCellStyle();
    // 设置底边框;
    style.setBorderBottom(BorderStyle.THIN);
    // 设置底边框颜色;
    style.setBottomBorderColor(blackCode);
    // 设置左边框;
    style.setBorderLeft(BorderStyle.THIN);
    // 设置左边框颜色;
    style.setLeftBorderColor(blackCode);
    // 设置右边框;
    style.setBorderRight(BorderStyle.THIN);
    // 设置右边框颜色;
    style.setRightBorderColor(blackCode);
    // 设置顶边框;
    style.setBorderTop(BorderStyle.THIN);
    // 设置顶边框颜色;
    style.setTopBorderColor(blackCode);
    // 在样式用应用设置的字体;
    style.setFont(font);
    // 设置自动换行;
    style.setWrapText(false);
    // 设置水平对齐的样式为居中对齐;
    style.setAlignment(HorizontalAlignment.CENTER);
    // 设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(VerticalAlignment.CENTER);

    return style;

  }

  /*
   * 列数据信息单元格样式
   */
  public static CellStyle getStyle(Workbook workbook) {
    // 设置字体
    Font font = workbook.createFont();
    // 设置样式;
    CellStyle style = workbook.createCellStyle();
    // 设置底边框;
    style.setBorderBottom(BorderStyle.THIN);
    // 设置底边框颜色;
    style.setBottomBorderColor(blackCode);
    // 设置左边框;
    style.setBorderLeft(BorderStyle.THIN);
    // 设置左边框颜色;
    style.setLeftBorderColor(blackCode);
    // 设置右边框;
    style.setBorderRight(BorderStyle.THIN);
    // 设置右边框颜色;
    style.setRightBorderColor(blackCode);
    // 设置顶边框;
    style.setBorderTop(BorderStyle.THIN);
    // 设置顶边框颜色;
    style.setTopBorderColor(blackCode);
    // 在样式用应用设置的字体;
    style.setFont(font);
    // 设置自动换行;
    style.setWrapText(false);
    // 设置水平对齐的样式为居中对齐;
    style.setAlignment(HorizontalAlignment.CENTER);
    // 设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
  }
}
