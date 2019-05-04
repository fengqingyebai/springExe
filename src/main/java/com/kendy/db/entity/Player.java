package com.kendy.db.entity;

import javax.persistence.*;

@Table(name = "members")
public class Player extends GenericEntity {
    @Id
    @Column(name = "playerId")
    private String playerid;

    @Column(name = "playerName")
    private String playername;

    private String gudong;

    @Column(name = "teamId")
    private String teamid;

    private String edu;

    @Column(name = "isParent")
    private String isparent;

    private String huibao;

    private String huishui;

  public Player() {
    super();
  }

  public Player(String gameId, String teamid, String gudong, String playerName, String edu, String isParent, String huibao, String huishui) {
    super();
    this.playerid = gameId;
    this.teamid = teamid;
    this.gudong = gudong;
    this.playername = playerName;
    this.edu = edu;
    this.isparent = isParent;
    this.huibao = huibao;
    this.huishui = huishui;
  }

  /**
     * @return playerId
     */
    public String getPlayerid() {
        return playerid;
    }

    /**
     * @param playerid
     */
    public void setPlayerid(String playerid) {
        this.playerid = playerid == null ? null : playerid.trim();
    }

    /**
     * @return playerName
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * @param playername
     */
    public void setPlayername(String playername) {
        this.playername = playername == null ? null : playername.trim();
    }

    /**
     * @return gudong
     */
    public String getGudong() {
        return gudong;
    }

    /**
     * @param gudong
     */
    public void setGudong(String gudong) {
        this.gudong = gudong == null ? null : gudong.trim();
    }

    /**
     * @return teamId
     */
    public String getTeamid() {
        return teamid;
    }

    /**
     * @param teamid
     */
    public void setTeamid(String teamid) {
        this.teamid = teamid == null ? null : teamid.trim();
    }

    /**
     * @return edu
     */
    public String getEdu() {
        return edu;
    }

    /**
     * @param edu
     */
    public void setEdu(String edu) {
        this.edu = edu == null ? null : edu.trim();
    }

    /**
     * @return isParent
     */
    public String getIsparent() {
        return isparent;
    }

    /**
     * @param isparent
     */
    public void setIsparent(String isparent) {
        this.isparent = isparent == null ? null : isparent.trim();
    }

    /**
     * @return huibao
     */
    public String getHuibao() {
        return huibao;
    }

    /**
     * @param huibao
     */
    public void setHuibao(String huibao) {
        this.huibao = huibao == null ? null : huibao.trim();
    }

    /**
     * @return huishui
     */
    public String getHuishui() {
        return huishui;
    }

    /**
     * @param huishui
     */
    public void setHuishui(String huishui) {
        this.huishui = huishui == null ? null : huishui.trim();
    }
}