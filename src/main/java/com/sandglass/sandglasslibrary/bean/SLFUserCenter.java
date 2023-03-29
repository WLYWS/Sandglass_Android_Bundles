package com.sandglass.sandglasslibrary.bean;

import static com.sandglass.sandglasslibrary.utils.SLFResourceUtils.getResources;

import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.sandglass.sandglasslibrary.BuildConfig;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.moudle.SLFUserDeviceSaved;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserDeviceListResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserInfoResponseBean;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.SLFSystemUtil;

import java.util.HashMap;
import java.util.TimeZone;
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
    public static String deviceId = "ABC";
    /**设备model*/
    public static String deviceModel="RVI";
    /**设备时区*/
    public static String deviceTimeZone = "";
    /**固件版本号*/
    public static String firmwareVersion= "0.0.0";
    /**phone type  1.ios,2.android*/
    public static int   phontType = 2;
    @SuppressWarnings("java:S1444")
    public static long timeDifference = System.currentTimeMillis() - SystemClock.elapsedRealtime(); //  本地时间和服务器时间差

    public static String rootPath = Environment.getExternalStorageDirectory().getPath() + "/SLF";

    public static final boolean isShowLog = true;
    public static final boolean isOpenScene = false;
    public static final boolean isCaughtException = true;

    public static SLFUserInfoResponseBean userInfoBean;

    public static SLFUserDeviceListResponseBean userDeviceListBean;

    private static HashMap<Long, SLFUserDeviceSaved> mInstance;
    /**保存用户信息的单例*/
    public synchronized static HashMap<Long, SLFUserDeviceSaved> getInstance(){
        if(mInstance==null){
            mInstance = new HashMap<Long, SLFUserDeviceSaved>();
        }
        return mInstance;
    }

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
        return Build.BRAND +" "+ Settings.Global.getString(SLFApi.getSLFContext().getContentResolver(), Settings.Global.DEVICE_NAME);
    }
    /**获取手机系统版本*/
    public static String getOSVersion(){
        return "Android " + android.os.Build.VERSION.RELEASE;
    }
    /**获取手机唯一识别码*/
    public static String getPhone_id(){
        if(TextUtils.isEmpty(phone_id)){
            phone_id = SLFSpUtils.getString(SLFSpUtils.SLF_PHONE_ID,getPhoneid());
            SLFSpUtils.putCommit(SLFSpUtils.SLF_PHONE_ID,phone_id);
        }
        return phone_id;
    }
    private static String getPhoneid(){
        String serial = Build.SERIAL;
        String androidID = Settings.Secure.getString(SLFApi.getSLFContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if(androidID==null){
            androidID = UUID.randomUUID().toString();
        }
        if(serial==null){
            serial = UUID.randomUUID().toString();
        }
        String id = androidID + serial;
        return id;
    }
    /**获取手机认证型号*/
    public static String getPhoneFactoryModel(){
        return android.os.Build.MODEL;
    }



//    public static String getuuId() {
//        String uuid = SLFMD5Util.encode(getUniquePsuedoID() + System.currentTimeMillis() + (int) ((Math.random() * 9 + 1) * 10000));
//        return uuid;
//    }

    public static String getDeviceTimeZone(){
        return TimeZone.getDefault().getID();
    }

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
