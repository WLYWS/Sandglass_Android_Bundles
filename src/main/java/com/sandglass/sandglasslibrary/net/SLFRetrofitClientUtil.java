package com.sandglass.sandglasslibrary.net;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFRetrofitClientUtil {
    // 网络请求的baseUrl
    private String mBaseUrl;
    // 设置数据解析器
    private Converter.Factory mBaseFactory;
    // 新增数据解析器
    private Converter.Factory mAddFactory;
    // 设置网络请求适配器
    private CallAdapter.Factory mCallFactory;
    // 设置OkHttpClient
    private OkHttpClient mOkHttpClient;

    private static volatile SLFRetrofitClientUtil retrofitUtil;

    public static SLFRetrofitClientUtil getRetrofitUtil() {
        if (retrofitUtil == null) {
            synchronized (SLFRetrofitClientUtil.class) {
                if (retrofitUtil == null) {
                    retrofitUtil = new SLFRetrofitClientUtil();
                }
            }
        }
        return retrofitUtil;
    }

    private SLFRetrofitClientUtil () {
        mBaseUrl = SLFHttpRequestConstants.BASE_URL;
        // 默认基础数据类型的解析
        mBaseFactory = ScalarsConverterFactory.create();
        // RxJava来处理Call返回值
        mCallFactory = RxJava3CallAdapterFactory.create();
    }

    // 设置BaseUrl
    public SLFRetrofitClientUtil setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
        return this;
    }

    // 设置数据解析
    public SLFRetrofitClientUtil addConverterFactory(Converter.Factory factory) {
        this.mAddFactory = factory;
        return this;
    }

    // 设置网络请求适配器
    public SLFRetrofitClientUtil addCallAdapterFactory(CallAdapter.Factory factory) {
        this.mCallFactory = factory;
        return this;
    }

    // 设置写入服务器的超时时间
    public SLFRetrofitClientUtil setOkHttpClient(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
        return this;
    }

    // 设置Build方法
    public Retrofit build() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(mBaseUrl);
        builder.addConverterFactory(mBaseFactory);
        if (mAddFactory!=null){
            builder.addConverterFactory(mAddFactory);
        }
        builder.addCallAdapterFactory(mCallFactory);
        builder.client(mOkHttpClient);
        return builder.build();
    }
}
