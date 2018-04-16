package com.thinkgem.jeesite.modules.md.entity;

/**
 * Created by kevin on 2017/10/2.
 */
public enum GoodsStatus {
    NO_TRANS(0),    //未交易
    MD(1),      //撮合交易
    ATTORN(2);  //一口价交易
    private int type;
    GoodsStatus(int type) {
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
