package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.commonapi.SLFApi;

public class SLFResourceUtils {

    private SLFResourceUtils(){}

    public static Resources getResources() {
        return SLFApi.getSLFContext().getResources();
    }

    public static int getColor(int id) {
        return getResources().getColor(id);
    }

    public static float getDimension(int id) {
        return getResources().getDimension(id);
    }

    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }

    /** dp转换为px */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /** px转换为dip  */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取R资源的id
     *
     * @param name
     *            资源名称
     * @param type
     *            资源类型。详见R.class内：anim 、drawable...
     * @param defaulRes
     *            默认的返回资源id
     */
    public static int getIdentifier(Context context, String name, String type,
                                    int defaulRes) {
        Resources res = context.getResources();
        int resid = res.getIdentifier(name, type, context.getPackageName());
        if (resid == 0) {
            resid = defaulRes;
        }

        return resid;
    }

    /**
     * 获取drawable资源id,找不到返回0
     *
     * @param name
     *            资源名称
     */
    public static int getIdentifierId(String name) {
        return getIdentifier(SLFApi.getSLFContext(), name, "id", 0);
    }

    /** 获取屏幕材质信息 */
    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

}
