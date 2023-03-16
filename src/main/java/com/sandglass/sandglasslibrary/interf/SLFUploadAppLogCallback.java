package com.sandglass.sandglasslibrary.interf;

/**
 * Greated by yangjie
 * describe:给app的回调接口，回调applog地址
 * time:2022/12/28
 */
public interface SLFUploadAppLogCallback {
    void getUploadAppLogUrl(String appLogUrl,String contentType);
}
