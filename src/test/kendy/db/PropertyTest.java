package kendy.db;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author linzt
 * @date
 */
public class PropertyTest extends BaseTest {

  @Value("${db.driver}")
  private String dbName;

  @Test
  public void test(){

    System.out.println("finishes..." + dbName);
  }

}
