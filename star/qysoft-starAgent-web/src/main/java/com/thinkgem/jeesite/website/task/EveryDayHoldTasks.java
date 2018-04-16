package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.service.EverydayHoldService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.service.ShopCommOrderService;
import com.thinkgem.jeesite.modules.user.service.UserAccountchangeService;
import com.thinkgem.jeesite.modules.user.service.UserReportService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.thinkgem.jeesite.common.config.EnumUtil.MoneyType.money;

/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class EveryDayHoldTasks implements UserTaskBusiness {
    @Resource
    private TransGoodsService transGoodsService;
    @Resource
    private EverydayHoldService everydayHoldService;
    @Resource
    private MdTradeService  mdTradeService;
    @Resource
    private MdTradeMainService mdTradeMainService;
    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        try {
            mdTradeService.validateIsTransDay();
        } catch (ValidationException e) {
            logger.error("不是交易日!");
            return false;
        }
        TransGoods transGoods = new TransGoods();
        transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
        List<TransGoods> goodsList = transGoodsService.findList(transGoods);
        for(TransGoods md:goodsList) {
            if(md.getNum()<=0){
                continue;
            }
            BigDecimal money = BigDecimal.ZERO;
            MdTradeMain mdTradeMain = mdTradeMainService.getNowMoney(md.getGroupId());
            if(mdTradeMain != null){
                money = mdTradeMain.getClosingPrice();
            }
            everydayHoldService.updateDataByType(md.getUserName(),md.getGroupId(),md.getNum(),money);
        }
        logger.error("end..................");

        return true;
    }




}
