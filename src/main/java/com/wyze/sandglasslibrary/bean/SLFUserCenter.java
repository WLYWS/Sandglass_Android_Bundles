package com.wyze.sandglasslibrary.bean;

import static com.wyze.sandglasslibrary.utils.SLFResourceUtils.getResources;

import android.os.Environment;
import android.os.SystemClock;
import android.util.DisplayMetrics;

/**
 * Greated by yangjie
 * discribe:资源文件工具类
 * time:2022/12/10
 * @author yangjie
 */
public class SLFUserCenter {

    public static String app_name = "Sandglass";//app名称
    public static String app_version = "";//版本号
    public static String user_id = "";
    public static String phone_id = "";
    public static String PACKAGE_NAME = ""; //包名
    public static String access_token; // App access_token
    public static String refresh_token; // App refresh_token
    /**
     * 统一的HTTP请求超时值
     */
    public static final int HTTP_TIMEOUT = 30 * 1000;
    @SuppressWarnings("java:S1444")
    public static long timeDifference = System.currentTimeMillis() - SystemClock.elapsedRealtime(); //  本地时间和服务器时间差

    public static String rootPath = Environment.getExternalStorageDirectory().getPath() + "/Wyze";

    public static final boolean isShowLog = true;
    public static final boolean isOpenScene = false;
    public static final boolean isCaughtException = true;

    public static String getUserAgent(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        return SLFUserCenter.app_name + "_android/" + SLFUserCenter.app_version+" ("+android.os.Build.MODEL+"; Android "+android.os.Build.VERSION.RELEASE+"; "+"Scale/"+density+"; Height/"+heightPixels+"; Width/"+widthPixels+")";
    }


}
