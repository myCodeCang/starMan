package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="BASE_GOODS_GROUP")
public class BaseGoodsGroup extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="category_id")
    private String categoryId;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="num")
    private String num;

    @Column(name="money")
    private BigDecimal money;

    @Column(name="status")
    private String status;

    @Column(name="trans_start_time")
    private Date transStartTime;

    @Column(name="star_product_name")
    private String starProductName;

    @Column(name="star_product_detail")
    private String starProductDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransStartTime() {
        return transStartTime;
    }

    public void setTransStartTime(Date transStartTime) {
        this.transStartTime = transStartTime;
    }

    public String getStarProductName() {
        return starProductName;
    }

    public void setStarProductName(String starProductName) {
        this.starProductName = starProductName;
    }

    public String getStarProductDetail() {
        return starProductDetail;
    }

    public void setStarProductDetail(String starProductDetail) {
        this.starProductDetail = starProductDetail;
    }

    @Override
    protected ErrorMessages validate() {
        return null;
    }
}
