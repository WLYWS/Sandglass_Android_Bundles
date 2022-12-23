package com.wyze.sandglasslibrary.utils.logutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.wyze.sandglasslibrary.bean.SLFConstants;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.tencent.mars.xlog.Log;

/**
 * Greated by yangjie
 * discribe:全局异常捕获
 * time:2022/12/10
 */
public class SLFCrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * context
     */
    private Context mContext;

    /**
     * 格式化时间
     */
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @SuppressLint("StaticFieldLeak")
    private static SLFCrashHandler mInstance;
    private static String appId;

    private SLFCrashHandler() {
    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized SLFCrashHandler getInstance() {
        if (mInstance == null) {
            mInstance = new SLFCrashHandler();
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @SuppressWarnings("java:S2696")
    public void setAppId(String pluginId) {
        appId = pluginId;
    }

    /**
     * uncaughtException 回调函数
     */
    @Override @SuppressWarnings("java:S2583")
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {//如果自己没处理交给系统处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 收集错误信息.发送到服务器
     *
     * @return 处理了该异常返回true, 否则false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread(() -> {
            StringBuilder crashMessage = new StringBuilder();
            crashMessage.append(ex.toString()).append("\n");
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                crashMessage.append(ex.getStackTrace()[i].toString()).append("\n");
            }
            SLFDebugLogUtils.getInstance().writeLog(SLFConstants.SLF_APP_ID, "SLF", SLFDebugConfig.CRASH, crashMessage.toString());
        }).start();
        for (OnCrashListener listener : crashListeners) {
            listener.onCrash(ex,appId);
        }

        SLFLogUtil.e("exception:"+appId,ex.getMessage());
        //收集设备参数信息
//        collectDeviceInfo(mContext)
        //添加自定义信息
//        addCustomInfo()
//        saveErrorMessages(ex)
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();
            }
        }.start();
        //保存日志文件
        return true;
    }



    /**
     *
     */
    @SuppressWarnings("unused")
    private void saveErrorMessages(Throwable e) {
        StringBuilder sb = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        // 循环取出Cause
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = e.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append(result);
        SLFLogUtil.e(appId,sb.toString());
        Log.appenderFlush(true); //同步日志到文件
    }


    private final List<OnCrashListener> crashListeners = new ArrayList<>();

    public void addCrashListener(OnCrashListener listener){
        crashListeners.add(listener);
    }

    public interface OnCrashListener {
        void onCrash(Throwable ex,String appId);
    }

}
