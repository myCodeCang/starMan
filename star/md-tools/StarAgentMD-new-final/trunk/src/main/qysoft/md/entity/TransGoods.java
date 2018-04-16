package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kevin on 2017/11/4.
 */
@Entity
@Table(name="TRANS_GOODS")
public class TransGoods extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="group_id")
    private String groupId;

    @Column(name="user_name")
    private String userName;

    @Column(name="num")
    private long num;

    @Column(name="buy_id")
    private Long buyId;

    @Column(name="status")
    private String status;

    @Column(name="create_date")
    private Date createDate;

    @Column(name="update_date")
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getBuyId() {
        return buyId;
    }

    public void setBuyId(Long buyId) {
        this.buyId = buyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return null;
    }
}
