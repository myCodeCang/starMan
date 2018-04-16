package com.thinkgem.jeesite.modules.md.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils2;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/2.
 */
public class MdTradeMain extends DataEntity<MdTradeMain> {
    private static final long serialVersionUID = 1L;

    private String groupId;     //明星商品id

    private String isTransDay;    //是否是交易日

    private String isOverTop;    //是否超出振幅配置

    private int amount;         //交易数量

    private BigDecimal transactionPrice;    //成交价

    private BigDecimal openingPrice;        //开盘价

    private BigDecimal closingPrice;        //收盘价

    private BigDecimal highestPrice;        //最高价

    private BigDecimal lowestPrice;         //最低价


    //拓展字段
    private Date createDateTimeBegin;
    private Date createDateEnd;


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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(BigDecimal transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(BigDecimal closingPrice) {
        this.closingPrice = closingPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getIsTransDay() {
        return isTransDay;
    }

    public void setIsTransDay(String isTransDay) {
        this.isTransDay = isTransDay;
    }

    public String getIsOverTop() {
        return isOverTop;
    }

    public void setIsOverTop(String isOverTop) {
        this.isOverTop = isOverTop;
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

    @Override
    protected void validateModule(boolean isInsert) {
        if (StringUtils2.isEmpty(groupId)) {
            throw new ValidationException("商品ID不能为空!");
        }

        if (amount < 0) {
            throw new ValidationException("交易数量不合法!");
        }

        if (transactionPrice == null) {
            throw new ValidationException("交易价不能为空");
        }

        if (openingPrice == null) {
            throw new ValidationException("开盘价不能为空");
        }

        if (closingPrice == null) {
            throw new ValidationException("收盘价不能为空");
        }

        if (highestPrice == null) {
            throw new ValidationException("最高价不能为空");
        }

        if (lowestPrice == null) {
            throw new ValidationException("最低价不能为空");
        }
    }
}
