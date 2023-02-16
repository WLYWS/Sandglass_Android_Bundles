package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

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


    /**
     *  android:inputType="textMultiLine"
     *  android:imeOptions="actionSend"同时生效
     * @param outAttrs
     * @return
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if(inputConnection != null){
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return inputConnection;
    }
}
