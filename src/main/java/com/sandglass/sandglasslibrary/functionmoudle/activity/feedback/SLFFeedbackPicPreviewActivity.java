package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFPreviewLeaveMsgPagerAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFPreviewPagerAdapter;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.commonui.SLFPhotoViewPager;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
//import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

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
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get picPathLists::"+picPathLists.size());
            mPreviewVpAdapter = new SLFPreviewPagerAdapter(getActivity(), picPathLists);
            mViewPager.setAdapter(mPreviewVpAdapter);
        }else if(leavePathLists!=null){
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get leavePathLists::"+leavePathLists.size());
            mPreViewLeaveMsgAdapter = new SLFPreviewLeaveMsgPagerAdapter(getActivity(),leavePathLists);
            mViewPager.setAdapter(mPreViewLeaveMsgAdapter);
        }else{
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get picPathLists::null");
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get leavePathLists::null");
        }
        mViewPager.setCurrentItem(position);

        if(picPathLists!=null){
            tvTitle.setText(position+1+"/"+picPathLists.size());
        }else if(leavePathLists!=null){
            tvTitle.setText(position+1+"/"+leavePathLists.size());
        }else{
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get picPathLists::null");
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": get leavePathLists::null");
        }
        final View viewParent = findViewById(R.id.slf_pic_preview_parent);

        if(from.equals("takephoto")){
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": from takephoto");
            tvTitle.setVisibility(View.GONE);
            img_back.setVisibility(View.GONE);
            bottomlayout.setVisibility(View.VISIBLE);
            re_take.setOnClickListener(v -> finish());
            use_btn.setOnClickListener(v -> gotoPhotoGridActivity());
        }else{
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": not from takephoto");
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
        SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+":pthotoPicPreview goto PhotoGridActivity");
        picPathLists.add(mediaData);
        Intent in = new Intent(SLFFeedbackPicPreviewActivity.this,SLFPhotoGridActivity.class);
        in.putExtra("mediaData",mediaData);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
        SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+":mediaData  preview----"+mediaData);
        finish();
    }
}
