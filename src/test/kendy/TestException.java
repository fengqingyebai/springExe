package kendy;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.kendy.model.RealBuyResult;
import com.kendy.util.StringUtil;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;

/**
 * @author linzt
 * @date
 */
public class TestException {

  public static void main(String[] args) {
    String playerId = "";
    String buyStack = "";

    try {

      throw  new ConnectTimeoutException("异常");
    } catch (ConnectTimeoutException e) {
      throwException(playerId, buyStack, e);
    }
//    catch (SocketTimeoutException e) {
//      throwException(playerId, buyStack, e);
//    } catch (IOException e) {
//      throwException(playerId, buyStack, e);
//    } catch (Exception e) {
//      throwException(playerId, buyStack, e);
//    }
  }

  private static void throwException(String playerId, String buyStack, Throwable e){
    e.printStackTrace();
    throw new RuntimeException(e.getMessage());
  }
}
