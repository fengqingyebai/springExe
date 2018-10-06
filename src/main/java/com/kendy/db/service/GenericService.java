package com.kendy.db.service;

import com.kendy.db.entity.GenericEntity;
import java.io.Serializable;
import java.util.List;

public interface GenericService<E extends GenericEntity, K extends Serializable> {

  public List<E> getAll();

  public E get(K id);

  /**
   * 根据主键更新实体全部字段，null值会被更新
   * 
   * @param entity
   * @return
   */
  int update(E entity);

  /**
   * 批量根据主键更新实体全部字段，null值会被更新
   * 
   * @return
   */
  int update(List <E> list);

  /**
   * 保存一个实体，null的属性也会保存，不会使用数据库默认值
   *
   * @param entity
   * @return
   */
  int save(E entity);

  /**
   * 批量保存实体，同save
   *
   * @param list
   * @return
   */
  int save(List <E> list);

  /**
   * 保存一个实体，null的属性不会保存，会使用数据库默认值
   *
   * @param entity
   * @return
   */
  int saveNotNull(E entity);

  /**
   * 保存或更新一个实体<br/>
   * 先尝试插入,主键冲突则进行修改
   *
   * @param id
   * @param entity
   * @return
   */
  int saveOrUpdate(K id, E entity);

  /**
   * 删除
   *
   * @param id
   * @return
   */
  int remove(K id);

  /**
   * 批量删除
   *
   * @param list
   * @return
   */
  int remove(List <K> list);

  /**
   * 根据主键更新属性不为null的值
   * 
   * @param entity
   * @return
   */
  int updateNotNull(E entity);

  int count(E entity);

  List<E> getByExample(E entity);

}
