package com.sandglass.sandglasslibrary.commonui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sandglass.sandglasslibrary.R;
/**
 *
 **created by yangjie
 **describe:公共的titlebar
 **time:2022/12/2
 *
 */
public class SLFTitleBar extends FrameLayout {

    private TextView slfTitle;
    private ImageView slfBack;
    private TextView slfslfTitleLeft;
    private TextView slfslfTitleRight;
    private ImageView slfsflIconRight;
    private ImageView slfsflIconRight2;
    private int slfStyle = 1;
    private static final int SLF_STYLE_BLACK_ICON = 1;
    private static final int SLF_STYLE_BLACK_TEXT = 2;
    private static final int SLF_STYLE_WHITE_ICON = 3;
    private static final int SLF_STYLE_WHITE_TEXT = 4;

    private String slfTitleName;
    private String slfTitleLeft;
    private String slfTitleRight;
    private int slfResIdRightIcon;
    private int slfResIdRightIcon2;
    private String sflIconRightSvg;
    private String sflIconRight2Svg;

    private OnTitleBarListener mListener;


    public SLFTitleBar(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public SLFTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SLFTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defslfStyleAttr) {
        super(context, attrs, defslfStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        View titleView;
        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SLFTitleBar);
            slfStyle = a.getInteger(R.styleable.SLFTitleBar_title_style, 1);
            slfTitleName = a.getString(R.styleable.SLFTitleBar_title_name);
            slfTitleLeft = a.getString(R.styleable.SLFTitleBar_title_left);
            slfTitleRight = a.getString(R.styleable.SLFTitleBar_title_right);
            slfResIdRightIcon = a.getResourceId(R.styleable.SLFTitleBar_title_right_icon, 0);
            sflIconRightSvg = a.getString(R.styleable.SLFTitleBar_title_right_icon_svg);
            slfResIdRightIcon2 = a.getResourceId(R.styleable.SLFTitleBar_title_right_icon2, 0);
            sflIconRight2Svg = a.getString(R.styleable.SLFTitleBar_title_right_icon2_svg);

            a.recycle();
        }

        switch (slfStyle) {
            case SLF_STYLE_WHITE_ICON:
            case SLF_STYLE_WHITE_TEXT:
                titleView = View.inflate(context, R.layout.slf_activity_titlebar_white, this);
                break;
            case SLF_STYLE_BLACK_ICON:
            case SLF_STYLE_BLACK_TEXT:
            default:
                titleView = View.inflate(context, R.layout.slf_all_common_title, this);
                break;
        }

        slfTitle = titleView.findViewById(R.id.slf_tv_title_name);
        slfBack = titleView.findViewById(R.id.slf_iv_back);
        slfslfTitleLeft = titleView.findViewById(R.id.slf_tv_title_left);
        slfslfTitleRight = titleView.findViewById(R.id.slf_tv_title_right);
        slfsflIconRight = titleView.findViewById(R.id.slf_iv_right);
        slfsflIconRight2 = titleView.findViewById(R.id.slf_iv_second_right);

        setTitle(slfTitleName);
//        slfTitle.setText(slfTitleName)
        slfslfTitleLeft.setText(slfTitleLeft);
        switch (slfStyle) {
            case SLF_STYLE_WHITE_ICON:
            case SLF_STYLE_BLACK_ICON:
                slfBack.setVisibility(View.VISIBLE);
                slfslfTitleLeft.setVisibility(View.GONE);
//                slfslfTitleRight.setVisibility(View.GONE)
                break;
            case SLF_STYLE_WHITE_TEXT:
            case SLF_STYLE_BLACK_TEXT:
                slfBack.setVisibility(View.GONE);
                slfslfTitleLeft.setVisibility(View.VISIBLE);
//                slfslfTitleRight.setVisibility(View.VISIBLE)
                break;
            default:
                break;
        }

        FrameLayout.LayoutParams titleParams = (FrameLayout.LayoutParams) slfTitle.getLayoutParams();
        //titleParams.width = WpkCommonUtil.getScreenWidth() / 4 * 3 - WpkConvertUtil.dp2px(40);
        slfTitle.setLayoutParams(titleParams);

        if (!TextUtils.isEmpty(slfTitleRight)) {
            setslfTitleRight(slfTitleRight);
        }
        if (slfResIdRightIcon > 0) {
            setRightIcon(slfResIdRightIcon);
        }
        if (sflIconRightSvg != null) {
            setRightIconSvg(sflIconRightSvg);
        }
        if (slfResIdRightIcon2 > 0) {
            setRightIcon2(slfResIdRightIcon2);
        }
        if (sflIconRight2Svg != null) {
            setRightIcon2Svg(sflIconRight2Svg);
        }

    }

    public SLFTitleBar setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return this;
        }

        if (title.length() > 24) {
            slfTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        } else if (title.length() > 18) {
            slfTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
        } else {
            slfTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }
        slfTitle.setText(title);
        return this;
    }

    public SLFTitleBar setslfTitleLeft(String text) {
        slfslfTitleLeft.setVisibility(View.VISIBLE);
        slfBack.setVisibility(View.GONE);
        slfslfTitleLeft.setText(text);
        return this;
    }

    public SLFTitleBar setslfTitleRight(String text) {
        slfslfTitleRight.setVisibility(View.VISIBLE);
        slfslfTitleRight.setText(text);
        slfsflIconRight.setVisibility(View.GONE);
        slfsflIconRight2.setVisibility(View.GONE);
        return this;
    }

    public SLFTitleBar setRightIcon(int resId) {
        if (resId > 0) {
            slfslfTitleRight.setVisibility(View.GONE);
            slfsflIconRight.setVisibility(View.VISIBLE);
            slfsflIconRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //slfsflIconRight.setImageDrawable(WpkResourcesUtil.getDrawable(resId));
        }
        return this;
    }

    public SLFTitleBar setRightIconSvg(String assetFileName) {
        if (assetFileName != null) {
            slfslfTitleRight.setVisibility(View.GONE);
            slfsflIconRight.setVisibility(View.VISIBLE);
            slfsflIconRight.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //WpkSVGUtils.setSVGAutoSize(slfsflIconRight, assetFileName);
        }
        return this;
    }

    public SLFTitleBar setRightIcon2(int resId) {
        if (resId > 0) {
            slfslfTitleRight.setVisibility(View.GONE);
            slfsflIconRight2.setVisibility(View.VISIBLE);
            slfsflIconRight2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //slfsflIconRight2.setImageDrawable(WpkResourcesUtil.getDrawable(resId));
            FrameLayout.LayoutParams titleParams = (FrameLayout.LayoutParams) slfTitle.getLayoutParams();
            //titleParams.width = WpkCommonUtil.getScreenWidth() / 3 * 2 - WpkConvertUtil.dp2px(40);
            slfTitle.setLayoutParams(titleParams);
        }
        return this;
    }

    public SLFTitleBar setRightIcon2Svg(String assetFileName) {
        if (assetFileName != null) {
            slfslfTitleRight.setVisibility(View.GONE);
            slfsflIconRight2.setVisibility(View.VISIBLE);
            slfsflIconRight2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //WpkSVGUtils.setSVGAutoSize(slfsflIconRight2, assetFileName);
            FrameLayout.LayoutParams titleParams = (FrameLayout.LayoutParams) slfTitle.getLayoutParams();
            //titleParams.width = WpkCommonUtil.getScreenWidth() / 3 * 2 - WpkConvertUtil.dp2px(40);
            slfTitle.setLayoutParams(titleParams);

        }
        return this;
    }

    public SLFTitleBar setTitleColor(int colorRes) {
        slfTitle.setTextColor(colorRes);
        return this;
    }

    public SLFTitleBar setLeftTextColor(int colorRes) {
        slfslfTitleLeft.setTextColor(colorRes);
        return this;
    }

    public SLFTitleBar setLeftTextEnable(boolean isEnable, int colorResId) {
        slfslfTitleLeft.setEnabled(isEnable);
        slfslfTitleLeft.setTextColor(colorResId);
        return this;
    }

    public SLFTitleBar setRightTextColor(int colorResId) {
        slfslfTitleRight.setTextColor(colorResId);
        return this;
    }

    public SLFTitleBar setRightTextEnable(boolean isEnable, int colorResId) {
        slfslfTitleRight.setEnabled(isEnable);
        slfslfTitleRight.setTextColor(colorResId);
        return this;
    }

    public TextView getTitleView() {
        return slfTitle;
    }

    public TextView getLeftTextView() {
        slfBack.setVisibility(View.GONE);
        slfslfTitleLeft.setVisibility(View.VISIBLE);
        return slfslfTitleLeft;
    }

    public ImageView getLeftIcon() {
        slfslfTitleLeft.setVisibility(View.GONE);
        slfBack.setVisibility(View.VISIBLE);
        return slfBack;
    }

    public TextView getRightTextView() {
        slfslfTitleRight.setVisibility(View.VISIBLE);
        slfsflIconRight.setVisibility(View.GONE);
        slfsflIconRight2.setVisibility(View.GONE);
        return slfslfTitleRight;
    }

    public ImageView getRightIcon() {
        slfsflIconRight.setVisibility(View.VISIBLE);
        slfsflIconRight2.setVisibility(View.VISIBLE);
        slfslfTitleRight.setVisibility(View.GONE);
        return slfsflIconRight;
    }

    public ImageView getRightIcon2() {
        slfsflIconRight.setVisibility(View.VISIBLE);
        slfsflIconRight2.setVisibility(View.VISIBLE);
        slfslfTitleRight.setVisibility(View.GONE);
        return slfsflIconRight2;
    }

    public void setTitleBarslfStyle(int slfStyle){
        this.slfStyle = slfStyle;
        removeAllViews();
        initView(getContext(),null);
    }

    public SLFTitleBar setOnTitleBarListener(OnTitleBarListener listener) {
        this.mListener = listener;
        if (listener == null) {
            return this;
        }
        slfBack.setOnClickListener(v -> mListener.onClickLeft());
        slfslfTitleLeft.setOnClickListener(v -> mListener.onClickLeft());
        slfslfTitleRight.setOnClickListener(v -> mListener.onClickRightText());
        slfsflIconRight.setOnClickListener(v -> mListener.onClickRightIcon());
        slfsflIconRight2.setOnClickListener(v -> mListener.onClickRightIcon2());
        return this;
    }

    public interface OnTitleBarListener {
        void onClickLeft();

        default void onClickTitle() {
        }

        default void onClickRightText() {
        }

        default void onClickRightIcon() {
        }

        default void onClickRightIcon2() {
        }
    }

}
