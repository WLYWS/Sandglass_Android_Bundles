package com.sandglass.sandglasslibrary.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;

public class SLFBaseBottomDialog extends Dialog {

    private final View view;
    public final Context context;

    private float startY;
    private float moveY;

    public SLFBaseBottomDialog (@NonNull Context context) {
        super(context);
        this.context = context;
        view = getWindow().getDecorView();
    }

    @Override
    public boolean onTouchEvent (@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY() - startY;
                view.scrollBy(0, -(int) moveY);
                startY = event.getY();
                if (view.getScrollY() > 0) {
                    view.scrollTo(0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (view.getScrollY() < -this.getWindow().getAttributes().height / 4 && moveY > 0) {
                    dismiss();
                }
                view.scrollTo(0, 0);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * dialog显示
     */
    @Override
    public void show ( ) {
        super.show();
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWindow.setWindowAnimations(R.style.slf_dialogBottomAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = SLFCommonUtils.getScreenWidth(); // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
    }

    /**
     * dialog退出
     */
    @Override
    public void dismiss ( ) {
        super.dismiss();
    }
}
