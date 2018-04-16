package main.qysoft.md.entity;

import org.apache.commons.lang3.StringUtils;
import main.qysoft.utils.ErrorMessages;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by kevin on 2017/10/1.
 */
@Entity
@Table(name="MD_TRADE_LOG")
public class MdTradeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="group_id")
    private String groupId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="price")
    private BigDecimal price;   //交易价格

    @Column(name="profit")
    private BigDecimal profit;  //收益（手续费）

    @Column(name="amount")
    private Long amount;         //交易数量

    @Column(name="type")
    private Integer type;        //交易类型  0--卖出  1--买入

    @Column(name="md_trade_id")
    private Long mdTradeId;

    @Column(name="message")
    private String message;     //描述信息

    @Column(name="create_date")
    private Date createDate;	            // 创建日期

    @Column(name="update_date")
    private Date updateDate;	            // 更新日期

    @Column(name="time_stamp")
    private Long timeStamp;

    @Column(name="remarks")
    private String remarks;	                // 备注

    @Column(name="del_flag")
    private String delFlag; 	            // 删除标记（0：正常；1：删除；2：审核）

    @Transient
    private Date createDateBegin;

    @Transient
    private Date createDateEnd;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public long getAmount() {
        return amount.longValue();
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getMdTradeId() {
        return mdTradeId;
    }

    public void setMdTradeId(Long mdTradeId) {
        this.mdTradeId = mdTradeId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
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

    @Override
    public ErrorMessages validate() {
        ErrorMessages errors = ErrorMessages.createErrorMessages();
        if (Objects.isNull(getId())) {
            errors.add("主键不能为空");
        }

        if (StringUtils.isEmpty(getGroupId())) {
            errors.add("商品组ID不能为空");
        }

        if (getAmount() < 0) {
            errors.add("交易数量不合法");
        }

        if (Objects.isNull(getUserId())) {
            errors.add("用户ID不能为空");
        }

        if (StringUtils.isEmpty(getUserName())) {
            errors.add("用户名不能为空");
        }
        return errors;
    }
}
