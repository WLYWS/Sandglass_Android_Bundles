package com.wyze.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/5
 */
interface SLFHttpRequestCallback<T> {
    void onRequestNetFail(Class<T> type);

    void onRequestSuccess(String result, Class<T> type);

    void onRequestFail(String value, String failCode, Class<T> type);
}
