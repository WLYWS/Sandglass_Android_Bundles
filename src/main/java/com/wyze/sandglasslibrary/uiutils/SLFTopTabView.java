package com.wyze.sandglasslibrary.uiutils;

/**
 * Greated by yangjie
 * describe:tab切换的view
 * time:2023/1/6
 */

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.viewpager.widget.ViewPager;

import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

public class SLFTopTabView extends LinearLayout implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    private Context context;
    private ViewPager mViewPager;
    private ImageView mIndicater;  //ָʾ��tV
    private ImageView mLine;
    private List<TextView> mList;
    private int currIndex = 0;   // ��ǰ��ʾ��index
    private int positions[];     // �洢�ƶ�λ��
    private int titleCount;      // ��������
    private static final int HEIGHT_NAVIGATION_BAR = 45; // TopTab的高度
    private static final int HEIGHT_INDICATOR = 3; // ָʾ��Ĭ�ϸ߶ȣ���λ��dp
    private int darkGreen = android.R.color.black;

    public SLFTopTabView(Context context) {
        super(context);
        this.context = context;
    }

    @SuppressLint("InlinedApi")
    public void attachToParent(ViewGroup viewGroup, String[] titles,
                               ViewPager viewPager) {
        this.mViewPager = viewPager;
        titleCount = titles.length;
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                SLFResourceUtils.dp2px(context,HEIGHT_INDICATOR+HEIGHT_NAVIGATION_BAR)));
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setOrientation(VERTICAL);
        mLine = new ImageView(context);
        mLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, SLFResourceUtils.dp2px(context,1)));
        mLine.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        this.addView(mLine);

        LinearLayout ll_indicator = new LinearLayout(context);
        ll_indicator.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, SLFResourceUtils.dp2px(context,HEIGHT_INDICATOR)));
        ll_indicator.setBackgroundColor(getResources().getColor(
                android.R.color.holo_blue_light));
        ll_indicator.setOrientation(HORIZONTAL);

        mIndicater = new ImageView(context);
        mIndicater.setImageResource(android.R.color.holo_red_dark);
        mIndicater.setScaleType(ScaleType.MATRIX);
        mIndicater.setLayoutParams(new LinearLayout.LayoutParams(
                SLFCommonUtils.getScreenWidth() / titleCount,
                SLFResourceUtils.dp2px(context,HEIGHT_INDICATOR)));
        ll_indicator.addView(mIndicater);

        this.addView(ll_indicator);


        LinearLayout ll_bottom = new LinearLayout(context);
        ll_bottom.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                SLFResourceUtils.dp2px(context,HEIGHT_NAVIGATION_BAR)));
        ll_bottom.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
        ll_bottom.setOrientation(HORIZONTAL);

        mList = new ArrayList<TextView>();
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(0,
                    SLFResourceUtils.dp2px(context,HEIGHT_NAVIGATION_BAR), 1));
            textView.setText(titles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(android.R.color.white));
            textView.setId(i);
            textView.setOnClickListener(this);
            ll_bottom.addView(textView);
            mList.add(textView);
        }

        this.addView(ll_bottom);

        viewGroup.addView(this);
        viewPager.setOnPageChangeListener(this);


        positions = new int[titleCount];
        positions[0] = 0;
        mList.get(positions[0]).setTextColor(getResources().getColor(darkGreen));
        int distance = (int) (SLFCommonUtils.getScreenWidth() / titleCount);
        for (int i = 1; i < titleCount; i++) {
            positions[i] = distance * i;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        Animation animation = new TranslateAnimation(currIndex * positions[1],
                positions[position], 0, 0);
        currIndex = position;
        animation.setFillAfter(true);
        animation.setDuration(300);
        mIndicater.startAnimation(animation);
        for(int i = 0; i < mList.size() ; i++){
            mList.get(i).setTextColor(getResources().getColor(android.R.color.white));
        }
        mList.get(position).setTextColor(getResources().getColor(darkGreen));
    }

    @Override
    public void onClick(View v) {
        mViewPager.setCurrentItem(v.getId());
    }


}

