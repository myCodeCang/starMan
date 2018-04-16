package com.thinkgem.jeesite.modules.user.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.payment.entity.PaymentCallback;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/28.
 */
public class AlipayCallbackService implements PaymentCallback{
    @Autowired
    private UserUserinfoService userUserinfoService;
    @Autowired
    private UserChargeLogService userChargeLogService;
    @Override
    public boolean callback(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return false;
        }
        String userName = params.get("body");   //支付时，userName放在请求参数的body中
        String amount = params.get("total_fee");
        String tradeNo = params.get("trade_no");
        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(tradeNo);
        if (null != chargeLog) {
            return true;
        }
        try {
            userUserinfoService.updateUserMoney(userName, new BigDecimal(amount), tradeNo, EnumUtil.MoneyChangeType.ALIPAY_RECHARGE);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
