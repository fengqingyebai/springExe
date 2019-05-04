package kendy.db;
import com.alibaba.fastjson.JSON;
import java.util.Date;

import com.kendy.db.entity.CurrentMoney;
import com.kendy.db.service.CurrentMoneyService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

/**
 * @author linzt
 * @date
 */
public class CurrentMoneyServiceTest extends BaseTest {

  @Resource
  CurrentMoneyService currentMoneyService;

  @Test
  public void test1(){
    CurrentMoney entity = new CurrentMoney();
    entity.setId("111");
    entity.setName("小林");
    entity.setMoney("300");
    entity.setLmb("30");
    entity.setEdu("100");
    entity.setSum("1000");
    entity.setUpdateTime(new Date());
    entity.setDecription("system_auto_buy");
    int save = currentMoneyService.save(entity);

    List<CurrentMoney> all = currentMoneyService.getAll();
    if (CollectionUtils.isNotEmpty(all)) {
      System.out.println(JSON.toJSONString(all));
    }
  }

}
