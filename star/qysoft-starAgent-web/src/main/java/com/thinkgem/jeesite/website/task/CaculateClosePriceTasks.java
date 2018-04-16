package com.thinkgem.jeesite.website.task;


import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.dao.RealTimePriceDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class CaculateClosePriceTasks implements UserTaskBusiness {
    @Resource
    private MdTradeMainService mdTradeMainService;
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private MdTradeLogService mdTradeLogService;
    @Resource
    private RealTimePriceDao realTimePriceDao;
    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
        baseGoodsGroup.setStatus(GoodsStatus.MD.toString());
        List<BaseGoodsGroup> tradingList = baseGoodsGroupService.findList(baseGoodsGroup);
        for (BaseGoodsGroup trading:tradingList){
            //获取当天最后一笔交易的交易时间
           MdTradeLog lastTradeLog = mdTradeLogService.findLastTradeLog(new Date(), trading.getId());
           if(lastTradeLog == null){
                continue;
           }
            Date lastDealTime = lastTradeLog.getCreateDate();
            Date beginTime = DateUtils2.getDatetimebeforeSeconds(lastDealTime,-180);
            MdTradeLog mdTradeLog = new MdTradeLog();
            mdTradeLog.setGroupId(trading.getId());
            mdTradeLog.setCreateDateBegin(DateUtils2.formatDateTime(beginTime));
            mdTradeLog.setCreateDateEnd(DateUtils2.formatDateTime(lastDealTime));
            List<MdTradeLog> threeMinutesTradeList = mdTradeLogService.findList(mdTradeLog);
            int sum = threeMinutesTradeList.stream().mapToInt(MdTradeLog::getAmount).sum();
            BigDecimal reduce = threeMinutesTradeList.stream().map(p ->
                    p.getPrice().multiply(BigDecimal.valueOf(p.getAmount()))
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal closePrice = BigDecimal.ZERO;
            if(sum > 0){
                closePrice = reduce.divide(BigDecimal.valueOf(sum),2,BigDecimal.ROUND_HALF_UP);
            }
            mdTradeMainService.updateClosingPrice(closePrice,new Date(),trading.getId());
            RealTimePrice realTimePrice = new RealTimePrice();
            realTimePrice.setGroupId(trading.getId());
            realTimePrice.setPrice(closePrice);
            realTimePrice.setCreateDate(new Date());
            realTimePrice.setTimeStamp(String.valueOf(System.nanoTime()));
            realTimePriceDao.insert(realTimePrice);
        }

        logger.error("end..................");
        return true;
    }




}
