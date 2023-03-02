package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Greated by yangjie
 * Description:图片预览界面的viewpager
 * time:2022/12/7
 */
public class SLFPhotoViewPager extends ViewPager {
    private static final String TAG = "PhotoViewPager";
    public SLFPhotoViewPager(Context context) {
        super(context);
    }

    public SLFPhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }
}
