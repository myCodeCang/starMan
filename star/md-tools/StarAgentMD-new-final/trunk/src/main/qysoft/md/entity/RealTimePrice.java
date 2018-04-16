package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/11/3.
 */
@Entity
@Table(name="REAL_TIME_PRICE")
public class RealTimePrice extends BaseEntity  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="group_id")
    private String groupId;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="create_date")
    private Date createDate;

    @Column(name="time_stamp")
    private long timeStamp;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    protected ErrorMessages validate() {
        return null;
    }
}
