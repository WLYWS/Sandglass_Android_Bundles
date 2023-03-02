package com.sandglass.sandglasslibrary.theme;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 反馈列表
 * Created by wangjian on 2022/12/26
 */
public class SLFRecordStyle {
    ///标题正常颜色
    public static int titleTextColor = SLFCommonStyle.subTitleColor;

    ///卡片背景颜色
    public static int cardBGColor = SLFResourceUtils.getColor(R.color.slf_detail_header_bg_color);


    ///时间颜色
    public static int timeTextColor = SLFResourceUtils.getColor(R.color.slf_detail_header_bg_color);

    ///问题颜色
    public static int problemTextColor = SLFResourceUtils.getColor(R.color.slf_detail_header_bg_color);

    ///问题类型颜色
    public static int typeTextColor = SLFCommonStyle.subTitleColor;

}
