package com.sandglass.sandglasslibrary.uiutils;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 **created by yangjie
 **describe:公共的字体工具类
 **time:2022/12/2
 *
 */
public class SLFFontsUtils {
    private static final String TAG = "SLFFontsUtil";

    private SLFFontsUtils(){}

    private static Typeface sTypeface = null;

    /**
     * 在所有在ViewGroup textview设置字体。递归搜索所有内部ViewGroups。
     */
    public static void setFont(ViewGroup group) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(sTypeface);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }

    public static void setFont(ViewGroup group,String fileName) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(group.getContext().getAssets(), fileName));
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, fileName);
            }
        }
    }

    /**
     * 设置TextView字体
     */
    public static void setFont(View v) {
        if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
            ((TextView) v).setTypeface(sTypeface);
        }
    }

    public static void setFont(View v,String fileName) {
        if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
            if (TextUtils.isEmpty(fileName)
                    || (!fileName.contains(".otf") && !fileName.contains(".TTF"))) {
                return;
            }
            ((TextView) v).setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), fileName));
        }
    }

    /**
     * 通过tag设置ViewGroup字体
     */
    public static void setFontByTag(View view) {
        setFontByTag(view, null);
    }
    public static void setFontByTag(View view, Object parentTag) {
        if(null == view){
            return;
        }
        Object tag = view.getTag();
        if(tag == null){
            tag = parentTag;
        }
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            View v;
            for (int i = 0; i < count; i++) {
                v = ((ViewGroup) view).getChildAt(i);
                if (v instanceof TextView) {

                    Object childTag = v.getTag() ==null?tag:v.getTag();

                    if (null != childTag) {
                        setFont(v, childTag.toString());
                    }
                } else if (v instanceof ViewGroup) {
                    setFontByTag(v, tag);
                }
            }
        } else if (null != tag && view instanceof TextView) {
            String fontPath = tag.toString();
            setFont(view, fontPath);
        }
    }


    /**
     * 释放可关闭的对象
     *
     * @param obj 可关闭的对象
     */
    private static void tryClose(Closeable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }
}
