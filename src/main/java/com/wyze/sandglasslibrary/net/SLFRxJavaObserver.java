package com.wyze.sandglasslibrary.net;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFRxJavaObserver implements Observer<String> {

    private SLFHttpRequestCallback mCallBack;
    private int mType;
    private Context mContext;
    private String mSecret;

    private final static String DATA = "data";
    private final static String CODE = "code";
    private final static String MSG = "message";
    private final static String SUCCESS = "SUCCESS";

    SLFRxJavaObserver(Context context, int type, SLFHttpRequestCallback callBack,String secret) {
        this.mCallBack = callBack;
        this.mContext = context;
        this.mType = type;
        this.mSecret = secret;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    /**
     * 接口请求成功
     */
    @Override
    public void onNext(@NonNull String s) {
        try {
            if (SLFHttpTool.isJsonObject(s)) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.has(DATA)) {
                    String response = SLFDecryptUtil.DecryAes(jsonObject.getString(DATA), mSecret);
                    JSONObject jsonObjectData = new JSONObject(response);
                    if (jsonObjectData.has(MSG)) {
                        String msg = jsonObjectData.getString(MSG);
                        if (msg.toUpperCase().equals(SUCCESS)) {
                            if (jsonObjectData.has(DATA)) {
//                                Gson gson = new Gson();
//                                gson.fromJson(jsonObjectData.getString(DATA), mType);
                                mCallBack.onRequestSuccess(jsonObjectData.getString(DATA), mType);
                            } else {
                                if (jsonObjectData.has(CODE) && jsonObjectData.has(MSG)) {
                                    String code = jsonObjectData.getString(CODE);
                                    String errMsg = jsonObjectData.getString(MSG);
                                    mCallBack.onRequestSuccess(errMsg, mType);
                                }
                            }
                        } else {
                            String errMsg = jsonObjectData.getString(MSG);
                            String code = jsonObjectData.getString(CODE);
                            mCallBack.onRequestFail(errMsg, code, mType);
                        }
                    }
                }
            } else {
                mCallBack.onRequestSuccess(s, mType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接口请求失败
     */
    @Override
    public void onError(@NonNull Throwable e) {
        // 1.检查网络设置
        if (!SLFHttpTool.hasNetwork(mContext)) {
            //MyToast.showCenterSortToast(mContext, mContext.getResources().getString(R.string.connect_error));
            onComplete();
            mCallBack.onRequestNetFail(mType);
            return;
        }
        // 2.非网络错误，接口请求错误
        mCallBack.onRequestFail(e.getMessage(), "0000", mType);
    }

    /**
     * 接口请求完成
     */
    @Override
    public void onComplete() {

    }
}