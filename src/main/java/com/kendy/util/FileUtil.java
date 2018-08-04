package com.kendy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
   * @param resourceFilePath
   * @param targetFilePath
   * @return
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
  
  
  /**
   * @复制文件，支持把源文件内容追加到目标文件末尾
   * @param src
   * @param dst
   * @param append
   * @throws Exception
   */
  public static void copy(File src, File dst) throws Exception {
    if(!dst.exists()) {
      dst.createNewFile();
    }
    int BUFFER_SIZE = 4096;
    InputStream in = null;
    OutputStream out = null;
    try {
      in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
      out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
      byte[] buffer = new byte[BUFFER_SIZE];
      int len = 0;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        in = null;
      }
      if (null != out) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        out = null;
      }
    }
  }
}
