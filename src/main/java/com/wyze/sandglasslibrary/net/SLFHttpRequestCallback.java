package com.wyze.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFHttpRequestCallback<T>{
    void onRequestNetFail();

    void onRequestSuccess(String result, T type);

    void onRequestFail(String value, String failCode);
}
