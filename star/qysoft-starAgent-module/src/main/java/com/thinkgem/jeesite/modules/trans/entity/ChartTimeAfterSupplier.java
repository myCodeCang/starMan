package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;

import java.text.ParseException;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Created by Administrator on 2017-07-04.
 */
public class ChartTimeAfterSupplier implements Supplier<KChartData> {

    private Date currentDate; // 当前时间


    private int intervalTime;// 每间隔多久取一个点

    private ChartTimeSupplier.IntervalType intervalType; //间隔类型  m:分钟   d:天   w: 周  M:月

    //有效时间区间
    private Date startTime1;
    private Date endTime1;
    private Date startTime2;
    private Date endTime2;



    public ChartTimeAfterSupplier(Date currentDate, int intervalTime, ChartTimeSupplier.IntervalType intervalType) {

        this.currentDate = currentDate;
        this.intervalTime = intervalTime;
        this.intervalType = intervalType;

        startTime1 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin"));
        endTime1 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end"));
        startTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin2"));
        endTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end2"));


    }

    @Override
    public KChartData get() {

        try {

                if (this.intervalType == ChartTimeSupplier.IntervalType.minute) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd HH:mm");

                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd HH:mm");
                    int minutes = dateTimeObj.getMinutes();
                    minutes = (minutes / intervalTime) * intervalTime;
                    dateTimeObj.setMinutes(minutes);


                    currentDate = DateUtils2.addMinutes(dateTimeObj,   intervalTime);

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(currentDate);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addMinutes(currentDate, intervalTime), -1));

                    return chartData;

                }
                else  if (this.intervalType == ChartTimeSupplier.IntervalType.day) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd");


                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd");


                    currentDate = DateUtils2.addDays(dateTimeObj,   intervalTime);

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(currentDate);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addDays(currentDate, intervalTime), -1));

                    return chartData;

                }
                else  if (this.intervalType == ChartTimeSupplier.IntervalType.week) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd");


                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd");

                    currentDate = DateUtils2.convertWeekDate(DateUtils2.addWeeks(dateTimeObj,   intervalTime));

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(currentDate);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addWeeks(currentDate, intervalTime), -1));

                    return chartData;

                }
                else  if (this.intervalType == ChartTimeSupplier.IntervalType.Mounth) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM");


                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM");


                    currentDate = DateUtils2.addMonths(dateTimeObj,   intervalTime);

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(currentDate);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addMonths(currentDate, intervalTime), -1));

                    return chartData;

                }

        } catch (ParseException e) {

            e.printStackTrace();
        }


        return null;
    }


}

