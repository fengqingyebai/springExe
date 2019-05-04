package com.kendy.db.service;

import com.kendy.db.entity.GenericEntity;
import java.io.Serializable;
import java.util.List;

public interface GenericService<E extends GenericEntity, K extends Serializable> {

  List<E> getAll();

  E get(K id);

  /**
   * 根据主键更新实体全部字段，null值会被更新
   */
  int update(E entity);

  /**
   * 批量根据主键更新实体全部字段，null值会被更新
   */
  int update(List<E> list);

  /**
   * 保存一个实体，null的属性也会保存，不会使用数据库默认值
   */
  int save(E entity);

  /**
   * 批量保存实体，同save
   */
  int save(List<E> list);

  /**
   * 保存一个实体，null的属性不会保存，会使用数据库默认值
   */
  int saveNotNull(E entity);

  /**
   * 保存或更新一个实体<br/> 先尝试插入,主键冲突则进行修改
   */
  int saveOrUpdate(K id, E entity);

  /**
   * 删除
   */
  int remove(K id);

  /**
   * 批量删除
   */
  int remove(List<K> list);

  /**
   * 根据主键更新属性不为null的值
   */
  int updateNotNull(E entity);

  int count(E entity);

  List<E> getByExample(E entity);

}
