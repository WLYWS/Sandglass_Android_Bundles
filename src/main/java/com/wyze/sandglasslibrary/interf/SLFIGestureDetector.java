package com.wyze.sandglasslibrary.interf;

import android.view.MotionEvent;

public interface SLFIGestureDetector {

    public boolean onTouchEvent(MotionEvent ev);

    public boolean isScaling();

    public boolean isDragging();

    public void setOnGestureListener(SLFIOnGestureListener listener);

}
