package com.kendy.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class GameRoomModel {

  // "createtime":1523160215149,
  // "roomid":28699483,
  // "gameroomtype":3,
  // "createuser":"银河VIP开桌",
  // "bigblind":10,
  // "roomname":"🏧51020-849",
  // "hands":0,
  // "smallblind":5,
  // "players":60,
  // "iAnte":2,
  // "leagueid":24461503,
  // "maxplayer":8

  /*
   * 创建时间
   */
  private String createtime;
  /*
   * 房间ID
   */
  private String roomid;
  /*
   * 房间类型
   */
  private String gameroomtype;
  /*
   * 创建人
   */
  private String createuser;
  /*
   * ？？大类？？
   */
  private String bigblind;
  /*
   * 房间名称
   */
  private String roomname;
  /*
   * 总手数
   */
  private String hands;
  /*
   * ？？小类？？
   */
  private String smallblind;
  /*
   * 玩家数量
   */
  private String players;
  /*
   * ？？iAnte？？
   */
  private String iAnte;
  /*
   * leagueid 团ID??
   */
  private String leagueid;
  /*
   * 最大玩家数
   */
  private String maxplayer;



  public static void main(String[] args) {
    String json =
        "{\"result\":{\"total\":1,\"list\":[{\"createtime\":1523188779115,\"roomid\":28707389,\"gameroomtype\":3,\"createuser\":\"银河ATM\",\"bigblind\":10,\"roomname\":\"🏧520-2k-238\",\"hands\":233,\"smallblind\":5,\"players\":136,\"iAnte\":5,\"leagueid\":21375883,\"maxplayer\":8}]},\"iErrCode\":0}";
    RespResult<GameRoomModel> parseObject =
        (RespResult<GameRoomModel>) JSON.parseObject(json, RespResult.class);
    System.out.println(parseObject.toString());

  }



  public String getCreatetime() {
    return createtime;
  }



  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }



  public String getRoomid() {
    return roomid;
  }



  public void setRoomid(String roomid) {
    this.roomid = roomid;
  }



  public String getGameroomtype() {
    return gameroomtype;
  }



  public void setGameroomtype(String gameroomtype) {
    this.gameroomtype = gameroomtype;
  }



  public String getCreateuser() {
    return createuser;
  }



  public void setCreateuser(String createuser) {
    this.createuser = createuser;
  }



  public String getBigblind() {
    return bigblind;
  }



  public void setBigblind(String bigblind) {
    this.bigblind = bigblind;
  }



  public String getRoomname() {
    return roomname;
  }



  public void setRoomname(String roomname) {
    this.roomname = roomname;
  }



  public String getHands() {
    return hands;
  }



  public void setHands(String hands) {
    this.hands = hands;
  }



  public String getSmallblind() {
    return smallblind;
  }



  public void setSmallblind(String smallblind) {
    this.smallblind = smallblind;
  }



  public String getPlayers() {
    return players;
  }



  public void setPlayers(String players) {
    this.players = players;
  }



  public String getiAnte() {
    return iAnte;
  }



  public void setiAnte(String iAnte) {
    this.iAnte = iAnte;
  }



  public String getLeagueid() {
    return leagueid;
  }



  public void setLeagueid(String leagueid) {
    this.leagueid = leagueid;
  }



  public String getMaxplayer() {
    return maxplayer;
  }



  public void setMaxplayer(String maxplayer) {
    this.maxplayer = maxplayer;
  }



}
