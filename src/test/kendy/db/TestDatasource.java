package kendy.db;

import java.sql.Connection;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Test;

/**
 * @author linzt
 * @date
 */
public class TestDatasource extends BaseTest{

  @Resource
  private DataSource dataSource;

  @Test
  public void test1() throws Exception{
    if (dataSource != null) {
      Connection connection = dataSource.getConnection();
      if (connection != null) {
        System.out.println("connection = " + connection);
        return;
      }
    }
    System.out.println("Error！");
  }
}
