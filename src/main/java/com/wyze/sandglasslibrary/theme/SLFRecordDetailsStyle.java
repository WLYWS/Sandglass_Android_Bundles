package com.wyze.sandglasslibrary.theme;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 反馈详情
 * Created by wangjian on 2022/12/26
 */
public class SLFRecordDetailsStyle {
    ///顶部背景颜色
    public static int HeaderBGColor = SLFResourceUtils.getColor(R.color.slf_detail_header_bg_color);

    ///底部（全部）背景颜色
    public static int bottomBGColor = SLFResourceUtils.getColor(R.color.slf_detail_bottom_bg_colorr);

    ///顶部背景分割线
    public static int LineColor = SLFResourceUtils.getColor(R.color.slf_detail_line_color);



    ///反馈id颜色
    public static int IDColor = SLFResourceUtils.getColor(R.color.slf_detail_id_color);

    ///问题类型颜色
    public static int typeTextColor = SLFResourceUtils.getColor(R.color.slf_detail_type_text_color);


    ///留言卡片背景颜色
    public static int cardBGColor = SLFResourceUtils.getColor(R.color.slf_detail_card_bg_color);


    ///卡片文字颜色
    public static int cardContentColor = SLFResourceUtils.getColor(R.color.slf_detail_card_content_color);

    ///卡片时间颜色
    public static int cardTimeColor = SLFResourceUtils.getColor(R.color.slf_detail_card_time_color);
}
