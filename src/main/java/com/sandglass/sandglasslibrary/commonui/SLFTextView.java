package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.sandglass.sandglasslibrary.R;

/**
 *
 **created by yangjie
 **describe:计划写的一个公共textview
 **time:2022/12/2
 *
 */
public class SLFTextView extends AppCompatTextView {

    private final String TAG = SLFTextView.class.getSimpleName();

    private static final int TTNORMSPRO_NORMAL = 0;
    private static final int TTNORMSPRO_MEDIUM = 1;
    private static final int TTNORMSPRO_BOLD = 2;
    private static final int TTNORMSPRO_LIGHT = 3;
    private static final int TTNORMSPRO_BLACK = 4;
    private static final int TTNORMSPRO_REGULAR = 5;// 默认
    private static final int TTNORMSPRO_EXTRABLACK = 6;
    private static final int TTNORMSPRO_THIN = 7;
    private static final int TTNORMSPRO_EXTRALIGHT = 8;
    private static final int TTNORMSPRO_EXTRABOLD = 9;
    private static final int TTNORMSPRO_DEMIBOLD = 10;


    public SLFTextView(Context context) {
        super(context);
    }

    public SLFTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SLFTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setTypeface(context, attrs);
    }


    private void setTypeface(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SLFTextView);
        Typeface typeface = null;
        if (typedArray.hasValue(R.styleable.SLFTextView_slfTypeface)) {
            int typefaceValue = typedArray.getInt(R.styleable.SLFTextView_slfTypeface, TTNORMSPRO_REGULAR);
            //TODO设置字体
//            switch (typefaceValue) {
//               switch case TTNORMSPRO_NORMAL:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_NORMAL);
//                    break;
//                case TTNORMSPRO_MEDIUM:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_MEDIUM);
//                    break;
//                case TTNORMSPRO_BOLD:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_BOLD);
//                    break;
//                case TTNORMSPRO_LIGHT:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_LIGHT);
//                    break;
//                case TTNORMSPRO_BLACK:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_BLACK);
//                    break;
//                case TTNORMSPRO_REGULAR:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_REGULAR);
//                    break;
//                case TTNORMSPRO_EXTRABLACK:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_EXTRABLACK);
//                    break;
//                case TTNORMSPRO_THIN:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_THIN);
//                    break;
//                case TTNORMSPRO_EXTRALIGHT:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_EXTRALIGHT);
//                    break;
//                case TTNORMSPRO_EXTRABOLD:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_EXTRABOLD);
//                    break;
//                case TTNORMSPRO_DEMIBOLD:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_DEMIBOLD);
//                    break;
//                default:
//                    typeface = SLFFontsUtils.getFont(SLFFontsUtils.TTNORMSPRO_REGULAR);
//            }
        }

        if (typeface != null) {
            setTypeface(typeface);
        }

        typedArray.recycle();
    }
}
