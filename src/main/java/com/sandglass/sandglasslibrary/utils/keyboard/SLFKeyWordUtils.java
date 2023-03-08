package com.sandglass.sandglasslibrary.utils.keyboard;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Greated by yangjie
 * describe:软键盘隐藏，显示，监听
 * time：2022/12/15
 */
public class SLFKeyWordUtils {



    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et,Activity activity) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}