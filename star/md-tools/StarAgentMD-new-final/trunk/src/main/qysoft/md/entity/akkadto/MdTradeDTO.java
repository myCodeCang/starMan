package main.qysoft.md.entity.akkadto;

import main.qysoft.md.entity.MdTrade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 多线程之间传递的交易对象，不可变类
 * Created by kevin on 2017/10/21.
 */
public final class MdTradeDTO extends BaseDTO{
    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String groupId;

    private final Long userId;

    private final String userName;

    private final BigDecimal price;   //交易价格

    private final BigDecimal profit;    // 手续费

    private final BigDecimal fee;  // 手续费

    private final long publishNum;     //发布数量

    private final long remainNum;      //剩余数量

    private final int type;           //交易类型  0--卖出  1--买入

    private final BigDecimal bond;    //保证金

    private final int state;          //状态 0--交易中  1--用户撤单  2--系统下架  3--交易完成

    private final int lastState;          //上次交易状态 3--正常  1--交易未完成  2--交易失败

    private final Date createDate;	            // 创建日期

    private final Date updateDate;

    public MdTradeDTO(MdTrade mdTrade) {
        Objects.requireNonNull(mdTrade, "交易对象为NULL");
        id = mdTrade.getId();
        groupId = mdTrade.getGroupId();
        userId = mdTrade.getUserId();
        userName = mdTrade.getUserName();
        price = mdTrade.getPrice();
        fee = mdTrade.getFee();
        profit = mdTrade.getProfit();
        publishNum = mdTrade.getPublishNum();
        remainNum = mdTrade.getRemainNum();
        type = mdTrade.getType();
        bond = mdTrade.getBond();
        state = mdTrade.getState();
        lastState = mdTrade.getLastState();
        createDate = mdTrade.getCreateDate();
        updateDate = mdTrade.getUpdateDate();
    }

    public MdTradeDTO(Long id, String groupId, Long userId, String userName, BigDecimal price, BigDecimal fee, BigDecimal profit, long publishNum,
                      long remainNum, int type, BigDecimal bond, int state, int lastState, Date createDate, Date updateDate) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.userName = userName;
        this.price = price;
        this.fee = fee;
        this.profit = profit;
        this.publishNum = publishNum;
        this.remainNum = remainNum;
        this.type = type;
        this.bond = bond;
        this.state = state;
        this.lastState = lastState;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getPublishNum() {
        return publishNum;
    }

    public long getRemainNum() {
        return remainNum;
    }

    public int getType() {
        return type;
    }

    public BigDecimal getBond() {
        return bond;
    }

    public int getState() {
        return state;
    }

    public int getLastState() {
        return lastState;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public MdTrade createTradeEntity() {
        MdTrade mdTrade = new MdTrade();
        mdTrade.setId(getId());
        mdTrade.setBond(getBond());
        mdTrade.setUserName(getUserName());
        mdTrade.setType(getType());
        mdTrade.setPublishNum(getPublishNum());
        mdTrade.setPrice(getPrice());
        mdTrade.setFee(getFee());
        mdTrade.setGroupId(getGroupId());
        mdTrade.setCreateDate(getCreateDate());
        mdTrade.setRemainNum(getRemainNum());
        mdTrade.setState(getState());
        mdTrade.setLastState(getLastState());
        mdTrade.setProfit(getProfit());
        mdTrade.setUserId(getUserId());
        mdTrade.setUpdateDate(getUpdateDate());

        return mdTrade;
    }

    public MdTradeDTO copy() {
        return new MdTradeDTO(getId(), getGroupId(), getUserId(), getUserName(), getPrice(), getFee(), getProfit(), getPublishNum(),
                getRemainNum(), getType(), getBond(), getState(), getLastState(), getCreateDate(), getUpdateDate());
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

        MdTradeDTO otherTrade = (MdTradeDTO) other;
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
