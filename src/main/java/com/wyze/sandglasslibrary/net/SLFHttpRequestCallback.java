package com.wyze.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFHttpRequestCallback{
    void onRequestNetFail(int type);

    void onRequestSuccess(String result, int type);

    void onRequestFail(String value, String failCode, int type);
}
