package com.wyze.sandglasslibrary.base;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.event.SLFEventCommon;
import com.wyze.sandglasslibrary.uiutils.SLFTitleBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
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

    public static Map<Integer, WeakReference<Activity>> mActivityStack = new TreeMap<>();
    /**绘制页面是否完成*/
    protected boolean isDrawPageFinish = false;

    protected boolean isWindowFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFTitleBarUtil.enableTranslucentStatus(this);
        isDrawPageFinish = false;
        EventBus.getDefault().register(this);
        /**设置activity无title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /**管理acitivty的集合*/
        mActivityStack.put(hashCode(), new WeakReference<>(this));
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isFinishing()){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        try{
            super.onDestroy();
            /**activity摧毁时根据hashcode从集合移除*/
            mActivityStack.remove(hashCode());
            if (EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().unregister(this);
            }
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

}