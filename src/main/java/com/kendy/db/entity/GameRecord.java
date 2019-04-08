package com.kendy.db.entity;

import javax.persistence.*;

@Table(name = "game_record")
public class GameRecord extends GenericEntity {
    /**
     * 软件时间
     */
    @Id
    @Column(name = "soft_time")
    private String softTime;

    @Id
    @Column(name = "clubId")
    private String clubid;

    @Id
    @Column(name = "tableId")
    private String tableid;

    @Id
    @Column(name = "playerId")
    private String playerid;

    /**
     * 原始战绩
     */
    private String yszj;

    /**
     * 单条记录的保险
     */
    @Column(name = "singleInsurance")
    private String singleinsurance;

    /**
     * 单个场次中单个俱乐部的保险之和
     */
    @Column(name = "clubInsurance")
    private String clubinsurance;

    /**
     * 单个场次中的所有保险总和
     */
    @Column(name = "currentTableInsurance")
    private String currenttableinsurance;

    /**
     * 实收=原始战绩 > 0 ? 原始战绩 * 0.95 : 原始战绩
     */
    private String shishou;

    /**
     * 出回水=原始战绩>0 ? 0 : 原始战绩 * 团队回水比例 * (-1) 
     */
    @Column(name = "chuHuishui")
    private String chuhuishui;

    /**
     * 收回水=原始战绩>0 ? 0 :　｜原始战绩　*　0.05｜
     */
    @Column(name = "shouHuishui")
    private String shouhuishui;

    /**
     * 水后险=原始保险 * 0.95 * （-1）
     */
    private String shuihouxian;

    /**
     * 回保=原始保险 * 团队保险比例 * (-1)
     */
    @Column(name = "huiBao")
    private String huibao;

    /**
     * 合利润=IF(收回水>0,收回水+出回水+回保+水后检,0)
     */
    @Column(name = "heLirun")
    private String helirun;

    @Column(name = "lmType")
    private String lmtype;

    @Column(name = "finished_time")
    private String finishedTime;

    @Column(name = "isJiesuaned")
    private String isjiesuaned;

    private String level;

    @Column(name = "sumHandsCount")
    private String sumhandscount;

    @Column(name = "isCleared")
    private String iscleared;

    @Column(name = "beginPlayerName")
    private String beginplayername;

    @Column(name = "importTime")
    private String importtime;

    @Column(name = "juType")
    private String jutype;

    /**
     * 获取软件时间
     *
     * @return soft_time - 软件时间
     */
    public String getSoftTime() {
        return softTime;
    }

    /**
     * 设置软件时间
     *
     * @param softTime 软件时间
     */
    public void setSoftTime(String softTime) {
        this.softTime = softTime == null ? null : softTime.trim();
    }

    /**
     * @return clubId
     */
    public String getClubid() {
        return clubid;
    }

    /**
     * @param clubid
     */
    public void setClubid(String clubid) {
        this.clubid = clubid == null ? null : clubid.trim();
    }

    /**
     * @return tableId
     */
    public String getTableid() {
        return tableid;
    }

    /**
     * @param tableid
     */
    public void setTableid(String tableid) {
        this.tableid = tableid == null ? null : tableid.trim();
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
     * 获取原始战绩
     *
     * @return yszj - 原始战绩
     */
    public String getYszj() {
        return yszj;
    }

    /**
     * 设置原始战绩
     *
     * @param yszj 原始战绩
     */
    public void setYszj(String yszj) {
        this.yszj = yszj == null ? null : yszj.trim();
    }

    /**
     * 获取单条记录的保险
     *
     * @return singleInsurance - 单条记录的保险
     */
    public String getSingleinsurance() {
        return singleinsurance;
    }

    /**
     * 设置单条记录的保险
     *
     * @param singleinsurance 单条记录的保险
     */
    public void setSingleinsurance(String singleinsurance) {
        this.singleinsurance = singleinsurance == null ? null : singleinsurance.trim();
    }

    /**
     * 获取单个场次中单个俱乐部的保险之和
     *
     * @return clubInsurance - 单个场次中单个俱乐部的保险之和
     */
    public String getClubinsurance() {
        return clubinsurance;
    }

    /**
     * 设置单个场次中单个俱乐部的保险之和
     *
     * @param clubinsurance 单个场次中单个俱乐部的保险之和
     */
    public void setClubinsurance(String clubinsurance) {
        this.clubinsurance = clubinsurance == null ? null : clubinsurance.trim();
    }

    /**
     * 获取单个场次中的所有保险总和
     *
     * @return currentTableInsurance - 单个场次中的所有保险总和
     */
    public String getCurrenttableinsurance() {
        return currenttableinsurance;
    }

    /**
     * 设置单个场次中的所有保险总和
     *
     * @param currenttableinsurance 单个场次中的所有保险总和
     */
    public void setCurrenttableinsurance(String currenttableinsurance) {
        this.currenttableinsurance = currenttableinsurance == null ? null : currenttableinsurance.trim();
    }

    /**
     * 获取实收=原始战绩 > 0 ? 原始战绩 * 0.95 : 原始战绩
     *
     * @return shishou - 实收=原始战绩 > 0 ? 原始战绩 * 0.95 : 原始战绩
     */
    public String getShishou() {
        return shishou;
    }

    /**
     * 设置实收=原始战绩 > 0 ? 原始战绩 * 0.95 : 原始战绩
     *
     * @param shishou 实收=原始战绩 > 0 ? 原始战绩 * 0.95 : 原始战绩
     */
    public void setShishou(String shishou) {
        this.shishou = shishou == null ? null : shishou.trim();
    }

    /**
     * 获取出回水=原始战绩>0 ? 0 : 原始战绩 * 团队回水比例 * (-1) 
     *
     * @return chuHuishui - 出回水=原始战绩>0 ? 0 : 原始战绩 * 团队回水比例 * (-1) 
     */
    public String getChuhuishui() {
        return chuhuishui;
    }

    /**
     * 设置出回水=原始战绩>0 ? 0 : 原始战绩 * 团队回水比例 * (-1) 
     *
     * @param chuhuishui 出回水=原始战绩>0 ? 0 : 原始战绩 * 团队回水比例 * (-1) 
     */
    public void setChuhuishui(String chuhuishui) {
        this.chuhuishui = chuhuishui == null ? null : chuhuishui.trim();
    }

    /**
     * 获取收回水=原始战绩>0 ? 0 :　｜原始战绩　*　0.05｜
     *
     * @return shouHuishui - 收回水=原始战绩>0 ? 0 :　｜原始战绩　*　0.05｜
     */
    public String getShouhuishui() {
        return shouhuishui;
    }

    /**
     * 设置收回水=原始战绩>0 ? 0 :　｜原始战绩　*　0.05｜
     *
     * @param shouhuishui 收回水=原始战绩>0 ? 0 :　｜原始战绩　*　0.05｜
     */
    public void setShouhuishui(String shouhuishui) {
        this.shouhuishui = shouhuishui == null ? null : shouhuishui.trim();
    }

    /**
     * 获取水后险=原始保险 * 0.95 * （-1）
     *
     * @return shuihouxian - 水后险=原始保险 * 0.95 * （-1）
     */
    public String getShuihouxian() {
        return shuihouxian;
    }

    /**
     * 设置水后险=原始保险 * 0.95 * （-1）
     *
     * @param shuihouxian 水后险=原始保险 * 0.95 * （-1）
     */
    public void setShuihouxian(String shuihouxian) {
        this.shuihouxian = shuihouxian == null ? null : shuihouxian.trim();
    }

    /**
     * 获取回保=原始保险 * 团队保险比例 * (-1)
     *
     * @return huiBao - 回保=原始保险 * 团队保险比例 * (-1)
     */
    public String getHuibao() {
        return huibao;
    }

    /**
     * 设置回保=原始保险 * 团队保险比例 * (-1)
     *
     * @param huibao 回保=原始保险 * 团队保险比例 * (-1)
     */
    public void setHuibao(String huibao) {
        this.huibao = huibao == null ? null : huibao.trim();
    }

    /**
     * 获取合利润=IF(收回水>0,收回水+出回水+回保+水后检,0)
     *
     * @return heLirun - 合利润=IF(收回水>0,收回水+出回水+回保+水后检,0)
     */
    public String getHelirun() {
        return helirun;
    }

    /**
     * 设置合利润=IF(收回水>0,收回水+出回水+回保+水后检,0)
     *
     * @param helirun 合利润=IF(收回水>0,收回水+出回水+回保+水后检,0)
     */
    public void setHelirun(String helirun) {
        this.helirun = helirun == null ? null : helirun.trim();
    }

    /**
     * @return lmType
     */
    public String getLmtype() {
        return lmtype;
    }

    /**
     * @param lmtype
     */
    public void setLmtype(String lmtype) {
        this.lmtype = lmtype == null ? null : lmtype.trim();
    }

    /**
     * @return finished_time
     */
    public String getFinishedTime() {
        return finishedTime;
    }

    /**
     * @param finishedTime
     */
    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime == null ? null : finishedTime.trim();
    }

    /**
     * @return isJiesuaned
     */
    public String getIsjiesuaned() {
        return isjiesuaned;
    }

    /**
     * @param isjiesuaned
     */
    public void setIsjiesuaned(String isjiesuaned) {
        this.isjiesuaned = isjiesuaned == null ? null : isjiesuaned.trim();
    }

    /**
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    /**
     * @return sumHandsCount
     */
    public String getSumhandscount() {
        return sumhandscount;
    }

    /**
     * @param sumhandscount
     */
    public void setSumhandscount(String sumhandscount) {
        this.sumhandscount = sumhandscount == null ? null : sumhandscount.trim();
    }

    /**
     * @return isCleared
     */
    public String getIscleared() {
        return iscleared;
    }

    /**
     * @param iscleared
     */
    public void setIscleared(String iscleared) {
        this.iscleared = iscleared == null ? null : iscleared.trim();
    }

    /**
     * @return beginPlayerName
     */
    public String getBeginplayername() {
        return beginplayername;
    }

    /**
     * @param beginplayername
     */
    public void setBeginplayername(String beginplayername) {
        this.beginplayername = beginplayername == null ? null : beginplayername.trim();
    }

    /**
     * @return importTime
     */
    public String getImporttime() {
        return importtime;
    }

    /**
     * @param importtime
     */
    public void setImporttime(String importtime) {
        this.importtime = importtime == null ? null : importtime.trim();
    }

    /**
     * @return juType
     */
    public String getJutype() {
        return jutype;
    }

    /**
     * @param jutype
     */
    public void setJutype(String jutype) {
        this.jutype = jutype == null ? null : jutype.trim();
    }
}