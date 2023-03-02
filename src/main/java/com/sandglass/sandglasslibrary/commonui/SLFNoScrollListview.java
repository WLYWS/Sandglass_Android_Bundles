package com.sandglass.sandglasslibrary.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 禁止滚动的ListView
 * Created by wangjian on 2023/1/5
 */
public class SLFNoScrollListview extends ListView{

    public SLFNoScrollListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}

