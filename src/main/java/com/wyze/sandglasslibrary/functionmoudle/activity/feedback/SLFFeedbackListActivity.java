package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFragmentAdapter;
import com.wyze.sandglasslibrary.functionmoudle.fragment.SLFFeedbackAllHistoryFragment;
import com.wyze.sandglasslibrary.functionmoudle.fragment.SLFFeedbackHistoryFragment;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * Greated by yangjie
 * describe:反馈列表
 * time:2023/1/6
 */
public class SLFFeedbackListActivity extends SLFBaseActivity {

    ViewPager mViewPager;
    ImageView slf_back_img;
    SlidingTabLayout tabLayout;
    String[] nameList = new String[]{SLFResourceUtils.getString(R.string.slf_history_feedback_title),SLFResourceUtils.getString(R.string.slf_history_all_feedback_title)};
    ViewPager viewPager;
    private Fragment[] fragments;
    private SLFFragmentAdapter adapter;
    private LinearLayout slf_linear_history_layout;
    private ImageView iv_back_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_history_feedback_main);

        slf_linear_history_layout = findViewById(R.id.slf_linear_top);
        tabLayout = findViewById(R.id.slf_tabs);
        viewPager = findViewById(R.id.slf_vp);
        iv_back_img = findViewById(R.id.slf_iv_back);


        fragments = new Fragment[2];
        fragments[0] = new SLFFeedbackAllHistoryFragment("first");
        fragments[1] = new SLFFeedbackAllHistoryFragment("second");

        adapter = new SLFFragmentAdapter(getSupportFragmentManager(), fragments, nameList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setViewPager(viewPager, nameList);


    }
}
