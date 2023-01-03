package com.wyze.sandglasslibrary.commonapi;

import android.content.Context;
import android.content.Intent;

import com.wyze.sandglasslibrary.interf.SLFCompressVideoCompelete;
import com.wyze.sandglasslibrary.interf.SLFUploadAppLogCallback;
import com.wyze.sandglasslibrary.interf.SLFUploadCompleteCallback;

/**
 * Greated by yangjie
 * describe:插件内接口调用
 * time:2022/12/19
 */
public class SLFLocalApi {

    private static SLFLocalApi mInstance;


    private SLFCompressVideoCompelete slfCompressVideoCompelete;
    public static SLFLocalApi getInstance(){
        if(mInstance==null){
            mInstance = new SLFLocalApi();
        }
        return mInstance;
    }
    /**设置监听通知是否上传完成*/
    public void setCompressVideoCompelete(SLFCompressVideoCompelete slfCompressVideoCompelet){
        this.slfCompressVideoCompelete = slfCompressVideoCompelet;
    }
    /**获取一个回传成功的监听*/
    public SLFCompressVideoCompelete getCompressVideoCompelete(){
        return slfCompressVideoCompelete;
    }
}
