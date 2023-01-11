package com.wyze.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * 自定义 正方形预览界面
 *
 * @packageName: com.wyze.sandglasslibarary
 * @fileName: AutoTextureView
 * @date: 2023/1/11  16:15
 * @author: yangjie
 */

public class SLFAutoTextureView extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public SLFAutoTextureView(Context context) {
        super(context);
    }

    public SLFAutoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLFAutoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }
}
