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

    public void setAppLogCallBack(SLFUploadAppLogCallback slfUploadAppLogCallback){
        this.slfUploadAppLogCallback = slfUploadAppLogCallback;
    }

    public SLFUploadAppLogCallback getAppLogCallBack(){
        return slfUploadAppLogCallback;
    }

    public void setUploadLogCompleteCallBack(SLFUploadCompleteCallback slfUploadCompleteCallback){
        this.slfUploadCompleteCallback = slfUploadCompleteCallback;
    }

    public SLFUploadCompleteCallback getUploadLogCompleteCallBack(){
        return slfUploadCompleteCallback;
    }
}
