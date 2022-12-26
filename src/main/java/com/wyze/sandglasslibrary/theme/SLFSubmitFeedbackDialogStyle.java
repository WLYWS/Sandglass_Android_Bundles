package com.wyze.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 提交反馈底部弹框自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFSubmitFeedbackDialogStyle {
    //弹框背景颜色
    public static int popupBgColor = SLFCommonStyle.componentBgColor;
    //选项背景色
    public static int popupItemBgColor =SLFResourceUtils.getColor(R.color.slf_popup_item_bg_color);
    //弹框标题文案颜色
    public static int popupTitleColor = SLFResourceUtils.getColor(R.color.slf_popup_title_color);
    //弹框关闭按钮icon
    public static Drawable popupCloseIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);
    //弹框小标题文案颜色
    public static int popupSubTitleColor = SLFCommonStyle.subTitleColor;
    //弹框选项卡背景颜色
    public static int popupTabBgColor = SLFResourceUtils.getColor(R.color.slf_popup_tab_bg_color);
    //弹框选中文案颜色
    public static int popupSelectedTextColor = SLFCommonStyle.themeColor;
    //弹框默认文案颜色
    public static int popupDefaultTextColor = SLFResourceUtils.getColor(R.color.slf_popup_default_text_color);
    //弹框选中icon
    public static Drawable popupSelectIcon = SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg);

}
