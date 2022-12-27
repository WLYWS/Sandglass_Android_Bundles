package com.wyze.sandglasslibrary.net;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttp;
import okhttp3.RequestBody;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFHttpRetrofitRequest<T> extends SLFHttpRetrofit implements SLFIHttpRequest<T> {

    private SLFApiService apiService;
    // 是否添加通用参数
    private boolean mIsAddCommonParams = true;

    private volatile static SLFHttpRetrofitRequest INSTANCES;


    public static SLFHttpRetrofitRequest getInstances() {
        if (INSTANCES == null) {
            synchronized (SLFHttpRetrofitRequest.class) {
                if (INSTANCES == null) {
                    INSTANCES = new SLFHttpRetrofitRequest();
                }
            }
        }
        return INSTANCES;
    }

    private SLFHttpRetrofitRequest () {
        apiService = getApiManager();
    }

    /**
     * 设置通用参数
     */
    public void isAddCommonParams(boolean isAddCommonParams) {
        this.mIsAddCommonParams = isAddCommonParams;
    }

    // Get请求（使用Path形式）
    @Override
    public void mHttpGetPath(Context context, String url, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_GET,url,null);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Observable<String> observable = apiService.getPath(url,headMap);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // GET请求(无参)
    @Override
    public void mHttpGet(Context context, String api, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_GET,SLFHttpRequestConstants.BASE_URL+api,null);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Observable<String> observable = apiService.getData(api,headMap);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // Get请求(带参)
    @Override
    public void mHttpGet(Context context, String api, TreeMap map, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_GET,api,map);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Observable<String> observable = apiService.getData(api, map,headMap);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // Post请求(无参)
    @Override
    public void mHttpPost(Context context, String api, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_POST,SLFHttpRequestConstants.BASE_URL+api,null);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Observable<String> observable = apiService.postData(api,headMap);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // Post请求(带参)
    // 以RequestBody方式提交
    @Override
    public void mHttpPost(Context context, String api, TreeMap map, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_POST,SLFHttpRequestConstants.BASE_URL+api,map);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Gson gson = new Gson();
        String param = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), param);
        Observable<String> observable = apiService.postData(api, requestBody,headMap);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // Post请求(包含数组)
    @Override
    public void mHttpPost(Context context, String api, TreeMap map, String[] data, T type, SLFHttpRequestCallback callBack) {
        TreeMap headMap = SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_POST,SLFHttpRequestConstants.BASE_URL+api,map);
        String secret = (String) headMap.get(SLFHttpRequestConstants.SECRET);
        headMap.remove(SLFHttpRequestConstants.SECRET);
        Observable<String> observable = apiService.postData(api, map,headMap,data);
        observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack,secret));
    }

    // 单文件上传
    @Override
    public void mHttpFile(Context context, String api, File file, String fileKey, TreeMap map, int type, SLFHttpRequestCallback callBack) {
        // 生成单个文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(fileKey, file.getName(), requestFile);

        if (mIsAddCommonParams) {
            SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_POST,SLFHttpRequestConstants.BASE_URL+api,map);
        }
        Map <String, RequestBody> mapValue = new HashMap<>();
//        for (Object key : map.keySet()) {
//            mapValue.put(key.toString(), SLFHttpTool.convertToRequestBody(map.get(key).toString()));
//        }
        Observable<String> observable = apiService.upload(api, mapValue, body);
        //observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack));
    }

    // 多文件上传
    @Override
    public void mHttpMultiFile(Context context, String api, List<File> list, List <String> listFileName, TreeMap map,int type, SLFHttpRequestCallback callBack) {
        //生成多个文件并添加到集合中
        List<MultipartBody.Part> params = new ArrayList <>();
        for (int i = 0; i < list.size(); i++) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), list.get(i));
            MultipartBody.Part body = MultipartBody.Part.createFormData(listFileName.get(i), list.get(i).getName(), requestFile);
            params.add(body);
        }
        if (mIsAddCommonParams) {
            SLFHttpTool.getTreeCrc(SLFHttpRequestConstants.REQUEST_METHOD_POST,SLFHttpRequestConstants.BASE_URL+api,map);
        }
        Map<String, RequestBody> mapValue = new HashMap <>();
//        for (Object key : map.keySet()) {
//            mapValue.put(key.toString(), SLFHttpTool.convertToRequestBody(Objects.requireNonNull(map.get(key)).toString()));
//        }
        // 发送异步请求
        Observable <String> observable = apiService.uploadMultipart(api, mapValue, params);
        //observable.retryWhen(new SLFRetryFunction()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SLFRxJavaObserver(context, type, callBack));
    }
}