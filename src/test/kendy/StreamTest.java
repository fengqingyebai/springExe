import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * 流表达式测试
 *
 * @author linzt
 * @date
 */
public class StreamTest {


  @Test
  public void testStream1(){
    List<String> list = Arrays.asList("A","B","C");
    list = list.stream().filter(e -> "D".equals(e)).collect(Collectors.toList());

    Assert.assertTrue(list != null);
  }


}
