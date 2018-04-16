package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/26.
 */
@Entity
@Table(name = "USER_ACCOUNTCHANGE")
public class UserAccountchange extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "change_money")
    private BigDecimal changeMoney;

    @Column(name = "before_money")
    private BigDecimal beforeMoney;

    @Column(name = "after_money")
    private BigDecimal afterMoney;

    @Column(name = "commt")
    private String commt;

    @Column(name = "status")
    private String status;

    @Column(name = "ischeck")
    private String isCheck;

    @Column(name = "change_type")
    private String changeType;

    @Column(name = "money_type")
    private String moneyType;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(BigDecimal changeMoney) {
        this.changeMoney = changeMoney;
    }

    public BigDecimal getBeforeMoney() {
        return beforeMoney;
    }

    public void setBeforeMoney(BigDecimal beforeMoney) {
        this.beforeMoney = beforeMoney;
    }

    public BigDecimal getAfterMoney() {
        return afterMoney;
    }

    public void setAfterMoney(BigDecimal afterMoney) {
        this.afterMoney = afterMoney;
    }

    public String getCommt() {
        return commt;
    }

    public void setCommt(String commt) {
        this.commt = commt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
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

    @Override
    protected ErrorMessages validate() {
        ErrorMessages errors = ErrorMessages.createErrorMessages();

        if (StringUtils.isBlank(getUserName())) {
            errors.add("用户名不能为空!");
        }

        if (null == getChangeMoney()) {
            errors.add("账变金额不能为空!");
        }

        if (null == getBeforeMoney()) {
            errors.add("账变前金额不能为空!");
        }

        if (null == getAfterMoney()) {
            errors.add("账变后金额不能为空!");
        }

        if (StringUtils.isBlank(getCommt())) {
            errors.add("备注不能为空!");
        }

        if (StringUtils.isBlank(getChangeType())) {
            errors.add("账变类型不能为空!");
        }

        if (StringUtils.isBlank(getMoneyType())) {
            errors.add("充值类型不能为空!");
        }

        return errors;
    }
}
