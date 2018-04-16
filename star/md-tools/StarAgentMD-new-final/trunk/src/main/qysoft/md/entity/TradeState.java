package main.qysoft.md.entity;

/**
 * Created by kevin on 2017/10/2.
 */
public enum TradeState {
    ON_TRADING(4),  //交易中
    CANCEL_SUCCESS(5), //撤单成功
    USER_CANCEL(1),  //用户申请撤单
    SYSTEM_OFF(2),    //系统下架
    FINISH(3);     //交易完成

    private int state;

    TradeState(int state) {
        this.state = state;
    }

    public int orginal() {
        return state;
    }

    @Override
    public String toString() {
        return String.valueOf(state);
    }
}
