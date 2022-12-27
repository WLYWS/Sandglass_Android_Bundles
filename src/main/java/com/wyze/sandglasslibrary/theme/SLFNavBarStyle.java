package com.wyze.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 导航栏自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFNavBarStyle {
    //导航栏返回键icon
    public static Drawable backIcon = SLFResourceUtils.getDrawable(R.drawable.slf_title_icon_white_back);
    //导航栏标题颜色
    public static int titleColor = SLFResourceUtils.getColor(R.color.slf_title_color);
}
