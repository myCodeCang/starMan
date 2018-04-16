package com.thinkgem.jeesite.modules.lianlianPay.entity;

import java.io.Serializable;

public class ApiBaseBean implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String            sign;                 // 签名串
    private String            sign_type;            // 签名方式
    private String            ret_code;             // 交易代码
    private String            ret_msg;              // 交易描述
    public String             channel_order;        // 交易流水号
    private String            correlationID;        // 日志跟踪号

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getSign_type()
    {
        return sign_type;
    }

    public void setSign_type(String signType)
    {
        sign_type = signType;
    }

    public String getRet_code()
    {
        return ret_code;
    }

    public void setRet_code(String retCode)
    {
        ret_code = retCode;
    }

    public String getRet_msg()
    {
        return ret_msg;
    }

    public void setRet_msg(String retMsg)
    {
        ret_msg = retMsg;
    }

    public String getCorrelationID()
    {
        return correlationID;
    }

    public void setCorrelationID(String correlationID)
    {
        this.correlationID = correlationID;
    }

    public String getChannel_order()
    {
        return channel_order;
    }

    public void setChannel_order(String channelOrder)
    {
        channel_order = channelOrder;
    }

}
