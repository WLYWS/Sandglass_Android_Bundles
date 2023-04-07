package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;

public class SLFWebView extends android.webkit.WebView {
    public SLFWebView(Context context) {
        super(context);
    }

    public SLFWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLFWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}