package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * created by yangjie
 * describe:生成图形遮罩
 * time: 2022/12/7
 */
public class SLFMaskDrawableUtil {

    private SLFMaskDrawableUtil() {
        // Utility class.
    }

    public static Drawable getMaskDrawable(Context context, int maskId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(maskId);
        } else {
            drawable = context.getResources().getDrawable(maskId);
        }

        if (drawable == null) {
            throw new IllegalArgumentException("maskId is invalid");
        }

        return drawable;
    }
}
