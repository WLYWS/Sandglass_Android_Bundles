package com.sandglass.sandglasslibrary.utils.logutil;

/**
 * Created by yangjie
 * discrib:Log的api
 * time:2022/12/10
 *
 */

public class SLFLogAPI {
    private static final String TAG = "SLFLogAPI";

    private SLFLogAPI(){}

    public static void init() {
        android.util.Log.d(TAG, "onCreate: init log");
        loadXlogLib();
        SLFLogUtil.getInstance();
        SLFLogUtil.setUserInfo();
    }

    private static void loadXlogLib() {
        try {
            System.loadLibrary("c++_shared");
        } catch (Exception e) {
            SLFLogUtil.e("xlog","load c++_shared error");
        }
        try {
            System.loadLibrary("marsxlog");
        } catch (Exception e) {
            SLFLogUtil.e("xlog","load marsxlog error");
        }
    }
}
