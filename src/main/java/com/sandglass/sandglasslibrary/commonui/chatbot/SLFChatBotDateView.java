package com.sandglass.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot上面的时间显示
 */
public class SLFChatBotDateView extends ConstraintLayout {

    public final static int TODAY_DATE = 1;
    public final static int YESTERDAY_DATE = 2;
    public final static int CURRENT_YEAR_DATE = 3;
    public final static int NO_CURRENT_YEAR_DATE = 4;
    public static final String HM = "HH:mm aa";
    public static final String MMDD = "MM/dd HH:mm aa";
    public static final String YHD = "MM/dd/yyyy HH:mm aa";
    private static final String TAG = "SLFChatBotDateView.CLASS";
    private static final String YESTERDAY = "Yesterday";
    private  ViewGroup parent;

    private TextView tv_chat_bot_date;
    public SLFChatBotDateView (Context context, ViewGroup parent) {
        this (context);
        this.parent = parent;
        initView(context,null);

    }
    public SLFChatBotDateView (Context context) {
        super(context);
        //initView(context,null);
    }

    public SLFChatBotDateView (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //initView(context,attrs);
    }

    public SLFChatBotDateView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initView(context,attrs);
    }

    private void initView (Context context, AttributeSet attrs) {
        View chat_bot_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_chat_bot_date, parent, false);
        //View chat_bot_view = View.inflate(context, R.layout.slf_chat_bot_date,parent);
        tv_chat_bot_date = chat_bot_view.findViewById(R.id.tv_chat_bot_date);
    }



    public void setDateText (String date) {
        int type = getDateType(date);
        switch (type){
            case TODAY_DATE:
                tv_chat_bot_date.setText(formatDate(Long.valueOf(date),HM));
                break;
            case YESTERDAY_DATE:
                tv_chat_bot_date.setText(YESTERDAY+" "+formatDate(Long.valueOf(date),HM));
                break;
            case CURRENT_YEAR_DATE:
                tv_chat_bot_date.setText(formatDate(Long.valueOf(date),MMDD));
                break;
            case NO_CURRENT_YEAR_DATE:
                tv_chat_bot_date.setText(formatDate(Long.valueOf(date),YHD));
                break;
        }
    }

    /**
     * 根据获取的时间判定显示的时间类型
     * @param date
     * @return
     */
    private int getDateType (String date) {
        if (TextUtils.isEmpty(date)){
            return TODAY_DATE;
        }
        long tsp = System.currentTimeMillis();
        String nowTime = formatDate(tsp,YHD);
        String dateTime = formatDate(Long.valueOf(date),YHD);
        int nowYear = getYear(nowTime);
        int nowMonth = getMonth(nowTime);
        int nowDay = getDay(nowTime);
        int dateYear = getYear(dateTime);
        int dateMonth = getMonth(dateTime);
        int dateDay = getDay(dateTime);
        if (isYesterday(Long.valueOf(date))){
            return YESTERDAY_DATE;
        }
        if (nowYear!=dateYear){
            return NO_CURRENT_YEAR_DATE;
        }
        if (nowMonth!=dateMonth||nowDay!=dateDay){
            return CURRENT_YEAR_DATE;
        }
        return TODAY_DATE;
    }

    /**
     * 判定是否是昨天
     * @return
     */
    public boolean isYesterday(long time) {
        boolean isYesterday = false;
        Date date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(sdf.format(new Date()));
            if (time < date.getTime() && time > (date.getTime() - 24*60*60*1000)) {
                isYesterday = true;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isYesterday;
    }

    private int getDay (String time) {
        try {
            //1、获取string对应date日期：
            Date date = new SimpleDateFormat(YHD).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int day = ca.get(Calendar.DAY_OF_MONTH);//一月中的第几天
            return day;
        }catch (Exception e){
            SLFLogUtil.sdke(TAG,"时间格式有问题"+e.getMessage());
        }
        return 0;
    }

    private int getMonth (String time) {
        try {
            //1、获取string对应date日期：
            Date date = new SimpleDateFormat(YHD).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int month = ca.get(Calendar.MONTH);//第几个月
            return month;
        }catch (Exception e){
            SLFLogUtil.sdke(TAG,"时间格式有问题"+e.getMessage());
        }
        return 0;
    }

    private int getYear (String time) {
        try {
            //1、获取string对应date日期：
            Date date = new SimpleDateFormat(YHD).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int year = ca.get(Calendar.YEAR);//年份数值
            return year;
        }catch (Exception e){
            SLFLogUtil.sdke(TAG,"时间格式有问题"+e.getMessage());
        }
        return 0;
    }

    /**
     * 格式化时间格式
     * @param date
     * @param format
     */
    private String formatDate (long date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        String dateTime = simpleDateFormat.format(date);
        return dateTime;
    }
}
