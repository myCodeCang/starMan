package com.thinkgem.jeesite.modules.md.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/1.
 */
public class MdTrade extends DataEntity<MdTrade> {
    private static final long serialVersionUID = 1L;

    private String groupId;

    private String userId;

    private String userName;

    private BigDecimal price;   //交易价格

    private int publishNum;     //发布数量

    private int remainNum;      //剩余数量

    private int type;           //交易类型  0--卖出  1--买入

    private BigDecimal bond;    //保证金

    private int state;          //状态 0--交易中  1--用户撤单  2--系统下架  3--交易完成

    private int laststate; //上一次交易状态

    private BigDecimal profit;    //手续费

    private BigDecimal fee; //手续费百分比


    //查询字段
    private Date createDateBegin;
    private Date createDateEnd;

    private Date createDateTimeBegin;
    private Date createDateTimeEnd;
    //拓展字段
    private Integer nowNum;
    private String name;  //产品名称
    private String trueName; //用户真实姓名

    public Integer getNowNum() {
        return nowNum;
    }

    public void setNowNum(Integer nowNum) {
        this.nowNum = nowNum;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(int publishNum) {
        this.publishNum = publishNum;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BigDecimal getBond() {
        return bond;
    }

    public void setBond(BigDecimal bond) {
        this.bond = bond;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateDateBegin() {
        return createDateBegin;
    }

    public void setCreateDateBegin(Date createDateBegin) {
        this.createDateBegin = createDateBegin;
    }

    public Date getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public Date getCreateDateTimeBegin() {
        return createDateTimeBegin;
    }

    public void setCreateDateTimeBegin(Date createDateTimeBegin) {
        this.createDateTimeBegin = createDateTimeBegin;
    }

    public Date getCreateDateTimeEnd() {
        return createDateTimeEnd;
    }

    public void setCreateDateTimeEnd(Date createDateTimeEnd) {
        this.createDateTimeEnd = createDateTimeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public int getLaststate() {
        return laststate;
    }

    public void setLaststate(int laststate) {
        this.laststate = laststate;
    }

    @Override
    protected void validateModule(boolean isInsert) {
        if (StringUtils2.isEmpty(userId)) {
            throw new ValidationException("用户ID不能为空!");
        }

        if (StringUtils2.isEmpty(userName)) {
            throw new ValidationException("用户名不能为空!");
        }

        if (StringUtils2.isEmpty(groupId)) {
            throw new ValidationException("商品ID不能为空!");
        }

        if (publishNum <= 0) {
            throw new ValidationException("发布数量不能为0!");
        }
    }

    @Override
    public void preInsert() throws ValidationException {
        validateModule(true);
        super.preInsert();
    }

    @Override
    public void preUpdate() throws ValidationException {
        validateModule(false);
        super.preUpdate();
    }
}
