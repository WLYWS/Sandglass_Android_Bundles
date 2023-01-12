package com.wyze.sandglasslibrary.base;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonapi.SLFApi;
import com.wyze.sandglasslibrary.commonui.SLFToastUtil;
import com.wyze.sandglasslibrary.moudle.event.SLFEventCommon;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFCategoryBean;
import com.wyze.sandglasslibrary.uiutils.SLFTitleBarUtil;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * created by yangjie
 * describe:activity的基类
 * time: 2022/12/5
 */
public class SLFBaseActivity extends FragmentActivity {

    private TextView tvTitle;

    protected final String TAG = this.getClass().getSimpleName();

    //public static Map<Integer, WeakReference<Activity>> mActivityStack = new TreeMap<>();
    /**绘制页面是否完成*/
    protected boolean isDrawPageFinish = false;

    protected boolean isWindowFocus = false;

    private boolean isOnPause = false;
    private boolean isOnStop = false;

    private boolean mDestroyed = false;

    private int loadingCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFTitleBarUtil.enableTranslucentStatus(this);
        SLFBaseApplication.addActivity(this);//记录当前应用的Activity,用于退出整个应用
        isDrawPageFinish = false;
        mDestroyed = false;
        EventBus.getDefault().register(this);
        /**设置activity无title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**管理acitivty的集合*/
        //mActivityStack.put(hashCode(), new WeakReference<>(this));

        /**是否要一键变灰*/
//        if(SLFConstants.isGreyed){
//            ColorGrayed();
//        }


    }

    public Context getContext() {
        return this;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        SLFTitleBarUtil.setTitleBar(this);
        tvTitle = findViewById(R.id.slf_tv_title_name);
        View viewBack = findViewById(R.id.slf_iv_back);
        if(viewBack!=null && !viewBack.hasOnClickListeners()){
            viewBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isDrawPageFinish = true;
        isWindowFocus = hasFocus;
    }

    public void setTitle(String titleText) {
        if (tvTitle != null) {
            tvTitle.setText(titleText);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnPause = false;
        isOnStop = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        if(isFinishing()){
            mDestroyed = true;
            EventBus.getDefault().unregister(this);
            hideLoading();
            SLFBaseApplication.exitActivity(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnStop = true;
    }

    @Override
    protected void onDestroy() {
        try{
            super.onDestroy();
            /**activity摧毁时根据hashcode从集合移除*/
            //mActivityStack.remove(hashCode());
            if (EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().unregister(this);
            }
            SLFBaseApplication.exitActivity(this);
        }catch (Exception e){
            Log.e(TAG,Log.getStackTraceString(e));
        }

    }

    @Override
    public void finish() {
        super.finish();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }
    /**一键变灰*/
    private void ColorGrayed(){
        Paint paint = new Paint();
        ColorMatrix colorMatrix= new ColorMatrix();
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    protected SLFBaseActivity getActivity() {
        return SLFBaseActivity.this;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFEventCommon event) {

    }

    public void showLoading() {
        showLoading(false, SLFToastUtil.LOADING_TYPE_WHITE,SLFToastUtil.SHORT_LOADING_DURATION);
    }

    public void showLoading(boolean canGoBack) {
        showLoading(canGoBack,SLFToastUtil.LOADING_TYPE_WHITE,SLFToastUtil.SHORT_LOADING_DURATION);
    }

    public void showLoading(int type) {
        showLoading(false,type,SLFToastUtil.SHORT_LOADING_DURATION);
    }

    public void showLoading(int type,boolean canGoBack) {
        showLoading(canGoBack,type,SLFToastUtil.SHORT_LOADING_DURATION);
    }

    public void showLoading(long duration) {
        showLoading(false,SLFToastUtil.LOADING_TYPE_WHITE2,duration);
    }

    public void showLoading(boolean canGoBack,int type,long duration) {
        if(loadingCount<0){
            loadingCount = 0;
        }
        loadingCount++;
        if(isOnStop){
            return;
        }
        if(isDrawPageFinish){
            SLFToastUtil.showCommonLoading(getActivity(),canGoBack,type,duration);
        }else{
            SLFToastUtil.showLoading(getActivity(),canGoBack,type,duration);
        }
        SLFToastUtil.setOnLoadingDismissListener(() -> {
            if(loadingCount>0){
                loadingCount = 0;
                onLoadingTimeout();
            }
        });

    }

    public void hideLoading() {
        loadingCount--;
        if(isOnStop){
            return;
        }
        SLFToastUtil.removeLoadingDismissListener();
        if(loadingCount<=0){
            loadingCount = 0;
            if(mDestroyed){
                SLFToastUtil.hideCommonLoading();
            }else{
                SLFToastUtil.hideLoading();
            }
        }

    }

    public void hideLoading(boolean forcedDismiss) {
        if(forcedDismiss){
            loadingCount = 0;
            if(isOnStop){
                return;
            }
            SLFToastUtil.removeLoadingDismissListener();
            if(mDestroyed){
                SLFToastUtil.hideCommonLoading();
            }else{
                SLFToastUtil.hideLoading();
            }
        }else{
            if(isOnStop){
                return;
            }
            hideLoading();
        }
    }

    public void onLoadingTimeout(){
    }

    public boolean isDrawFinish(){
        return isDrawPageFinish;
    }

    public boolean isOnPause(){
        return isOnPause;
    }


    /**显示提示文字*/
    public void showToast(String toastText) {
        SLFToastUtil.cancelToast();
        SLFToastUtil.showText(toastText);
    }
    /**Toast显示在底部距离自定义*/
    public void showToastWithMarginBottom(String toastText, float marginBottom) {
        SLFToastUtil.cancelToast();
        SLFToastUtil.showToastWithMarginBottom(toastText,
                SLFCommonUtils.dip2px(SLFApi.getSLFContext(), marginBottom));
    }
    /**Toast显示在中间*/
    public void showCenterToast(String toastText) {
        SLFToastUtil.cancelToast();
        SLFToastUtil.showCenterText(toastText);
    }
    /**Toast没有网络提示*/
    public void showNetworkError() {
        SLFToastUtil.cancelToast();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_network_error));
    }
}