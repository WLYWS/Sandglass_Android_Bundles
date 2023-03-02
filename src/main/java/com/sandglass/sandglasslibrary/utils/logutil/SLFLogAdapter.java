package com.sandglass.sandglasslibrary.utils.logutil;

import android.util.Log;

/**
 * Created by yangjie
 * discrib:log适配器
 * time: 2022/12/10
 */
class SLFLogAdapter {
    SLFLogAdapter() {
    }
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    public void e(String tag, String message) {
        Log.e(tag, message);
    }

    public void w(String tag, String message) {
        Log.w(tag, message);
    }

    public void i(String tag, String message) {
        Log.i(tag, message);
    }

    public void v(String tag, String message) {
        Log.v(tag, message);
    }

}
