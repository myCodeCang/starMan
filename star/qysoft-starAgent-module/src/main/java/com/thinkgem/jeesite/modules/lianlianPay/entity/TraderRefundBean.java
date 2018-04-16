package com.thinkgem.jeesite.modules.lianlianPay.entity;

public class TraderRefundBean extends ApiBaseBean{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String            no_order;//原商家订单号
    private String            no_refund;//商家退款单号
    private String            dt_refund;//退款时间
    private String            oid_partner;//商户号
    private String            money_refund;//退款金额
    private String            refund_reason;//退款理由
    private String            risk_item;//暂时不用
    private String            notify_url;//通知地址
    private String            memo;//备注
    private int               type_refund;//是否实时退款 0表示非实时退款，1表示实时退款
    private String            account_name;//银行账户名称
    
    
    
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public int getType_refund() {
		return type_refund;
	}

	public void setType_refund(int type_refund) {
		this.type_refund = type_refund;
	}

	public String getRefund_reason()
    {
        return refund_reason;
    }

    public void setRefund_reason(String refundReason)
    {
        refund_reason = refundReason;
    }

    public String getNo_order()
    {
        return no_order;
    }

    public void setNo_order(String noOrder)
    {
        no_order = noOrder;
    }

    public String getNo_refund()
    {
        return no_refund;
    }

    public void setNo_refund(String noRefund)
    {
        no_refund = noRefund;
    }

    public String getDt_refund()
    {
        return dt_refund;
    }

    public void setDt_refund(String dtRefund)
    {
        dt_refund = dtRefund;
    }

    public String getOid_partner()
    {
        return oid_partner;
    }

    public void setOid_partner(String oidPartner)
    {
        oid_partner = oidPartner;
    }

    public String getMoney_refund()
    {
        return money_refund;
    }

    public void setMoney_refund(String moneyRefund)
    {
        money_refund = moneyRefund;
    }

    public String getRisk_item()
    {
        return risk_item;
    }

    public void setRisk_item(String riskItem)
    {
        risk_item = riskItem;
    }

    public String getNotify_url()
    {
        return notify_url;
    }

    public void setNotify_url(String notifyUrl)
    {
        notify_url = notifyUrl;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

}
