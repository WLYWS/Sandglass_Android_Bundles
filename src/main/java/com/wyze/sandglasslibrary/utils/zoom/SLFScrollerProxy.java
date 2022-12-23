package com.wyze.sandglasslibrary.utils.zoom;

import android.content.Context;
import android.os.Build;

public abstract class SLFScrollerProxy {

    public static SLFScrollerProxy getScroller(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            return new SLFPreGingerScroller(context);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new SLFGingerScroller(context);
        } else {
            return new SLFIcsScroller(context);
        }
    }

    public abstract boolean computeScrollOffset();

    public abstract void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY,
                               int maxY, int overX, int overY);

    public abstract void forceFinished(boolean finished);

    public abstract boolean isFinished();

    public abstract int getCurrX();

    public abstract int getCurrY();


}
