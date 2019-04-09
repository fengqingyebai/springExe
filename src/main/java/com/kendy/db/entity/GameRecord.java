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
     * 个人回保
     */
    @Column(name = "personal_huibao")
    private String personalHuibao;

    /**
     * 个人回水=个人回水比例 * 负数战绩
     */
    @Column(name = "personal_huishui")
    private String personalHuishui;

    /**
     * 联盟币扣减
     */
    @Column(name = "lmb_koujian")
    private String lmbKoujian;

    /**
     * 保险再分配
     */
    @Column(name = "baoxian_zaifenpei")
    private String baoxianZaifenpei;

    /**
     * 俱乐部再分配
     */
    @Column(name = "club_zaifenpei")
    private String clubZaifenpei;

    /**
     * 是否庄位-加勒比海
     */
    @Column(name = "is_zhuangwei")
    private String isZhuangwei;

    /**
     * 玩家手数-加勒比海
     */
    @Column(name = "wanjia_shoushu")
    private String wanjiaShoushu;

    /**
     * 玩家总加注-加勒比海
     */
    @Column(name = "wanjia_zong_jiazhu")
    private String wanjiaZongJiazhu;

    /**
     * 玩家总下注-加勒比海
     */
    @Column(name = "wanjia_zong_xiazhu")
    private String wanjiaZongXiazhu;

    /**
     * 俱乐部分成-德州牛仔
     */
    @Column(name = "club_fencheng")
    private String clubFencheng;

    /**
     * 联盟分成-德州牛仔
     */
    @Column(name = "lianmeng_fencheng")
    private String lianmengFencheng;

    /**
     * 所属代理-德州牛仔
     */
    @Column(name = "suoshu_proxy")
    private String suoshuProxy;

    /**
     * 代理返水-德州牛仔
     */
    @Column(name = "proxy_fanshui")
    private String proxyFanshui;

    /**
     * 个人是否已经结算
     */
    @Column(name = "personal_jiesuan")
    private String personalJiesuan;

    /**
     * 自定义回水回保类型：0团队  1个人
     */
    @Column(name = "hshb_type")
    private String hshbType;

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

    /**
     * 获取个人回保
     *
     * @return personal_huibao - 个人回保
     */
    public String getPersonalHuibao() {
        return personalHuibao;
    }

    /**
     * 设置个人回保
     *
     * @param personalHuibao 个人回保
     */
    public void setPersonalHuibao(String personalHuibao) {
        this.personalHuibao = personalHuibao == null ? null : personalHuibao.trim();
    }

    /**
     * 获取个人回水=个人回水比例 * 负数战绩
     *
     * @return personal_huishui - 个人回水=个人回水比例 * 负数战绩
     */
    public String getPersonalHuishui() {
        return personalHuishui;
    }

    /**
     * 设置个人回水=个人回水比例 * 负数战绩
     *
     * @param personalHuishui 个人回水=个人回水比例 * 负数战绩
     */
    public void setPersonalHuishui(String personalHuishui) {
        this.personalHuishui = personalHuishui == null ? null : personalHuishui.trim();
    }

    /**
     * 获取联盟币扣减
     *
     * @return lmb_koujian - 联盟币扣减
     */
    public String getLmbKoujian() {
        return lmbKoujian;
    }

    /**
     * 设置联盟币扣减
     *
     * @param lmbKoujian 联盟币扣减
     */
    public void setLmbKoujian(String lmbKoujian) {
        this.lmbKoujian = lmbKoujian == null ? null : lmbKoujian.trim();
    }

    /**
     * 获取保险再分配
     *
     * @return baoxian_zaifenpei - 保险再分配
     */
    public String getBaoxianZaifenpei() {
        return baoxianZaifenpei;
    }

    /**
     * 设置保险再分配
     *
     * @param baoxianZaifenpei 保险再分配
     */
    public void setBaoxianZaifenpei(String baoxianZaifenpei) {
        this.baoxianZaifenpei = baoxianZaifenpei == null ? null : baoxianZaifenpei.trim();
    }

    /**
     * 获取俱乐部再分配
     *
     * @return club_zaifenpei - 俱乐部再分配
     */
    public String getClubZaifenpei() {
        return clubZaifenpei;
    }

    /**
     * 设置俱乐部再分配
     *
     * @param clubZaifenpei 俱乐部再分配
     */
    public void setClubZaifenpei(String clubZaifenpei) {
        this.clubZaifenpei = clubZaifenpei == null ? null : clubZaifenpei.trim();
    }

    /**
     * 获取是否庄位-加勒比海
     *
     * @return is_zhuangwei - 是否庄位-加勒比海
     */
    public String getIsZhuangwei() {
        return isZhuangwei;
    }

    /**
     * 设置是否庄位-加勒比海
     *
     * @param isZhuangwei 是否庄位-加勒比海
     */
    public void setIsZhuangwei(String isZhuangwei) {
        this.isZhuangwei = isZhuangwei == null ? null : isZhuangwei.trim();
    }

    /**
     * 获取玩家手数-加勒比海
     *
     * @return wanjia_shoushu - 玩家手数-加勒比海
     */
    public String getWanjiaShoushu() {
        return wanjiaShoushu;
    }

    /**
     * 设置玩家手数-加勒比海
     *
     * @param wanjiaShoushu 玩家手数-加勒比海
     */
    public void setWanjiaShoushu(String wanjiaShoushu) {
        this.wanjiaShoushu = wanjiaShoushu == null ? null : wanjiaShoushu.trim();
    }

    /**
     * 获取玩家总加注-加勒比海
     *
     * @return wanjia_zong_jiazhu - 玩家总加注-加勒比海
     */
    public String getWanjiaZongJiazhu() {
        return wanjiaZongJiazhu;
    }

    /**
     * 设置玩家总加注-加勒比海
     *
     * @param wanjiaZongJiazhu 玩家总加注-加勒比海
     */
    public void setWanjiaZongJiazhu(String wanjiaZongJiazhu) {
        this.wanjiaZongJiazhu = wanjiaZongJiazhu == null ? null : wanjiaZongJiazhu.trim();
    }

    /**
     * 获取玩家总下注-加勒比海
     *
     * @return wanjia_zong_xiazhu - 玩家总下注-加勒比海
     */
    public String getWanjiaZongXiazhu() {
        return wanjiaZongXiazhu;
    }

    /**
     * 设置玩家总下注-加勒比海
     *
     * @param wanjiaZongXiazhu 玩家总下注-加勒比海
     */
    public void setWanjiaZongXiazhu(String wanjiaZongXiazhu) {
        this.wanjiaZongXiazhu = wanjiaZongXiazhu == null ? null : wanjiaZongXiazhu.trim();
    }

    /**
     * 获取俱乐部分成-德州牛仔
     *
     * @return club_fencheng - 俱乐部分成-德州牛仔
     */
    public String getClubFencheng() {
        return clubFencheng;
    }

    /**
     * 设置俱乐部分成-德州牛仔
     *
     * @param clubFencheng 俱乐部分成-德州牛仔
     */
    public void setClubFencheng(String clubFencheng) {
        this.clubFencheng = clubFencheng == null ? null : clubFencheng.trim();
    }

    /**
     * 获取联盟分成-德州牛仔
     *
     * @return lianmeng_fencheng - 联盟分成-德州牛仔
     */
    public String getLianmengFencheng() {
        return lianmengFencheng;
    }

    /**
     * 设置联盟分成-德州牛仔
     *
     * @param lianmengFencheng 联盟分成-德州牛仔
     */
    public void setLianmengFencheng(String lianmengFencheng) {
        this.lianmengFencheng = lianmengFencheng == null ? null : lianmengFencheng.trim();
    }

    /**
     * 获取所属代理-德州牛仔
     *
     * @return suoshu_proxy - 所属代理-德州牛仔
     */
    public String getSuoshuProxy() {
        return suoshuProxy;
    }

    /**
     * 设置所属代理-德州牛仔
     *
     * @param suoshuProxy 所属代理-德州牛仔
     */
    public void setSuoshuProxy(String suoshuProxy) {
        this.suoshuProxy = suoshuProxy == null ? null : suoshuProxy.trim();
    }

    /**
     * 获取代理返水-德州牛仔
     *
     * @return proxy_fanshui - 代理返水-德州牛仔
     */
    public String getProxyFanshui() {
        return proxyFanshui;
    }

    /**
     * 设置代理返水-德州牛仔
     *
     * @param proxyFanshui 代理返水-德州牛仔
     */
    public void setProxyFanshui(String proxyFanshui) {
        this.proxyFanshui = proxyFanshui == null ? null : proxyFanshui.trim();
    }

    /**
     * 获取个人是否已经结算
     *
     * @return personal_jiesuan - 个人是否已经结算
     */
    public String getPersonalJiesuan() {
        return personalJiesuan;
    }

    /**
     * 设置个人是否已经结算
     *
     * @param personalJiesuan 个人是否已经结算
     */
    public void setPersonalJiesuan(String personalJiesuan) {
        this.personalJiesuan = personalJiesuan == null ? null : personalJiesuan.trim();
    }

    /**
     * 获取自定义回水回保类型：0团队  1个人
     *
     * @return hshb_type - 自定义回水回保类型：0团队  1个人
     */
    public String getHshbType() {
        return hshbType;
    }

    /**
     * 设置自定义回水回保类型：0团队  1个人
     *
     * @param hshbType 自定义回水回保类型：0团队  1个人
     */
    public void setHshbType(String hshbType) {
        this.hshbType = hshbType == null ? null : hshbType.trim();
    }
}