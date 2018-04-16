package main.qysoft.md.entity;

public enum ChangeType {
    MD_SUCCESS_ADD_SELLER_MONEY("101"),    //撮合成功更新卖家余额
    USER_CANCEL_REFUND("102");      //用户撤单成功退还金额

    private String type;
    ChangeType(String type) {
        this.type = type;
    }

    public String orignal() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(type);
    }
}
