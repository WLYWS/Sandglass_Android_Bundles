package com.sandglass.sandglasslibrary.commonapi;

import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.os.StrictMode;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sandglass.sandglasslibrary.base.SLFBaseApplication;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.interf.SLFUploadAppLogCallback;
import com.sandglass.sandglasslibrary.interf.SLFUploadCompleteCallback;
import com.sandglass.sandglasslibrary.utils.SLFFileUtils;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFCrashHandler;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogAPI;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.util.List;

/**
 * Greated by yangjie
 * describe:对外统一接口
 * time:2022/12/19
 */
public class SLFApi  {

    private static SLFApi mInstance;

    private static Context mContext;

    private SLFUploadAppLogCallback slfUploadAppLogCallback;

    private SLFUploadCompleteCallback slfUploadCompleteCallback;

    private boolean isDebug;

    public static SLFApi getInstance(Context context){
        if(mInstance==null){
            mInstance = new SLFApi(context);
        }
        return mInstance;
    }


    public SLFApi(Context context){
        mContext = context;
    }

    public static Context getSLFContext(){
        return mContext;
    }
    public void init(boolean isDebug){

        this.isDebug = isDebug;
        try {
            SLFConstants.isOpenConsoleLog = isDebug;
        } catch (NullPointerException var10) {
            SLFLogUtil.e("SLF", "BJA-115 *** AppConfig NullPointerException ***");
            SLFLogUtil.e("SLF", Log.getStackTraceString(var10));
        }
        long startTime = System.currentTimeMillis();

        long endTime = System.currentTimeMillis();
        SLFSpUtils.getInstance(mContext, mContext.getPackageName() + "_slf_sp");
        SLFLogAPI.init();
        SLFLogUtil.e("ArouterInitTime:", endTime - startTime + "");
        SLFFileUtils.delete(SLFConstants.CROP_IMAGE_PATH);
        List<File> logList = SLFFileUtils.listFileSortByName(SLFConstants.apiLogPath);
        if (logList.size() > 3) {
            for(int i = 3; i < logList.size(); ++i) {
                SLFFileUtils.delete((File)logList.get(i));
            }
        }

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    /**跳转到插件反馈*/
    public void gotoHelpAndFeedback(Context context){
        Intent in = new Intent("slf.sdk.action.SLFHelpAndFeedback");
        context.startActivity(in);
    }
    /**设置监听获取上传applog的路径和固件log的地址*/
    public void setAppLogCallBack(SLFUploadAppLogCallback slfUploadAppLogCallback){
        this.slfUploadAppLogCallback = slfUploadAppLogCallback;
    }
    /**获取一个上传监听用于回调*/
    public SLFUploadAppLogCallback getAppLogCallBack(){
        return slfUploadAppLogCallback;
    }
    /**设置监听通知是否上传完成*/
    public void setUploadLogCompleteCallBack(SLFUploadCompleteCallback slfUploadCompleteCallback){
        this.slfUploadCompleteCallback = slfUploadCompleteCallback;
    }
    /**获取一个回传成功的监听*/
    public SLFUploadCompleteCallback getUploadLogCompleteCallBack(){
        return slfUploadCompleteCallback;
    }
}
