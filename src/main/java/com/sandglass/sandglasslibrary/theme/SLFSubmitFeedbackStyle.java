package com.sandglass.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 提交反馈完成页
 * Created by wangjian on 2022/12/26
 */
public class SLFSubmitFeedbackStyle {
    //完成页icon
    public static Drawable finishIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //完成页标题颜色
    public static int finishTitleColor = SLFResourceUtils.getColor(R.color.slf_complete_title_color);
    //完成页文案颜色
    public static int finishTextColot = SLFResourceUtils.getColor(R.color.slf_complete_text_color);

}
