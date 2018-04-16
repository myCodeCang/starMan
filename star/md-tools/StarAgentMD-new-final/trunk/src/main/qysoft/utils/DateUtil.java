package main.qysoft.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static final int GREATER_THAN = 1;

    public static final int LESS_THAN = -1;

    public static final int EQUAL = 0;

    /**
     * 比较日期大小。若date1大于date2则返回大于0的数，若小，返回小于0的数，相等则返回0
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(Date date1, Date date2) throws Exception {
        if (null == date1 || null == date2) {
            throw new Exception("Invalid date!");
        }

        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date1.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        if (localDateTime1.isAfter(localDateTime2)) {
            return GREATER_THAN;
        }

        if (localDateTime1.isBefore(localDateTime2)) {
            return LESS_THAN;
        }

        return EQUAL;
    }

    /**
     * 比较日期大小。若date1大于date2则返回大于0的数，若小，返回小于0的数，相等则返回0
     * @param date1
     * @param date2
     * @param pattern
     * @return
     */
    public static int compareDate(String date1, String date2, String pattern) throws Exception {
        if (StringUtils.isEmpty(date1) || StringUtils.isEmpty(date2)) {
            throw new Exception("Invalid date!");
        }

        DateTimeFormatter formatter = null;
        if (StringUtils.isEmpty(pattern)) {
            formatter = DateTimeFormatter.ofPattern(parsePatterns[1]);
        } else {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        if (localDateTime1.isAfter(localDateTime2)) {
            return GREATER_THAN;
        }

        if (localDateTime1.isBefore(localDateTime2)) {
            return LESS_THAN;
        }

        return EQUAL;
    }

    /**
     * 比较日期大小。若date1大于date2则返回大于0的数，若小，返回小于0的数，相等则返回0
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(String date1, String date2) throws Exception {
        return compareDate(date1, date2, null);
    }

    /**
     * 得到相对日期
     * @param date
     * @param day
     * @return
     */
    public static Date getDiffDatetime(Date date, int day){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 字符串格式化为Date
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        SimpleDateFormat format;
        Date date = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            date = format.parse(str);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date localDateToDate(LocalDate localDate){
        if(localDate == null){
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date ;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        if(localDateTime == null){
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static Date formatLocaDateToDate(LocalDate localDate,int h,int m,int s) {
        if(localDate == null){
            return null;
        }
        LocalDateTime createDateBeginDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
       return localDateTimeToDate(createDateBeginDateTime);
    }
}
