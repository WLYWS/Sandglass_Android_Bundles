package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFPreviewPagerAdapter;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFPhotoViewPager;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
//import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;

public class SLFFeedbackPicPreviewActivity extends SLFBaseActivity {

    private ArrayList<SLFMediaData> picPathLists;

    private SLFPreviewPagerAdapter mPreviewVpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_feed_back_pic_preview);
        initView();
        //SLFLogUtil.d(TAG,"feedbackpreview onCreate");
    }

    @SuppressLint("SetTextI18n")
    private void initView(){
        //SLFLogUtil.d(TAG,"feedbackpreview initView");

        final View titleBar = findViewById(R.id.slf_title_bar);
        final TextView tvTitle = findViewById(R.id.slf_tv_title_name);

        findViewById(R.id.slf_iv_back).setOnClickListener(v -> finish());
        SLFPhotoViewPager mViewPager = findViewById(R.id.slf_vp_preview);

        picPathLists = getIntent().getParcelableArrayListExtra("photoPath");
        int position = getIntent().getIntExtra("position",0);
        mPreviewVpAdapter = new SLFPreviewPagerAdapter(getActivity(), picPathLists);
        mViewPager.setAdapter(mPreviewVpAdapter);
        mViewPager.setCurrentItem(position);

        if(picPathLists!=null){
            tvTitle.setText(position+1+"/"+picPathLists.size());
        }
        final View viewParent = findViewById(R.id.slf_pic_preview_parent);

        mPreviewVpAdapter.setOnClickListener(() -> {
//            if(titleBar.getVisibility() == View.VISIBLE){
//                titleBar.setVisibility(View.GONE);
//                viewParent.setBackgroundColor(getResources().getColor(R.color.black));
//            }else{
//                titleBar.setVisibility(View.VISIBLE);
//                viewParent.setBackgroundColor(getResources().getColor(R.color.transparent));
//            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //
            }
            @Override
            public void onPageSelected(int i) {
                //SLFLogUtil.d(TAG,"feedbackpreview onPageSelected");
                tvTitle.setText(i+1+"/"+picPathLists.size());
            }
            @Override
            public void onPageScrollStateChanged(int i) {
                //
            }
        });
    }
}
