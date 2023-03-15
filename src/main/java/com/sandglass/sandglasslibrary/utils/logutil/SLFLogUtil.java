//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sandglass.sandglasslibrary.utils.logutil;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.Build.VERSION;
import android.util.Log;

import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.utils.SLFFileUtils;
import com.tencent.mars.xlog.Xlog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public final class SLFLogUtil extends Xlog {
    private static final String TAG = "API SLFLogUtil";
    //private static final String XLOG_PUBKEY = "587aa95c61833d2aeddfb0ea1a1da9a6e023d337955730e8d0384bd23f9e0efbfb51410989bb3cd8db37680f9d72e6515f4d041ac495c63b2b0b6b45d6579f7b";
    private static final String XLOG_PUBKEY = "c7b41a50681125813cb88c40653dcf4bb1ca1ab8a62626e9b6bb8a8b0bfa419ae3e142158c586864ec8aaf10a5ba9240be41e9f278ee7696f97f7ae7caa3c0e4";
    private static volatile SLFLogUtil log = null;
    private static final String PREFIX = "WYZE_";
    private static List<String> listValue = new ArrayList();
    private static boolean canWirte = true;
    private static boolean toWrite = true;
    private static List<byte[]> listValueByte = new ArrayList();
    private static FileWriter fileWriter = null;
    private static BufferedWriter writer = null;
    private static SLFLogPrinter printer = new SLFLogPrinter();

    public static SLFLogUtil getInstance() {
        if (null == log) {
            Class var0 = SLFLogUtil.class;
            synchronized(SLFLogUtil.class) {
                if (null == log) {
                    fileWriter = null;
                    writer = null;
                    log = new SLFLogUtil();
                    Log.i("API SLFLogUtil", "create new instance");
                }
            }
        } else {
            Log.i("API SLFLogUtil", "exist one instance");
        }

        return log;
    }

    private SLFLogUtil() {
        com.tencent.mars.xlog.Log.appenderFlush(true);
        createFile(true);
        Xlog.appenderOpen(0, 0, SLFConstants.xlogCachePath, SLFConstants.apiLogPath, "SLF_API", 0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
    }

    public static void initAPILog() {
        getInstance();
        setUserInfo();
    }

    public void initPluginXlog(String pluginId) {
        com.tencent.mars.xlog.Log.appenderFlush(true);
        this.createFile(pluginId);
        com.tencent.mars.xlog.Log.appenderClose();
        Xlog.appenderOpen(0, 0, SLFFileUtils.getPluginDataPath(pluginId) + "/xlog/", SLFFileUtils.getPluginDataPath(pluginId) + "/log/", "SLF_API", 0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
        Log.i("API SLFLogUtil", "create new plugin instance");
    }

    public static void syncPermissonCreate() {
        createFile(true);
        com.tencent.mars.xlog.Log.appenderClose();
        Xlog.appenderOpen(0, 0, SLFConstants.xlogCachePath, SLFConstants.apiLogPath, "SLF_API", 0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
    }

    public static void i(String tag, String content) {
        com.tencent.mars.xlog.Log.i(tag, content);
    }

    public static void s(String tag, String content) {
        if (SLFConstants.isOpenConsoleLog) {
            Log.i(tag, content);
        }

    }

    public static void d(String tag, String content) {
        com.tencent.mars.xlog.Log.d(tag, content);
    }

    public static void e(String tag, String content) {
        com.tencent.mars.xlog.Log.e(tag, content);
    }

    public static void v(String tag, String content) {
        com.tencent.mars.xlog.Log.v(tag, content);
    }

    public static void w(String tag, String content) {
        com.tencent.mars.xlog.Log.w(tag, content);
    }

    public static void f(String tag, String msg) {
        if (tag != null && tag.length() != 0 && msg != null && msg.length() != 0) {
            int segmentSize = 3072;
            long length = (long)msg.length();
            if (length <= (long)segmentSize) {
                e(tag, "FLOG---->" + msg);
            } else {
                while(msg.length() > segmentSize) {
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    e(tag, "FLOG---->" + logContent);
                }

                e(tag, "FLOG---->" + msg);
            }

            com.tencent.mars.xlog.Log.w(tag, msg);
        }
    }

    private static void createFile(boolean isUseXlog) {
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
        } else {
            createLogFile(isUseXlog, filePath);
        }
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
                    n0 = n0.replace("WYZE_", "");
                    n1 = n1.replace("WYZE_", "");
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

            fileName = "WYZE_" + getTime(false).append(".txt").toString();
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

    private void createFile(String pluginId) {
        String pluginPath = SLFFileUtils.getPluginDataPath(pluginId);
        File filePath = new File(pluginPath + "/log/");
        boolean isMkdirs;
        if (!filePath.exists()) {
            isMkdirs = filePath.mkdirs();
            com.tencent.mars.xlog.Log.e("isMkdirs", isMkdirs + "");
        }

        filePath = new File(pluginPath + "/xlog");
        if (!filePath.exists()) {
            isMkdirs = filePath.mkdirs();
            com.tencent.mars.xlog.Log.e("isMkdirs", isMkdirs + "");
        }

    }

    public static void setUserInfo() {
        if (canWirte) {
            StringBuilder buf = new StringBuilder();
            buf = buf.append("SLF_API ");
            buf.append("\n\n");
            buf = buf.append("UserName:").append(SLFUserCenter.user_id).append("  ");
            buf.append("\n\n");
            buf.append("TimeZone:").append(TimeZone.getDefault().getDisplayName());
            buf.append("\n\n");
            buf.append("Time:").append(getTime(true).toString());
            buf.append("\n\n");
            buf.append("phone model:").append(Build.MODEL);
            buf.append("\n\n");
            buf.append("Android version：").append(VERSION.SDK_INT);
            buf.append("\n\n");
            com.tencent.mars.xlog.Log.i("", buf.toString());
        }

    }

    private static StringBuilder getTime(boolean isReadable) {
        Calendar cal = Calendar.getInstance();
        StringBuilder buffer = new StringBuilder();
        Integer year = cal.get(1);
        buffer.append(year);
        if (isReadable) {
            buffer.append("-");
        }

        Integer month;
        month = cal.get(2) +1;
        if (month < 10) {
            buffer.append("0").append(month);
        } else {
            buffer.append(month);
        }

        if (isReadable) {
            buffer.append("-");
        }

        Integer day;
        day = cal.get(5);
        if (day < 10) {
            buffer.append("0").append(day);
        } else {
            buffer.append(day);
        }

        if (isReadable) {
            buffer.append("  ");
        }

        Integer hour;
        hour =cal.get(11);
        if (hour < 10) {
            buffer.append("0").append(hour);
        } else {
            buffer.append(hour);
        }

        if (isReadable) {
            buffer.append(":");
        }

        Integer mintute;
        mintute = cal.get(12);
        if (mintute  < 10) {
            buffer.append("0").append(mintute);
        } else {
            buffer.append(mintute);
        }

        if (isReadable) {
            buffer.append(":");
        }

        Integer second;
        second = cal.get(13);
        if (second < 10) {
            buffer.append("0").append(second);
        } else {
            buffer.append(second);
        }

        if (isReadable) {
            buffer.append(".");
        }

        Integer millisecond = cal.get(14);
        if (millisecond < 10) {
            buffer.append("00").append(millisecond);
        } else if (millisecond < 100) {
            buffer.append("0").append(millisecond);
        } else {
            buffer.append(millisecond);
        }

        return buffer;
    }

    private static void log(String code, String tag, String msg) {
        if (toWrite) {
            String str = "\n" + code + "  " + tag + ":\n" + msg;
            listValue.add(str);
            listValueByte.add(str.getBytes());
            toWrite = false;
            byte i = 0;

            while(i < listValue.size()) {
                try {
                    if (null != writer) {
                        writer.write(getTime(true).toString() + "   " + System.currentTimeMillis());
                        if (SLFConstants.isLogFileEncrypt) {
                            writer.append(Arrays.toString((byte[])listValueByte.get(0)));
                        } else {
                            writer.append((CharSequence)listValue.get(0));
                        }

                        writer.newLine();
                        writer.newLine();
                        writer.flush();
                    } else {
                        printer.e("SLFLogUtil: log", "write file error:writer is null", new Object[0]);
                    }

                    move();
                } catch (Exception var6) {
                    printer.e("SLFLogUtil: log", "write file error:" + var6.getMessage(), new Object[0]);
                    move();
                    break;
                }
            }

            toWrite = true;
        }
    }

    private static void move() {
        if (null != listValue && !listValue.isEmpty()) {
            if (SLFConstants.isLogFileEncrypt) {
                listValue.remove(0);
                listValueByte.remove(0);
            } else {
                listValue.remove(0);
            }
        }

    }

    public static void closeFile() {
        if (canWirte) {
            try {
                if (null != writer) {
                    writer.close();
                }

                if (null != fileWriter) {
                    fileWriter.close();
                }

                log = null;
            } catch (Exception var1) {
                printer.e("SLFLogUtil: closeFile", "close file error:" + var1.getMessage(), new Object[0]);
            }
        }

    }

    public static void closeLog() {
        try {
            closeFile();
        } catch (Exception var1) {
            printer.e("SLFLogUtil: closeLog", "close SLFLogUtil error:" + var1.getMessage(), new Object[0]);
        }

        canWirte = false;
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

    public static SLFLogSettings getSet() {
        return printer.getSet();
    }

    public static void json(String tag, String json) {
        if (SLFConstants.isOpenConsoleLog) {
            printer.json(tag, json);
        }

    }

    public static void xml(String tag, String xml) {
        printer.xml(tag, xml);
    }

    public static SLFLogPrinter getPrinter() {
        return printer;
    }
}
