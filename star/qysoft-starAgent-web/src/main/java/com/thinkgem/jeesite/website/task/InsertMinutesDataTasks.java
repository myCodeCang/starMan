package com.thinkgem.jeesite.website.task;


import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.FileUtils2;
import com.thinkgem.jeesite.modules.md.entity.GoodsStatus;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
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
import java.io.File;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class InsertMinutesDataTasks implements UserTaskBusiness {
    @Resource
    private BaseGoodsGroupService baseGoodsGroupService;
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
            //判断前一分钟是否有交易产生
//            RealTimePrice realTimePrice = new RealTimePrice();
//            Date endTime = new Date();
//            Date beginTime = DateUtils2.getDatetimebeforeSeconds(endTime,-60);
//            realTimePrice.setCreateDateBegin(beginTime);
//            realTimePrice.setCreateDateEnd(endTime);
            RealTimePrice lastMinutesData = realTimePriceDao.getNowPrice(trading.getId());
            RealTimePrice insertData = new RealTimePrice();
            insertData.setGroupId(trading.getId());
            BigDecimal price = BigDecimal.ZERO;
            if(lastMinutesData!=null){
                price = lastMinutesData.getPrice();
            }
            insertData.setPrice(price);
            insertData.setCreateDate(new Date());
            insertData.setTimeStamp(String.valueOf(System.nanoTime()));
            realTimePriceDao.insert(insertData);
//            RealTimePrice allData = new RealTimePrice();
//            allData.setGroupId(trading.getId());
//            allData.setCreateDate(new Date());
//            List<RealTimePrice> list = realTimePriceDao.findList(allData);
//            List<RealTimePrice> everyMinutesList = list.stream().sorted(Comparator.comparing(RealTimePrice::getCreateDate)).collect(Collectors.toList());
//            Map<String, RealTimePrice> result = Maps.newHashMap();
//            result.put("newest",realTimePriceDao.getNowPrice(trading.getId()));
//            File path = new File(this.getClass().getResource("/").getPath());
//            StringBuffer fileName = new StringBuffer();
//            fileName.append(path).append(File.separator).append("timeShare").append(File.separator).append(trading.getId()).append(".txt");
//            String listJson = JsonMapper.getInstance().toJson(result);
//            FileUtils2.writeToFile(fileName.toString(),listJson.toString(),false);
        }
        logger.error("end..................");
        return true;
    }




}
