package com.example.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private static final String[] mWeeks = new String[]{
            "","星期天","星期一","星期二","星期三","星期四","星期五","星期六"
    };




    public static String getTodayDate(String mode){
        SimpleDateFormat format = new SimpleDateFormat(mode, Locale.CHINA);
        return format.format(new Date());
    }


    /**
     * 两个日期中间隔了多少天
     *
     * @param firstDay
     * @param otherDay
     * @return
     */
    public static int spanTodayAndOtherDay(String firstDay, String otherDay,String date_mode) {
        SimpleDateFormat format = new SimpleDateFormat(date_mode, Locale.CHINA);
        Date dateToday = null;
        Date other = null;
        try {
            dateToday = format.parse(firstDay);
            other = format.parse(otherDay);

            long time = Math.abs(dateToday.getTime() - other.getTime());

            return accordingTimeGetSpanDay(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据时间算出天数
     *
     * @param time
     * @return
     */
    private static int accordingTimeGetSpanDay(long time) {
        return (int) time / 1000 / 60 / 60 / 24;
    }


    /**
     * 得到某个日期的前一天的日期
     *
     * @return
     */
    private String beforeLastDate(String lastDate,String date_mode) {

        DateFormat df = new SimpleDateFormat(date_mode, Locale.CHINA);

        Date parse = null;
        try {
            parse = df.parse(lastDate);
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(parse);

            calendar.add(Calendar.DAY_OF_MONTH, -1);

            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = CommonUtil.parseDay(calendar.get(Calendar.MONTH));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


            return year + month + day;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String accordingDateGetDateAndWeek(String date,String inputMode,String outputMode){
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputMode,Locale.CHINESE);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputMode,Locale.CHINESE);
        try {
            Date da = inputFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(da);

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String week = mWeeks[day];

            //Date opDate = outputFormat.parse(date);
            String outputDate = outputFormat.format(da);

            return outputDate+" "+week;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }




}
