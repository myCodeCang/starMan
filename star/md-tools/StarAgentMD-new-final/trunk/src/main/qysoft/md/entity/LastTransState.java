package main.qysoft.md.entity;

/**
 * Created by kevin on 2017/10/23.
 */
public enum LastTransState {
    UNFINISHED(1),  //交易未完成，有剩余可交易数量
    FAIL(2),    //交易失败
    NORMAL(3);     //正常状态

    private int state;

    LastTransState(int state) {
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
