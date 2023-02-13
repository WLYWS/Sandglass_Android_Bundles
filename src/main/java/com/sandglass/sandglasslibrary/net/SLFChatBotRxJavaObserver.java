package com.sandglass.sandglasslibrary.net;

import android.content.Context;

import com.google.gson.Gson;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFChatBotRxJavaObserver<T> implements Observer<String> {

    private SLFHttpChatBotRequestCallback mCallBack;
    private T mType;
    private Context mContext;
    private String mSecret;
    private long requestTime;

    private final static String DATA = "data";
    private final static String CODE = "code";
    private final static String MSG = "message";
    private final static String SUCCESS = "SUCCESS";

    SLFChatBotRxJavaObserver (Context context, T type, long requestTime,SLFHttpChatBotRequestCallback callBack, String secret) {
        this.mCallBack = callBack;
        this.mContext = context;
        this.mType = type;
        this.mSecret = secret;
        this.requestTime = requestTime;
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
                    SLFLogUtil.d("request","请求体返回解密 | Response:"+response);
                    if(response!=null){
                        JSONObject jsonObjectData = new JSONObject(response);
                        if (jsonObjectData.has(MSG)) {
                            String msg = jsonObjectData.getString(MSG);
                            if (msg.toUpperCase().equals(SUCCESS)) {
                                Gson gson = new Gson();
                                T t = gson.fromJson(response, (Type) mType);
                                mCallBack.onRequestChatBotSuccess(response, t,requestTime);
                            } else {
                                String errMsg = jsonObjectData.getString(MSG);
                                String code = jsonObjectData.getString(CODE);
                                mCallBack.onRequestChatBotFail(errMsg, code,mType,requestTime);
                            }
                        }
                    }
                }
            } else {
                mCallBack.onRequestChatBotSuccess(s, mType,requestTime);
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
            mCallBack.onRequestChatBotNetFail(mType,requestTime);
            SLFLogUtil.d("request","网络原因请求失败 | Response:"+requestTime);
            return;
        }
        // 2.非网络错误，接口请求错误
        mCallBack.onRequestChatBotFail(e.getMessage(), "1000",mType,requestTime);
        SLFLogUtil.d("request","请求失败 | Response:"+requestTime);
    }

    /**
     * 接口请求完成
     */
    @Override
    public void onComplete() {

    }
}