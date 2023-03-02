package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Greated by yangjie
 * describe:自定义scrollview开放监听Y的滑动距离
 * time:2022/12/15
 *
 */
public class SLFScrollView extends ScrollView {

    private OnScrollListener onScrollListener;

    public SLFScrollView(Context context) {
        super(context);
    }

    public SLFScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLFScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 接口对外公开
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     *
     * 滚动的回调接口
     *
     * @author yangjie
     *
     */
    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *              、
         */
        void onScroll(int scrollY);
    }
}
