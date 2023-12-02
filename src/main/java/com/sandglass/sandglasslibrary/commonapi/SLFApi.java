package com.sandglass.sandglasslibrary.commonapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.functionmoudle.activity.chatbot.SLFChatBotActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListDetailActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback.SLFHelpAndFeedback;
import com.sandglass.sandglasslibrary.interf.SLFCompressProgress;
import com.sandglass.sandglasslibrary.interf.SLFSetTokenCallback;
import com.sandglass.sandglasslibrary.interf.SLFUploadAppLogCallback;
import com.sandglass.sandglasslibrary.interf.SLFUploadCompleteCallback;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFRecord;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserInfoMoudle;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserInfoResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserinfoDeviceMoudle;
import com.sandglass.sandglasslibrary.utils.SLFFileUtils;
import com.sandglass.sandglasslibrary.utils.SLFNetworkChangeReceiver;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFCrashHandler;
import com.sandglass.sandglasslibrary.utils.logutil.SLFDebugConfig;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogAPI;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:对外统一接口
 * time:2022/12/19
 */
public class SLFApi{  //implements SLFSetNewTokentoFeed {

    private static SLFApi mInstance;

    private static Context mContext;

    private SLFUploadAppLogCallback slfUploadAppLogCallback;

    private SLFUploadCompleteCallback slfUploadCompleteCallback;

    private SLFCompressProgress slfCompressProgress;

    //private SLFSetTokenCallback slfSetTokenCallback;

    private boolean isDebug;

    private SLFNetworkChangeReceiver mNetworkChangedReceiver;

    private static final String XLOG_PUBKEY = "c7b41a50681125813cb88c40653dcf4bb1ca1ab8a62626e9b6bb8a8b0bfa419ae3e142158c586864ec8aaf10a5ba9240be41e9f278ee7696f97f7ae7caa3c0e4";

    private static List<Activity> mList = new LinkedList<>();//记录当前应用的Activity,用于退出整个应用销毁所有Activity

    public static SLFApi getInstance(Context context){
        if(mInstance==null){
            mInstance = new SLFApi(context);
        }
        return mInstance;
    }

    public SLFApi(Context context){
        mContext = context;
        initReceiver();
    }

    public static Context getSLFContext(){
        return mContext;
    }
    public void init(boolean isDebug){
        this.isDebug = isDebug;
        SLFConstants.isOpenConsoleLog = isDebug;
        SLFConstants.isUseXlog = !isDebug;
        long startTime = System.currentTimeMillis();

        long endTime = System.currentTimeMillis();
        SLFSpUtils.getInstance(mContext, mContext.getPackageName() + "_slf_sp");
        SLFLogAPI.init();
        //SLFLogUtil.syncPermissonCreate();
        //SLFLogUtil.sdkinitXLog(SLFConstants.xlogCachePath,SLFConstants.apiLogPath,XLOG_PUBKEY);
        SLFLogUtil.sdke("ArouterInitTime:", endTime - startTime + "");
        SLFFileUtils.delete(SLFConstants.CROP_IMAGE_PATH);
        List<File> logList = SLFFileUtils.listFileSortByName(SLFConstants.apiLogPath);
        if (logList.size() > 3) {
            for(int i = 3; i < logList.size(); ++i) {
                SLFFileUtils.delete((File)logList.get(i));
            }
        }
//        SLFCrashHandler.getInstance().init(mContext);
        SLFDebugConfig.setOpenLogEnable(true);
//        SLFCrashHandler.getInstance().addCrashListener((ex, appId) -> SLFLogUtil.sdke("slf_crash", appId));
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    /**设置token*/
    public void setToken(String token){
        SLFConstants.token = token;
    }

    /**设置USER_ID*/
    public void setUserId(String userId){
        SLFConstants.userId = userId;
    }

    /**跳转到插件反馈*/
    public void gotoHelpAndFeedback(Context context, HashMap<String,Object> paramsMap, HashMap<String,String> deviceInfo){

        gotoHelpAndFeedback(context, paramsMap);
        SLFUserCenter.defaultDevice = new SLFUserinfoDeviceMoudle(deviceInfo.get("deviceId"), deviceInfo.get("deviceName"), deviceInfo.get("deviceModel"), deviceInfo.get("firmwareVersion"));
    }
    /**跳转到插件反馈*/
    public void gotoHelpAndFeedback(Context context, HashMap<String,Object> paramsMap){

        SLFUserCenter.defaultDevice = null;
        setUserId(paramsMap.get(SLFConstants.USER_ID).toString());
        Object enterPage = paramsMap.get("enterPage");
        if (enterPage == "FAQ"){
            Intent in = new Intent(getSLFContext(), SLFHelpAndFeedback.class);
            context.startActivity(in);
        } else if (enterPage == "Feedback") {
            if (paramsMap.get("email") != null && paramsMap.get("email").toString() != null){
                SLFUserInfoMoudle userInfo = new SLFUserInfoMoudle(paramsMap.get("email").toString(),"");
                SLFUserCenter.userInfoBean = new SLFUserInfoResponseBean("", userInfo);
            }
            Intent in = new Intent(getSLFContext(), SLFFeedbackSubmitActivity.class);
            context.startActivity(in);
        }else {
            Intent in = new Intent(getSLFContext(), SLFChatBotActivity.class);
            if (paramsMap.get("chatbotMessage") != null && paramsMap.get("chatbotMessage").toString() != null){
                // 附加额外的数据到Intent对象
                in.putExtra("EXTRA_KEY_MESSAGE", paramsMap.get("chatbotMessage").toString());
            }
            context.startActivity(in);
        }
        SLFSpUtils.putHashMapData(SLFConstants.PARAMSMAP,paramsMap);
    }

    /**跳转到留言详情页*/
    public void gotoLeaveMessagePage(Context context, HashMap<String,Object> paramsMap){
        Intent in = new Intent(getSLFContext(), SLFFeedbackListDetailActivity.class);

        setUserId(paramsMap.get(SLFConstants.USER_ID).toString());
        SLFSpUtils.putHashMapData(SLFConstants.PARAMSMAP,paramsMap);
        //HashMap<String,Object> map = SLFSpUtils.getHashMapData(SLFConstants.PARAMSMAP);
        if(paramsMap!=null&&paramsMap.size()>0){
            String jsonNotify = (String) paramsMap.get(SLFConstants.LEAVE_MESSAGE_DATA);
            try {
                JSONObject json = new JSONObject(jsonNotify);
                String serviceType = json.optString("serviceTypeText")==null?"":json.optString("serviceTypeText");
                String categoryText = json.optString("categoryText")==null?"":json.optString("categoryText");
                String subCategoryText = json.optString("subCategoryText")==null?"":json.optString("subCategoryText");
                long feek_id = json.optLong("id");
                int sendLog = json.optInt("sendLog");
                int status = json.optInt("status");
                SLFRecord slfRecord = new SLFRecord();
                slfRecord.setServiceTypeText(serviceType);
                slfRecord.setCategoryText(categoryText);
                slfRecord.setSubCategoryText(subCategoryText);
                slfRecord.setId(feek_id);
                slfRecord.setSendLog(sendLog);
                slfRecord.setStatus(status);
                in.putExtra(SLFConstants.RECORD_DATA,slfRecord);
                context.startActivity(in);
            } catch (JSONException e) {
                Log.e("yj","Exception:"+e.toString());
            }
        }else{
            Log.d("yj","slfRecord:::else：");
        }

    }
    /**设置监听获取上传applog的路径和固件log的地址*/
    public void setAppLogCallBack(SLFUploadAppLogCallback slfUploadAppLogCallback){
        this.slfUploadAppLogCallback = slfUploadAppLogCallback;
    }
    /**设置监听获取token的接口*/
//    public void setTokenCallBack(SLFSetTokenCallback slfSetTokenCallback){
//        this.slfSetTokenCallback = slfSetTokenCallback;
//    }

    /**获取一个上传监听用于回调*/
    public SLFUploadAppLogCallback getAppLogCallBack(){
        return slfUploadAppLogCallback;
    }
//    public SLFSetTokenCallback getTokenCallBack(){
//        return slfSetTokenCallback;
//    }
    /**获取一个token回调*/
//    public SLFSetTokenCallback getSlfSetTokenCallback(){
//        return slfSetTokenCallback;
//    }
    /**设置监听通知是否上传完成*/
    public void setUploadLogCompleteCallBack(SLFUploadCompleteCallback slfUploadCompleteCallback){
        this.slfUploadCompleteCallback = slfUploadCompleteCallback;
    }
    /**获取一个回传成功的监听*/
    public SLFUploadCompleteCallback getUploadLogCompleteCallBack(){
        return slfUploadCompleteCallback;
    }

    public void setCompressProgress(SLFCompressProgress compressProgress){
        this.slfCompressProgress = compressProgress;
    }

    public SLFCompressProgress getSlfCompressProgress(){
        return slfCompressProgress;
    }

    private void initReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mNetworkChangedReceiver = new SLFNetworkChangeReceiver();
        mContext.registerReceiver(mNetworkChangedReceiver, filter);
    }

    public static void addActivity(Activity activity) {
        mList.add(activity);
    }

    public static void exitAllActivity() {
        try {
            for (Activity activity : mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void exitActivity(Activity activity){
        mList.remove(activity);
    }

//    @Override
//    public void getNewToken(String token) {
//        SLFConstants.token = token;
//        SLFLogUtil.sdkd("yj","get toekn====:::"+token);
//    }
}
