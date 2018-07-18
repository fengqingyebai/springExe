package com.kendy.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.filechooser.FileSystemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kendy.controller.SMAutoController;

/**
 * 自动下载服务类
 * 
 * @author 林泽涛
 * @time 2018年7月6日 下午9:40:36
 */
@Component
public class AutoDownloadZJExcelService{

  @Autowired
  public SMAutoController smAutoController; // 托管控制类

  private final String DOWN_LOAD_EXCEL_URL =
      "http://cms.pokermanager.club/cms-api/game/exportGame";

  public void autoDown(String fileName, String rooId, String token) throws Exception {

    //SMAutoController smAutoController = MyController.smAutoController;

    String urlString = String.format("%s?roomId=%s&token=%s", DOWN_LOAD_EXCEL_URL, rooId, token);

    String path =
        getUserDeskPath() + smAutoController.getSelectedDate().toString() + "\\" + fileName;

    File dir = new File(getUserDeskPath() + smAutoController.getSelectedDate().toString());

    if (!dir.exists()) {
      dir.mkdir();
    }
    download(urlString, path);
  }

  public String getUserDeskPath() {
    String absolutePath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
    return absolutePath + "\\";
  }

  /**
   * 下载网络文件
   * 
   * @time 2018年4月10日
   * @param url
   * @param path
   * @throws IOException
   * @throws Exception
   */
  @SuppressWarnings("resource")
  public void download(String url, String path) throws IOException {

    File file = null;
    HttpURLConnection httpCon = null;
    URLConnection con = null;
    URL urlObj = null;
    file = new File(path);
    if (!file.exists()) {
      file.createNewFile();
    }
    urlObj = new URL(url);
    con = urlObj.openConnection();
    httpCon = (HttpURLConnection) con;
    httpCon.setConnectTimeout(5 * 1000);
    httpCon.setReadTimeout(5 * 1000);

    InputStream in = httpCon.getInputStream();
    FileOutputStream fos = new FileOutputStream(file);
    int responseCode = httpCon.getResponseCode();
    if (responseCode != 200) {
      throw new IOException("自动下载返回码：" + responseCode);
    }
    // 获取自己数组
    byte[] getData = readInputStream(in);
    fos.write(getData);
  }


  /**
   * 从输入流中获取字节数组
   * 
   * @param inputStream
   * @return
   * @throws IOException
   */
  public byte[] readInputStream(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024 * 4];
    int len = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = inputStream.read(buffer)) != -1) {
      bos.write(buffer, 0, len);
    }
    bos.close();
    return bos.toByteArray();
  }



}
