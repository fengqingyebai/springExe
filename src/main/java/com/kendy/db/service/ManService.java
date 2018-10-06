package com.kendy.db.service;

import com.kendy.db.entity.Man;

public interface ManService extends GenericService<Man, String> {

  Man getMan();

}
