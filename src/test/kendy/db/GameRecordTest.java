package kendy.db;

import com.alibaba.fastjson.JSON;
import com.kendy.db.dao.GameRecordDao;
import com.kendy.db.entity.GameRecord;
import com.kendy.model.GameRecordModel;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * @author linzt
 * @date
 */
public class GameRecordTest extends BaseTest{

  @Resource
  GameRecordDao gameRecordDao;

  @Test
  public void testJson(){
    GameRecordModel model = new GameRecordModel();
    model.setSoftTime("2019-04-09");
    model.setClubid("555559");
    model.setTableid("第120局");
    model.setPlayerid("123456");
    model.setLianmengFencheng("12.32");
    GameRecord entity = new GameRecord();
    BeanUtils.copyProperties(model, entity);
    String json = JSON.toJSONString(entity);
    System.out.println("json:" + json);
  }
  @Test
  public void testGameRecordAdd(){
    GameRecord entity = new GameRecord();
    entity.setSoftTime("2019-04-09");
    entity.setClubid("555559");
    entity.setTableid("第120局");
    entity.setPlayerid("123456");
    entity.setLianmengFencheng("12.32");
    gameRecordDao.insert(entity);
    System.out.println("success");
  }




}
