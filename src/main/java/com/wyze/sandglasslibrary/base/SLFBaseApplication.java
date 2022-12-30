package com.wyze.sandglasslibrary.base;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.StrictMode;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.utils.SLFFileUtils;
import com.wyze.sandglasslibrary.utils.SLFNetworkChangeReceiver;
import com.wyze.sandglasslibrary.utils.SLFSpUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogAPI;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.util.List;

/**
 * created by yangjie
 * describe:Application
 * time: 2022/12/6
 */
public class SLFBaseApplication extends Application {

    private static Context sInstance;

    public static Context getAppContext() {
        return sInstance;
    }

    private SLFNetworkChangeReceiver mNetworkChangedReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        SLFSpUtils.getInstance(this, getPackageName() + "_slf_sp");
        long startTime = System.currentTimeMillis();
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);
        long endTime = System.currentTimeMillis();
        SLFLogAPI.init();
        SLFLogUtil.setUserInfo();
        SLFLogUtil.initAPILog();
        SLFLogUtil.getInstance().initPluginXlog("slf");
        SLFLogUtil.e("ArouterInitTime:",(endTime-startTime)+"");
        SLFFileUtils.delete(SLFConstants.CROP_IMAGE_PATH);

        List<File> logList = SLFFileUtils.listFileSortByName(SLFConstants.apiLogPath);
        if (logList.size() > 3) {
            for (int i = 3; i < logList.size(); i++) {
                SLFFileUtils.delete(logList.get(i));
            }
        }

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        initReceiver();
    }



    private void initReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mNetworkChangedReceiver = new SLFNetworkChangeReceiver();

        registerReceiver(mNetworkChangedReceiver, filter);

    }

}
