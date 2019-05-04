package com.kendy.db.service.Impl;

import com.kendy.db.dao.GenericDao;
import com.kendy.db.entity.GenericEntity;
import com.kendy.db.service.GenericService;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

/**
 * 子类覆盖save,update及insert方法时请加上@Transactional,否则会失去事务管理
 *
 * 摘自 @author chenxing21 www.10155.com
 *
 * @version 2017年8月10日
 */

public abstract class GenericServiceImpl<D extends GenericDao<E>, E extends GenericEntity, K extends Serializable>
    implements GenericService<E, K> {

  /**
   * Log variable for all child classes. Uses LoggerFactory.getLogger(getClass()) from Commons
   * Logging
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  Class<E> entityClass;

  String sqlNameSpace;

  @Autowired
  SqlSessionFactory sqlSessionFactory;

  @SuppressWarnings("unchecked")
  public GenericServiceImpl() {
    // 通过反射获取泛型类型
    ParameterizedType parameterizedType =
        (ParameterizedType) this.getClass().getGenericSuperclass();
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    for (Type actualTypeArgument : actualTypeArguments) {
      if (actualTypeArgument instanceof Class<?>) {
        Class<?> clazz = (Class<?>) actualTypeArgument;
        if (GenericEntity.class.isAssignableFrom(clazz)) {
          // 获取实体类类型
          entityClass = (Class<E>) clazz;
        }
        if (actualTypeArgument instanceof Class<?>
            && (Mapper.class.isAssignableFrom((Class<?>) actualTypeArgument)
            || GenericDao.class.isAssignableFrom((Class<?>) actualTypeArgument))) {
          // 获取SQL命名空间
          // 无法通过dao.getClass().getName()获取,dao已被代理,获取到的是proxy$xxxx
          sqlNameSpace = clazz.getName();
        }
      }
    }
  }

  @PostConstruct
  protected void init() {
    //cacheRedisMesaageService.addGenericService(this.sqlNameSpace, this);
  }

  @Autowired
  protected D dao;

  /**
   * 获取数据库Dao
   */
  protected D getDao() {
    return dao;
  }

  /**
   * {@inheritDoc}
   */
  public List<E> getAll() {
    return dao.selectAll();
  }

  /**
   * {@inheritDoc}
   */
  public E get(K id) {
    E e = dao.selectByPrimaryKey(id);
    return e;
  }

  /**
   * {@inheritDoc}
   */
  public boolean exists(K id) {
    return dao.selectByPrimaryKey(id) != null;
  }

  /**
   * {@inheritDoc}
   */

  @Transactional
  public int save(E object) {
    return dao.insert(object);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int save(List<E> list) {
    int count = 0;
    // 新获取一个模式为BATCH，自动提交为false的session
    // 如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
    String sqlId = sqlNameSpace + ".insert";
    SqlSession sqlSession = null;
    try {
      sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
      if (null != list && list.size() > 0) {
        int lsize = list.size();
        for (int i = 0, n = list.size(); i < n; i++) {
          // SqlSession session = sqlSessionFactory.openSession();
          E e = list.get(i);
          sqlSession.insert(sqlId, e);
          // save(list.get(i));
          if ((i > 0 && i % 1000 == 0) || i == lsize - 1) {
            // 手动每1000个一提交，提交后无法回滚
            sqlSession.commit();
            // 清理缓存，防止溢出
            sqlSession.clearCache();
          }
          count++;
        }
      }
    } catch (Exception e) {
      // 没有提交的数据可以回滚
      sqlSession.rollback();
      logger.error("批量插入异常", e);
    } finally {
      sqlSession.close();
    }

    return count;
  }

  @Transactional
  @Override
  public int saveOrUpdate(K key, E object) {
    int result = 0;
    try {
      if (!exists(key)) {
        result = save(object);
      } else {
        result = update(object);
      }
    } catch (DuplicateKeyException e) {
      logger.error("saveOrUpdate主键冲突: " + object, e);
      result = update(object);
    }
    return result;
  }


  /**
   * {@inheritDoc}
   */

  @Transactional
  @Override
  public int saveNotNull(E object) {
    return dao.insertSelective(object);
  }

  /**
   * {@inheritDoc}
   */

  @Transactional
  @Override
  public int remove(K id) {
    return dao.deleteByPrimaryKey(id);
  }

  /**
   * {@inheritDoc}
   */

  @Transactional
  @Override
  public int remove(List<K> list) {
    int i = 0;
    for (K k : list) {
      i = i + remove(k);
    }
    return i;
  }



  /*
   * (non-Javadoc)
   *
   * @see cn.womusic.bp.db.service.GenericService#update(java.lang.Object)
   */
  @Override
  @Transactional
  public int update(E entity) {
    return dao.updateByPrimaryKey(entity);
  }

  @Override
  @Transactional
  public int update(List<E> list) {
    int i = 0;
    for (E e : list) {
      i = i + update(e);
    }
    return i;
  }


  /*
   * (non-Javadoc)
   *
   * @see cn.womusic.bp.db.service.GenericService#updateNotNull(java.lang.Object)
   */
  @Override
  @Transactional
  public int updateNotNull(E entity) {
    return dao.updateByPrimaryKeySelective(entity);
  }

  /*
   * (non-Javadoc)
   *
   * @see cn.womusic.bp.db.service.GenericService#getByExample(java.lang.Object)
   */
  @Override
  public List<E> getByExample(E entity) {
    return dao.selectByExample(entity);
  }

  /*
   * (non-Javadoc)
   *
   * @see cn.womusic.bp.db.service.GenericService#count(java.lang.Object)
   */
  @Override
  public int count(E entity) {
    return dao.selectCount(entity);
  }

  /**
   * 根据SqlId删除,如有业务缓存,请自行删除
   */
  @Transactional
  protected int deleteBySqlId(String sqlId, Object parameter) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.delete(sqlNameSpace + "." + sqlId, parameter);
    }
  }

  /**
   * 根据SqlId更新,如有业务缓存,请自行删除
   */
  @Transactional
  protected int updateBySqlId(String sqlId, Object parameter) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.update(sqlNameSpace + "." + sqlId, parameter);
    }
  }

  /**
   * 根据SqlId查询
   */
  @Transactional
  protected List<E> selectBySqlId(String sqlId, Object parameter) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      return sqlSession.selectList(sqlNameSpace + "." + sqlId, parameter);
    }
  }

  /**
   * 如果返回值不为空,则会在get时自动放入业务缓存,remove及update时自动清理缓存 <br /> <br /> 子类重写此方法并返回cacheKey即可享受统一业务缓存
   */
  protected String getCacheKey(K key, E entity) {
    return null;
  }

  /**
   * 清除iBatis缓存
   */
  protected void clearLocalCache() {
    if (this.sqlSessionFactory.getConfiguration().hasCache(this.sqlNameSpace)) {
      try {
        Cache cache = this.sqlSessionFactory.getConfiguration().getCache(this.sqlNameSpace);
        cache.clear();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }



}
