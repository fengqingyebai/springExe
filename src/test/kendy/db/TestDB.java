package kendy.db;

import com.kendy.db.dao.GameRecordDao;
import com.kendy.db.entity.GameRecord;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;

/**
 * @author linzt
 * @date
 */
public class TestDB extends BaseTest{

  @Resource
  private GameRecordDao gameRecordDao;

  @Test
  public void test() {
    if (gameRecordDao != null) {
      System.out.println("Success, the dao class is not null !");
      List<GameRecord> gameRecords = gameRecordDao.selectAll();
      System.out.println("count:" + gameRecords.size());
    }


  }

}
