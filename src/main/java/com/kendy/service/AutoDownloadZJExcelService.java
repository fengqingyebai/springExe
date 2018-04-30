package com.kendy.service;

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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class AutoDownloadZJExcelService {
	
	private static Logger log = Logger.getLogger(AutoDownloadZJExcelService.class);
    
    private static final String DOWN_LOAD_EXCEL_URL = "http://cms.pokermanager.club/cms-api/game/exportGame";
    
    public static void autoDown(String fileName, String rooId, String token) throws Exception{
    	
    	String urlString = String.format("%s?roomId=%s&token=%s", DOWN_LOAD_EXCEL_URL,rooId, token) ;
    	
    	String path = getUserDeskPath() + LocalDate.now().toString() + "\\" + fileName;
    	
    	File dir = new File(getUserDeskPath() + LocalDate.now().toString() );
    	
    	if(!dir.exists()) {
    		dir.mkdir();
    	}
    	download(urlString, path);
    }
    
    public static String getUserDeskPath() {
    	String absolutePath = FileSystemView.getFileSystemView() .getHomeDirectory().getAbsolutePath();
    	return absolutePath + "\\";
    	
    }
    
    /**
     * 下载网络文件
     * @time 2018年4月10日
     * @param url
     * @param path
     * @throws Exception
     */
    public static void download(String url,String path) throws Exception {  
    	
        File file= null;  
        HttpURLConnection httpCon = null;  
        URLConnection  con = null;  
        URL urlObj=null;  
        try {
			file = new File(path); 
			if(!file.exists()) {
//				FileUtils.forceMkdir(file);
				file.createNewFile();
			}
			urlObj = new URL(url);  
			con = urlObj.openConnection();  
			httpCon =(HttpURLConnection) con;
			httpCon.setConnectTimeout(5 * 1000);
			httpCon.setReadTimeout(5 * 1000);
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
        String token = "305c300d06092a864886f70d0101010500034b0030480241009d78a8ad9148ee88fa4977a5b1b08b21e5bc09e27e5a1940e1064d1a390fb3a76fa5f60d31f0ce39741a2a7417ea6e3664d08521261b99c378578f3f1abeb7a90203010001";
        return token;
    }

}
