package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotDateView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

import static com.wyze.sandglasslibrary.utils.SLFDateFormatUtils.getSystemTimeFormat;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotTimeViewHolder extends SLFChatBotBaseViewHodler{

    public final static int TODAY_DATE = 1;
    public final static int YESTERDAY_DATE = 2;
    public final static int CURRENT_YEAR_DATE = 3;
    public final static int NO_CURRENT_YEAR_DATE = 4;
    public static final String HM_12 = "hh:mm aa";
    public static final String HM_24 = "HH:mm";
    public static final String MMDD_12 = "MM/dd hh:mm aa";
    public static final String MMDD_24 = "MM/dd HH:mm";
    public static final String YHD_12 = "MM/dd/yyyy hh:mm aa";
    public static final String YHD_24 = "MM/dd/yyyy HH:mm";
    private static final String TAG = "SLFChatBotDateView.CLASS";
    private static final String YESTERDAY = "Yesterday";
    private Context context;

    private SLFChatBotDateView itemView;
    private TextView tv_chat_bot_date;
    public SLFChatBotTimeViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tv_chat_bot_date = itemView.findViewById(R.id.tv_chat_bot_date);
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        setDateText(String.valueOf(bean.getMsgTime()));
    }

    public void setDateText (String date) {
        int type = getDateType(date);
        switch (type){
            case TODAY_DATE:
                if (is12Hour()){
                    tv_chat_bot_date.setText(replaceAMPM(formatDate(Long.valueOf(date),HM_12)));
                }else {
                    tv_chat_bot_date.setText(formatDate(Long.valueOf(date),HM_24));
                }
                break;
            case YESTERDAY_DATE:
                if (is12Hour()){
                    tv_chat_bot_date.setText(YESTERDAY+" "+replaceAMPM(formatDate(Long.valueOf(date),HM_12)));
                }else {
                    tv_chat_bot_date.setText(YESTERDAY+" "+formatDate(Long.valueOf(date),HM_24));
                }
                break;
            case CURRENT_YEAR_DATE:
                if (is12Hour()){
                    tv_chat_bot_date.setText(replaceAMPM(formatDate(Long.valueOf(date),MMDD_12)));
                }else {
                    tv_chat_bot_date.setText(formatDate(Long.valueOf(date),MMDD_24));
                }
                break;
            case NO_CURRENT_YEAR_DATE:
                if (is12Hour()){
                    tv_chat_bot_date.setText(replaceAMPM(formatDate(Long.valueOf(date),YHD_12)));
                }else {
                    tv_chat_bot_date.setText(formatDate(Long.valueOf(date),YHD_24));
                }
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
        String nowTime = formatDate(tsp,YHD_24);
        String dateTime = formatDate(Long.valueOf(date),YHD_24);
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
            Date date = new SimpleDateFormat(YHD_24).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int day = ca.get(Calendar.DAY_OF_MONTH);//一月中的第几天
            return day;
        }catch (Exception e){
            SLFLogUtil.e(TAG,"时间格式有问题"+e.getMessage());
        }
        return 0;
    }

    private int getMonth (String time) {
        try {
            //1、获取string对应date日期：
            Date date = new SimpleDateFormat(YHD_24).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int month = ca.get(Calendar.MONTH);//第几个月
            return month;
        }catch (Exception e){
            SLFLogUtil.e(TAG,"时间格式有问题"+e.getMessage());
        }
        return 0;
    }

    private int getYear (String time) {
        try {
            //1、获取string对应date日期：
            Date date = new SimpleDateFormat(YHD_24).parse(time);
            //2、获取date对应的Calendar对象
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            //3、可以从ca中获取各种该日期的属性值：
            int year = ca.get(Calendar.YEAR);//年份数值
            return year;
        }catch (Exception e){
            SLFLogUtil.e(TAG,"时间格式有问题"+e.getMessage());
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

    private boolean is12Hour(){
        String typeTime = SLFDateFormatUtils.getSystemTimeFormat(context);
        if (typeTime.equals("12")){
            return true;
        }else{
            return false;
        }
    }

    private String replaceAMPM(String time){
        if (time.contains("上午")){
            return time.replace("上午", "AM");
        }
        if (time.contains("下午")){
            return time.replace("下午", "PM");
        }
        return time;
    }
}
