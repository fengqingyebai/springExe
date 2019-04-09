package com.kendy.db.dao;

import com.kendy.db.entity.GameRecord;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface GameRecordDao extends GenericDao<GameRecord>, Mapper<GameRecord> {

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
}