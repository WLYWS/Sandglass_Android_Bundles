package com.sandglass.sandglasslibrary.net;

import android.content.Context;

import com.google.gson.Gson;
import com.sandglass.sandglasslibrary.bean.SLFHttpStatusCode;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.interf.SLFSetNewTokentoFeed;
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
public class SLFRxJavaObserver<T> implements Observer<String>, SLFSetNewTokentoFeed {

    private SLFHttpRequestCallback mCallBack;
    private T mType;
    private Context mContext;
    private String mSecret;

    private final static String DATA = "data";
    private final static String CODE = "code";
    private final static String MSG = "message";
    private final static String SUCCESS = "SUCCESS";

    SLFRxJavaObserver(Context context, T type, SLFHttpRequestCallback callBack,String secret) {
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
                    SLFLogUtil.sdkd("request","请求体返回解密 | Response:"+response);
                    if(response!=null){
                        JSONObject jsonObjectData = new JSONObject(response);
                        if (jsonObjectData.has(MSG)) {
                            String msg = jsonObjectData.getString(MSG);
                            if (msg.toUpperCase().equals(SUCCESS)) {
                                Gson gson = new Gson();
                                T t = gson.fromJson(response, (Type) mType);
                                mCallBack.onRequestSuccess(response, t);
                            } else {
                                String errMsg = jsonObjectData.getString(MSG);
                                String code = jsonObjectData.getString(CODE);
                                SLFLogUtil.sdkd("yj","CODE::::"+code);
                                if(code.equals(SLFHttpStatusCode.TOKEN_FAILED)){
                                    SLFLogUtil.sdkd("yj","CODE::xxx::401");
                                    //TODO 临时放在这里，之后放在失败那里
                                    if(SLFApi.getInstance(SLFApi.getSLFContext()).getSlfSetTokenCallback()!=null){
                                        SLFApi.getInstance(SLFApi.getSLFContext()).getSlfSetTokenCallback().setToken();
                                    }
                                }else {
                                    mCallBack.onRequestFail(errMsg, code, mType);
                                }
                            }
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
            SLFLogUtil.sdkd("request","网络原因请求失败RequestNetFail");
            return;
        }
        // 2.非网络错误，接口请求错误
        mCallBack.onRequestFail(e.getMessage(), "1000",mType);
        SLFLogUtil.sdkd("request","非网络原因请求失败reuqestFail");
    }

    /**
     * 接口请求完成
     */
    @Override
    public void onComplete() {

    }

    @Override
    public void getNewToken(String token) {
        SLFLogUtil.sdkd("yj","token===:::"+token);
    }
}