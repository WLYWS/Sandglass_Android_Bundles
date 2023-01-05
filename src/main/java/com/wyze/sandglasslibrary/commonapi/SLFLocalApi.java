package com.wyze.sandglasslibrary.commonapi;

import com.wyze.sandglasslibrary.interf.SLFVideoUploadedCallback;

/**
 * Greated by yangjie
 * describe:插件内接口调用
 * time:2022/12/19
 */
public class SLFLocalApi {

    private static SLFLocalApi mInstance;

    private SLFVideoUploadedCallback slfVideoUploadedCallback;
    public static SLFLocalApi getInstance(){
        if(mInstance==null){
            mInstance = new SLFLocalApi();
        }
        return mInstance;
    }

    /**设置监听通知是否视频加载完成*/
    public void setSlfVideoUploadedCallback(SLFVideoUploadedCallback slfVideoUploadedCallback){
        this.slfVideoUploadedCallback = slfVideoUploadedCallback;
    }
    /**获取一个回传加载完成成功的监听*/
    public SLFVideoUploadedCallback getSLFVideoUploadedCallback(){
        return slfVideoUploadedCallback;
    }
}
