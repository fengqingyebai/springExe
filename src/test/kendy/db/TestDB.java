package kendy.db;

import com.kendy.db.DBUtil;
import com.kendy.db.dao.GameRecordDao;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.service.GameRecordService;
import com.kendy.entity.Club;
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

  @Test
  public void test3() throws Exception {
    DBUtil dbUtil = new DBUtil();
    for (int i = 0; i < 50; i++) {
      Club club = new Club();
      club.setClubId(i+"");
      club.setName("俱乐部" + i);
      club.setEdu("0");
      club.setZhuoFei("0");
      club.setYiJieSuan("0");
      club.setZhuoFei2("10" + i);
      club.setZhuoFei3("12"+i) ;
      club.setYiJieSuan2("0");
      club.setYiJieSuan3("0");
      club.setEdu2("1" + i);
      club.setEdu3("2" + i);
      club.setGudong("C");
      dbUtil.saveOrUpdateClub(club);
    }
    System.out.println("finishes...");
  }

}
