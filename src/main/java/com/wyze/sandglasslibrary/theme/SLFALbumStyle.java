package com.wyze.sandglasslibrary.theme;

import android.graphics.drawable.Drawable;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * 相册自定义样式
 * Created by wangjian on 2022/12/26
 */
public class SLFALbumStyle {
    //相片未选中图片
    public static Drawable photoNotSelectIcon = SLFResourceUtils.getDrawable(R.drawable.slf_photo_selector_title_down);
    //相片选中图片
    public static Drawable photoSelectIcon = SLFResourceUtils.getDrawable(R.drawable.slf_photo_help_choose_selected);
    //相片不能选择时的蒙版颜色
    public static int photoCannotSelectLayerColor = SLFResourceUtils.getColor(R.color.slf_photo_cannot_select_layer_color);
    //相片不能选择时的蒙版透明度
    public static double photoCannotSelectLayerAlpha = 0.8;


}
