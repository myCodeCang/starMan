package main.qysoft.md.entity;

/**
 * Created by kevin on 2017/10/26.
 */
public enum TradeIsOverTop {
    NO("0"),
    YES("1");

    private String state;

    TradeIsOverTop(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
