package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFragmentAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.fragment.SLFFeedbackAllHistory;
import com.sandglass.sandglasslibrary.functionmoudle.fragment.SLFFeedbackHistory;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:反馈列表
 * time:2023/1/6
 */
public class SLFFeedbackListActivity extends SLFBaseActivity{

    //进行中
    public final static int IN_PROGRESS = 1;
    //全部
    public final static int ALL = 2;
    ViewPager mViewPager;
    ImageView slf_back_img;
    SlidingTabLayout tabLayout;
    String[] nameList = new String[]{SLFResourceUtils.getString(R.string.slf_history_feedback_title),SLFResourceUtils.getString(R.string.slf_history_all_feedback_title)};
    ViewPager viewPager;
    private Fragment[] fragments;
    private SLFFragmentAdapter adapter;
    private LinearLayout slf_linear_history_layout;
    private ImageView iv_back_img;
    private boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_history_feedback_main);
        isCreate = true;
        slf_linear_history_layout = findViewById(R.id.slf_linear_top);
        tabLayout = findViewById(R.id.slf_tabs);
        viewPager = findViewById(R.id.slf_vp);
        iv_back_img = findViewById(R.id.slf_iv_back);
        initData();
//        fragments = new Fragment[2];
//        fragments[0] = new SLFFeedbackHistoryFragment(IN_PROGRESS);
//        fragments[1] = new SLFFeedbackAllHistoryFragment(ALL);


//        adapter = new SLFFragmentAdapter(getSupportFragmentManager(), fragments, nameList);
//        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(0);


        tabLayout.setViewPager(viewPager, nameList);
        SLFFontSet.setSLF_RegularFont(getContext(),tabLayout.getTitleView(0));
        SLFFontSet.setSLF_RegularFont(getContext(),tabLayout.getTitleView(1));
    }


    private void initData() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SLFFeedbackHistory<>());
        fragments.add(new SLFFeedbackAllHistory<>());
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

}
