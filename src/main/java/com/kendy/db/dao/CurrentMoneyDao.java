package com.kendy.db.dao;

import com.kendy.db.entity.CurrentMoney;
import org.apache.ibatis.annotations.Delete;
import tk.mybatis.mapper.common.Mapper;

public interface CurrentMoneyDao extends GenericDao<CurrentMoney>, Mapper<CurrentMoney> {

  @Delete("delete from current_money")
  int removeAll();
}