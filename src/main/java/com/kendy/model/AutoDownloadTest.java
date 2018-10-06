package com.kendy.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;

public class AutoDownloadTest {

  private static Logger log = Logger.getLogger(AutoDownloadTest.class);

  private static final String DOWN_LOAD_EXCEL_URL =
      "http://cms.pokermanager.club/cms-api/game/exportGame";

  public static void main(String[] args) {
    autoDown("294.xls", "28793755", getToken());
  }

  private static void autoDown(String fileName, String rooId, String token) {
    try {
      String urlString = String.format("%s?roomId=%s&token=%s", DOWN_LOAD_EXCEL_URL, rooId, token);
      String path = "D:/" + fileName;
      long start = System.currentTimeMillis();
      download(urlString, path);
      long end = System.currentTimeMillis();
      log.info("已成功下载" + fileName + ",,耗时：" + (end - start) + "毫秒");
    } catch (UnknownHostException ue) {
      log.error("自动下载异常：UnknownHostException，" + ue.getMessage());
    } catch (FileNotFoundException notfoundE) {
      log.error("自动下载异常：FileNotFoundException，原因，已经下载过，且正被使用中。");
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 下载网络文件
   *
   * @time 2018年4月10日
   */
  public static void download(String url, String path) throws Exception {
    long start = System.currentTimeMillis();
    File file = null;
    HttpURLConnection httpCon = null;
    URLConnection con = null;
    URL urlObj = null;
    try {
      file = new File(path);
      urlObj = new URL(url);
      con = urlObj.openConnection();
      httpCon = (HttpURLConnection) con;
      httpCon.setConnectTimeout(5 * 1000);
      httpCon.setReadTimeout(5 * 1000);
    } catch (Exception e1) {
      throw e1;
    }

    try (InputStream in = httpCon.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);) {
      int responseCode = httpCon.getResponseCode();
      if (responseCode != 200) {
        throw new Exception("自动下载返回码：" + responseCode);
      }
      Map<String, List<String>> headerFields = httpCon.getHeaderFields();
      log.info("respHeader:" + JSON.toJSONString(headerFields));
      // 获取自己数组
      byte[] getData = readInputStream(in);
      fos.write(getData);
      long end = System.currentTimeMillis();
      log.info("已成功下载,耗时：" + (end - start) + "毫秒");

    } catch (Exception e) {
      throw e;
    }
  }


  /**
   * 从输入流中获取字节数组
   */
  public static byte[] readInputStream(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024 * 4];
    int len = 0;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((len = inputStream.read(buffer)) != -1) {
      bos.write(buffer, 0, len);
    }
    bos.close();
    return bos.toByteArray();
  }

  private static Map<String, String> getParams(String roomId) {
    Map<String, String> params = new HashMap<>();
    params.put("roomId", roomId);
    return params;
  }

  private static Map<String, Object> getHeaders() {
    Map<String, Object> params = new HashMap<>();
    String token = getToken();
    params.put("token", token);
    return params;

  }


  private static String getToken() {
    String token =
        "305c300d06092a864886f70d0101010500034b003048024100844b9262dac8ceae5c7b29447843b8b3cf15c505bd18df02f610fdfd78781043c7c95c7ed781c9cc3807a6cfc6fca3ee5210dec01fef7bf3587f99b1260af47f0203010001";
    return token;
  }

}
