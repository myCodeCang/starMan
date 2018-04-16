package main.qysoft.md.entity.akkadto;

import main.qysoft.md.actors.ActorMsg;
import main.qysoft.md.entity.akkadto.MdTradeDTO;

import java.math.BigDecimal;

/**
 * 撮合结果DTO，用户在Actor间传递数据
 * Created by kevin on 2017/10/23.
 */
public final class TransResultDTO extends BaseDTO {
    private final ActorMsg msg;

    private final MdTradeDTO newPublishedTrade;

    private final MdTradeDTO tradeInQueue;

    private final BigDecimal currentTransPrice;  //成交价

    private final long transNum;     //交易量

    private long timeStamp;     // 撮合时间戳

    public TransResultDTO(ActorMsg msg, MdTradeDTO newPublishedTrade, MdTradeDTO tradeInQueue, BigDecimal currentTransPrice, long transNum, long timeStamp) {
        this.msg = msg;
        this.newPublishedTrade = newPublishedTrade;
        this.tradeInQueue = tradeInQueue;
        this.currentTransPrice = currentTransPrice;
        this.transNum = transNum;
        this.timeStamp = timeStamp;
    }

    public ActorMsg getMsg() {
        return msg;
    }

    public MdTradeDTO getNewPublishedTrade() {
        return newPublishedTrade;
    }

    public MdTradeDTO getTradeInQueue() {
        return tradeInQueue;
    }

    public BigDecimal getCurrentTransPrice() {
        return currentTransPrice;
    }

    public long getTransNum() {
        return transNum;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
