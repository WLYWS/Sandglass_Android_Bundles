package com.wyze.sandglasslibrary.commonapi;

import android.content.Context;
import android.content.Intent;

import com.wyze.sandglasslibrary.interf.SLFUploadAppLogCallback;
import com.wyze.sandglasslibrary.interf.SLFUploadCompleteCallback;

/**
 * Greated by yangjie
 * describe:对外统一接口
 * time:2022/12/19
 */
public class SLFApi  {

    private static SLFApi mInstance;

    private SLFUploadAppLogCallback slfUploadAppLogCallback;

    private SLFUploadCompleteCallback slfUploadCompleteCallback;

    public static SLFApi getInstance(){
        if(mInstance==null){
            mInstance = new SLFApi();
        }
        return mInstance;
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
