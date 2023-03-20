package com.sandglass.sandglasslibrary.utils.logutil;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.utils.SLFDateFormatUtils;
import com.tencent.mars.BuildConfig;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SLFLogUtils {
    private final static String TAG = "App Log";
    private final static String SDKTAG = "HLSDK_";
    private static Xlog sdkXlog;
    private static long sdkXloginstance = -1;
    /**
     * s
     * 控制整个文件是否可写入，当存储空间不足或需全部关闭Log的时候置为false
     */
    private static boolean canWrite = true;

    public static boolean isUseXLog = true;
    public static String PREFIX = "";
    public static String apiLogPath = "";
    public static String xlogCachePath = "";
    public static String XLog_KEY = "";
    private static HandlerThread logHandlerThread;
    private static Handler logHandler;



    static {
        initThreads();
    }

    private static void initThreads() {
        logHandlerThread = new HandlerThread("WriteLog");
        logHandlerThread.start();
        logHandler = new Handler(logHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle data = (Bundle) msg.obj;
                if (data != null) {
                    String tag = data.getString("TAG");
                    String content = data.getString("CONTENT");
                    if (isUseXLog) {
                        String methodName = "";
                        int lineNumber = 0;
                        String fileName = data.getString("THREAD");
                        if (fileName == null) {
                            fileName = "";
                        }
                        long handlerThreadId = logHandlerThread.getId();
                        long looperId = logHandlerThread.getLooper().getThread().getId();
                        switch (msg.what) {
                            case 1: {
                                Log.getImpl().logV(0, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                            }
                            break;
                            case 2: {
                                Log.getImpl().logD(0, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                            }
                            break;
                            case 3: {
                                Log.getImpl().logE(0, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                            }
                            break;
                            case 4: {
                                Log.getImpl().logW(0, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                            }
                            break;
                            case 5: {
                                if (sdkXlog != null) {
                                    sdkXlog.logI(sdkXloginstance, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                                } else {
                                    android.util.Log.i(tag, content);
                                }
                            }
                            break;
                            case 6: {
                                if (sdkXlog != null) {
                                    sdkXlog.logV(sdkXloginstance, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                                } else {
                                    android.util.Log.i(tag, content);
                                }
                            }
                            break;
                            case 7: {
                                if (sdkXlog != null) {
                                    sdkXlog.logD(sdkXloginstance, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                                } else {
                                    android.util.Log.i(tag, content);
                                }
                            }
                            break;
                            case 8: {
                                if (sdkXlog != null) {
                                    sdkXlog.logE(sdkXloginstance, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                                } else {
                                    android.util.Log.i(tag, content);
                                }
                            }
                            break;
                            case 9: {
                                if (sdkXlog != null) {
                                    sdkXlog.logW(sdkXloginstance, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                                } else {
                                    android.util.Log.i(tag, content);
                                }
                            }
                            break;
                            default: {
                                Log.getImpl().logI(0, tag, fileName, methodName, lineNumber, Process.myPid(), handlerThreadId, looperId, content);
                            }
                        }
                    } else {
                        switch (msg.what) {
                            case 6:
                            case 1: {
                                android.util.Log.v(tag, content);
                            }
                            break;
                            case 7:
                            case 2: {
                                android.util.Log.d(tag, content);
                            }
                            break;
                            case 8:
                            case 3: {
                                android.util.Log.e(tag, content);
                            }
                            break;
                            case 9:
                            case 4: {
                                android.util.Log.w(tag, content);
                            }
                            break;
                            default: {
                                android.util.Log.i(tag, content);
                            }
                        }
                    }
                }
            }
        };
    }

    private static Bundle combineData(String TAG, String content) {
//        int lineNumber = 0;
//        String methodName = "";
//        String fileName = "";
//        if (Thread.currentThread().getStackTrace().length > 4) {
//            lineNumber = Thread.currentThread().getStackTrace()[4].getLineNumber(); //行号
//            methodName = Thread.currentThread().getStackTrace()[4].getMethodName(); //函数名
//            fileName = Thread.currentThread().getStackTrace()[4].getFileName(); //文件名
//        }
        String thread = Thread.currentThread().getName();
        Message obtain = Message.obtain();
        Bundle data = obtain.getData();
        data.putString("TAG", TAG);
        data.putString("CONTENT", content);
        data.putString("THREAD", thread);
//        data.putInt("LINE_NUMBER", lineNumber);
//        data.putString("METHOD_NAME", methodName);
//        data.putString("FILE_NAME", fileName);
        return data;
    }

    public static void i(String TAG, String content) {
        logHandler.obtainMessage(0, combineData(TAG, content)).sendToTarget();
    }

    public static void v(String TAG, String content) {
        logHandler.obtainMessage(1, combineData(TAG, content)).sendToTarget();
    }

    public static void d(String TAG, String content) {
        logHandler.obtainMessage(2, combineData(TAG, content)).sendToTarget();
    }

    public static void e(String TAG, String content) {
        logHandler.obtainMessage(3, combineData(TAG, content)).sendToTarget();
    }


    public static void w(String tag, String content) {
        logHandler.obtainMessage(4, combineData(TAG, content)).sendToTarget();
    }

    private static Bundle combineSDKData(String TAG, String content) {
        int lineNumber = 0;
        String methodName = "";
        String fileName = "";
        if (Thread.currentThread().getStackTrace().length > 6) {
            lineNumber = Thread.currentThread().getStackTrace()[6].getLineNumber(); //行号
            methodName = Thread.currentThread().getStackTrace()[6].getMethodName(); //函数名
            fileName = Thread.currentThread().getStackTrace()[6].getFileName(); //文件名
        }
        Message obtain = Message.obtain();
        Bundle data = obtain.getData();
        data.putString("TAG", TAG);
        data.putString("CONTENT", content);
        data.putInt("LINE_NUMBER", lineNumber);
        data.putString("METHOD_NAME", methodName);
        data.putString("FILE_NAME", fileName);
        String thread = Thread.currentThread().getName();
        data.putString("THREAD", thread);
        return data;
    }

    public static void sdki(String TAG, String content) {
        logHandler.obtainMessage(5, combineData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdkv(String TAG, String content) {
        logHandler.obtainMessage(6, combineData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdkd(String TAG, String content) {
        logHandler.obtainMessage(7, combineData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdke(String TAG, String content) {
        logHandler.obtainMessage(8, combineData(SDKTAG + TAG, content)).sendToTarget();
    }


    public static void sdkw(String tag, String content) {
        logHandler.obtainMessage(9, combineData(SDKTAG + TAG, content)).sendToTarget();
    }

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    private static boolean isInitXLog = false;

    public static void initXLog(String cachePath, String xlogCachePath) {
        //init xlog
        if (!isInitXLog) {
            Xlog xlog = new Xlog();
            Log.setLogImp(xlog);
            checkFileExists(xlog, cachePath, xlogCachePath,"SLF_APP_API");
        }
    }

    public static void initXLog(String cachePath, String xlogCachePath,String PREFIX, String key) {
        //init xlog
        if (!isInitXLog) {
            Xlog xlog = new Xlog();
            Xlog.XLogConfig xLogConfig = new Xlog.XLogConfig();
            xLogConfig.pubkey = key;
            xlog.newXlogInstance(xLogConfig);
            Log.setLogImp(xlog);
            checkFileExists(xlog, cachePath, xlogCachePath,PREFIX);
        }
    }

    private static void checkFileExists(Xlog xlog, String cachePath, String xlogCachePath,String prefix) {
        String path = prefix + "_" + SLFDateFormatUtils.dateToString(System.currentTimeMillis(),SLFDateFormatUtils.YMDHM_CN) + ".xlog";
        File log = new File(xlogCachePath + path);
        if (!log.exists()) {
            xlog.appenderClose();
            xlog.appenderOpen(0, Xlog.AppednerModeSync, cachePath, xlogCachePath, PREFIX + "API", 0);
            setUserInfo();
            xlog.setMaxAliveTime(0, 3 * 24 * 60 * 60);
            if (BuildConfig.DEBUG) {
                Log.setConsoleLogOpen(true);
            } else {
                Log.setConsoleLogOpen(false);
            }

        }

    }


    public static void initSDKXlog(int level, int mode, String cacheDir, String logDir, String nameprefix, int cacheDays, String pubKey) {
        //init xlog
        if (sdkXlog == null) {
            sdkXlog = new Xlog();
        } else {
            return;
        }
        String path = nameprefix + SLFDateFormatUtils.dateToString(System.currentTimeMillis(),SLFDateFormatUtils.YMDHM_CN) + ".xlog";
        File log = new File(logDir + path);

        Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
        logConfig.level = level;
        logConfig.mode = mode;
        logConfig.logdir = logDir;
        logConfig.nameprefix = nameprefix;
        logConfig.compressmode = Xlog.ZLIB_MODE;
        logConfig.pubkey = pubKey;
        logConfig.cachedir = cacheDir;
        logConfig.cachedays = cacheDays;
        sdkXloginstance = sdkXlog.newXlogInstance(logConfig);
        sdkXlog.appenderFlush(sdkXloginstance, false);
        sdkXlog.setMaxAliveTime(sdkXloginstance, 3 * 24 * 60 * 60);

    }

    public static void sdkAppenderFlush(boolean isSync) {
        sdkXlog.appenderFlush(sdkXloginstance, isSync);
    }

    public static void appenderFlush(boolean isSync) {
        if (Log.getImpl() != null) {
            Log.getImpl().appenderFlush(0, isSync);
        }
    }

    public static void setSDKLogIsOpen(boolean logOpen) {
        if (sdkXlog == null || sdkXloginstance == -1) {
            return;
        }
        sdkXlog.setConsoleLogOpen(sdkXloginstance, logOpen);
    }


    /**
     * 写入用户名 同时包括所在时区和时间，时区和时间为当前运行的手机所设定的时区和时间
     */
    public static void setUserInfo() {
        if (canWrite) {
            StringBuffer buf = new StringBuffer();
            buf = buf.append("_API ");
            buf.append("\n\n");
            // 用户名
            buf = buf.append("UserName:");
            buf.append("\n\n");
            // 时区
            buf.append("TimeZone:").append(TimeZone.getDefault().getDisplayName());
            buf.append("\n\n");
            // 时间
            buf.append("Time:").append(getTime(true).toString());
            buf.append("\n\n");
            // 手机型号
            buf.append("phone model:").append(android.os.Build.MODEL);
            buf.append("\n\n");
            // 系统版本
            buf.append("Android version：").append(android.os.Build.VERSION.SDK_INT);

            // App版本
            buf.append("App version：");
            buf.append("\n\n");
            Log.w("", buf.toString());
        }
    }

    /**
     * 获取当前时间，用于构建文件名和文件中记录的时间
     *
     * @return StringBuffer
     */
    private static StringBuffer getTime(boolean isReadable) {
        Calendar cal = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        Integer year, month, day, hour, mintute, second, millisecond;
        year = cal.get(Calendar.YEAR);
        buffer.append(year);
        if (isReadable)
            buffer.append("-");
        if ((month = cal.get(Calendar.MONTH) + 1) < 10) {
            buffer.append("0").append(month);
        } else {
            buffer.append(month);
        }
        if (isReadable)
            buffer.append("-");
        if ((day = cal.get(Calendar.DAY_OF_MONTH)) < 10) {
            buffer.append("0").append(day);
        } else {
            buffer.append(day);
        }
        if (isReadable)
            buffer.append("  ");
        if ((hour = cal.get(Calendar.HOUR_OF_DAY)) < 10) {

            buffer.append("0").append(hour);
        } else {
            buffer.append(hour);
        }
        if (isReadable)
            buffer.append(":");
        if ((mintute = cal.get(Calendar.MINUTE)) < 10) {
            buffer.append("0").append(mintute);
        } else {
            buffer.append(mintute);
        }
        if (isReadable)
            buffer.append(":");
        if ((second = cal.get(Calendar.SECOND)) < 10) {
            buffer.append("0").append(second);
        } else {
            buffer.append(second);
        }
        if (isReadable)
            buffer.append(".");
        millisecond = cal.get(Calendar.MILLISECOND);
        if (millisecond < 10) {
            buffer.append("00").append(millisecond);
        } else if (millisecond >= 10 && millisecond < 100) {
            buffer.append("0").append(millisecond);
        } else {
            buffer.append(millisecond);
        }
        return buffer;
    }

    /**
     * 判断空间是否可用
     *
     * @param status 存储状态：0_sdcard存储 1_data目录下存储
     * @param size   需要剩余的空间下线
     * @return false 空间过小不可用 true 空间够大可用
     */
    private static boolean isAvailableSpace(int status, int size) {
        String sdcard = "";
        if (status == 0) {
            sdcard = Environment.getExternalStorageDirectory().getPath();
        } else {
            sdcard = Environment.getDataDirectory().getPath();
        }
        try {
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = (blocks * blockSize) / (1024 * 1024);
            return size <= availableSpare;
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.d("Log:isAvaiableSpace", "Exception: " + e.getMessage());
            return false;
        }
    }

}
