package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

public class SLFProgressView extends View {

    private Paint mBackgroundPaint;
    private Paint mProgressPaint;
    private RectF mProgressRect;

    private float mProgress = 0;

    private int mWidth;
    private int mHeight;

    public SLFProgressView(Context context) {
        super(context);
        init();
    }

    public SLFProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SLFProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化背景画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(SLFResourceUtils.getColor(R.color.slf_progress_stroke_bg));
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(6);

        // 初始化进度画笔
        mProgressPaint = new Paint();
        mProgressPaint.setColor(SLFResourceUtils.getColor(R.color.slf_progress_inprogress));
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(6);

        // 初始化进度矩形
        mProgressRect = new RectF();
    }

    // 重写测量大小的 onMeasure 方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景圆环
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - 10, mBackgroundPaint);

        // 绘制进度圆弧
        mProgressRect.set(10, 10, mWidth - 10, mHeight - 10);
        canvas.drawArc(mProgressRect, -90, mProgress * 360, false, mProgressPaint);
    }

    public synchronized void setProgress(float progress) {
        mProgress = progress;
        new Thread() {
            @Override
            public void run() {
                if (mProgress == 1.0f) { return;} // 完毕
                postInvalidate();
            }
        }.start();
        clearFocus();
      //  invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("yj", "[EventDemoActivity-->dispatchTouchEvent]ev=" + event.getAction());
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果触摸事件在进度圆弧内，不处理事件并将事件传递给下一层视图
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("yj","action_down:::");
                break;
        }
        // 否则，处理事件
        return false;
    }


}
