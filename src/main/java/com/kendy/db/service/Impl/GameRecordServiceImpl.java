package com.kendy.db.service.Impl;

import com.kendy.db.dao.GameRecordDao;
import com.kendy.db.entity.GameRecord;
import com.kendy.db.entity.pk.GameRecordPK;
import com.kendy.db.service.GameRecordService;
import com.kendy.model.GameRecordModel;
import com.kendy.util.ErrorUtil;
import com.kendy.util.NumUtil;
import com.kendy.util.StringUtil;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author linzt
 * @date
 */
@Service("gameRecordService")
public class GameRecordServiceImpl extends GenericServiceImpl<GameRecordDao, GameRecord, GameRecordPK>
    implements GameRecordService {


  @Override
  public List<GameRecordModel> test1() {
    return getDao().test1();
  }

  @Override
  public List<GameRecordModel> getGameRecordsByMaxTime(String maxRecordTime) {
    try {
      return getDao().getGameRecordsByMaxTime(maxRecordTime);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：天）失败", e);
    }
    return Collections.emptyList();
  }

  @Override
  public List<GameRecordModel> getGameRecordsByMaxTimeAndClub(String maxRecordTime, String clubId) {
    try {
      return getDao().getGameRecordsByMaxTimeAndClub(maxRecordTime, clubId);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：当天俱乐部）失败", e);
    }
    return Collections.emptyList();
  }

  @Override
  public List<GameRecordModel> getGameRecordsByClubId(String clubId) {
    try {
      return getDao().getGameRecordsByClubId(clubId);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录（单位：当天俱乐部）失败", e);
    }
    return Collections.emptyList();
  }

  @Override
  public List<GameRecordModel> getStaticDetailRecords(String clubId, String teamId,
      String softTime) {
    try {
      return getDao().getStaticDetailRecords(clubId, teamId, softTime);
    } catch (Exception e) {
      ErrorUtil.err("获取最新的战绩记录失败", e);
    }
    return Collections.emptyList();
  }

  @Override
  public String getTotalZJByPId(String playerId) {
    try {
      String totalZj = getDao().getTotalZJByPId(playerId);
      return StringUtil.isBlank(totalZj) ? "0.0" : NumUtil.digit0(totalZj);
    } catch (Exception e) {
      ErrorUtil.err("会员历史战绩查询失败", e);
    }
    return "0.0";
  }

  @Override
  public List<GameRecord> getPersonalRecords(String softDate, String clubId) {
    return getDao().getPersonalRecords(softDate, clubId);
  }

  @Override
  public int updatePersonalJieSuan(String playerId) {
    return getDao().updatePersonalJieSuan(playerId);
  }

  @Override
  public List<GameRecordModel> getRecordsByPlayerId(String softTime, String clubId,
      String playerId) {
    return getDao().getRecordsByPlayerId(softTime, clubId, playerId);
  }


}
