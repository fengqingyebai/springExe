package com.kendy.db.dao;

import com.kendy.db.entity.Man;
import tk.mybatis.mapper.common.Mapper;

public interface ManDao extends Mapper<Man>, GenericDao<Man> {

  Man getMan();
}