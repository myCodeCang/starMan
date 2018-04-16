package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.payment.entity.ThirdPartPayCallbackEntity;
import com.thinkgem.jeesite.modules.payment.service.HuanxunPayService;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.service.UserChargeLogService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by yankai on 2017/6/27.
 */
@Service("huanxunPayService")
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class HuanxunPayBusinessService implements HuanxunPayService {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Autowired
    private UserUserinfoService userInfoService;

    @Autowired
    private UserChargeLogService userChargeLogService;

    @Override
    public boolean doPayBusiness(ThirdPartPayCallbackEntity entity) {
        if (null == entity) {
            return false;
        }

        //获取流水号，判断订单是否存在，避免重复充值
        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(entity.getBillNo());
        if (null != chargeLog) {
            return true;
        }

        try {
            userInfoService.updateUserMoney(entity.getUserName(), new BigDecimal(entity.getAmount()), entity.getBillNo(), EnumUtil.MoneyChangeType.HUANXUN_PAY);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return false;
        }
        return false;
    }
}
