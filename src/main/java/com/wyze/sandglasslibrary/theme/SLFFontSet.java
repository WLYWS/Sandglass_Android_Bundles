package com.wyze.sandglasslibrary.theme;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 *自定义字体名：接入方可自行配置细/中/粗三种字体对应的字体
 * Created by wangjian on 2022/12/26
 */
public class SLFFontSet {
    //细
    public static String SLF_RegularFont = "fonts/Rany.otf";
    //中
    public static String SLF_MediumFont = "fonts/Rany-Medium.otf";
    //粗
    public static String SLF_BoldFont = "fonts/Rany-Bold.otf";

    /**
     *  设置细字体
     * @param context
     * @param textView
     */
    public static void setSLF_RegularFont(Context context,TextView textView){
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),SLF_RegularFont);
        textView.setTypeface(typeFace);
    }

    /**
     *  设置中字体
     * @param context
     * @param textView
     */
    public static void setSLF_MediumFontt(Context context,TextView textView){
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),SLF_MediumFont);
        textView.setTypeface(typeFace);
    }

    /**
     *  设置粗字体
     * @param context
     * @param textView
     */
    public static void setSLF_BoldFont(Context context,TextView textView){
        Typeface typeFace =Typeface.createFromAsset(context.getAssets(),SLF_BoldFont);
        textView.setTypeface(typeFace);
    }
}
