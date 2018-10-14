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
import org.apache.commons.io.FileUtils;
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
public class AutoDownloadZJExcelService {

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
   */
//  @SuppressWarnings("resource")
  public void download(String url, String path) throws IOException {

    FileUtils.copyURLToFile(new URL(url), new File(path));

  }




}
