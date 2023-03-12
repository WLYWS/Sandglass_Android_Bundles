package com.sandglass.sandglasslibrary.commonui.slfrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFRereshHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

public class SLFSmartRefreshLayout extends SmartRefreshLayout {
    public static void init() {
        // 指定全局的下拉Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new SLFRereshHeader(context);
            }
        });

        // 指定全局的上拉Footer
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new SLFRefreshFooter(context);
            }
        });
    }

    public SLFSmartRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public SLFSmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setReboundDuration(300); // 设置回弹动画时长
        setPrimaryColorsId(R.color.slf_theme_color);  // 主题色

    }

    // 下拉/上拉完成
    public void complete() {
        if (mState == RefreshState.Loading) {
            finishLoadMore();
        } else {
            finishRefresh();
        }
    }
}
