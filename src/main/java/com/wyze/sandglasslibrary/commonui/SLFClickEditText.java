package com.wyze.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by wangjian on 2023/1/9
 */
public class SLFClickEditText extends AppCompatEditText {
    public SLFClickEditText (@NonNull Context context) {
        super(context);
    }

    public SLFClickEditText (@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SLFClickEditText (@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
//        if (!isEnabled()){
//            return false;
//        }
        return false;
    }
}
