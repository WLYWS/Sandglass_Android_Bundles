package com.wyze.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 提交反馈模块页面自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFSubmitFeedBackPageStyle {

    //小标题颜色
    public static int subtitleColor = SLFCommonStyle.subTitleColor;
    //选项卡/输入框背景色
    public static int tabBgColor = SLFCommonStyle.componentBgColor;
    //默认文案颜色
    public static int defaultTextColor = SLFResourceUtils.getColor(R.color.slf_feedback_default_text_color);
    //输入文案颜色
    public static int inputTextColor = SLFResourceUtils.getColor(R.color.slf_feedback_input_text_color);
    //展开icon
    public static Drawable openIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //下划线颜色
    public static int underlineColor = SLFResourceUtils.getColor(R.color.slf_feedback_underline_color);
    //添加图片背景颜色
    public static int addPhotosBgColor = SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_bg_color);
    //添加图片icon
    public static Drawable addPhotosIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //添加图片文字颜色
    public static int addPhotosTextColor = SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_text_color);
    //email错误文案颜色
    public static int emailErrorTextColor = SLFCommonStyle.warningColor;
    //清空文案icon
    public static Drawable cleanTextIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //senglog默认icon
    public static Drawable sendlogDefaultIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //sendlog选中icon
    public static Drawable sendlogSelectIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //sendlog文案颜色
    public static int sendlogTextColor = SLFResourceUtils.getColor(R.color.slf_feedback_send_log_text_color);
    //submit按钮不可用背景颜色
    public static int submitButtonDisableBgColor = SLFCommonStyle.componentBgColor;
    //submit按钮不可用文案颜色
    public static int submitButtonDisableTextColor = SLFCommonStyle.subTitleColor;
    //submit按钮可用背景颜色
    public static int submitButtonUsableBgColor = SLFCommonStyle.themeColor;
    //submit按钮可用文案颜色
    public static int submitButtonUsableTextColor = SLFResourceUtils.getColor(R.color.slf_feedback_submit_button_usable_text_color);
}
