package com.kendy.db.service;

import com.kendy.db.entity.CurrentMoney;
import com.kendy.db.entity.CurrentMoneyPK;

public interface CurrentMoneyService extends GenericService<CurrentMoney, CurrentMoneyPK> {

  int removeAll();
}
