package com.wyze.sandglasslibrary.theme;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 公共自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFCommonStyle {
    //背景颜色
    public static int bgColor = SLFResourceUtils.getColor(R.color.slf_bg_color);
    //主题色
    public static int themeColor = SLFSetTheme.defaultThemeColor;
    //组件背景色
    public static int componentBgColor = SLFResourceUtils.getColor(R.color.slf_component_bg_color);
    //子标题颜色
    public static int subTitleColor = SLFResourceUtils.getColor(R.color.slf_sub_title_color);
    //警告色
    public static int warningColor = SLFResourceUtils.getColor(R.color.slf_warning_color);

}
