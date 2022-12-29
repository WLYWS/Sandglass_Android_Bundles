package com.wyze.sandglasslibrary.bean;

import static com.wyze.sandglasslibrary.utils.SLFResourceUtils.getResources;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.wyze.sandglasslibrary.BuildConfig;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFMD5Util;
import com.wyze.sandglasslibrary.utils.SLFSpUtils;
import com.wyze.sandglasslibrary.utils.SLFSystemUtil;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.UUID;

/**
 * Greated by yangjie
 * discribe:资源文件工具类
 * time:2022/12/10
 * @author yangjie
 */
public class SLFUserCenter {

    /**app名称*/
    public static String app_name = "Sandglass";//app名称
    /**app版本号*/
    public static String app_version = "";//版本号
    /**用户id*/
    public static String user_id = "";
    /**手机唯一识别*/
    public static String phone_id = SLFSpUtils.getString(SLFSpUtils.SLF_PHONE_ID,"");
    public static String PACKAGE_NAME = ""; //包名
    public static String access_token; // App access_token
    public static String refresh_token; // App refresh_token
    /**设备id*/
    public static String deviceId = "";
    /**设备model*/
    public static String deviceModel="";
    /**固件版本号*/
    public static String firmwareVersion= "";
    /**phone type  1.ios,2.android*/
    public static int   phontType = 2;
    @SuppressWarnings("java:S1444")
    public static long timeDifference = System.currentTimeMillis() - SystemClock.elapsedRealtime(); //  本地时间和服务器时间差

    public static String rootPath = Environment.getExternalStorageDirectory().getPath() + "/SLF";

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
    /**获取APP的版本号*/
    public static int getAppVersion(){
        return SLFSystemUtil.getVersionCode();
    }

    /**获取APP的版本名称*/
    public static String getAppVersionName(){
        return SLFSystemUtil.getVersionName();
    }

    /**获取plugin的版本号*/
    public static String getPluginversion(){
        return BuildConfig.versionName;
    }
    /**获取手机型号*/
    public static String getPhoneModel(){
        return android.os.Build.BRAND;
    }
    /**获取手机系统版本*/
    public static String getOSVersion(){
        return android.os.Build.VERSION.RELEASE;
    }
    /**获取手机唯一识别码*/
    public static String getPhone_id(){
        if(TextUtils.isEmpty(phone_id)){
            phone_id = SLFSpUtils.getString(SLFSpUtils.SLF_PHONE_ID,UUID.randomUUID().toString());
            SLFSpUtils.putCommit(SLFSpUtils.SLF_PHONE_ID,phone_id);
        }
        return phone_id;
    }
    /**获取手机认证型号*/
    public static String getPhoneFactoryModel(){
        return android.os.Build.MODEL;
    }

//    public static String getuuId() {
//        String uuid = SLFMD5Util.encode(getUniquePsuedoID() + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 10000));
//        return uuid;
//    }

    /**
     * 获取手机的唯一标识
     *
     * @return
     */
//    public static String getUniquePsuedoID() {
//        String serial = null;
//
//        String m_szDevIDShort = "35" +
//                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
//
//                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
//
//                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
//
//                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
//
//                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
//
//                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
//
//                Build.USER.length() % 10; //13 位
//
//        try {
//            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
//            //API>=9 使用serial号
//            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
//        } catch (Exception exception) {
//            //serial需要一个初始化
//            serial = "serial"; // 随便一个初始化
//        }
//        //使用硬件信息拼凑出来的15位号码
//        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
//    }
}
