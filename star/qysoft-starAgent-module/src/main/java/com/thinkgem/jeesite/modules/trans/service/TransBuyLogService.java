/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.trans.service;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.trans.dao.TransBuyLogDao;
import com.thinkgem.jeesite.modules.trans.dao.TransGoodsGroupDao;
import com.thinkgem.jeesite.modules.trans.entity.*;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户交易表Service
 *
 * @author xueyuliang
 * @version 2017-05-08
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class TransBuyLogService extends CrudService<TransBuyLogDao, TransBuyLog> {
    @Autowired
    private UserUserinfoService userUserinfoService;

    @Resource
    private TransGoodsGroupDao transGoodsGroupDao ;

    public TransBuyLog get(String id) {
        return super.get(id);
    }

    public List<TransBuyLog> findList(TransBuyLog transBuyLog) {
        return super.findList(transBuyLog);
    }

    public Page<TransBuyLog> findPage(Page<TransBuyLog> page, TransBuyLog transBuyLog) {

        return super.findPage(page, transBuyLog);

    }


    public void save(TransBuyLog transBuyLog) throws ValidationException {
        super.save(transBuyLog);
        super.save(transBuyLog);
    }


    public void delete(TransBuyLog transBuyLog) throws ValidationException {
        super.delete(transBuyLog);
    }


    /**
     * 根据日期查询交易量
     * @param transBuyLog
     * @return
     */
    public TransBuyLog getTransBuySumValue(TransBuyLog transBuyLog) {

        return dao.getTransBuySumValue(transBuyLog);
    }

    /**
     * 插入交易记录
     *
     * @param userName
     * @param transUserName
     * @param groupId
     * @param num
     * @param money
     * @param message
     * @param type
     */
    public void addTransLog(String userName, String transUserName, String groupId, String applyId, int num, BigDecimal money, String message, String type) throws ValidationException {
        TransBuyLog transBuyLog = new TransBuyLog();
        transBuyLog.setUserName(userName);
        transBuyLog.setTransUserName(transUserName);
        transBuyLog.setGroupId(groupId);
        transBuyLog.setApplyId(applyId);
        transBuyLog.setNum(num);
        transBuyLog.setMoney(money);
        transBuyLog.setMessage(message);
        transBuyLog.setType(type);
        this.save(transBuyLog);
    }

    public List<TransBuyLog> getChartDatasBuyGroup(String groupId) {
        return dao.getChartDatasBuyGroup(groupId);
    }

    public List<TransStatistics> getTransStatistics(String sql) {
        return dao.getTransStatistics(sql);
    }

    public List<TransStatistics> getTransStatisticsMinute(String groupId, String beginTime) {
        return dao.getTransStatisticsMinute(groupId, beginTime);
    }

    public Map<String, Object> getKChartData(String timeDayCycle, String timeInterval, String maxLineNum, String groupId) {

        if (StringUtils2.isBlank(timeDayCycle)) {
            throw new ValidationException("时间查询参数有误");
        }

        if (StringUtils2.isBlank(timeInterval)) {
            throw new ValidationException("时间间隔参数有误");
        }

        if (StringUtils2.isBlank(maxLineNum)) {
            throw new ValidationException("最大K线数量参数有误");
        }

        int maxLineNumInt = Integer.parseInt(maxLineNum);

        List<TransBuyLog> transBuyLogList = new ArrayList<TransBuyLog>();
        TransBuyLog lastTrasnBuyLog = new TransBuyLog(); //  上一条交易记录
        TransBuyLog firstTrasnBuyLog = new TransBuyLog(); //  第一条交易记录

        //查询当前商品
        TransGoodsGroup  transGoodsGroup = transGoodsGroupDao.get(groupId);


        //查询??分钟内的交易
        if (timeDayCycle.endsWith("m")) {
            int timeStr = Integer.parseInt(timeDayCycle.replace("m", ""));
            TransBuyLog transBuyLog = new TransBuyLog();
            transBuyLog.setTypeArray(new String[]{"1","4"});
            transBuyLog.setCreateDateTimeBegin(DateUtils2.addMinutes(new Date(), -timeStr));
            transBuyLog.setGroupId(groupId);
            transBuyLogList = this.findList(transBuyLog);

            //减一秒向上查询
            transBuyLog.setCreateDateTimeBegin(null);
            transBuyLog.setCreateDateTimeEnd(DateUtils2.addSeconds(DateUtils2.addMinutes(new Date(), -timeStr), -1));
            List<TransBuyLog> transBuyLogListTemp = this.findList(transBuyLog);

            transBuyLogListTemp =  transBuyLogListTemp.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());

            if (transBuyLogListTemp.size() > 0) {
                lastTrasnBuyLog = transBuyLogListTemp.get(0);
            } else {
                lastTrasnBuyLog.setMoney(transGoodsGroup.getStartMoney());
            }


        }
        //查询??天内的交易
        else if (timeDayCycle.endsWith("d")) {
            int timeStr = Integer.parseInt(timeDayCycle.replace("d", ""));
            TransBuyLog transBuyLog = new TransBuyLog();
            transBuyLog.setTypeArray(new String[]{"1","4"});
            transBuyLog.setCreateDateTimeBegin(DateUtils2.addDays(new Date(), -timeStr));
            transBuyLog.setGroupId(groupId);
            transBuyLogList = this.findList(transBuyLog);

            //减一秒向上查询
            transBuyLog.setCreateDateTimeBegin(null);
            transBuyLog.setCreateDateTimeEnd(DateUtils2.addSeconds(DateUtils2.addDays(new Date(), -timeStr), -1));
            List<TransBuyLog> transBuyLogListTemp = this.findList(transBuyLog);
            transBuyLogListTemp =  transBuyLogListTemp.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());
            if (transBuyLogListTemp.size() > 0) {
                lastTrasnBuyLog = transBuyLogListTemp.get(0);
            } else {
                lastTrasnBuyLog.setMoney(transGoodsGroup.getStartMoney());
            }
        }
        //查询??年内的交易
        else if (timeDayCycle.endsWith("y")) {
            int timeStr = Integer.parseInt(timeDayCycle.replace("y", ""));
            TransBuyLog transBuyLog = new TransBuyLog();
            transBuyLog.setTypeArray(new String[]{"1","4"});
            transBuyLog.setCreateDateTimeBegin(DateUtils2.addYears(new Date(), -timeStr));
            transBuyLog.setGroupId(groupId);
            transBuyLogList = this.findList(transBuyLog);

            //减一秒向上查询
            transBuyLog.setCreateDateTimeBegin(null);
            transBuyLog.setCreateDateTimeEnd(DateUtils2.addSeconds(DateUtils2.addYears(new Date(), -timeStr), -1));
            List<TransBuyLog> transBuyLogListTemp = this.findList(transBuyLog);

            transBuyLogListTemp =  transBuyLogListTemp.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());
            if (transBuyLogListTemp.size() > 0) {
                lastTrasnBuyLog = transBuyLogListTemp.get(0);
            } else {
                lastTrasnBuyLog.setMoney(transGoodsGroup.getStartMoney());
            }
        }

        //查询第一条出现的交易记录
        Page<TransBuyLog> logPage = new Page<>();
        logPage.setOrderBy("createDate asc");
        logPage.setPageNo(1);
        logPage.setPageSize(1);

        TransBuyLog transBuyLogTemp =  new TransBuyLog();
        transBuyLogTemp.setGroupId(groupId);
        transBuyLogTemp.setTypeArray(new String[]{"1","4"});

        Page<TransBuyLog> firstPage =  this.findPage(logPage, transBuyLogTemp);
        if(firstPage.getList().size() > 0)
        {
            firstTrasnBuyLog = firstPage.getList().get(0);
        }
        else
        {
            firstTrasnBuyLog = new TransBuyLog();
            firstTrasnBuyLog.setCreateDate(DateUtils2.parseDate("2000-01-01"));

        }

        List<KChartData> kChartDataList = new ArrayList<>();

        int timeStr = 0;
        int timeIntervalVal = 0;
        Stream<KChartData> dateStream = null;
        Date xianzhiDate = null;

        //如果是分钟,说明是当前数据, 构造当天的时间数据
        if (timeDayCycle.endsWith("m")) {
            timeStr = Integer.parseInt(timeDayCycle.replace("m", ""));
             xianzhiDate = DateUtils2.addMinutes(new Date(), -1 * timeStr); //限制时间

        } else if (timeDayCycle.endsWith("d")) {
            timeStr = Integer.parseInt(timeDayCycle.replace("d", ""));
             xianzhiDate = DateUtils2.addDays(new Date(), -1 * timeStr); //限制时间
        } else if (timeDayCycle.endsWith("y")) {
            timeStr = Integer.parseInt(timeDayCycle.replace("y", ""));
            xianzhiDate = DateUtils2.addYears(new Date(), -1 * timeStr); //限制时间
        } else {
            throw new ValidationException("时间格式有误!");
        }



        if (timeInterval.endsWith("m")) {
            timeIntervalVal = Integer.parseInt(timeInterval.replace("m", ""));


            xianzhiDate = xianzhiDate.getTime() >= firstTrasnBuyLog.getCreateDate().getTime()? xianzhiDate:firstTrasnBuyLog.getCreateDate();
            dateStream = Stream.generate(new ChartTimeSupplier(new Date(),xianzhiDate , timeIntervalVal, ChartTimeSupplier.IntervalType.minute));

        } else if (timeInterval.endsWith("d")) {
            timeIntervalVal = Integer.parseInt(timeInterval.replace("d", ""));

            xianzhiDate = xianzhiDate.getTime() >= firstTrasnBuyLog.getCreateDate().getTime()? xianzhiDate:firstTrasnBuyLog.getCreateDate();
            dateStream = Stream.generate(new ChartTimeSupplier(new Date(), xianzhiDate, timeIntervalVal, ChartTimeSupplier.IntervalType.day));

        }else if (timeInterval.endsWith("w")) {
            timeIntervalVal = Integer.parseInt(timeInterval.replace("w", ""));

            xianzhiDate = xianzhiDate.getTime() >= firstTrasnBuyLog.getCreateDate().getTime()? xianzhiDate:firstTrasnBuyLog.getCreateDate();
            dateStream = Stream.generate(new ChartTimeSupplier(new Date(), xianzhiDate, timeIntervalVal, ChartTimeSupplier.IntervalType.week));

        } else if (timeInterval.endsWith("u")) {
            timeIntervalVal = Integer.parseInt(timeInterval.replace("u", ""));

            xianzhiDate = xianzhiDate.getTime() >= firstTrasnBuyLog.getCreateDate().getTime()? xianzhiDate:firstTrasnBuyLog.getCreateDate();
            dateStream = Stream.generate(new ChartTimeSupplier(new Date(), xianzhiDate, timeIntervalVal, ChartTimeSupplier.IntervalType.Mounth));

        }  else {
            throw new ValidationException("时间格式有误!");
        }



        dateStream = dateStream.limit(maxLineNumInt).filter(kChartData -> kChartData != null).sorted((o1, o2) -> o1.getDateBegin().compareTo(o2.getDateBegin()));


        kChartDataList = dateStream.collect(Collectors.toList());

        //向后补足剩余点位
        if(kChartDataList.size() < maxLineNumInt){
            if (timeInterval.endsWith("m")) {
                timeIntervalVal = Integer.parseInt(timeInterval.replace("m", ""));

                dateStream = Stream.generate(new ChartTimeAfterSupplier(new Date(), timeIntervalVal, ChartTimeSupplier.IntervalType.minute));

            } else if (timeInterval.endsWith("d")) {
                timeIntervalVal = Integer.parseInt(timeInterval.replace("d", ""));

                dateStream = Stream.generate(new ChartTimeAfterSupplier(new Date(), timeIntervalVal, ChartTimeSupplier.IntervalType.day));

            }else if (timeInterval.endsWith("w")) {
                timeIntervalVal = Integer.parseInt(timeInterval.replace("w", ""));

                dateStream = Stream.generate(new ChartTimeAfterSupplier(new Date(), timeIntervalVal, ChartTimeSupplier.IntervalType.week));

            } else if (timeInterval.endsWith("u")) {
                timeIntervalVal = Integer.parseInt(timeInterval.replace("u", ""));

                dateStream = Stream.generate(new ChartTimeAfterSupplier(new Date(), timeIntervalVal, ChartTimeSupplier.IntervalType.Mounth));

            }  else {
                throw new ValidationException("时间格式有误!");
            }

            dateStream = dateStream.limit(maxLineNumInt-kChartDataList.size()).map(kChartDataMap -> {
                kChartDataMap.setValue(-1);
                return  kChartDataMap;
            });

            kChartDataList.addAll(dateStream.collect(Collectors.toList()));

        }

        KChartData lastKChartData = null;
        for (KChartData chartData : kChartDataList) {

            if (lastKChartData == null) {
                chartData.setOpen(lastTrasnBuyLog.getMoney().doubleValue());
                chartData.setClose(lastTrasnBuyLog.getMoney().doubleValue());
            } else {
                chartData.setOpen(lastKChartData.getClose());
                chartData.setClose(lastKChartData.getClose());
            }
            lastKChartData = chartData;

            List<TransBuyLog> finalTransBuyLogList = transBuyLogList;
            Stream<TransBuyLog> buyLogStream = finalTransBuyLogList.stream().filter(transBuyLog -> {
                return transBuyLog.getCreateDate().getTime() >= chartData.getDateBegin().getTime() && transBuyLog.getCreateDate().getTime() <= chartData.getDateEnd().getTime();
            }).sorted((o1, o2) -> o1.getId().compareTo(o1.getId()));
            List<TransBuyLog> buyLogList = buyLogStream.collect(Collectors.toList());

            double maxMoney = chartData.getOpen();
            double minMoney = chartData.getOpen();
            double valueNum = 0;
            for (TransBuyLog buyLogTemp : buyLogList) {

                if (buyLogTemp.getMoney().doubleValue() > maxMoney) {
                    maxMoney = buyLogTemp.getMoney().doubleValue();
                } else if (buyLogTemp.getMoney().doubleValue() < minMoney) {
                    minMoney = buyLogTemp.getMoney().doubleValue();
                }

                valueNum += buyLogTemp.getNum();
            }

            if (buyLogList.size() > 0) {
                chartData.setClose(buyLogList.get(0).getMoney().doubleValue());
            }


            chartData.setHighest(maxMoney);
            chartData.setLowest(minMoney);
            if(valueNum > 0){
                chartData.setValue(valueNum);
            }

        }


        //构造数据

        List<String> dates = new ArrayList<>();
        List<Double> volumns = new ArrayList<>();
        List<Double[]> data = new ArrayList<>();

        for (KChartData chartData : kChartDataList) {
//            if (timeDayCycle.endsWith("m")) {

//            }


            dates.add(DateUtils2.formatDate(chartData.getDateBegin(), "yyyy-MM-dd HH:mm"));

            if(chartData.getValue() != -1){
                volumns.add(chartData.getValue());
                Double[] dataArray = new Double[]{chartData.getOpen(), chartData.getClose(), chartData.getLowest(), chartData.getHighest(), chartData.getValue()};
                data.add(dataArray);
            }

        }


        Map<String, Object> resultMap = new HashedMap();
        resultMap.put("dates", dates);
        resultMap.put("volumns", volumns);
        resultMap.put("data", data);

        return resultMap;
    }
}