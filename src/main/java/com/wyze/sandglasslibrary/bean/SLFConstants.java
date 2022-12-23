package com.wyze.sandglasslibrary.bean;

import android.os.Environment;

import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;

import java.io.File;
import java.io.Serializable;
/**
 * created by yangjie
 * describe:一个存储全局变量的类
 * time: 2022/12/2
 * @author yangjie
 */
public class SLFConstants {

    public static boolean isGreyed = false;

    public static final boolean isShowLog = true;
    public static final boolean isUseXlog = false;
    public static boolean isOpenConsoleLog = true;
    public static boolean isLogFileEncrypt = false;

    public static final String rootPath = SLFBaseApplication.getAppContext().getFilesDir() + "/SLF"; // App
    public static final String cacheRootPath = SLFBaseApplication.getAppContext().getCacheDir() + "/SLF"; // App

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

    private static final String externalRootPath = SLFBaseApplication.getAppContext().getExternalFilesDir("slf").getAbsolutePath(); // storge root App 不暴露
    public static final String externalCachePath = externalRootPath + "/cache/"; // storge cache path
    public static final String externalDocumentPath = externalRootPath + "/document/"; // storge document path
    public static final String externalGallery = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + "/slf/"; // storge gallery path

    public static String logTime;

    public static final String SLF_APP_ID = "wapk_181259b351899207";

    public static String json = "{\n" +
            "  \"code\": 0,\n" +
            "  \"message\": \"success\",\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"All device types\",\n" +
            "      \"sub\": [\n" +
            "        {\n" +
            "          \"id\": 2,\n" +
            "          \"name\": \"RVI 问题\",\n" +
            "\t\t  \"deviceModel\": \"RVI\",\n" +
            "          \"sub\": [\n" +
            "            {\n" +
            "              \"id\": 3,\n" +
            "              \"name\": \"绑定问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 11,\n" +
            "                  \"name\": \"绑定时提示失败\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 12,\n" +
            "                  \"name\": \"绑定时提示超时\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 13,\n" +
            "                  \"name\": \"绑定时不能连接网络\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 14,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 4,\n" +
            "              \"name\": \"设备连接问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 15,\n" +
            "                  \"name\": \"经常出现断连\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 16,\n" +
            "                  \"name\": \"绑定完成后一直连接不到摄像机\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 17,\n" +
            "                  \"name\": \"只有某个摄像机连接失败\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 18,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 5,\n" +
            "              \"name\": \"升级问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 19,\n" +
            "                  \"name\": \"升级时提示失败\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 20,\n" +
            "                  \"name\": \"升级时提示超时\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 21,\n" +
            "                  \"name\": \"提示升级完成但未升级\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 22,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 6,\n" +
            "              \"name\": \"图像问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 23,\n" +
            "                  \"name\": \"图像质量模糊\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 24,\n" +
            "                  \"name\": \"切换清晰度不成功\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 25,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 7,\n" +
            "              \"name\": \"音频问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 26,\n" +
            "                  \"name\": \"app语音对话有杂音\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 27,\n" +
            "                  \"name\": \"设备语音有杂音\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 28,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 8,\n" +
            "              \"name\": \"硬件问题\",\n" +
            "              \"sub\": [\n" +
            "                {\n" +
            "                  \"id\": 29,\n" +
            "                  \"name\": \"摄像机损坏\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 30,\n" +
            "                  \"name\": \"摄像机发热\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 31,\n" +
            "                  \"name\": \"摄像机云台失灵\",\n" +
            "                  \"sub\": []\n" +
            "                },\n" +
            "                {\n" +
            "                  \"id\": 32,\n" +
            "                  \"name\": \"其他\",\n" +
            "                  \"sub\": []\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 9,\n" +
            "              \"name\": \"其他\",\n" +
            "              \"sub\": []\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 10,\n" +
            "              \"name\": \"提建议\",\n" +
            "              \"sub\": []\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "\n" +
            " ";

    public static final String json2 = "{\n" +
            "  \"code\": 0,\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"RVI问题\",\n" +
            "      \"sub\": [\n" +
            "        {\n" +
            "          \"id\": 2,\n" +
            "          \"name\": \"绑定问题\",\n" +
            "          \"sub\": [\n" +
            "            {\n" +
            "              \"id\": 20,\n" +
            "              \"name\": \"绑定失败提示失败\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"id\": 21,\n" +
            "              \"name\": \"绑定失败提示超时\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 3,\n" +
            "          \"name\": \"连接问题\",\n" +
            "          \"sub\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 4,\n" +
            "          \"name\": \"SD卡问题\",\n" +
            "          \"sub\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 5,\n" +
            "          \"name\": \"升级问题\",\n" +
            "          \"sub\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10,\n" +
            "      \"name\": \"APP问题\",\n" +
            "      \"sub\": [\n" +
            "        {\n" +
            "          \"id\": 11,\n" +
            "          \"name\": \"有bug\",\n" +
            "          \"sub\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 13,\n" +
            "          \"name\": \"不好用\",\n" +
            "          \"sub\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 14,\n" +
            "          \"name\": \"崩溃\",\n" +
            "          \"sub\": []\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": 15,\n" +
            "          \"name\": \"222\",\n" +
            "          \"sub\": []\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 16,\n" +
            "      \"name\": \"固件问题\",\n" +
            "      \"sub\": []\n" +
            "    }\n" +
            "  ],\n" +
            "  \"instance_id\": \"f851eff568b74f7fbdda8c0299a5c81f\",\n" +
            "  \"message\": \"Success\"\n" +
            "}";

    public static final String ALL_ROUND = "all_round";

    public static final String ROUND_FIRST="first";

    public static final String ROUND_END = "end";

    public static final String ROUND_MIDDLE = "middle";

    public static String appLogName  = "SLFlog_" + System.currentTimeMillis() / 1000 + ".zip";

    public static String firmwareLogName = "SLFfirmwarelog_" + System.currentTimeMillis() / 1000 + ".zip";
    public static String firmwareIOTLogName = "SLFfirmwarelog_IOT_" + System.currentTimeMillis() / 1000 + ".zip";
    public static String firmwareBluetoothLogName = "SLFfirmwarelog_Bluetooth_" + System.currentTimeMillis() / 1000 + ".zip";
}
