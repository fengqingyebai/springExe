package com.kendy.db.service;

import com.kendy.db.entity.GameRecord;
import com.kendy.db.entity.pk.GameRecordPK;
import com.kendy.model.GameRecordModel;
import java.util.List;

public interface GameRecordService extends GenericService<GameRecord, GameRecordPK> {


  List<GameRecordModel> test1();

  /**
   * 获取最新的战绩记录列表（单位：天）
   * @param maxRecordTime eg.2019-04-10
   * @return
   */
  List<GameRecordModel> getGameRecordsByMaxTime(String maxRecordTime);

  /**
   * 获取最新的战绩记录列表（单位：当天某俱乐部）
   * @param maxRecordTime
   * @param clubId
   * @return
   */
  List<GameRecordModel> getGameRecordsByMaxTimeAndClub(String maxRecordTime, String clubId);


  List<GameRecordModel> getGameRecordsByClubId(String clubId);

  List<GameRecordModel> getStaticDetailRecords(String clubId, String teamId, String softTime);

  String getTotalZJByPId(String playerId);

  List<GameRecord> getPersonalRecords(String softDate, String clubId);

  int updatePersonalJieSuan(String playerId);

}
