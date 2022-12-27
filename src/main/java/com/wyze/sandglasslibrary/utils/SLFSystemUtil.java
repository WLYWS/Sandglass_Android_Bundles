package com.wyze.sandglasslibrary.utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * created by yangjie
 * describe:安卓系统管理工具类
 * time: 2022/12/22
 */
@SuppressWarnings({"deprecation", "unused"})
public final class SLFSystemUtil {
    private static final String TAG = "SLFSystemUtil";

    private SLFSystemUtil() {
    }

    /**
     * 获取调用当前本方法类名
     *
     * @return 当前调用本方法类名
     */
    public static String getTag() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = 3;
        for (; stackOffset < trace.length; stackOffset++) {
            StackTraceElement e = trace[stackOffset];
            String name = e.getClassName();
            if (!name.equals(SLFSystemUtil.class.getName())) {
                break;
            }
        }
        String name = trace[stackOffset].getClassName();
        int lastIndex = name.lastIndexOf('.');
        if (-1 == lastIndex)
            return "ignorant of class";
        return name.substring(lastIndex + 1).split("\\$")[0];
    }

    /**
     * 跳转网页
     */
    public static void gotoUri(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }


    /**
     * 获取手机MAC地址
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            }
        }
        return "";
    }

    @SuppressLint("HardwareIds")
    public static String getPhoneId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取是否存在NavigationBar
     */
    public static boolean isHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            @SuppressLint("PrivateApi") Class<?> cls = Class.forName("android.os.SystemProperties");
            Method m = cls.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(cls, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            //empty code
        }

        return hasNavigationBar;

    }

    /**
     * 获取NavigationBar的高度
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && isHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取手机本地IP地址
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface info = en.nextElement();
                Enumeration<InetAddress> enAddr = info.getInetAddresses();
                while (enAddr.hasMoreElements()) {
                    InetAddress addr = enAddr.nextElement();
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            SLFLogUtil.e(TAG, e.getMessage());
        }
        return "";
    }

    /**
     * 跳转到网络设置页面
     */
    public static void gotoNetworkSetting(Context context) {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_WIRELESS_SETTINGS);
        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * 获取客户端的版本代号
     */
    public static int getVersionCode() {
        PackageManager pm = SLFBaseApplication.getAppContext().getPackageManager();
        int versionCode;
        try {
            PackageInfo info = pm.getPackageInfo(SLFBaseApplication.getAppContext().getPackageName(), 0);
            versionCode = info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 0;
        }
        return versionCode;
    }

    /**
     * 获取客户端的版本名称
     */
    public static String getVersionName() {
        PackageManager pm = SLFBaseApplication.getAppContext().getPackageManager();
        String versionName;
        try {
            PackageInfo info = pm.getPackageInfo(SLFBaseApplication.getAppContext().getPackageName(), 0);
            versionName = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            versionName = "--";
        }
        return versionName;
    }

    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(manager!=null){
            //遍历所有应用
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : infos) {
                if (info.pid == pid)//得到当前应用
                    return info.processName;//返回包名
            }
        }

        return "";
    }

    /**
     * 判断是否打开网络
     */
    public static boolean isNetWorkAvailable(Context context) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isExternalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内存可用空间
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blocksize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blocksize * availableBlocks;
    }

    /**
     * 获取SD卡的可用空间
     */
    public static long getAvailableExternalMemorySize() {
        if (isExternalMemoryAvailable()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(sdcardDir.getPath());
            long blockSize = statFs.getBlockSize();
            long availableBlocks = statFs.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            return -1;
        }
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Activity activity) {
        int value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            //empty code
        }
        return value;
    }

    /**
     * 设置屏幕的亮度
     */
    public static void setScreenBrightness(Activity activity, int value) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = value / 255f;
        activity.getWindow().setAttributes(params);
    }

    /** 启动应用的设置*/
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}