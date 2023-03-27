package com.sandglass.sandglasslibrary.utils;

import android.content.ContentResolver;
import android.content.Context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SLFDateFormatUtils {

    /**
     * 时间转换 add by yangjie 2022/12/6
     *
     * @param
     * @return
     */
    public static final String YMDHMSS = "yyyy年MM月dd日 HH:mm";
    public static final String YMDHM_CN = "yyyy年MM月dd日";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String MDYT = "MM/dd/yyyy HH:mm";
    public static final String MDYT12 = "MM/dd/yyyy HH:mm aa";
    public static final SimpleDateFormat yymmdd = new SimpleDateFormat(YYYYMMDD);
    public static final SimpleDateFormat ymdhmsSDF = new SimpleDateFormat(YMDHMS);
    public static final SimpleDateFormat ymdhm_cnSDF = new SimpleDateFormat(YMDHM_CN);
    public static final SimpleDateFormat mdytSDF = new SimpleDateFormat(MDYT);
    //时间戳转换日期格式
    public static String timeStampToDate(Context context, long tsp, String... format) {
        SimpleDateFormat simpleDateFormat;
        String dateTime = "";
        String typeTime = getSystemTimeFormat(context);
        if (format.length < 1) {
            if (typeTime.equals("12"))
                simpleDateFormat = new SimpleDateFormat("MM/dd h:mm aa", Locale.getDefault());
            else
                simpleDateFormat = new SimpleDateFormat("MM/dd H:mm", Locale.getDefault());
            dateTime = simpleDateFormat.format(tsp);
            if (dateTime.contains("上午"))
                dateTime = dateTime.replace("上午", "AM");
            else
                dateTime = dateTime.replace("下午", "PM");
        } else {
            simpleDateFormat = new SimpleDateFormat(format[0], Locale.getDefault());
            dateTime = simpleDateFormat.format(tsp);
        }
        return dateTime;
    }

    public static String getSystemTimeFormat(Context context) {
        String timeType = "12";
        ContentResolver cv = context.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);

//        if (strTimeFormat.equals("24")) {
        if ("24".equals(strTimeFormat)) {
            timeType = "24";
        }
        return timeType;
    }


    public static String getNumberTypeForTitle(long c) {
        return new DecimalFormat(",###").format(c);
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @return
     */
    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * 时间戳转换成需要的格式
     */
    public static String getDateToMyString(Context context,long second){
        Date date = new Date(second);
        String typeTime = getSystemTimeFormat(context);
        SimpleDateFormat format;
        String time;
            if (typeTime.equals("12")) {
                format = new SimpleDateFormat(MDYT12, Locale.getDefault());
                time = replaceAMPM(format.format(date));
                return time;
            }else {
                format = new SimpleDateFormat(MDYT, Locale.getDefault());
                return format.format(date);
            }

    }

    public static String replaceAMPM(String time){
        if (time.contains("上午")){
            return time.replace("上午", "AM");
        }
        if (time.contains("下午")){
            return time.replace("下午", "PM");
        }
        return time;
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        return getCurrentTime(new SimpleDateFormat(YMDHMS));
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime(SimpleDateFormat format) {
        return format.format(new Date());
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String dateToString(long date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
