package kendy.db;

import com.kendy.db.dao.GameRecordDao;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.service.GameRecordService;
import com.kendy.model.GameRecordModel;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;

/**
 * @author linzt
 * @date
 */
public class TestDB extends BaseTest{

  @Resource
  private GameRecordService gameRecordService;

  @Test
  public void test1() {
    System.out.println(gameRecordService.getTotalZJByPId("1871535391"));
  }

  @Test
  public void test2() {
    List<GameRecordModel> gameRecords = gameRecordService.getGameRecordsByMaxTime("2019-04-10");
    System.out.println("gameRecords.size() = " + gameRecords.size());
  }

  @Test
  public void test() {
    if (gameRecordService != null) {
      System.out.println("Success, the dao class is not null !");
      List<GameRecord> gameRecords = gameRecordService.getAll();
      System.out.println("count:" + gameRecords.size());
    }
  }

}
