package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 艺术品交易次数统计
 * Created by Administrator on 2017/6/8.
 */
public class TransGoodsNumReport extends DataEntity<TransGoodsNumReport> {

    private int goodsNum = 0;
    private int zero = 0;
    private int one  = 0;
    private int two  = 0;
    private int three  = 0;
    private int four  = 0;
    private int five  = 0;
    private int more  = 0;
    private int pickNum  = 0;
    private String groupId;
    private String groupName;
    private BigDecimal avePrice = new BigDecimal(0);


    public BigDecimal getAvePrice() {
        return avePrice;
    }

    public void setAvePrice(BigDecimal avePrice) {
        this.avePrice = avePrice;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getPickNum() {
        return pickNum;
    }

    public void setPickNum(int pickNum) {
        this.pickNum = pickNum;
    }

    public int getZero() {
        return zero;
    }

    public void setZero(int zero) {
        this.zero = zero;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    public int getThree() {
        return three;
    }

    public void setThree(int three) {
        this.three = three;
    }

    public int getFour() {
        return four;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }
}
