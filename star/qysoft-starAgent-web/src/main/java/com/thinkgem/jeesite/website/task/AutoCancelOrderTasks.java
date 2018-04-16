package com.thinkgem.jeesite.website.task;


import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;

import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;

import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;


/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class AutoCancelOrderTasks implements UserTaskBusiness {
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private MdTradeService mdTradeService;
    @Resource
    private TransGoodsService transGoodsService;
    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        List<MdTrade> mdTradeList = mdTradeService.findList(new MdTrade());
        List<MdTrade> collect = mdTradeList.stream().filter(p -> p.getState() == 3 || p.getState() == 4).collect(Collectors.toList());
        for(MdTrade mdTrade: collect){
            try {
                mdTradeService.autoCancelOrder(mdTrade.getId(),mdTrade.getUserName());
            } catch (Exception e) {
                logger.error("撤单失败,失败订单编号:"+mdTrade.getId()+"失败原因:"+e.getMessage());
            }
        }
        logger.error("end..................");
        return true;
    }




}
