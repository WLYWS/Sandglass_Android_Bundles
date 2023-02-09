package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFPreviewLeaveMsgPagerAdapter;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFPreviewPagerAdapter;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFPhotoViewPager;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;
//import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;

public class SLFFeedbackPicPreviewActivity extends SLFBaseActivity {

    private ArrayList<SLFMediaData> picPathLists;

    private ArrayList<SLFLeveMsgRecordMoudle> leavePathLists;

    private SLFPreviewPagerAdapter mPreviewVpAdapter;

    private SLFPreviewLeaveMsgPagerAdapter mPreViewLeaveMsgAdapter;

    private SLFMediaData mediaData;

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
        final ImageView img_back = findViewById(R.id.slf_iv_back);
        final RelativeLayout bottomlayout = findViewById(R.id.slf_bottom_use_relative);
        final TextView use_btn =findViewById(R.id.slf_use_photo);
        final TextView re_take = findViewById(R.id.slf_retake);
        img_back.setOnClickListener(v -> finish());
        SLFPhotoViewPager mViewPager = findViewById(R.id.slf_vp_preview);
        leavePathLists = (ArrayList<SLFLeveMsgRecordMoudle>) getIntent().getSerializableExtra("leaveMsg");
        picPathLists = getIntent().getParcelableArrayListExtra("photoPath");
        int position = getIntent().getIntExtra("position",0);
        String from = getIntent().getStringExtra("from");
        mediaData = getIntent().getParcelableExtra("take_photo");
        if(picPathLists!=null) {
            mPreviewVpAdapter = new SLFPreviewPagerAdapter(getActivity(), picPathLists);
            mViewPager.setAdapter(mPreviewVpAdapter);
        }else if(leavePathLists!=null){
            mPreViewLeaveMsgAdapter = new SLFPreviewLeaveMsgPagerAdapter(getActivity(),leavePathLists);
            mViewPager.setAdapter(mPreViewLeaveMsgAdapter);
        }
        mViewPager.setCurrentItem(position);

        if(picPathLists!=null){
            tvTitle.setText(position+1+"/"+picPathLists.size());
        }else if(leavePathLists!=null){
            tvTitle.setText(position+1+"/"+leavePathLists.size());
        }
        final View viewParent = findViewById(R.id.slf_pic_preview_parent);

        if(from.equals("takephoto")){
            tvTitle.setVisibility(View.GONE);
            img_back.setVisibility(View.GONE);
            bottomlayout.setVisibility(View.VISIBLE);
            re_take.setOnClickListener(v -> finish());
            use_btn.setOnClickListener(v -> gotoPhotoGridActivity());
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            img_back.setVisibility(View.VISIBLE);
            bottomlayout.setVisibility(View.GONE);
        }
//        mPreviewVpAdapter.setOnClickListener(() -> {
////            if(titleBar.getVisibility() == View.VISIBLE){
////                titleBar.setVisibility(View.GONE);
////                viewParent.setBackgroundColor(getResources().getColor(R.color.black));
////            }else{
////                titleBar.setVisibility(View.VISIBLE);
////                viewParent.setBackgroundColor(getResources().getColor(R.color.transparent));
////            }
//        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //
            }
            @Override
            public void onPageSelected(int i) {
                //SLFLogUtil.d(TAG,"feedbackpreview onPageSelected");
                if(picPathLists!=null) {
                    if (tvTitle.getVisibility() == View.VISIBLE)
                        tvTitle.setText(i + 1 + "/" + picPathLists.size());
                }else if(leavePathLists!=null){
                    if (tvTitle.getVisibility() == View.VISIBLE)
                        tvTitle.setText(i + 1 + "/" + leavePathLists.size());
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {
                //
            }
        });
    }

    private void gotoPhotoGridActivity(){
        picPathLists.add(mediaData);
        Intent in = new Intent(SLFFeedbackPicPreviewActivity.this,SLFPhotoGridActivity.class);
        in.putExtra("mediaData",mediaData);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        SLFLogUtil.d("yj","mediaData  preview----"+mediaData);
        finish();
    }
}
