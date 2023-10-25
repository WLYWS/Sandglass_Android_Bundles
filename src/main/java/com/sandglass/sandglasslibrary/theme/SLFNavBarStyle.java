package com.sandglass.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 导航栏自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFNavBarStyle {
    //导航栏返回键icon
    public static Drawable backIcon = SLFResourceUtils.getDrawable(R.drawable.slf_btn_common_back_white);
    //导航栏标题颜色
    public static int titleColor = SLFResourceUtils.getColor(R.color.slf_title_color);
}
