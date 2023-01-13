package com.wyze.sandglasslibrary.net;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFIHttpRequest<T> {

    // Get请求（使用Path形式）
    void mHttpGetPath(Context context, String url, T type, SLFHttpRequestCallback mCallBack);

    // GET请求(无参)
    void mHttpGet(Context context, String api, T type, SLFHttpRequestCallback mCallBack);

    // Get请求(带参)
    void mHttpGet(Context context, String api, TreeMap map, T type, SLFHttpRequestCallback mCallBack);

    // Post请求(无参)
    void mHttpPost(Context context, String api, T type, SLFHttpRequestCallback mCallBack);

    // Post请求(带参)
    // 以RequestBody方式提交
    void mHttpPost(Context context, String api, TreeMap map, T type, SLFHttpRequestCallback mCallBack);

    // Post请求(带参)
    // 以RequestBody方式提交
    //携带时间戳的传递方式
    void mHttpPost(Context context, String api, TreeMap map, T type,long requestTime,SLFHttpChatBotRequestCallback mCallBack);

    // Post请求(包含数组)
    void mHttpPost(Context context, String api, TreeMap treeMap, String[] data, T type, SLFHttpRequestCallback mCallBack);

    // 单文件上传
    void mHttpFile(Context context, String api, File file,int type, SLFHttpRequestCallback mCallBack);

    // 多文件上传
    void mHttpMultiFile(Context context, String api, List<File> list, List <String> fileList, TreeMap map, int type, SLFHttpRequestCallback mCallBack);

    void putHttpFile(Context context,String url,File file ,String mediaType,int type,SLFHttpRequestCallback mCallBack);
}
