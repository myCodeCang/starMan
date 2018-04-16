package main.qysoft.md.entity;

/**
 * Created by kevin on 2017/10/26.
 */
public enum IsTransDay {
    NO("0"),
    YES("1");

    private String state;

    IsTransDay(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return state;
    }
}
