package com.sandglass.sandglasslibrary.commonui.slfrefreshlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * @author yangjie
 */
public class SLFRefreshFooter extends RelativeLayout implements RefreshFooter {
    private ProgressBar mCircleProgressView;

    public SLFRefreshFooter(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public SLFRefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public SLFRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setMinimumHeight(SLFResourceUtils.dp2px(context, 60));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        View headerView = View.inflate(context, R.layout.slf_feedback_list_common_header, null);
        mCircleProgressView = (ProgressBar) headerView.findViewById(R.id.slf_header);
        addView(headerView, params);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) { // 尺寸定义完成
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
//        if (mCircleProgressView == null){ return;}
//        float startPercent = 0.20f;
//
//        if (percent > startPercent && percent < 1) {
//            float tempPercent = (percent-startPercent) * 1.0f / (1 - startPercent);
//            mCircleProgressView.setProgressPersent(tempPercent);
//        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }




    @SuppressLint("RestrictedApi")
    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        mCircleProgressView.setVisibility(VISIBLE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mCircleProgressView.setVisibility(GONE);
        return 0; // 动画结束,延迟多少毫秒之后再收回
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPrimaryColors(int... colors) {
        setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    @NonNull
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }
}