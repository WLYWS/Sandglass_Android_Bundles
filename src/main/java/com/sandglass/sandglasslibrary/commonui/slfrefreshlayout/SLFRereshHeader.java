package com.sandglass.sandglasslibrary.commonui.slfrefreshlayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * @author yangjie
 */
public class SLFRereshHeader extends RelativeLayout implements RefreshHeader {
    private ProgressBar mCircleProgressView;

    public SLFRereshHeader(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public SLFRereshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public SLFRereshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setMinimumHeight(dp2px(context, 0));
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
        //mCircleProgressView.clearAnimation();
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
        setBackgroundColor(getResources().getColor(R.color.slf_bg_color));
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
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) { // 状态改变事件
    }

    /**
     * dp转px
     */
    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}