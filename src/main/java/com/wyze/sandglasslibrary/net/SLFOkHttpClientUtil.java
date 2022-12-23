package com.wyze.sandglasslibrary.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFOkHttpClientUtil {
    // 是否开启拦截，默认情况下开启
    private boolean mIsIntercept = true;
    // 设置数据读取超时时间
    private long mReadTimeOut = 20000;
    // 设置网络连接超时时间
    private long mConnectTimeOut = 20000;
    // 设置写入服务器的超时时间
    private long mWriteTimeOut = 20000;

    private static volatile SLFOkHttpClientUtil okHttpUtil;

    static SLFOkHttpClientUtil getOkHttpUtil() {
        if (okHttpUtil == null) {
            synchronized (SLFOkHttpClientUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new SLFOkHttpClientUtil();
                }
            }
        }
        return okHttpUtil;
    }

    /**
     * 私有构造函数，保证全局唯一
     */
    private SLFOkHttpClientUtil (){

    }

    // 设置数据读取超时时间
    SLFOkHttpClientUtil setTimeOutTime(long timeout) {
        mReadTimeOut = timeout;
        return this;
    }

    // 设置网络连接超时时间
    SLFOkHttpClientUtil setConnectTime(long timeout) {
        mConnectTimeOut = timeout;
        return this;
    }

    // 设置写入服务器的超时时间
    SLFOkHttpClientUtil setWriteTime(long timeout) {
        mWriteTimeOut = timeout;
        return this;
    }

    // 设置拦截器
    SLFOkHttpClientUtil setIntercept(boolean isIntercept) {
        this.mIsIntercept = isIntercept;
        return this;
    }

    // 设置Build方法
    public OkHttpClient build() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.connectTimeout(mConnectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS);
        // 默认开启请求的打印信息数据，在每次发布版本的时候可以手动关闭
        if (mIsIntercept) {
            okHttpClient.addInterceptor(new SLFHttpRequestInterceptor());
        }
        return okHttpClient.build();
    }
}
