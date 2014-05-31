package com.baidu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 
 * @author liaoqiqi
 * @email liaoqiqi@baidu.com
 * 
 */
public final class DateUtils {

    private static final long MS_IN_DAY = 24 * 3600 * 1000;

    private DateUtils() {
    }

    /**
     * 获得给定时间的第N天零时的日期对象 例如：若给定时间为（2004-08-01 11:30:58），将获得（2004-08-02
     * 00:00:00）的日期对象 若给定时间为（2004-08-31 11:30:58），将获得（2004-09-01 00:00:00）的日期对象
     * 
     * @param dt
     *            Date 给定的java.util.Date对象
     * @return Date java.util.Date对象
     */
    public static Date getNextDay(final Date dt, final int n) {

        final Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        return new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + n)
                .getTime();

    }

    /**
     * 按照给定格式返回代表日期的字符串
     * 
     * @param pDate
     *            Date
     * @param format
     *            String 日期格式
     * @return String 代表日期的字符串
     */
    public static String formatDate(final java.util.Date pDate,
            final String format) {

        final SimpleDateFormat sdf = new SimpleDateFormat(format,
                Locale.SIMPLIFIED_CHINESE);

        java.util.Date pDateToBeReturn = pDate;
        if (pDateToBeReturn == null) {
            pDateToBeReturn = new java.util.Date();
        }

        return sdf.format(pDateToBeReturn);
    }

    /**
     * 验证字符串是不是合法的日期；严格判断日期格式YYYYMMDD的正则表达式：包括闰年的判断、大月小月的判断
     * 
     * @param dateString
     *            待验证的日期字符串
     * @return 满足则返回true，不满足则返回false
     */
    public static boolean validateDateString(final String dateString) {

        if (dateString == null || dateString.equals("")) {
            return false; // NOPMD by liaoqiqi on 12-10-12 下午8:39
        }

        // 日期格式YYYYMMDD的正则表达式,世纪年为闰年，如2000
        final String regDate = "^(((([02468][048])|([13579][26]))[0]{2})(02)(([0][1-9])|([1-2][0-9])))"
                +
                // 世纪年不为闰年如2100
                "|(((([02468][1235679])|([13579][01345789]))[0]{2})(02)(([0][1-9])|([1][0-9])|([2][0-8])))"
                +
                // 非世纪年为闰年，如1996
                "|(([0-9]{2}(([0][48])|([2468][048])|([13579][26])))(02)(([0][1-9])|([1-2][0-9])))"
                +
                // 非世纪年不为闰年，如1997
                "|(([0-9]{2}(([02468][1235679])|([13579][01345789])))(02)(([0][1-9])|([1][0-9])|([2][0-8])))"
                +
                // 大月，有31天
                "|(([0-9]{4})(([0]{1}(1|3|5|7|8))|10|12)(([0][1-9])|([1-2][0-9])|30|31))"
                +
                // 小月，只有30天
                "|(([0-9]{4})(([0]{1}(4|6|9))|11)(([0][1-9])|([1-2][0-9])|30))$";

        return dateString.matches(regDate);
    }

    public static boolean validateDatetimeString(final String datetimeString,
            final String format) {
        if (datetimeString == null || datetimeString.equals("")) {
            return false;
        }
        final SimpleDateFormat sdf = new SimpleDateFormat(format,
                Locale.SIMPLIFIED_CHINESE);
        try {
            sdf.parse(datetimeString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 获取两个日期之间的间隔日期
     * 
     * @author modi
     * @version 1.0.0
     */
    public static int getDaysBetween(final String beginDate,
            final String endDate) {

        try {
            final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",
                    Locale.SIMPLIFIED_CHINESE);
            final Date bDate = format.parse(beginDate);
            final Date eDate = format.parse(endDate);
            Calendar d1 = new GregorianCalendar();
            d1.setTime(bDate);
            final Calendar d2 = new GregorianCalendar();
            d2.setTime(eDate);

            if (d1.after(d2)) {
                return 0;
            }

            int days = d2.get(Calendar.DAY_OF_YEAR)
                    - d1.get(Calendar.DAY_OF_YEAR);
            final int y2 = d2.get(Calendar.YEAR);
            if (d1.get(Calendar.YEAR) != y2) {
                d1 = (Calendar) d1.clone();
                do {
                    days += d1.getActualMaximum(Calendar.DAY_OF_YEAR); // 得到当年的实际天数
                    d1.add(Calendar.YEAR, 1);
                } while (d1.get(Calendar.YEAR) != y2);
            }
            return days;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取两个日期之间的间隔日期
     * 
     * @author modi
     * @version 1.0.0
     */
    public static int getDaysBetween(final Date beginDate, final Date endDate) {

        Calendar d1 = new GregorianCalendar();
        d1.setTime(beginDate);
        final Calendar d2 = new GregorianCalendar();
        d2.setTime(endDate);

        if (d1.after(d2)) {
            return 0; // NOPMD by liaoqiqi on 12-10-12 下午11:06
        }

        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        final int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR); // 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获取从开始时间到结束时间的不同的天数 例如: 10月1号23点到10月2号1点是2天，经过了10月1号和10月2号。
     * 10月1号23点到10月2号0点是1天，10月2号0点=10月1号24点
     * 
     * @author Hualei
     * @since 1.0.0
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDaysAmong(final String beginDate, final String endDate) {

        if (beginDate == null || endDate == null || beginDate.length() != 14
                || endDate.length() != 14) {
            return 0;
        }

        try {
            final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd",
                    Locale.SIMPLIFIED_CHINESE);
            final Date bDate = format1.parse(beginDate.substring(0, 8));
            Date eDate = format1.parse(endDate.substring(0, 8));
            if (eDate.getTime() < bDate.getTime()) {
                return 0;
            }

            long days = (eDate.getTime() - bDate.getTime()) / MS_IN_DAY;
            final SimpleDateFormat format2 = new SimpleDateFormat(
                    "yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE);
            eDate = format2.parse(endDate);
            final Calendar calendar = new GregorianCalendar();
            calendar.setTime(eDate);
            if (calendar.get(Calendar.HOUR_OF_DAY) != 0) {
                days += 1;
            }
            // 当天
            if (days == 0) {
                days = 1;
            }

            return (int) days;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取两个日期之间的间隔毫秒数
     * 
     * @author dongying
     * @since 1.0.9
     * @return
     */
    public static long getMinllisBetween(final Date beginDate,
            final Date endDate) {

        final Calendar d1 = new GregorianCalendar();
        d1.setTime(beginDate);
        final Calendar d2 = new GregorianCalendar();
        d2.setTime(endDate);

        if (d1.after(d2)) {
            return 0;
        }

        return d2.getTimeInMillis() - d1.getTimeInMillis();
    }

}
