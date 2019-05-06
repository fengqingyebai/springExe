package kendy.db;

import com.kendy.db.entity.Player;
import com.kendy.db.service.PlayerService;
import com.kendy.excel.ExcelReaderUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author linzt
 * @date
 */
public class PlayerTest extends BaseTest {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  PlayerService playerService;

  @Test
  public void testInsertBatch() {
    try {
      ExcelReaderUtil excelReaderUtil = new ExcelReaderUtil();
      Map<String, Player> allPlayers =
          excelReaderUtil
              .readMembersRecord(new File("C:\\Users\\kendy\\Desktop\\四份表\\超盟四份表\\新名单登记表-德扑圈.xls"));
      // 插入到数据库
      List<Player> players = new ArrayList<>(allPlayers.values());
      long start = System.currentTimeMillis();
      System.out.println("人员总数：" + players.size() + "，开始批量导入数据库...");
      playerService.insertBatch(players);
      System.out.println("人员批量导入库耗时：" + (System.currentTimeMillis() - start));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}