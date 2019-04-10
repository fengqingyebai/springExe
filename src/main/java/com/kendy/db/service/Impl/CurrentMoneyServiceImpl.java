package com.kendy.db.service.Impl;

import com.kendy.db.dao.CurrentMoneyDao;
import com.kendy.db.entity.CurrentMoney;
import com.kendy.db.entity.CurrentMoneyPK;
import com.kendy.db.service.CurrentMoneyService;
import org.springframework.stereotype.Service;

/**
 * @author linzt
 * @date
 */
@Service("currentMoneyService")
public class CurrentMoneyServiceImpl extends GenericServiceImpl<CurrentMoneyDao, CurrentMoney, CurrentMoneyPK>
    implements CurrentMoneyService {

  @Override
  public int removeAll() {
    return getDao().removeAll();
  }
}
