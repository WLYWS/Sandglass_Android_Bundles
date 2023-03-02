package com.sandglass.sandglasslibrary.theme;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

/**
 * Alert自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFAlertStyle {
    //背景颜色
    public static int bgColor = SLFResourceUtils.getColor(R.color.slf_alert_bg_color);
    //标题颜色
    public static int titleColor = SLFResourceUtils.getColor(R.color.slf_alert_title_color);
    //detail颜色
    public static int detailColor = SLFResourceUtils.getColor(R.color.slf_alert_detail_color);
    //item normal颜色
    public static int itemNormalColor = SLFResourceUtils.getColor(R.color.slf_alert_item_normal_color);
    //item highlight颜色
    public static int itemHighlightColor = SLFCommonStyle.themeColor;
}
