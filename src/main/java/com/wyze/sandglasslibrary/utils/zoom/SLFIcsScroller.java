package com.wyze.sandglasslibrary.utils.zoom;

import android.annotation.TargetApi;
import android.content.Context;

@TargetApi(14)
public class SLFIcsScroller extends SLFGingerScroller {

    public SLFIcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}
