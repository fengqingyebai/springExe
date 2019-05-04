package com.kendy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.kendy.util.ErrorUtil;

/**
 * 连接数据库
 *
 * @author 林泽涛
 * @time 2018年1月1日 下午10:54:27
 */
public class DBConnection {

  private static Logger log = Logger.getLogger(DBConnection.class);

  private static Connection connection = null;

  private static final String URL = "jdbc:mysql://localhost:3306/financial?autoReconnect=true&useSSL=false&&serverTimezone=Hongkong";
  private static final String USER = "root";
  private static final String PASSWORD = "123456";

  public DBConnection() {
    connection = null;
  }

  public static Connection getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      ErrorUtil.err("Where is your mysql JDBC Driver?");
      return null;
    }
    try {
      connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      //ErrorUtil.err("Connection Failed! ", e);
      e.printStackTrace();
      return null;
    }
    return connection;
  }

  public static void main(String[] args) {
    if (getConnection() == null) {
      System.out.println("fail to connect the database..");
      return;
    }
    System.out.println("success.." + System.currentTimeMillis());
  }

}

