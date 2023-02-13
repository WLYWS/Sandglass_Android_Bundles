package com.sandglass.sandglasslibrary.net;

import retrofit2.Converter;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFHttpRetrofit {

    // 设置数据读取超时时间
    public SLFHttpRetrofit setTimeOutTime(long timeout) {
        SLFOkHttpClientUtil.getOkHttpUtil().setTimeOutTime(timeout);
        return this;
    }

    // 设置网络连接超时时间
    public SLFHttpRetrofit setConnectTime(long timeout) {
        SLFOkHttpClientUtil.getOkHttpUtil().setConnectTime(timeout);
        return this;
    }

    // 设置写入服务器的超时时间
    public SLFHttpRetrofit setWriteTime(long timeout) {
        SLFOkHttpClientUtil.getOkHttpUtil().setWriteTime(timeout);
        return this;
    }

    // 设置拦截器
    public SLFHttpRetrofit setIntercept(boolean isIntercept) {
        SLFOkHttpClientUtil.getOkHttpUtil().setIntercept(isIntercept);
        return this;
    }

    // 设置BaseUrl
    public SLFHttpRetrofit setBaseUrl(String baseUrl) {
        SLFRetrofitClientUtil.getRetrofitUtil().setBaseUrl(baseUrl);
        return this;
    }

    // 设置数据解析
    public SLFHttpRetrofit addConverterFactory(Converter.Factory factory) {
        SLFRetrofitClientUtil.getRetrofitUtil().addConverterFactory(factory);
        return this;
    }

    /**
     * 生成请求接口的实例
     * @return ApiService
     */
    public SLFApiService getApiManager(){
        return SLFRetrofitClientUtil.getRetrofitUtil().setOkHttpClient(SLFOkHttpClientUtil.getOkHttpUtil().build()).build().create(SLFApiService.class);
    }
}
