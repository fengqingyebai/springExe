package com.kendy.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * 文件工具类
 *
 * @author 林泽涛
 * @time 2018年4月21日 上午10:55:03
 */
public class FileUtil {


  /**
   * 移动文件
   *
   * @time 2018年4月18日
   */
  public static void moveFile(String resourceFilePath, String targetFilePath) throws IOException {
    FileUtils.moveFile(new File(resourceFilePath), new File(targetFilePath));
  }


  /**
   * 截取tableId
   */
  public static String getTableId(String pathString) {
    String tableId =
        pathString.substring(pathString.lastIndexOf("-") + 1, pathString.lastIndexOf("."));
    return "第" + tableId + "局";
  }

  public static String getPureTableId(String pathString) {
    String tableId =
        pathString.substring(pathString.lastIndexOf("-") + 1, pathString.lastIndexOf("."));
    return tableId;
  }

  /**
   * 获取文件名称
   */
  public static String getFileName(String filePath) {
    String fileName = "";
    if (StringUtil.isNotBlank(filePath)) {
      int index = 0;
      if (filePath.contains(File.separator)) {
        index = filePath.lastIndexOf(File.separator);
      } else {
        index = filePath.lastIndexOf("/");
      }
      fileName = filePath.substring(index + 1);
    }
    return fileName;
  }


}
