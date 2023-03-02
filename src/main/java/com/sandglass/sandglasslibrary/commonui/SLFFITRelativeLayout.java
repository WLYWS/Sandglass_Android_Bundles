package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class SLFFITRelativeLayout extends RelativeLayout {

    public SLFFITRelativeLayout(Context context) {
        super(context);
    }

    public SLFFITRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLFFITRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SLFFITRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        insets.top = 0;
        return super.fitSystemWindows(insets);
    }

}
