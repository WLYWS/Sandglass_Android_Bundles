package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;

import java.util.ArrayList;

/**
 * Greated by yangjie
 * describe:反馈列表
 * time:2023/1/6
 *
 */
public class SLFFeedbackList extends SLFBaseActivity {

    /**返回图标*/
    private ImageView slf_back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slf_history_feedback_main);
    }

}
