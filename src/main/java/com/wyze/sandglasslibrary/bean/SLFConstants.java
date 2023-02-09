package com.wyze.sandglasslibrary.bean;

import android.os.Environment;

import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.commonapi.SLFApi;

import java.io.File;
import java.io.Serializable;

/**
 * created by yangjie
 * describe:一个存储全局变量的类
 * time: 2022/12/2
 *
 * @author yangjie
 */
public class SLFConstants {

    public static boolean isGreyed = false;

    public static final boolean isShowLog = true;
    public static final boolean isUseXlog = false;
    public static boolean isOpenConsoleLog = true;
    public static boolean isLogFileEncrypt = false;

    public static final String rootPath = SLFApi.getSLFContext().getFilesDir() + "/SLF"; // App
    public static final String cacheRootPath = SLFApi.getSLFContext().getCacheDir() + "/SLF"; // App

    public static final String documentPath = rootPath + "/document/";

    public static final String devRootPath = documentPath + "platformkit/";
    public static String cachePath = cacheRootPath + "/slfcache/";

    public static String slfCachePath = cachePath + "sandglass/";

    public static String apiLogPath = slfCachePath + "alog/";  //  API日志存放路径
    public static String fwLogPath = slfCachePath + "apilog/";  //  API日志存放路径
    public static String tutkLogPath = slfCachePath + "tutkLog/";  //  tutk日志存放路径
    public static String xlogCachePath = devRootPath + "xlog";
    public static String feedbacklogPath = slfCachePath + "feedLog/";//反馈上传log

    public static String CROP_IMAGE_PATH = slfCachePath + "img/crop/";

    private static final String externalRootPath = SLFApi.getSLFContext().getExternalFilesDir("slf").getAbsolutePath(); // storge root App 不暴露
    public static final String externalCachePath = externalRootPath + "/cache/"; // storge cache path
    public static final String externalDocumentPath = externalRootPath + "/document/"; // storge document path
    public static final String externalGallery = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + "/slf/"; // storge gallery path

    public static String logTime;

    public static final String SLF_APP_ID = "wapk_181259b351899207";

    public static final String ALL_ROUND = "all_round";

    public static final String ROUND_FIRST = "first";

    public static final String ROUND_END = "end";

    public static final String ROUND_MIDDLE = "middle";

    public static String appLogName = "SLFlog_" + System.currentTimeMillis() / 1000 + ".zip";

    public static String firmwareLogName = "SLFfirmwarelog_" + System.currentTimeMillis() / 1000 + ".zip";
    public static String firmwareIOTLogName = "SLFfirmwarelog_IOT_" + System.currentTimeMillis() / 1000 + ".zip";
    public static String firmwareBluetoothLogName = "SLFfirmwarelog_Bluetooth_" + System.currentTimeMillis() / 1000 + ".zip";

    public static final String UPLOADING = "uploading";
    public static final String UPLOADED = "uploaded";
    public static final String UPLOADIDLE = "uploadidle";
    public static final String UPLOADFAIL = "uploadfail";

    public static final String LOGID = "logid";

    //网络不可用
    public static final String NETWORK_UNAVAILABILITY = "network_unavailability";
    //正在使用流量
    public static final String MOBILE_AVAILABILITY = "mobile_availability";
    //正在使用wifi
    public static final String WIFI_AVAILABILITY = "wifi_availability";
    //相机模式
    public static final String CAMERA_PHOTO = "camera_photo";
    public static final String CAMERA_VIDEO = "camera_video";
    //recode对象数据
    public static final String RECORD_DATA = "record_data";
    //历史留言对象
    public static final String LEAVE_MSG_DATA = "leave_msg_data";

}
