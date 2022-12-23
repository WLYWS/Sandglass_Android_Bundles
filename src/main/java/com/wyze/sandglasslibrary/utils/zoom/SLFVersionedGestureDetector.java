package com.wyze.sandglasslibrary.utils.zoom;

import android.content.Context;
import android.os.Build;

import com.wyze.sandglasslibrary.interf.SLFIGestureDetector;
import com.wyze.sandglasslibrary.interf.SLFIOnGestureListener;

public final class SLFVersionedGestureDetector {

    public static SLFIGestureDetector newInstance(Context context,
                                                  SLFIOnGestureListener listener) {
        final int sdkVersion = Build.VERSION.SDK_INT;
        SLFIGestureDetector detector;

        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            detector = new SLFCupckaGestureDetector(context);
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            detector = new SLFEclairGestureDetector(context);
        } else {
            detector = new SLFFroyoGestureDetector(context);
        }

        detector.setOnGestureListener(listener);

        return detector;
    }

}
