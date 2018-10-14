package kendy.db;

import com.kendy.db.dao.ManDao;
import com.kendy.db.dao.TbPersonDao;
import com.kendy.db.entity.TbPerson;
import com.kendy.db.service.ManService;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linzt
 * @date
 */
public class TestDB extends BaseTest{

  @Autowired
  private TbPersonDao pDao;

  @Autowired
  private ManDao dao;

  @Autowired
  private ManService manService;

  @Test
  public void testInsert() {

    /*Man man = dao.getMan();

    List<Man> list = dao.selectAll();

    for (int i = 0; i < 10; i++) {
      Man entity = new Man();
      entity.setName("name" + i);
      entity.setAge("age"+i);
      dao.insert(entity);
    }

    //dao.insert();
    if(CollectUtil.isHaveValue(list)){
      list.stream().forEach(e-> System.out.println(e.getName()+"==" + e.getAge()));
    }*/

/*    TbPerson p = new TbPerson();
    p.setPersonId(System.currentTimeMillis() + "");
    p.setPersonName("kendy");
    p.setAge(28);

    int insert = pDao.insert(p);*/

    List<TbPerson> tbPeople = pDao.selectAll();

    String a_gir_i_love = "ss";

    System.out.println("finishes..." + tbPeople.get(0).toString());
  }

}
