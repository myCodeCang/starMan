package com.thinkgem.jeesite.modules.user.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.payment.entity.PaymentCallback;
import com.thinkgem.jeesite.modules.payment.entity.ThirdPartPayCallbackEntity;
import com.thinkgem.jeesite.modules.payment.service.HuanxunPayService;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/1.
 */

@Service("huanxunCallback")
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class HuanxunCallbackService implements HuanxunPayService{
    @Autowired
    private UserUserinfoService userUserinfoService;
    @Autowired
    private UserChargeLogService userChargeLogService;

    private  Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean doPayBusiness(ThirdPartPayCallbackEntity entity) {
        if (entity == null) {
            return false;
        }
        UserUserinfo byNameLock = userUserinfoService.getByNameLock(entity.getUserName());
        if(byNameLock == null){
            return true;
        }
        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(entity.getBillNo());
        if (null != chargeLog) {
            return true;
        }
        try {
            userUserinfoService.updateUserMoney(entity.getUserName(), new BigDecimal(entity.getAmount()), entity.getBillNo(), EnumUtil.MoneyChangeType.HUANXUN_PAY);
        } catch (Exception ex) {
            logger.error("HuanxuPayLog:-------" + ex.getLocalizedMessage());
            return false;
        }
        return true;
    }

}
