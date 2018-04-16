package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.dao.BaseGoodsGroupDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import com.thinkgem.jeesite.modules.trans.entity.TransBuy;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.service.TransBuyService;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Component
@Lazy(false)
public class AutoChangeScoreTasks implements UserTaskBusiness {
    @Resource
    private BaseGoodsGroupDao baseGoodsGroupDao;
    @Resource
    private TransBuyService transBuyService;
    @Resource
    private TransGoodsService transGoodsService;
    @Resource
    private MdTradeMainService mdTradeMainService;

    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        int marryDay;
        int transDay;
        try {
            marryDay =  Integer.parseInt(Global.getOption("md_config","marry_days","90"));
            transDay =  Integer.parseInt(Global.getOption("system_trans","attorn_days","30"));
        } catch (NumberFormatException e) {
            logger.error("失败：交易天数配之错误");
            return false;
        }
        List<BaseGoodsGroup> list = baseGoodsGroupDao.findList(new BaseGoodsGroup());
        Set<String> collect = list.stream().filter(p -> "1".equals(p.getStatus())).map(BaseGoodsGroup::getId).collect(Collectors.toSet());
        List<BaseGoodsGroup> transCollect = list.stream().filter(p -> "2".equals(p.getStatus())).collect(Collectors.toList());
        Date createDate = new Date();
        if(collect.size()>0){
            List<MdTradeMain> mdTradeMainList = mdTradeMainService.sumTransDayGroupByGroupId();
            List<MdTradeMain> mainList = mdTradeMainList.stream().filter(q -> collect.contains(q.getGroupId())&& q.getAmount()>=marryDay).collect(Collectors.toList());
            for(MdTradeMain marryGoods : mainList){
                baseGoodsGroupDao.updateStatusTime(marryGoods.getGroupId(),"2",createDate);
            }
        }
        if (transCollect.size()>0) {
            List<BaseGoodsGroup> endTransCollect = transCollect.stream().filter(p -> {
                try {
                    return DateUtils2.getDistanceOfTwoDate(new Date(),DateUtils2.getDiffDatetime(p.getTransStartTime(), transDay)) <= 0;
                } catch (Exception e) {
                    return false;
                }
            }).collect(Collectors.toList());

            Set<String> groupIdcollect = endTransCollect.stream().map(BaseGoodsGroup::getId).collect(Collectors.toSet());
            String[] groupArray =  endTransCollect.stream().map(BaseGoodsGroup::getId).toArray(String[]::new);


            //处理剩余订单

            if (groupArray.length>0) {
                TransBuy transBuy = new TransBuy();
                transBuy.setGroupIdArray(groupArray);
                transBuy.setStatus("1");
                List<TransBuy> buyList = transBuyService.findList(transBuy);
                for(TransBuy trans: buyList){
                    try {
                        transBuyService.promiseCancel(trans.getSellUserName(),trans.getId(), EnumUtil.TransBuyStatus.downAuto,"系统自动下架");
                    } catch (ValidationException e) {
                        logger.error("撤单失败，失败订单："+trans.getId());
                        return false;
                    }
                }
            }


            //处理商品
            TransGoods transGoods = new TransGoods();
            transGoods.setStatus(EnumUtil.GoodsType.hold.toString());
            List<TransGoods> transGoodsList = transGoodsService.findList(transGoods);
            List<TransGoods> exchangeList = transGoodsList.stream().filter(p -> groupIdcollect.contains(p.getGroupId())).collect(Collectors.toList());
            for (TransGoods goods:exchangeList){
                try {
                    transBuyService.exchangeScore(goods.getId(),goods.getGroupId(),goods.getUserName(),goods.getNum(),"系统自动");
                } catch (ValidationException e) {
                    logger.error("兑换失败：失败用户："+goods.getUserName() + "失败原因:"+e.getMessage());
                    continue;
                }
            }
            //删除过期持仓记录
            transGoodsService.deleteExceed(groupArray);

            for (String id : groupArray){
                baseGoodsGroupDao.updateStatusTime(id,"-1",new Date());
            }
        }

        logger.error("end..................");

        return true;
    }




}
