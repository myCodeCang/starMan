package main.qysoft.md.entity;

import org.apache.commons.lang3.StringUtils;
import main.qysoft.utils.ErrorMessages;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by kevin on 2017/10/2.
 */
@Entity
@Table(name="MD_TRADE_MAIN")
public class MdTradeMain extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="group_id")
    private String groupId;     //明星商品id

    @Column(name="amount")
    private long amount;         //交易数量

    @Column(name="transaction_price")
    private BigDecimal transactionPrice;    //成交价

    @Column(name="opening_price")
    private BigDecimal openingPrice;        //开盘价

    @Column(name="closing_price")
    private BigDecimal closingPrice;        //收盘价

    @Column(name="highest_price")
    private BigDecimal highestPrice;        //最高价

    @Column(name="lowest_price")
    private BigDecimal lowestPrice;         //最低价

    @Column(name="is_trans_day")
    private String isTransDay;              //是否交易日

    @Transient
    //@Column(name="is_over_top")
    private String isOverTop;               //是否已超过振幅

    @Column(name="create_date")
    private Date createDate;	            // 创建日期

    @Column(name="update_date")
    private Date updateDate;	            // 更新日期

    @Column(name="remarks")
    private String remarks;	                // 备注

    @Column(name="del_flag")
    private String delFlag; 	            // 删除标记（0：正常；1：删除；2：审核）

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsTransDay() {
        return isTransDay;
    }

    public void setIsTransDay(String isTransDay) {
        this.isTransDay = isTransDay;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getIsOverTop() {
        return isOverTop;
    }

    public void setIsOverTop(String isOverTop) {
        this.isOverTop = isOverTop;
    }

    @Override
    public ErrorMessages validate() {
        ErrorMessages errors = ErrorMessages.createErrorMessages();
        if (Objects.isNull(getId())) {
            errors.add("主键不能为空");
        }

        if (StringUtils.isEmpty(getGroupId())) {
            errors.add("商品ID不能为空!");
        }

        if (getAmount() < 0) {
            errors.add("交易数量不合法!");
        }

        if (Objects.isNull(getTransactionPrice())) {
            errors.add("交易价不能为空");
        }

        if (Objects.isNull(getOpeningPrice())) {
            errors.add("开盘价不能为空");
        }

        if (Objects.isNull(getClosingPrice())) {
            errors.add("收盘价不能为空");
        }

        if (Objects.isNull(getHighestPrice())) {
            errors.add("最高价不能为空");
        }

        if (Objects.isNull(getLowestPrice())) {
            errors.add("最低价不能为空");
        }
        return errors;
    }
}
