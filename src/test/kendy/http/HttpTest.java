package kendy.http;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.kendy.model.CoinBuyerRusult;
import com.kendy.model.RealBuyResult;
import com.kendy.util.HttpUtils;
import com.kendy.util.HttpUtils.HttpResult;
import com.kendy.util.StringUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author linzt
 */
public class HttpTest {

  public static void main(String[] args) {
   // buyLmbCoin();
    HttpUtils httpUtils = HttpUtils.getInstance();

    Map<String, String> params = new HashMap<>();
    params.put("", "");
    Map<String, String> header = new HashMap<>();
    header.put("token", "305c300d06092a864886f70d0101010500034b003048024100cad9d38ddd598ccfbe289965a9f9b2ab75c093b54d41a95e112d403df556fcad8543e1d95a4d6fcbfd191c11db1b6e392ce7a7f954c3794058fccfead11adbd30203010001");
    header.put("Accept", "application/json;charset=UTF-8");
    header.put("X-Requested-With", "XMLHttpRequest");
    header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    Charset charset = Charset.defaultCharset();
    String url = "http://cms.pokermanager.club/cms-api/leaguecredit/getLeagueCoinApplyList";
    try {
      HttpResult result = httpUtils.formPost(url, charset, params, header);
      if (result.isOK()) {
        String content = result.getContent();
        System.out.println("content = " + content);
        CoinBuyerRusult lmbBuyers = JSON.parseObject(content, CoinBuyerRusult.class);
        if (lmbBuyers == null) {
          throw new RuntimeException("获取联盟币玩家列表为空！");
        }
        if(lmbBuyers.getiErrCode()!=0){
          String errMsg = "获取联盟币玩家列表返回错误码：" + lmbBuyers.getiErrCode();
          // TODO log this error mssage
          throw new RuntimeException(errMsg);
        }
        // TODO 进行成功的逻辑处理

      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void buyLmbCoin() {
    HttpUtils httpUtils = HttpUtils.getInstance();
    String ACCEPT_BUY_URL = "http://cms.pokermanager.club/cms-api/leaguecredit/acceptLeagueCoinApply";

    Map<String, String> params = new HashMap<>();
    params.put("uniqueId", "12368");
    params.put("userShowId", "1871535391");
    Map<String, String> header = new HashMap<>();
    header.put("token", "305c300d06092a864886f70d0101010500034b003048024100cad9d38ddd598ccfbe289965a9f9b2ab75c093b54d41a95e112d403df556fcad8543e1d95a4d6fcbfd191c11db1b6e392ce7a7f954c3794058fccfead11adbd30203010001");
    header.put("Accept", "application/json;charset=UTF-8");
    header.put("X-Requested-With", "XMLHttpRequest");
    header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    String result = "";
    try {
      HttpResult httpResult = httpUtils.formPost(ACCEPT_BUY_URL, Charset.defaultCharset(),
          params, header);
      result = httpResult.getContent();
      System.out.println("result = " + result);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
