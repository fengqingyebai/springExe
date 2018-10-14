package com.kendy.db.dao;

import com.kendy.db.entity.TbPerson;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface TbPersonDao extends Mapper<TbPerson> { //GenericDao<TbPerson>,

  @Select(" select * from tb_person")
  List<TbPerson> myPerson();

}