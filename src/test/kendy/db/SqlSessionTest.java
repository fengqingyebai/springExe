package kendy.db;

import com.kendy.db.entity.TbPerson;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linzt
 * @date
 */
public class SqlSessionTest extends BaseTest{

  @Autowired
  private SqlSessionFactory sqlSessionFactory;

  @Autowired
  private SqlSessionTemplate sqlSessionTemplate;


  @Test
  public void sqlSessionFactoryTest(){
    // SqlSession 的实例不是线程安全的，因此是不能被共享的
    // 建议每次收到的 HTTP 请求，就可以打开一个 SqlSession，返回一个响应，就关闭它
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      if (sqlSession != null) {
        System.out.println("sqlSession is not null");
//        List<TbPerson> list = sqlSession.getMapper(TbPersonDao.class).myPerson();
//        //sqlSessionTemplate.getMapper()
//        System.out.println(list.get(0).toString());
      }
    }

    System.out.println("finishes...");
  }

}
