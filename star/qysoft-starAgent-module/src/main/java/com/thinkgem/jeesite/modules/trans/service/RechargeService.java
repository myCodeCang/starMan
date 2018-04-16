package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.service.UserChargeLogService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by yankai on 2017/6/10.
 */
@Service("rechargeService")
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class RechargeService {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    private UserUserinfoService userInfoService;

    @Autowired
    private UserChargeLogService userChargeLogService;

    public boolean updateUserMoney(Object params) {
        if (params == null || !(params instanceof Map)) {
            return false;
        }

        Map param = (Map)params;
        if (!param.containsKey("body") || !param.containsKey("total_fee")) {
            return false;
        }

        String body = ((String[]) param.get("body"))[0];
        String amount = ((String[]) param.get("total_fee"))[0];
        String tradeNo = ((String[]) param.get("trade_no"))[0];
        //String outTradeNo = (String) param.get("out_trade_no");

        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(tradeNo);
        if (null != chargeLog) {
            return true;
        }

        String[] split = body.split(":");
        if (split == null || split.length < 3) {
            return false;
        }

        try {
            userInfoService.updateUserMoney(split[2], new BigDecimal(amount), tradeNo, EnumUtil.MoneyChangeType.ALIPAY_RECHARGE);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }

        return true;
    }
}
