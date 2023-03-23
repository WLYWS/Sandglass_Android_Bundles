package com.sandglass.sandglasslibrary.utils.logutil;

import com.sandglass.sandglasslibrary.bean.SLFConstants;

/**
 * Created by yangjie
 * discrib:Log的api
 * time:2022/12/10
 *
 */

public class SLFLogAPI {
    private static final String TAG = "SLFLogAPI";

    public static final String XLOG_PUBKEY = "c7b41a50681125813cb88c40653dcf4bb1ca1ab8a62626e9b6bb8a8b0bfa419ae3e142158c586864ec8aaf10a5ba9240be41e9f278ee7696f97f7ae7caa3c0e4";

    private SLFLogAPI(){}

    public static void init() {
        android.util.Log.d(TAG, "onCreate: init log");
        //loadXlogLib();
       // SLFLogUtil.getInstance();
        SLFLogUtil.createFile(SLFConstants.isUseXlog);
        //SLFLogUtil.initSDKXlog(0,0, SLFConstants.xlogCachePath,SLFConstants.apiLogPath,"slf_",0,XLOG_PUBKEY);
       // SLFLogUtil.initXLog(SLFConstants.xlogCachePath,SLFConstants.apiLogPath,XLOG_PUBKEY);
        //SLFLogUtil.setUserInfo();
        //AppConfig.initPathDir(getExternalCacheDir().getAbsolutePath(), getCacheDir().getAbsolutePath());
        SLFLogUtil.initSDKXlog(0, 0, SLFConstants.xlogCachePath,SLFConstants.apiLogPath, "SLF", 0, XLOG_PUBKEY);
        //SLFLogUtil.initXLog(SLFConstants.xlogCachePath,SLFConstants.apiLogPath);
    }

    private static void loadXlogLib() {
        try {
            System.loadLibrary("c++_shared");
        } catch (Exception e) {
            SLFLogUtil.sdke("xlog","load c++_shared error");
        }
        try {
            System.loadLibrary("marsxlog");
        } catch (Exception e) {
            SLFLogUtil.sdke("xlog","load marsxlog error");
        }
    }
}
