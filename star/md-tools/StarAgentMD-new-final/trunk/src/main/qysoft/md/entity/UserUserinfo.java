package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kevin on 2017/10/23.
 */
@Entity
@Table(name="USER_USERINFO")
public class UserUserinfo extends BaseEntity {
    public static final int IS_USER_CENTER = 1;

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private long id;

    @Column(name="user_name")
    private String userName;

    @Column(name="mobile")
    private String mobile;

    @Column(name="true_name")
    private String trueName;

    @Column(name="user_nicename")
    private String nickName;

    @Column(name="user_pass")
    private String userPass;

    @Column(name="level_no")
    private int levelNo;

    @Column(name="office_id")
    private int officeId;

    @Column(name="user_level_id")
    private String userLevelId;


    @Column(name="user_status")
    private String userStatus;

    @Column(name="user_type")
    private String userType;

    @Column(name="renzheng")
    private String renzheng;

    @Column(name="money")
    private BigDecimal money;

    @Column(name="score")
    private BigDecimal score;

    @Column(name="coin")
    private BigDecimal coin;

    @Column(name="money2")
    private BigDecimal money2;

    @Column(name="money3")
    private BigDecimal money3;

    @Column(name="money4")
    private BigDecimal money4;

    @Column(name="money5")
    private BigDecimal money5;

    @Column(name="money6")
    private BigDecimal money6;

    @Column(name="parent_name")
    private String parentName;

    @Column(name="parent_list")
    private String parentList;

    @Column(name="is_check")
    private String isCheck;

    @Column(name="is_usercenter")
    private int isUserCenter;

    @Column(name="usercenter_address")
    private String userCenterAddress;

    @Column(name="usercenter_addtime")
    private Date userCenterAddtime;

    @Column(name="create_date")
    private Date createDate;

    @Column(name="update_date")
    private Date updateDate;

    @Column(name="active_status")
    private String activeStatus;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public String getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(String userLevelId) {
        this.userLevelId = userLevelId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRenzheng() {
        return renzheng;
    }

    public void setRenzheng(String renzheng) {
        this.renzheng = renzheng;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getCoin() {
        return coin;
    }

    public void setCoin(BigDecimal coin) {
        this.coin = coin;
    }

    public BigDecimal getMoney2() {
        return money2;
    }

    public void setMoney2(BigDecimal money2) {
        this.money2 = money2;
    }

    public BigDecimal getMoney3() {
        return money3;
    }

    public void setMoney3(BigDecimal money3) {
        this.money3 = money3;
    }

    public BigDecimal getMoney4() {
        return money4;
    }

    public void setMoney4(BigDecimal money4) {
        this.money4 = money4;
    }

    public BigDecimal getMoney5() {
        return money5;
    }

    public void setMoney5(BigDecimal money5) {
        this.money5 = money5;
    }

    public BigDecimal getMoney6() {
        return money6;
    }

    public void setMoney6(BigDecimal money6) {
        this.money6 = money6;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsUserCenter() {
        return isUserCenter;
    }

    public void setIsUserCenter(int isUserCenter) {
        this.isUserCenter = isUserCenter;
    }

    public String getUserCenterAddress() {
        return userCenterAddress;
    }

    public void setUserCenterAddress(String userCenterAddress) {
        this.userCenterAddress = userCenterAddress;
    }

    public Date getUserCenterAddtime() {
        return userCenterAddtime;
    }

    public void setUserCenterAddtime(Date userCenterAddtime) {
        this.userCenterAddtime = userCenterAddtime;
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

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    @Override
    protected ErrorMessages validate() {
        return null;
    }
}
