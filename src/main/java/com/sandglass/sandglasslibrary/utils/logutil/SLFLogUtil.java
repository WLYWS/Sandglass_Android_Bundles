package com.sandglass.sandglasslibrary.utils.logutil;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.utils.SLFFileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

/**
 * created by yangjie
 * describe:统一的Log打印工具类
 * time: 2022/12/10
 */
public final class SLFLogUtil extends Xlog {
    private static final String TAG = "API SLFLogUtil";
    private static final String XLOG_PUBKEY = "587aa95c61833d2aeddfb0ea1a1da9a6e023d337955730e8d0384bd23f9e0efbfb51410989bb3cd8db37680f9d72e6515f4d041ac495c63b2b0b6b45d6579f7b";
    @SuppressWarnings("java:S3077")
    private static volatile SLFLogUtil log = null;
    private static final String PREFIX = "SLF_";
    // 队列存放需要写入的内容
    private static List<String> listValue = new ArrayList<>();
    // 控制整个文件是否可写入，当存储空间不足或需全部关闭Log的时候置为false
    private static boolean canWirte = true;
    // 写文件开关
    private static boolean toWrite = true;
    // 加密文件 List
    private static List<byte[]> listValueByte = new ArrayList<>();
    private static FileWriter fileWriter = null;
    private static BufferedWriter writer = null;

    /**
     * 单利模式创建Log类的实例
     *
     * @return log
     */
    public static SLFLogUtil getInstance() {
        if (null == log) {
            synchronized (SLFLogUtil.class) {
                if (null == log) {
                    fileWriter = null;
                    writer = null;
                    log = new SLFLogUtil();
                    android.util.Log.i(TAG, "create new instance");
                }
            }
        } else {
            android.util.Log.i(TAG, "exist one instance");
        }
        return log;
    }


    /**
     * Log构造函数，当Log开关为“开”的时候调用创建日志文件方法
     */
    private SLFLogUtil() {
        Log.appenderFlush(true); //同步日志到文件
        createFile(SLFConstants.isUseXlog);
        Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, SLFConstants.xlogCachePath, SLFConstants.apiLogPath, PREFIX+"API",0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
    }

    public static void initAPILog() {
        SLFLogUtil.getInstance();
        SLFLogUtil.setUserInfo();
    }

    /**
     * 初始化插件xlog目录
     */
    public void initPluginXlog(String slf) {
        Log.appenderFlush(true); //同步日志到文件
        createFile(slf);
        Log.appenderClose();
        Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, SLFFileUtils.getPluginDataPath(slf)+"/xlog/", SLFFileUtils.getPluginDataPath(slf)+"/log/", PREFIX+"API",0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        Log.setLogImp(new Xlog());
        android.util.Log.i(TAG, "create new plugin instance");

    }

    public static void syncPermissonCreate(){
        createFile(SLFConstants.isUseXlog);
        Log.appenderClose();
        Xlog.appenderOpen(Xlog.LEVEL_ALL, Xlog.AppednerModeAsync, SLFConstants.xlogCachePath, SLFConstants.apiLogPath, "SLF_API", 0, XLOG_PUBKEY);
        Xlog.setConsoleLogOpen(SLFConstants.isOpenConsoleLog);
        com.tencent.mars.xlog.Log.setLogImp(new Xlog());
    }


    public static void i(String tag, String content) {
        if (SLFConstants.isShowLog) {
            if (SLFConstants.isUseXlog) {
//                checkFileExists();
                Log.i(tag, content);
            } else {
                printer.i(tag, content);
                log("info", tag, content);
            }
        }
    }

    public static void s(String tag, String content) {
        if (SLFConstants.isShowLog && SLFConstants.isOpenConsoleLog) {
            android.util.Log.i(tag, content);
        }
    }

    public static void d(String tag, String content) {
        if (SLFConstants.isShowLog) {
            if (SLFConstants.isUseXlog) {
//                checkFileExists();
                Log.d(tag, content);
            } else {
                printer.d(tag,content);
                log("debug", tag, content);
            }
        }
    }

    public static void e(String tag, String content) {
        if (SLFConstants.isShowLog) {
            if (SLFConstants.isUseXlog) {
//                checkFileExists();
                Log.e(tag, content);
            } else {
                printer.e(tag,content);
                log("error", tag, content);
            }
        }
    }

    public static void v(String tag, String content) {
        if (SLFConstants.isShowLog) {
            if (SLFConstants.isUseXlog) {
//                checkFileExists();
                Log.v(tag, content);
            } else {
                printer.v(tag,content);
                log("warning", tag, content);
            }
        }
    }

    public static void w(String tag, String content) {
        if (SLFConstants.isShowLog) {
            if (SLFConstants.isUseXlog) {
//                checkFileExists();
                Log.w(tag, content);
            } else {
                printer.w(tag,content);
                log("warning", tag, content);
            }
        }
    }


    /**
     * 创建Log日志文件
     */
    private static void createFile(boolean isUseXlog) {

        File tutkLog = new File(SLFConstants.tutkLogPath);
        if (!tutkLog.exists()) {
            boolean isTutkLogMkdirs = tutkLog.mkdirs();
            SLFLogUtil.e("isTutkLogMkdirs",isTutkLogMkdirs+"");
        }

        File filePath = new File(SLFConstants.feedbacklogPath);
        // 判断feedbackLog文件夹是否存在，如果不存在：创建
        if (!filePath.exists()) {
            boolean isFeedbackLogMkdirs = filePath.mkdirs();
            SLFLogUtil.e("isFeedbackLogMkdirs",isFeedbackLogMkdirs+"");
        }

        filePath = new File(SLFConstants.apiLogPath);
        // 判断Log文件夹是否存在，如果不存在：创建
        if (!filePath.exists()) {
            boolean isApiLogPathMkdirs = filePath.mkdirs();
            SLFLogUtil.e("isApiLogPathMkdirs",isApiLogPathMkdirs+"");
        }

        File alarmPicPath = new File(SLFConstants.apiLogPath);
        if (!alarmPicPath.exists()) {
            boolean isAlarmPicPath = alarmPicPath.mkdirs();
            SLFLogUtil.e("isAlarmPicPath",isAlarmPicPath+"");
        }

        // 根据存储状态判断存储空间是否够大
        if (!isAvaiableSpace(0, 1)) {
            // 如果不够大不做任何操作
            canWirte = false;
            return;
        }
        createLogFile(isUseXlog, filePath);

    }

    @SuppressWarnings("java:S4042")
    private static void createLogFile(boolean isUseXlog, File filePath) {
        // 获取当前日期，创建当前的日志文件
        SLFConstants.logTime = getTime(false).toString();

        if(isUseXlog){
            return;
        }

        File[] fileList = filePath.listFiles();

        // 后去log文件夹下的文件，并比较建立日期，删除日期较小的一个
        if (null != fileList && fileList.length > 2) {
            String name0 = fileList[0].getName();
            String name1 = fileList[1].getName();
            fileList[1].getPath();
            String n0 = name0.substring(0, name0.length() - 4);
            String n1 = name1.substring(0, name1.length() - 4);
            try {
                n0 = n0.replace(PREFIX, "");
                n1 = n1.replace(PREFIX, "");
                if (Long.parseLong(n0) < Long.parseLong(n1)) {
                    boolean isDelete = fileList[0].delete();
                    SLFLogUtil.e("flieList[0]_isDelete",isDelete+"");
                } else {
                    boolean isDelete = fileList[1].delete();
                    SLFLogUtil.e("flieList[1]_isDelete",isDelete+"");
                }
            } catch (Exception e) {
                //
            }
        }

        String fileName = PREFIX + getTime(false).append(".txt").toString();
        File file1 = new File(filePath, fileName);
        try {
            fileWriter = new FileWriter(file1);
            writer = new BufferedWriter(fileWriter);
            writer.append("		Title:").append(getTime(false));
            writer.newLine();
            writer.newLine();
        } catch (Exception e) {
            android.util.Log.e("SLFLogUtil: CreateFile", "write file error:" + e.getMessage());
        }
    }

    private void createFile(String slf){

        String pluginPath = SLFFileUtils.getPluginDataPath(slf);
        File filePath = new File(pluginPath+"/log/");
        // 判断feedbackLog文件夹是否存在，如果不存在：创建
        if (!filePath.exists()) {
            boolean isMkdirs = filePath.mkdirs();
            Log.e("isMkdirs",isMkdirs+"");
        }

        filePath = new File(pluginPath+"/xlog");
        // 判断Log文件夹是否存在，如果不存在：创建
        if (!filePath.exists()) {
            boolean isMkdirs = filePath.mkdirs();
            Log.e("isMkdirs",isMkdirs+"");
        }

    }

    /**
     * 写入用户名 同时包括所在时区和时间，时区和时间为当前运行的手机所设定的时区和时间
     */
    public static void setUserInfo() {
        if (canWirte) {
            StringBuilder buf = new StringBuilder();
            buf = buf.append("SLF_API ");
            buf.append("\n\n");
            // 用户名
            buf = buf.append("UserName:").append(SLFUserCenter.user_id).append("  ");
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
            buf.append("\n\n");
            if (SLFConstants.isUseXlog){
                Log.i("", buf.toString());
            }else{
                log(buf.toString(), "", "");
            }
        }
    }

    /**
     * 获取当前时间，用于构建文件名和文件中记录的时间
     *
     * @return StringBuffer
     */
    @SuppressWarnings("java:S3776")
    private static StringBuilder getTime(boolean isReadable) {
        Calendar cal = Calendar.getInstance();
        StringBuilder buffer = new StringBuilder();
        Integer year;
        Integer month;
        Integer day;
        Integer hour;
        Integer mintute;
        Integer second;
        Integer millisecond;
        year = cal.get(Calendar.YEAR);
        buffer.append(year);
        if (isReadable) {
            buffer.append("-");
        }
        if ((month = cal.get(Calendar.MONTH) + 1) < 10) {
            buffer.append("0").append(month);
        } else {
            buffer.append(month);
        }
        if (isReadable) {
            buffer.append("-");
        }
        if ((day = cal.get(Calendar.DAY_OF_MONTH)) < 10) {
            buffer.append("0").append(day);
        } else {
            buffer.append(day);
        }
        if (isReadable) {
            buffer.append("  ");
        }
        if ((hour = cal.get(Calendar.HOUR_OF_DAY)) < 10) {

            buffer.append("0").append(hour);
        } else {
            buffer.append(hour);
        }
        if (isReadable) {
            buffer.append(":");
        }
        if ((mintute = cal.get(Calendar.MINUTE)) < 10) {
            buffer.append("0").append(mintute);
        } else {
            buffer.append(mintute);
        }
        if (isReadable) {
            buffer.append(":");
        }
        if ((second = cal.get(Calendar.SECOND)) < 10) {
            buffer.append("0").append(second);
        } else {
            buffer.append(second);
        }
        if (isReadable) {
            buffer.append(".");
        }
        millisecond = cal.get(Calendar.MILLISECOND);
        if (millisecond < 10) {
            buffer.append("00").append(millisecond);
        } else if (millisecond < 100) {
            buffer.append("0").append(millisecond);
        } else {
            buffer.append(millisecond);
        }
        return buffer;
    }

    /**
     * 向文件中写数据
     *
     * @param code log级别 如：debug、error等等
     */
    private  static synchronized void log(String code, String tag, String msg) {
        if(!toWrite){
            return;
        }
        String str = "\n" + code + "  " + tag + ":\n" + msg;
        listValue.add(str);
        listValueByte.add(str.getBytes());

        toWrite = false;
        for (int i = 0; i < listValue.size(); ) {
            try {
                if (null != writer) {
                    writer.write(getTime(true).toString() + "   " + System.currentTimeMillis());

                    if (SLFConstants.isLogFileEncrypt) {
                        writer.append(Arrays.toString(listValueByte.get(0)));
                    } else {
                        writer.append(listValue.get(0));
                    }

                    writer.newLine(); // 回车
                    writer.newLine(); // 空行
                    writer.flush();
                } else {
                    printer.e("SLFLogUtil: log",
                            "write file error:writer is null");
                }
                move();
            } catch (Exception e) {
                printer.e("SLFLogUtil: log", "write file error:" + e.getMessage());
                move();
                break;
            }

        }
        toWrite = true;
    }


    /**
     * 将写过的信息从list队列中删除
     */
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

    /**
     * 关闭文件流
     */
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
            } catch (Exception e) {
                printer.e("SLFLogUtil: closeFile", "close file error:" + e.getMessage());
            }
        }
    }

    /**
     * 关闭Log类
     */
    public static void closeLog() {
        try {
            closeFile();
        } catch (Exception e) {
            printer.e("SLFLogUtil: closeLog", "close SLFLogUtil error:" + e.getMessage());
        }
        canWirte = false;
    }

    /**
     * 判断空间是否可用
     *
     * @param status 存储状态：0_sdcard存储 1_data目录下存储
     * @param size   需要剩余的空间下线
     * @return false 空间过小不可用 true 空间够大可用
     */
    @SuppressLint("LongLogTag")
    @SuppressWarnings("deprecation")
    private static boolean isAvaiableSpace(int status, int size) {
        String sdcard;
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
            android.util.Log.d("SLFLogUtil:isAvaiableSpace", "Exception: " + e.getMessage());
            return false;
        }
    }

    private static SLFLogPrinter printer = new SLFLogPrinter();

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
