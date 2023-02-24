package com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SLFRecyleItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int space;//边距
    private Paint dividerPaint;//绘制边距颜色的画笔

    /**
     *
     * @param context
     * @param space 边距
     * @param color 边距的颜色
     */
    public SLFRecyleItemDecoration(Context context, int space, int color) {
        this.context = context;
        this.space = space;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    /**
     * 设置边距
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = this.space;//设置下边距
    }

    /**
     * 绘制背景内容, 不会覆盖原有内容
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + space;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

    /**
     * 绘制前景内容, 会覆盖原有内容
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
