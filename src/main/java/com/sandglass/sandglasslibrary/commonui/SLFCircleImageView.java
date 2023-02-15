package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.view.animation.Animation;

public class SLFCircleImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Animation.AnimationListener mListener;

    public SLFCircleImageView(Context context) {
        super(context);
    }


    public void setAnimationListener(Animation.AnimationListener listener) {
        mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
        if (mListener != null) {
            mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (mListener != null) {
            mListener.onAnimationEnd(getAnimation());
        }
    }
}