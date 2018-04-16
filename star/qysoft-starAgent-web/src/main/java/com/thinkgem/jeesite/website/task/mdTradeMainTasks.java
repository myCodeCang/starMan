package com.thinkgem.jeesite.website.task;


import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTradeMain;
import com.thinkgem.jeesite.modules.md.service.MdTradeMainService;
import com.thinkgem.jeesite.modules.star.dao.RealTimePriceDao;
import com.thinkgem.jeesite.modules.star.entity.BaseGoodsGroup;
import com.thinkgem.jeesite.modules.star.entity.RealTimePrice;
import com.thinkgem.jeesite.modules.star.service.BaseGoodsGroupService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class mdTradeMainTasks implements UserTaskBusiness {
    @Resource
    private MdTradeMainService mdTradeMainService;
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
    @Resource
    private RealTimePriceDao realTimePriceDao;
    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
        //获取前一天的交易价格
            BaseGoodsGroup baseGoodsGroup = new BaseGoodsGroup();
            baseGoodsGroup.setStatus(GoodsStatus.MD.toString());
            List<BaseGoodsGroup> list = baseGoodsGroupService.findList(baseGoodsGroup);
            Set<String> collect = list.stream().map(BaseGoodsGroup::getId).collect(Collectors.toSet());
            Date lastDate = DateUtils2.getDiffDatetime(new Date(),-1);
            MdTradeMain mdTradeMain = new MdTradeMain();
            mdTradeMain.setCreateDate(lastDate);
            List<MdTradeMain> lastDayTransList = mdTradeMainService.findList(mdTradeMain);
            String week = DateUtils2.getWeekOfDate(new Date());
            String isTransDay = EnumUtil.YesNO.NO.toString();
            if("on".equals(Global.getOption("md_config",week))){
                isTransDay = EnumUtil.YesNO.YES.toString();
            }
            lastDayTransList =  lastDayTransList.stream().filter(p -> collect.contains(p.getGroupId())).collect(Collectors.toList());
            for (MdTradeMain todayDayTransList : lastDayTransList){
                MdTradeMain newTrade = new MdTradeMain();
                newTrade.setAmount(0);
                newTrade.setClosingPrice(todayDayTransList.getClosingPrice());
                newTrade.setCreateDate(new Date());
                newTrade.setGroupId(todayDayTransList.getGroupId());
                newTrade.setHighestPrice(todayDayTransList.getClosingPrice());
                newTrade.setLowestPrice(todayDayTransList.getClosingPrice());
                newTrade.setOpeningPrice(todayDayTransList.getClosingPrice());
                newTrade.setIsOverTop(EnumUtil.YesNO.NO.toString());
                newTrade.setTransactionPrice(todayDayTransList.getClosingPrice());
                newTrade.setIsTransDay(isTransDay);
                mdTradeMainService.save(newTrade);

                RealTimePrice realTimePrice = new RealTimePrice();
                realTimePrice.setGroupId(todayDayTransList.getGroupId());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=Global.getOption("md_config", "md_time_begin");
                String tempCreateDate = DateUtils2.formatDate(new Date())+" "+time;
                Date createDate = null;
                try {
                    createDate = simpleDateFormat.parse(tempCreateDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                realTimePrice.setCreateDate(createDate);
                realTimePrice.setTimeStamp(String.valueOf(System.nanoTime()));
                realTimePrice.setPrice(todayDayTransList.getClosingPrice());
                realTimePriceDao.insert(realTimePrice);
            }

        logger.error("end..................");
        return true;
    }




}
