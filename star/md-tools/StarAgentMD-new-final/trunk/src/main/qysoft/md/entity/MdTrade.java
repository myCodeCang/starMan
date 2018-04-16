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
@Table(name="MD_TRADE")
public class MdTrade extends BaseEntity {
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
    private BigDecimal profit;  //手续费

    @Column(name="publish_num")
    private long publishNum;     //发布数量

    @Column(name="remain_num")
    private long remainNum;      //剩余数量

    @Column(name="type")
    private int type;           //交易类型  0--卖出  1--买入

    @Column(name="bond")
    private BigDecimal bond;    //保证金

    @Column(name="fee")
    private BigDecimal fee;    //手续费单价

    @Column(name="state")
    private int state;          //状态 0--交易中  1--用户撤单  2--系统下架  3--交易完成

    @Column(name="last_state")
    private int lastState;          //上次交易状态 3--正常  1--交易未完成  2--交易失败

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

    public long getPublishNum() {
        return publishNum;
    }

    public void setPublishNum(long publishNum) {
        this.publishNum = publishNum;
    }

    public long getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(long remainNum) {
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

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
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

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal feePrice) {
        this.fee = feePrice;
    }

    @Override
    public ErrorMessages validate() {
        ErrorMessages errors = ErrorMessages.createErrorMessages();
        if (Objects.isNull(getId())) {
            errors.add("主键不能为空");
        }

        if (Objects.isNull(getUserId())) {
            errors.add("用户ID不能为空");
        }

        if (StringUtils.isEmpty(getUserName())) {
            errors.add("用户名不能为空");
        }

        if (StringUtils.isEmpty(getGroupId())) {
            errors.add("商品ID不能为空");
        }

        if (getPublishNum() < 0) {
            errors.add("发布数量不能小于0");
        }
        return errors;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (null == other) {
            return false;
        }

        if (getClass() != other.getClass()) {
            return false;
        }

        MdTrade otherTrade = (MdTrade) other;
        return getId().equals(otherTrade.getId()) &&
                getGroupId().equals(otherTrade.getGroupId()) &&
                getUserName().equals(otherTrade.getUserName()) &&
                getType() == otherTrade.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroupId(), getUserName(), getType());
    }
}
