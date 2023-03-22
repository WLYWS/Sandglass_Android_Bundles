package com.sandglass.sandglasslibrary.utils.logutil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.os.StatFs;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.utils.SLFDateFormatUtils;
import com.tencent.mars.BuildConfig;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SLFLogUtil {
    private final static String TAG = "SLF Log";
    private final static String SDKTAG = "SLF_";
    private static Xlog sdkXlog;
    private static  long sdkXloginstance;
    /**
     * s
     * 控制整个文件是否可写入，当存储空间不足或需全部关闭Log的时候置为false
     */
    private static boolean canWrite = true;

    public static boolean isUseXLog = true;
    private static HandlerThread logHandlerThread;
    private static Handler logHandler;

    private static volatile SLFLogUtil log = null;
    private static final String PREFIX = "SLF_";
    private static List<String> listValue = new ArrayList();
    private static boolean canWirte = true;
    private static boolean toWrite = true;
    private static List<byte[]> listValueByte = new ArrayList();
    private static FileWriter fileWriter = null;
    private static BufferedWriter writer = null;
    private static long gitprt;//可写不同文件的参数
    private static SLFLogPrinter printer = new SLFLogPrinter();



    static {
        initThreads();
    }

    private static void initThreads() {
        logHandlerThread = new HandlerThread("WriteSdkLog");
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
        logHandler.obtainMessage(5, combineSDKData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdkv(String TAG, String content) {
        logHandler.obtainMessage(6, combineSDKData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdkd(String TAG, String content) {
        logHandler.obtainMessage(7, combineSDKData(SDKTAG + TAG, content)).sendToTarget();
    }

    public static void sdke(String TAG, String content) {
        logHandler.obtainMessage(8, combineSDKData(SDKTAG + TAG, content)).sendToTarget();
    }


    public static void sdkw(String tag, String content) {
        logHandler.obtainMessage(9, combineSDKData(SDKTAG + TAG, content)).sendToTarget();
    }

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("marsxlog");
    }

    private static boolean isInitXLog = false;

    public static void initXLog(String cachePath, String xlogCachePath) {
        //init xlog
        if (!isInitXLog) {
            Log.setLogImp(sdkXlog);
            checkFileExists(sdkXlog, cachePath, xlogCachePath);
        }
    }

    public static void initXLog(String cachePath, String xlogCachePath, String key) {
        //init xlog
        if (!isInitXLog) {
            Xlog xlog = new Xlog();
            Xlog.XLogConfig xLogConfig = new Xlog.XLogConfig();
            xLogConfig.pubkey = key;
            xlog.newXlogInstance(xLogConfig);
            Log.setLogImp(xlog);
            checkFileExists(xlog, cachePath, xlogCachePath);
        }
    }

    private static void checkFileExists(Xlog xlog, String cachePath, String xlogCachePath) {
        String path = PREFIX + "_" + SLFDateFormatUtils.dateToString(System.currentTimeMillis(),SLFDateFormatUtils.YMDHM_CN) + ".xlog";
        File log = new File(xlogCachePath + path);
        if (!log.exists()) {
            xlog.appenderClose();
            xlog.appenderOpen(0, Xlog.AppednerModeSync, cachePath, xlogCachePath, PREFIX, 0);
            //setUserInfo();
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

//        String path = nameprefix+"API_" + SLFDateFormatUtils.dateToString(System.currentTimeMillis(),SLFDateFormatUtils.YMDHM_CN) + ".xlog";
//        File log = new File(logDir + path);


            Xlog.XLogConfig logConfig = new Xlog.XLogConfig();
            logConfig.level = 0;
            logConfig.mode = 0;
            logConfig.logdir = SLFConstants.apiLogPath;
            logConfig.nameprefix = "SLF";
            logConfig.compressmode = Xlog.ZLIB_MODE;
            logConfig.pubkey = SLFLogAPI.XLOG_PUBKEY;
            logConfig.cachedir = SLFConstants.xlogCachePath;
            logConfig.cachedays = 0;
             sdkXloginstance = sdkXlog.newXlogInstance(logConfig) & Long.MAX_VALUE;
            sdkXlog.appenderFlush(sdkXloginstance, false);
            sdkXlog.setMaxAliveTime(sdkXloginstance, 3 * 24 * 60 * 60*1000);

    }

    public static  BigDecimal readUnsignedLong(long value) throws IOException {
        if (value >= 0)
            return new BigDecimal(value);
        long lowValue = value & 0x7fffffffffffffffL;
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
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


    public static void createFile(boolean isUseXlog) {
        File tutkLog = new File(SLFConstants.tutkLogPath);
        if (!tutkLog.exists()) {
            boolean isTutkLogMkdirs = tutkLog.mkdirs();
            e("isTutkLogMkdirs", isTutkLogMkdirs + "");
        }

        File filePath = new File(SLFConstants.feedbacklogPath);
        boolean isApiLogPathMkdirs;
        if (!filePath.exists()) {
            isApiLogPathMkdirs = filePath.mkdirs();
            e("isFeedbackLogMkdirs", isApiLogPathMkdirs + "");
        }

        filePath = new File(SLFConstants.apiLogPath);
        if (!filePath.exists()) {
            isApiLogPathMkdirs = filePath.mkdirs();
            e("isApiLogPathMkdirs", isApiLogPathMkdirs + "");
        }

        File alarmPicPath = new File(SLFConstants.apiLogPath);
        if (!alarmPicPath.exists()) {
            boolean isAlarmPicPath = alarmPicPath.mkdirs();
            e("isAlarmPicPath", isAlarmPicPath + "");
        }

        if (!isAvaiableSpace(0, 1)) {
            canWirte = false;
            return;
        }
        createLogFile(isUseXlog, filePath);
    }

    private static void createLogFile(boolean isUseXlog, File filePath) {
        SLFConstants.logTime = getTime(false).toString();
        if (!isUseXlog) {
            File[] fileList = filePath.listFiles();
            String fileName;
            if (null != fileList && fileList.length > 2) {
                fileName = fileList[0].getName();
                String name1 = fileList[1].getName();
                String n0 = fileName.substring(0, fileName.length() - 4);
                String n1 = name1.substring(0, name1.length() - 4);

                try {
                    n0 = n0.replace("SLF_", "");
                    n1 = n1.replace("SLF_", "");
                    boolean isDelete;
                    if (Long.parseLong(n0) < Long.parseLong(n1)) {
                        isDelete = fileList[0].delete();
                        e("flieList[0]_isDelete", isDelete + "");
                    } else {
                        isDelete = fileList[1].delete();
                        e("flieList[1]_isDelete", isDelete + "");
                    }
                } catch (Exception var9) {
                }
            }

            fileName = "SLF_" + getTime(false).append(".txt").toString();
            File file1 = new File(filePath, fileName);

            try {
                fileWriter = new FileWriter(file1);
                writer = new BufferedWriter(fileWriter);
                writer.append("\t\tTitle:").append(getTime(false));
                writer.newLine();
                writer.newLine();
            } catch (Exception var8) {
                Log.e("SLFLogUtil: CreateFile", "write file error:" + var8.getMessage());
            }

        }
    }

    @SuppressLint({"LongLogTag"})
    private static boolean isAvaiableSpace(int status, int size) {
        String sdcard;
        if (status == 0) {
            sdcard = Environment.getExternalStorageDirectory().getPath();
        } else {
            sdcard = Environment.getDataDirectory().getPath();
        }

        try {
            StatFs statFs = new StatFs(sdcard);
            long blockSize = (long)statFs.getBlockSize();
            long blocks = (long)statFs.getAvailableBlocks();
            long availableSpare = blocks * blockSize / 1048576L;
            return (long)size <= availableSpare;
        } catch (Exception var10) {
            Log.d("SLFLogUtil:isAvaiableSpace", "Exception: " + var10.getMessage());
            return false;
        }
    }

}
