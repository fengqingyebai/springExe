package com.kendy.db.dao;

import com.kendy.db.entity.GameRecord;
import tk.mybatis.mapper.common.Mapper;

public interface GameRecordDao extends GenericDao<GameRecord>, Mapper<GameRecord> {
}