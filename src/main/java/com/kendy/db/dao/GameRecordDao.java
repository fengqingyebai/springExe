package com.kendy.db.dao;

import com.kendy.db.entity.GameRecord;
import com.kendy.entity.TotalInfo2;
import com.kendy.model.GameRecordModel;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface GameRecordDao extends GenericDao<GameRecord>, Mapper<GameRecord> {

  @Update("update game_record set personal_jiesuan = '1' where playerId = #{playerId}")
  int updatePersonalJieSuan(@Param("playerId")String playerId);

  @Select("select * from game_record r where clubId = #{clubId} and soft_time = #{softDate} and hshb_type = '1' and personal_jiesuan = '0' ")
  List<GameRecord> getPersonalRecords(@Param("softDate") String softDate, @Param("clubId") String clubId);

//  @Insert("insert into users(name, age) values(#{name}, #{age})")
//  public int insertUser(User user);
//  @Delete("delete from users where id=#{id}")
//  public int deleteUserById(int id);
//  @Update("update users set name=#{name},age=#{age} where id=#{id}")
//  public int updateUser(User user);
//  @Select("select * from users where id=#{id}")
//  public User getUserById(int id);
//  @Select("select * from users")
//  public List<User> getAllUser();

  List<GameRecordModel> test1();

  List<GameRecordModel> getGameRecordsByMaxTime(@Param("maxRecordTime") String maxRecordTime);

  List<GameRecordModel> getGameRecordsByMaxTimeAndClub(@Param("maxRecordTime") String maxRecordTime, @Param("clubId") String clubId);

  List<GameRecordModel> getGameRecordsByClubId(@Param("clubId")String clubId);

  List<GameRecordModel> getStaticDetailRecords(@Param("clubId")String clubId, @Param("teamId") String teamId, @Param("softTime") String softTime);

  String getTotalZJByPId(@Param("playerId")String playerId);

  List<GameRecordModel> getRecordsByPlayerId( @Param("softTime") String softTime, @Param("clubId")String clubId, @Param("playerId") String playerId);
}