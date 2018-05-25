package com.kendy.spider;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.kendy.util.StringUtil;

import application.MyController;

// 爬取网站后台接口数据
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	
	private static final String BUY_LIST_URL="http://cms.pokermanager.club/cms-api/game/getBuyinList";
	private static final String ACCEPT_BUY_URL="http://cms.pokermanager.club/cms-api/game/acceptBuyin";
	
	public static void main(String[] args) throws Exception{

		System.out.println("正在连接...");
        try {
        	String token = 
        			"305c300d06092a864886f70d0101010500034b003048024100991d6650cde25eb73ae1b65465b86443dc57574bfc31f194ab0192f733912631c2a86d191a64300d14e67e0385c9b0a3ddbaf947d88d9f50aecfcf0df0f485b70203010001";

        	
        	List<WanjiaApplyInfo> buyinList = getBuyinList(token);
        	if(buyinList == null ) {
        		System.out.println("申请列表为null！");
        		return;
        	}else if(buyinList.isEmpty()) {
        		System.out.println("申请列表:暂时没有人买入申请！");
        		return;
        	}else {
        		buyinList.forEach(info -> info.toString());
        		Optional<WanjiaApplyInfo> wanjiaOpt = buyinList.stream().filter(info -> "番红花".equals(info.getStrNick())).findFirst();
        		if(!wanjiaOpt.isPresent()) {
        			System.err.println("=====================================番红花不存在");
        		}else {
        			System.out.println("即将审核番红花......");
        			
        			WanjiaApplyInfo wanjia = wanjiaOpt.get();
        			Long uuid = wanjia.getUuid();
        			Long gameRoomId = wanjia.getGameRoomId();
        			System.out.println("uuid:"+uuid.toString()+"\nroomid:"+gameRoomId);
        			boolean acceptBuy = acceptBuy(uuid, gameRoomId, token);
        			System.out.println("审核：" + acceptBuy);
        		}
        		
        	}
		
            System.out.println("结束 ！");
        } catch (Exception e) {
  			e.printStackTrace();
  		}
        System.out.println("finishes...");
		
  
//玩家信息
//    　　　　{
//  　　　　　　"showId":"2162968366",
//  　　　　　　"strNick":"番红花",
//  　　　　　　"gameRoomId":28325032,
//  　　　　　　"gameRoomName":"🏧24-207",
//  　　　　　　"buyStack":200,
//  　　　　　　"uuid":894521,
//  　　　　　　"totalBuyin":2200,
//  　　　　　　"totalProfit":-135
//  　　　　}
//
//审核同意
//		userUuid:894521
//		roomId:28325032
//	}
	}
	
	/**
	 * 发起请求：爬取后台的玩家列表
	 * 
	 * @time 2018年3月29日
	 * @param token
	 * @return
	 */
	public static WanjiaListResult getWanjiaListResult(String token) {
		WanjiaListResult wanjiaListResult = null;
		try {
			URL url = new URL(BUY_LIST_URL);
			HttpURLConnection openConnection = (HttpURLConnection)url.openConnection();
			openConnection.setRequestProperty("token", token);
//			openConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//观察后没有这个request Head
			InputStream urlStream = openConnection.getInputStream();
			String charsetName = MyController.smAutoController.sysCodeField.getText();
			String ResString = org.apache.commons.io.IOUtils.toString(urlStream, Charset.forName(charsetName));
			log.info("后台的玩家列表: "+ResString);
			if(StringUtil.isBlank(ResString)) {
				return null;
			}
			wanjiaListResult = JSON.parseObject(ResString, WanjiaListResult.class);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wanjiaListResult;
	}
	
	/**
	 * 爬取后台的玩家列表
	 * @time 2018年3月29日
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static List<WanjiaApplyInfo> getBuyinList(String token) throws Exception{
		WanjiaListResult wanjiaListResult = getWanjiaListResult(token);
		if(wanjiaListResult == null) {
			return null;
		}else if(wanjiaListResult.getiErrCode() != 0) {
			return null;
		}else {
			return wanjiaListResult.getResult();
		}
	}
	
	public static boolean acceptBuy(Long userUuid, Long roomId, String token) {
		Map<String,String> map = new HashMap<>();
		map.put("userUuid", userUuid+"");
		map.put("roomId", roomId+"");
		String ResString = "";
		try {
			ResString = sendPost(ACCEPT_BUY_URL,map,token);
		} catch (Exception e) {
			log.info("请求异常：" + e.getMessage());
			return false;
		}
		
		log.info(ResString);
		if(StringUtil.isBlank(ResString) || ResString.endsWith("\"iErrCode\":1}") || (!ResString.endsWith("0}"))) {
			return false;
		}else {
			return true;
		}
		
	}
	
	
	
	public static final String CHARSET = "UTF-8";  
    
	private static final CloseableHttpClient httpclient;  
    static{  
        RequestConfig config = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(10000).build();  
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();  
    }  
    
    public static String sendPost(String url, Map<String, String> params, String token)
            throws ClientProtocolException, IOException {

        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
            }
        }
        HttpPost httpPost = new HttpPost(url);
        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
        }
        httpPost.setHeader("token", token);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            httpPost.abort();
            throw new RuntimeException("HttpClient,error status code :" + statusCode);
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            response.close();
            return result;
        } else {
            return null;
        }
    }
	
  	

}