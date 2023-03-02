package com.sandglass.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFHttpChatBotRequestCallback<T>{
    void onRequestChatBotNetFail(T type,long requestTime);

    void onRequestChatBotSuccess(String result, T type,long requestTime);

    void onRequestChatBotFail(String value, String failCode,T typ,long requestTime);
}
