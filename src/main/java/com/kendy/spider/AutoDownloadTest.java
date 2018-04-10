package com.kendy.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class AutoDownloadTest {

    public static void main(String[] args) {
        System.out.println("开始下载...");
        autoDown();
        System.out.println("finishes...");

    }
    
    private static final String DOWN_LOAD_EXCEL_URL = "http://cms.pokermanager.club/cms-api/game/exportGame";
    
    private static void autoDown() {
        Map<String, String> params = getParams("28758406");
        try {
//            String resString = HttpUtil.sendPost(DOWN_LOAD_EXCEL_URL, params, getToken());
            String urlString = DOWN_LOAD_EXCEL_URL + "?roomId=28759512&token=" + getToken();
            String path = "D:/";
            download(urlString, path);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @SuppressWarnings("deprecation")
    public static void download(String url,String path){  
        File file= null;  
        FileOutputStream fos=null;  
        String downloadName= "bss.xls";
        HttpURLConnection httpCon = null;  
        URLConnection  con = null;  
        URL urlObj=null;  
        InputStream in =null;  
        byte[] size = new byte[1024];  
        int num=0;  
        try {  
            file = new File(path+downloadName);  
            fos = new FileOutputStream(file);  
            urlObj = new URL(url);  
            con = urlObj.openConnection();  
            httpCon =(HttpURLConnection) con;  
            int responseCode = httpCon.getResponseCode();
            in = httpCon.getInputStream();  
            while((num=in.read(size)) != -1){  
                for(int i=0;i<num;i++)   
                    fos.write(size[i]);  
            }  
              
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{  
            try {  
                in.close();  
                fos.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
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
        String token = "305c300d06092a864886f70d0101010500034b003048024100ab57b1ddd9a1dd6a179f6d904d0c9f9ce8e5a4f43829433f5dadcb45f39b79eb4e202b0accdfd076ac3db0ba99df0dff22e0b691943eb0a0c515b82e804b56c10203010001";
        return token;
    }

}
