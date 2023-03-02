package com.sandglass.sandglasslibrary.utils;

/**
 * Greated by yangjie
 * describe:获取占位符的字符串
 * time:2022/12/14
 */
public class SLFStringFormatUtil {

    public static String getFormatString(int viewid,int value){
        String viewStr = SLFResourceUtils.getString(viewid);
        String format = String.format(viewStr,value);
        return format;
    }

    public static String getFormatString(int viewid,long value){
        String viewStr = SLFResourceUtils.getString(viewid);
        String format = String.format(viewStr,value);
        return format;
    }
}
