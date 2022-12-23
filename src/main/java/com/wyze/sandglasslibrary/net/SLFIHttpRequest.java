package com.wyze.sandglasslibrary.net;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFIHttpRequest {

    // Get请求（使用Path形式）
    void mHttpGetPath(Context context, String url, int type, SLFHttpRequestCallback mCallBack);

    // GET请求(无参)
    void mHttpGet(Context context, String api, int type, SLFHttpRequestCallback mCallBack);

    // Get请求(带参)
    void mHttpGet(Context context, String api, TreeMap map, int type, SLFHttpRequestCallback mCallBack);

    // Post请求(无参)
    void mHttpPost(Context context, String api, int type, SLFHttpRequestCallback mCallBack);

    // Post请求(带参)
    // 以RequestBody方式提交
    void mHttpPost(Context context, String api, TreeMap map, int type, SLFHttpRequestCallback mCallBack);

    // Post请求(包含数组)
    void mHttpPost(Context context, String api, TreeMap treeMap, String[] data, int type, SLFHttpRequestCallback mCallBack);

    // 单文件上传
    void mHttpFile(Context context, String api, File file, String fileKey, TreeMap map, int type, SLFHttpRequestCallback mCallBack);

    // 多文件上传
    void mHttpMultiFile(Context context, String api, List<File> list, List <String> fileList, TreeMap map, int type, SLFHttpRequestCallback mCallBack);
}
