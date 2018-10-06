package com.kendy.db.service.Impl;

import com.kendy.db.dao.ManDao;
import com.kendy.db.entity.Man;
import com.kendy.db.service.ManService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author linzt
 * @date
 */
@Service("manService")
public class ManServiceImpl extends GenericServiceImpl<ManDao, Man, String> implements ManService {

  @Override
  public Man getMan() {
    return getDao().getMan();
  }

}
