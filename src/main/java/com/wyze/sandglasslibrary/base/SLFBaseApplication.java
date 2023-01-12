package com.wyze.sandglasslibrary.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.StrictMode;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonapi.SLFApi;
import com.wyze.sandglasslibrary.utils.SLFFileUtils;
import com.wyze.sandglasslibrary.utils.SLFNetworkChangeReceiver;
import com.wyze.sandglasslibrary.utils.SLFSpUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogAPI;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.util.LinkedList;
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

    private static List<Activity> mList = new LinkedList<>();//记录当前应用的Activity,用于退出整个应用销毁所有Activity

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        SLFApi.getInstance(this).init();
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

    public static void addActivity(Activity activity) {
        mList.add(activity);
    }

    public static void exitAllActivity() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void exitActivity(Activity activity){
       mList.remove(activity);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
