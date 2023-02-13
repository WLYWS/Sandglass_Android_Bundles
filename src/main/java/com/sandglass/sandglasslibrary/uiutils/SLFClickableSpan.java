package com.sandglass.sandglasslibrary.uiutils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * created by yangjie
 * describe:dialog监听适配
 * time: 2022/12/15
 */
public class SLFClickableSpan extends ClickableSpan {

    private OnClickListener mListener;

    public SLFClickableSpan(OnClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View widget) {
        if(mListener!=null){
            mListener.onClick();
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }

    public interface OnClickListener {
        void onClick();
    }
}