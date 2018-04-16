package com.thinkgem.jeesite.modules.trans.entity;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

/**
 * Created by Administrator on 2017-07-04.
 */
public class ChartTimeSupplier implements Supplier<KChartData> {

    private Date currentDate; // 当前时间

    private Date beginDateEnd; //时间限制, 生成的时间不可超过这个时间

    private int intervalTime;// 每间隔多久取一个点

    private IntervalType intervalType; //间隔类型  m:分钟   d:天   w: 周  M:月

    //有效时间区间
    private Date startTime1;
    private Date endTime1;
    private Date startTime2;
    private Date endTime2;


    //用户类型枚举
    public static enum IntervalType {
        //间隔类型  m:分钟   d:天   w: 周  M:月
        minute(0),
        day(1),
        week(1),
        Mounth(1);
        private int nCode;

        private IntervalType(int _nCode) {
            this.nCode = _nCode;
        }

        @Override
        public String toString() {
            return String.valueOf(this.nCode);
        }
    }


    public ChartTimeSupplier(Date currentDate, Date beginDateEnd, int intervalTime, IntervalType intervalType) {

        this.currentDate = currentDate;
        this.intervalTime = intervalTime;
        this.intervalType = intervalType;
        this.beginDateEnd = beginDateEnd;

        startTime1 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin"));
        endTime1 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end"));
        startTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_begin2"));
        endTime2 = DateUtils2.formatStrTime(Global.getOption("system_trans", "trans_time_end2"));


        try {
            if (this.intervalType == IntervalType.minute) {
                String dateTime = DateUtils2.formatDate(this.beginDateEnd, "yyyy-MM-dd HH:mm");
                this.beginDateEnd = DateUtils2.parseDate(dateTime, "yyyy-MM-dd HH:mm");
                int minutes = this.beginDateEnd.getMinutes();
                minutes = (minutes / intervalTime) * intervalTime;
                this.beginDateEnd.setMinutes(minutes);
            }
            else if (this.intervalType == IntervalType.day) {
                String dateTime = DateUtils2.formatDate(this.beginDateEnd, "yyyy-MM-dd");
                this.beginDateEnd = DateUtils2.parseDate(dateTime, "yyyy-MM-dd");

            }
            else if (this.intervalType == IntervalType.week) {
                this.beginDateEnd = DateUtils2.convertWeekDate(this.beginDateEnd);
            }
            else if (this.intervalType == IntervalType.Mounth) {
                String dateTime = DateUtils2.formatDate(this.beginDateEnd, "yyyy-MM");
                this.beginDateEnd = DateUtils2.parseDate(dateTime, "yyyy-MM");

            }


        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public KChartData get() {

        try {
            while (currentDate.getTime() >= beginDateEnd.getTime()) {

                if (this.intervalType == IntervalType.minute) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd HH:mm");

                    if (currentDate.getTime() < beginDateEnd.getTime()) {

                        return null;
                    }

                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd HH:mm");
                    int minutes = dateTimeObj.getMinutes();
                    minutes = (minutes / intervalTime) * intervalTime;
                    dateTimeObj.setMinutes(minutes);


                    currentDate = DateUtils2.addMinutes(dateTimeObj, -1 * intervalTime);


                    Date currdateTimeObj = DateUtils2.formatDateDate(dateTimeObj, "HH:mm:ss");

                    //过滤周六周日
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateTimeObj);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                        continue;
                    }

                    if ((currdateTimeObj.getTime() >= startTime1.getTime() && currdateTimeObj.getTime() <= endTime1.getTime()) || (currdateTimeObj.getTime() >= startTime2.getTime() && currdateTimeObj.getTime() <= endTime2.getTime())) {

                        KChartData chartData = new KChartData();
                        chartData.setDateBegin(dateTimeObj);
                        chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addMinutes(dateTimeObj, intervalTime), -1));

                        return chartData;
                    }

                    //如果是小时,是9点,特殊处理为允许
                    if(intervalTime == 60 && this.intervalType == IntervalType.minute && DateUtils2.formatStr2Date("09:00:00", "HH:mm:ss").getTime() ==currdateTimeObj.getTime() ){
                        KChartData chartData = new KChartData();

                        Date beginDateTemp = DateUtils2.formatStr2Date(DateUtils2.formatDate(dateTimeObj,"yyyy-MM-dd")+" 09:30:00","yyyy-MM-dd HH:mm:ss");
                        Date endDateTemp = DateUtils2.addSeconds((DateUtils2.formatStr2Date(DateUtils2.formatDate(dateTimeObj,"yyyy-MM-dd")+" 09:59:59","yyyy-MM-dd HH:mm:ss")), -1);

                        chartData.setDateBegin(beginDateTemp);
                        chartData.setDateEnd(endDateTemp);

                        return chartData;
                    }

                }
                else  if (this.intervalType == IntervalType.day) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd");

                    if (currentDate.getTime() < beginDateEnd.getTime()) {

                        return null;
                    }

                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd");

                    currentDate = DateUtils2.addDays(dateTimeObj, -1 * intervalTime);
                    //过滤周六周日
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateTimeObj);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                        return null;
                    }

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(dateTimeObj);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addDays(dateTimeObj, intervalTime), -1));

                    return chartData;
                }
                else  if (this.intervalType == IntervalType.week) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM-dd");

                    if (currentDate.getTime() < beginDateEnd.getTime()) {

                        return null;
                    }

                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM-dd");

                    currentDate = DateUtils2.convertWeekDate(DateUtils2.addWeeks(dateTimeObj, -1 * intervalTime));

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(dateTimeObj);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addWeeks(dateTimeObj, intervalTime), -1));

                    return chartData;

                }
                else  if (this.intervalType == IntervalType.Mounth) {
                    String dateTime = DateUtils2.formatDate(currentDate, "yyyy-MM");

                    if (currentDate.getTime() < beginDateEnd.getTime()) {

                        return null;
                    }

                    Date dateTimeObj = DateUtils2.parseDate(dateTime, "yyyy-MM");


                    currentDate = DateUtils2.addMonths(dateTimeObj, -1 * intervalTime);

                    KChartData chartData = new KChartData();
                    chartData.setDateBegin(dateTimeObj);
                    chartData.setDateEnd(DateUtils2.addSeconds(DateUtils2.addMonths(dateTimeObj, intervalTime), -1));

                    return chartData;

                }

            }
        } catch (ParseException e) {

            e.printStackTrace();
        }


        return null;
    }


}

