package com.kendy.db.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "current_money")
public class CurrentMoney extends GenericEntity {
    /**
     * 玩家ID
     */
    @Id
    private String id;

    /**
     * 名称，包括玩家名称，其他名称
     */
    @Id
    private String name;

    /**
     * 实时金额
     */
    private String money;

    /**
     * 联盟币
     */
    private String lmb;

    /**
     * 个人额度
     */
    private String edu;

    /**
     * 联合总额度=sum(实时金额)+max(edu)+sum(联盟币)
     */
    private String sum;

    /**
     * 结束时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 备注（用于记录修改的桌号）
     */
    private String decription;

    /**
     * 获取玩家ID
     *
     * @return id - 玩家ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置玩家ID
     *
     * @param id 玩家ID
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取名称，包括玩家名称，其他名称
     *
     * @return name - 名称，包括玩家名称，其他名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称，包括玩家名称，其他名称
     *
     * @param name 名称，包括玩家名称，其他名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取实时金额
     *
     * @return money - 实时金额
     */
    public String getMoney() {
        return money;
    }

    /**
     * 设置实时金额
     *
     * @param money 实时金额
     */
    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    /**
     * 获取联盟币
     *
     * @return lmb - 联盟币
     */
    public String getLmb() {
        return lmb;
    }

    /**
     * 设置联盟币
     *
     * @param lmb 联盟币
     */
    public void setLmb(String lmb) {
        this.lmb = lmb == null ? null : lmb.trim();
    }

    /**
     * 获取个人额度
     *
     * @return edu - 个人额度
     */
    public String getEdu() {
        return edu;
    }

    /**
     * 设置个人额度
     *
     * @param edu 个人额度
     */
    public void setEdu(String edu) {
        this.edu = edu == null ? null : edu.trim();
    }

    /**
     * 获取联合总额度=sum(实时金额)+max(edu)+sum(联盟币)
     *
     * @return sum - 联合总额度=sum(实时金额)+max(edu)+sum(联盟币)
     */
    public String getSum() {
        return sum;
    }

    /**
     * 设置联合总额度=sum(实时金额)+max(edu)+sum(联盟币)
     *
     * @param sum 联合总额度=sum(实时金额)+max(edu)+sum(联盟币)
     */
    public void setSum(String sum) {
        this.sum = sum == null ? null : sum.trim();
    }

    /**
     * 获取结束时间
     *
     * @return update_time - 结束时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置结束时间
     *
     * @param updateTime 结束时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取备注（用于记录修改的桌号）
     *
     * @return decription - 备注（用于记录修改的桌号）
     */
    public String getDecription() {
        return decription;
    }

    /**
     * 设置备注（用于记录修改的桌号）
     *
     * @param decription 备注（用于记录修改的桌号）
     */
    public void setDecription(String decription) {
        this.decription = decription == null ? null : decription.trim();
    }
}