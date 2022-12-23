package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFPreviewPagerAdapter;
import com.wyze.sandglasslibrary.base.SLFPhotoBaseActivity;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonui.SLFPhotoViewPager;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.uiutils.SLFPhotoViewAttacher;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SLFPhotoPreviewActivity extends SLFPhotoBaseActivity {

    private SLFPhotoViewPager mViewPager;
    private ImageView ivBack;
    private ImageView ivPhotograph;
    private View titleBar;
    private TextView tvIndicator;

    private SLFPhotoViewAttacher mAttacher;

    private static final int REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST = 101;
    //	private String photoPath
    private SLFMediaData mCurrentData;
    private SLFPreviewPagerAdapter mPreviewVpAdapter;
    private ArrayList<SLFMediaData> picPathLists;
    private Uri outputUri;
    private Uri imageUri;

    private int aspect_X;
    private int aspect_Y;
    private boolean isDirectCrop;

//	private ProgressBar mProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        initView();
        initListener();

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.slf_photo_preview);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
//		photoPath = getIntent().getStringExtra("photoPath")
        picPathLists = getIntent().getParcelableArrayListExtra("photoPath");
//		picPathLists = getIntent().getStringArrayListExtra("photoPath")

        aspect_X = getIntent().getIntExtra("aspect_x",0);
        aspect_Y = getIntent().getIntExtra("aspect_y",0);
        isDirectCrop = getIntent().getBooleanExtra("direct_crop",false);

        mViewPager = findViewById(R.id.slf_vp_preview);
        ivBack = findViewById(R.id.slf_photo_title_back);
        titleBar = findViewById(R.id.slf_photo_title_titleBar);
        TextView tvTitle = findViewById(R.id.slf_photo_title_filename);
        LinearLayout titlelinear = findViewById(R.id.slf_photo_title_center_linear);
        ImageView titleimg = findViewById(R.id.slf_iv_arrow_down);
        titleimg.setVisibility(View.GONE);
        titlelinear.setBackgroundColor(SLFResourceUtils.getColor(R.color.transparent));
//		ImageView imgPreview = (ImageView) findViewById(R.id.iv_preview)

//		mProgressBar = findViewById(R.id.progress_bar)

        tvTitle.setText("Preview");
//		ivPhotograph.setVisibility(View.VISIBLE)
//		btnCrop.updateBackGround(getColor())
//		btnConfirm.updateBackGround(getColor())


        if(picPathLists!=null && !picPathLists.isEmpty()){
            mCurrentData = picPathLists.get(0);
            //isCrop();
            mPreviewVpAdapter = new SLFPreviewPagerAdapter(getContext(),picPathLists);
            mViewPager.setAdapter(mPreviewVpAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int arg0) {
                    mCurrentData = picPathLists.get(arg0);
                    //isCrop();
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    //
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    //
                }
            });

            mPreviewVpAdapter.setOnClickListener(() -> {
//                if(titleBar.getVisibility() == View.VISIBLE){
//                    titleBar.setVisibility(View.GONE);
//                    btnCrop.setVisibility(View.GONE);
//                    btnConfirm.setVisibility(View.GONE);
//                }else{
//                    titleBar.setVisibility(View.VISIBLE);
//                    btnCrop.setVisibility(View.VISIBLE);
//                    btnConfirm.setVisibility(View.VISIBLE);
//                }
                //isCrop();
            });
        }

//		mAttacher = new PhotoViewAttacher(imgPreview)
        /**是否裁剪图片*/
//        if(isDirectCrop){
//            cropPhoto();
//        }
    }


    private void initListener() {
        ivBack.setOnClickListener(arg0 -> finish());
    }

//    private void isCrop() {
//        if(mCurrentData.getMimeType().contains("gif")){
//            btnCrop.setVisibility(View.GONE);
//        }else if(mCurrentData.getMimeType().contains("video")){
//            btnCrop.setVisibility(View.GONE);
//        }else{
//            if(titleBar.getVisibility() == View.VISIBLE){
//                btnCrop.setVisibility(View.VISIBLE);
//            }
//        }
//    }


    private void cropPhoto() {

        if(aspect_X == 0 || aspect_Y == 0){
            Uri source = Uri.fromFile(new File(picPathLists.get(mViewPager.getCurrentItem()).getOriginalPath()));
            File filePath = new File(SLFConstants.CROP_IMAGE_PATH +SLFUtils.getCharacterAndNumber() + ".jpg");
            if (!filePath.exists() && filePath.getParentFile()!=null) {
                filePath.getParentFile().mkdir();
                try {
                    //创建文件
                    filePath.createNewFile();
                } catch (IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }

            outputUri = Uri.fromFile(filePath);
//            Crop.of(source, outputUri)
//                    .setColor(getColor())
//                    .withAspect(aspect_X,aspect_Y).start(this);
        }else{
//            Intent intent = new Intent(SLFPhotoPreviewActivity.this,CropPhotoActivity.class);
//            intent.putExtra("pic_path",picPathLists.get(mViewPager.getCurrentItem()).getOriginalPath());
//            intent.putExtra("aspect_x", aspect_X);
//            intent.putExtra("aspect_y", aspect_Y);
//            intent.putExtra("app_color", getColor());
//            startActivityForResult(intent,CropPhotoActivity.REQUEST_CODE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Intent intent = new Intent("takePhoto", imageUri);
                SLFPhotoPreviewActivity.this.setResult(CAMERA_REQUEST, intent);
                finish();
            }
//            if (requestCode == Crop.REQUEST_CROP) {
//                returnResult();
//            } else if (requestCode == CropPhotoActivity.REQUEST_CODE && data != null) {
//                String path = data.getStringExtra("pic_path");
//                int index = mViewPager.getCurrentItem();
//                picPathLists.get(index).setOriginalPath(path);
//                mViewPager.setAdapter(mPreviewVpAdapter);
//                mViewPager.setCurrentItem(index);
//            } else if (requestCode == CAMERA_REQUEST) {
//                Intent intent = new Intent("takePhoto", imageUri);
//                PhotoPreviewActivity.this.setResult(CAMERA_REQUEST, intent);
//                finish();
//            }
        }

    }

    private void returnResult() {
        int index = mViewPager.getCurrentItem();
        picPathLists.get(index).setOriginalPath(outputUri.getEncodedPath());
        mViewPager.setAdapter(mPreviewVpAdapter);
        mViewPager.setCurrentItem(index);
    }

}
