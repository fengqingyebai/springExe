package com.kendy.service;

import com.kendy.constant.DataConstans;
import com.kendy.enums.PermissionTabEnum;

/**
 * @author linzt
 * @date
 */
public abstract class BasicService {

  public boolean hasPermission(PermissionTabEnum tabEnum){
    return DataConstans.permissions.containsKey(tabEnum.getTabName());
  }

  public boolean noPermission(PermissionTabEnum tabEnum){
    return !hasPermission(tabEnum);
  }


}
