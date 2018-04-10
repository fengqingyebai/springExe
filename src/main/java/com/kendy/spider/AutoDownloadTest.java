package com.kendy.spider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class AutoDownloadTest {
	
	private static Logger log = Logger.getLogger(AutoDownloadTest.class);

    public static void main(String[] args) {
        System.out.println("开始下载...");
        autoDown("b.xls");
        System.out.println("finishes...");

    }
    
    private static final String DOWN_LOAD_EXCEL_URL = "http://cms.pokermanager.club/cms-api/game/exportGame";
    
    private static void autoDown(String fileName) {
        Map<String, String> params = getParams("28763605");
        try {
//            String resString = HttpUtil.sendPost(DOWN_LOAD_EXCEL_URL, params, getToken());
            String urlString = DOWN_LOAD_EXCEL_URL + "?roomId=28763605&token=" + getToken();
            String path = "D:/" + fileName;
            download(urlString, path);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public static void download(String url,String path) throws Exception {  
    	long start = System.currentTimeMillis();
        File file= null;  
        HttpURLConnection httpCon = null;  
        URLConnection  con = null;  
        URL urlObj=null;  
        try {
			file = new File(path);  
			urlObj = new URL(url);  
			con = urlObj.openConnection();  
			httpCon =(HttpURLConnection) con;
			httpCon.setConnectTimeout(15 * 1000);
			httpCon.setReadTimeout(10 * 1000);
		} catch (Exception e1) {
			throw e1;
		}  
        
        try(InputStream in = httpCon.getInputStream();
        	FileOutputStream fos = new FileOutputStream(file);) {  
            int responseCode = httpCon.getResponseCode();
            if(responseCode != 200) {
            	throw new Exception("自动下载返回码："+responseCode);
            }
            
            //获取自己数组  
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
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024 * 4];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }
    
    private static Map<String,String> getParams(String roomId) {
        Map<String,String> params = new HashMap<>();
        params.put("roomId", roomId);
        return params;
    }
    
    private static Map<String,Object> getHeaders(){
        Map<String,Object> params = new HashMap<>();
        String token = getToken();
        params.put("token", token);
        return params;
        
    }
    

    private static String getToken() {
        String token = "305c300d06092a864886f70d0101010500034b003048024100b7e2f18624017476266bdb2657ceaa66909e1f9370052c31c443d9f777e364eeb26c033ed1254a6275ccb2759b9db9c4a2350974b0f85a10426d04fb2e372cf50203010001";
        return token;
    }

}
