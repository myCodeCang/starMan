package main.qysoft.md.entity;

/**
 * Created by kevin on 2017/10/2.
 */
public enum TradeType {
    SELL_OUT(2),    //售出
    BUY_IN(1);      //买入

    private int type;
    TradeType(int type) {
        this.type = type;
    }

    public int orignal() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}
