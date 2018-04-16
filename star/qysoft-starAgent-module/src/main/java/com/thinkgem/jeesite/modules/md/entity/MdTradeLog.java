package com.thinkgem.jeesite.modules.md.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/1.
 */
public class MdTradeLog extends DataEntity<MdTradeLog> {
    private static final long serialVersionUID = 1L;

    private String groupId;

    private String userId;

    private String userName;

    private BigDecimal price;   //交易价格

    private BigDecimal profit;  //收益（手续费）

    private int amount;         //交易数量

    private int type ;           //交易类型  2--卖出  1--买入

    private String message;     //描述信息

    private String isCheck;   //是否统计

    //拓展字段
    private String name;   //产品名称
    private String trueName;
    private String userType;
    private String shopId;
    private String userRemarks;
    private int amountSum; //当日成交量
    //查询字段
    private String createDateBegin;
    private String createDateEnd;
    private String parentListLike;
    private String sumBuy;
    private String sumSell;
    private String sumProfit;
    private BigDecimal volume;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public String getSumProfit() {
        return sumProfit;
    }

    public void setSumProfit(String sumProfit) {
        this.sumProfit = sumProfit;
    }

    public int getAmountSum() {
        return amountSum;
    }

    public void setAmountSum(int amountSum) {
        this.amountSum = amountSum;
    }

    public String getSumBuy() {
        return sumBuy;
    }

    public void setSumBuy(String sumBuy) {
        this.sumBuy = sumBuy;
    }

    public String getSumSell() {
        return sumSell;
    }

    public void setSumSell(String sumSell) {
        this.sumSell = sumSell;
    }

    public String getParentListLike() {
        return parentListLike;
    }

    public void setParentListLike(String parentListLike) {
        this.parentListLike = parentListLike;
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

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDateBegin() {
        return createDateBegin;
    }

    public void setCreateDateBegin(String createDateBegin) {
        this.createDateBegin = createDateBegin;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserRemarks() {
        return userRemarks;
    }

    public void setUserRemarks(String userRemarks) {
        this.userRemarks = userRemarks;
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

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
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

        if (amount <= 0) {
            throw new ValidationException("交易数量不能为0!");
        }
    }
}
