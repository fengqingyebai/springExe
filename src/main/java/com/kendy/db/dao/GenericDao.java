package com.kendy.db.dao;

import java.util.List;
import org.apache.ibatis.session.RowBounds;


/**
 * 摘自@author chenxing21 www.10155.com
 * @version 2017年8月9日
 * @param <E>
 */
public interface GenericDao<E> {
  int deleteByPrimaryKey(Object id);

  int delete(E record);

  int insert(E record);

  int insertSelective(E record);

  E selectByPrimaryKey(Object id);

  List<E> selectAll();

  int updateByPrimaryKey(E record);

  int updateByPrimaryKeySelective(E record);

  List<E> selectByRowBounds(E record, RowBounds rowBounds);

  List<E> selectByExample(Object entity);

  int selectCount(E entity);
}
