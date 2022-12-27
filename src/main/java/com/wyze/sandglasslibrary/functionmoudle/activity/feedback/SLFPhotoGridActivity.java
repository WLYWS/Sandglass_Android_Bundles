package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFUploadFileReponseBean;
import com.wyze.sandglasslibrary.commonapi.SLFCommonUpload;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFileListAdapter;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFPhotoListAdapter;
import com.wyze.sandglasslibrary.base.SLFPhotoBaseActivity;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.moudle.SLFPhotoFolderInfo;
import com.wyze.sandglasslibrary.moudle.event.SLFEventUpdatePhotolist;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFCropUtil;
import com.wyze.sandglasslibrary.utils.SLFPermissionManager;
import com.wyze.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.wyze.sandglasslibrary.utils.SLFPhotoTools;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;
import com.wyze.sandglasslibrary.utils.SLFUtils;
import com.wyze.sandglasslibrary.utils.SLFViewUtil;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;
//import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by yangjie
 * describe:选择相册展示页
 * time: 2022/12/6
 * @author yangjie
 */
public class SLFPhotoGridActivity extends SLFPhotoBaseActivity{


    private ImageView ivBack;
    private ImageView ivPhotograph;
    private Button selected_btn;
    private String selectedImgPath;

    private TextView tvFileName;
    private LinearLayout photo_title_center;

    private GridView gvPhotoList;
    private ListView lvFileList;
    private SLFPhotoListAdapter mPhotoListAdapter;
    private SLFFileListAdapter mFileListAdapter;

    private List<SLFPhotoFolderInfo> mAllPhotoFolderList = new ArrayList<SLFPhotoFolderInfo>();
    private List<SLFMediaData> mCurPhotoList = new ArrayList<>();

    private int RESULT_LOAD_IMAGE = 100;
    private int CAMERA_REQUEST = 101;
    private Uri imageUri;
    private int aspect_X;
    private int aspect_Y;

    private ArrayList<SLFMediaData> picPathLists;

    //private boolean isSingle;
    private int selectedNum;
    private boolean isDirectCrop;

    /**定义permissions*/
    private String[] permissionCamrea = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    private String[] permissionStorage = android.os.Build.VERSION.SDK_INT>29?new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}:new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean isCreate;

    ArrayList<SLFMediaData> oldCurrentList = new ArrayList<>();
    ArrayList<SLFMediaData> newCurrentList = new ArrayList<>();
    ArrayList<String> oldPickPositions = new ArrayList<>();
    public static ExecutorService singleThreadExecutor;
    private static Runnable getPhotoRunable;
    private static Runnable confirmRunnable;
    private boolean isEvent;
    private TextView slf_preview_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SLFLogUtil.d(TAG,"photoGrid onCreate");
        SLFStatusBarColorChange.transparencyBar(this);
        isCreate = true;
        initView();
        requestPermission();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SLFLogUtil.d(TAG,"photoGrid onResume");
        if(!isCreate){
            isEvent = true;
            getPhotos();
        }
        //SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this,permissionStorage,permissionsResumeResult);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.slf_activity_select_photo);
    }

    private void initView() {
        aspect_X = getIntent().getIntExtra("aspect_x", 0);
        aspect_Y = getIntent().getIntExtra("aspect_y", 0);
        selectedNum = getIntent().getIntExtra("selected_number", 1);
        isDirectCrop = getIntent().getBooleanExtra("direct_crop", false);
//        if(selectedNum>1){
//            isSingle = false;
//        }else if(selectedNum == 1){
//            isSingle = true;
//        }

        ivBack = findViewById(R.id.slf_photo_title_back);
        selected_btn = findViewById(R.id.slf_complate_selector);
        slf_preview_text = findViewById(R.id.slf_preview_text);
        tvFileName = findViewById(R.id.slf_photo_title_filename);
        photo_title_center = findViewById(R.id.slf_photo_title_center_linear);
        gvPhotoList = findViewById(R.id.slf_gv_photolist);
        lvFileList = findViewById(R.id.slf_lv_filelist);

        findViewById(R.id.slf_iv_arrow_down).setVisibility(View.VISIBLE);

        if (SLFPhotoSelectorUtils.selectType == SLFMediaType.Video) {
            tvFileName.setText("All videos");
        } else {
            tvFileName.setText("All pictures");
        }

        selected_btn.setText(SLFResourceUtils.getString(R.string.slf_feedback_grid_bottom_right_no_selected));


//        selected_btn.updateBackGround(getColor());
//        if (isDirectCrop) {
//            selected_btn.setVisibility(View.GONE);
//        }

        slf_preview_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPreview();
            }
        });

    }

    private void initGetPhotosRunnable(){
        getPhotoRunable = new Runnable() {
            @Override
            public void run() {
                mAllPhotoFolderList.clear();
                List<SLFPhotoFolderInfo> allFolderList = SLFPhotoTools.getAllPhotoFolder(SLFPhotoGridActivity.this);
                mAllPhotoFolderList.addAll(allFolderList);

                mCurPhotoList.clear();
                if (allFolderList.size() > 0) {
                    if (allFolderList.get(0).getPhotoList() != null) {
                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                    }
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFileListAdapter = new SLFFileListAdapter(getBaseContext(), mAllPhotoFolderList);
                        lvFileList.setAdapter(mFileListAdapter);
                        //mPhotoListAdapter = new SLFPhotoListAdapter(getBaseContext(), mCurPhotoList, isSingle, selectedNum);
                        mPhotoListAdapter = new SLFPhotoListAdapter(getBaseContext(), mCurPhotoList, selectedNum);
                        gvPhotoList.setAdapter(mPhotoListAdapter);
                        //hideLoading();
                        if (isEvent) {
                            EventBus.getDefault().post(new SLFEventUpdatePhotolist(true));
                        }
                    }
                });
            }
        };
    }

    private void initConfirmRunable(){
        confirmRunnable = new Runnable() {
            @Override
            public void run() {

                        for (int i = 0; i < picPathLists.size(); i++) {
                            if(picPathLists.get(i).getMimeType().contains("jpg")
                                    || picPathLists.get(i).getMimeType().contains("jpeg")){

                                final String path = picPathLists.get(i).getOriginalPath();

                                String fileName = SLFUtils.getCharacterAndNumber()+".jpg";
                                Bitmap bmp = SLFViewUtil.getBitmapFromPath(path);
                                if(bmp!=null){
                                    //裁剪
                                    int rotate = SLFCropUtil.getExifRotation(new File(path));
                                    try {
                                        if (aspect_X <= 0 || aspect_Y <= 0) {
                                            SLFLogUtil.e(TAG,"SLFConstants.CROP_IMAGE_PATH::::"+SLFConstants.CROP_IMAGE_PATH);
                                            SLFViewUtil.savePicture(SLFCropUtil.rotateImage(bmp, rotate)
                                                    , SLFConstants.CROP_IMAGE_PATH, fileName);
                                        } else {
                                            SLFLogUtil.e(TAG,"SLFConstants.CROP_IMAGE_PATH:::else::"+SLFConstants.CROP_IMAGE_PATH);
                                            SLFViewUtil.savePicture(SLFViewUtil.zoomBitmap(SLFCropUtil.rotateImage(bmp, rotate), aspect_X, aspect_Y)
                                                    , SLFConstants.CROP_IMAGE_PATH, fileName);
                                        }
                                    }catch(Exception e){
                                        SLFLogUtil.e(TAG,"picPathList crop error::"+e.toString());
                                    }
                                    picPathLists.get(i).setOriginalPath(SLFConstants.CROP_IMAGE_PATH+fileName);
                                    picPathLists.get(i).setLength(new File(SLFConstants.CROP_IMAGE_PATH+fileName).length());
                                }

                            }else if(picPathLists.get(i).getMimeType().contains("png")){
                                final String path = picPathLists.get(i).getOriginalPath();
                                String fileName = SLFUtils.getCharacterAndNumber()+".png";
                                try {
                                    Bitmap bmp = SLFViewUtil.getBitmapFromPath(path);
                                    if(bmp!=null){
                                        if(aspect_X <= 0 || aspect_Y <= 0){
                                            SLFViewUtil.savePNGPicture(bmp, SLFConstants.CROP_IMAGE_PATH,fileName);
                                        }else{
                                            SLFViewUtil.savePNGPicture(SLFViewUtil.zoomBitmap(bmp,aspect_X,aspect_Y)
                                                    , SLFConstants.CROP_IMAGE_PATH,fileName);
                                        }
                                        picPathLists.get(i).setOriginalPath(SLFConstants.CROP_IMAGE_PATH+fileName);
                                        picPathLists.get(i).setLength(new File(SLFConstants.CROP_IMAGE_PATH+fileName).length());
                                    }
                                } catch (IOException e) {
                                    Log.e(TAG, Log.getStackTraceString(e));
                                }
                            }

                        }

                        if (!picPathLists.isEmpty()) {
                            if(SLFPhotoSelectorUtils.mListenter!=null){
                                setResult(RESULT_OK);
                                runOnUiThread(() -> SLFPhotoSelectorUtils.mListenter.onSelect(picPathLists));
                            }
                            for(int i=0;i<picPathLists.size();i++){
                                setUploadUrl(i,picPathLists);
                                SLFLogUtil.d("yj","uploadpath====confirm:::"+picPathLists.get(i).getUploadPath());
                                SLFLogUtil.d("yj","uploadthumpath====confirm:::"+picPathLists.get(i).getUploadThumPath());
                            }
                            finish();
                        } else {
                            showCenterToast("Please choose a picture!");
                        }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        isCreate = false;
        oldCurrentList.clear();
        oldPickPositions.clear();
        oldCurrentList.addAll(mCurPhotoList);
        oldPickPositions.addAll(mPhotoListAdapter.getPickPositions());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        oldCurrentList = null;
        oldPickPositions = null;
        newCurrentList = null;
    }

    private void requestPermission() {
       SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this,permissionStorage,permissionsRequestResult);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                comeBack();
            }
        });
        photo_title_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                gvPhotoList.smoothScrollToPosition(0);
                selectFile();
            }
        });

        selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SLFLogUtil.d("yj","click selected");
                gotoFeedback();
                finish();
            }
        });

        gvPhotoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                if(position==mCurPhotoList.size()-1){
                    SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this,permissionCamrea,permissionsCamraResult);
                }else {
                    mPhotoListAdapter.setCurrentPosition(position);
                    if (isDirectCrop) {
                        gotoPreview();
                    }
                    if(mPhotoListAdapter.getPicList().size() <=0) {
                        selected_btn.setText(SLFResourceUtils.getString(R.string.slf_feedback_grid_bottom_right_no_selected));
                        selected_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_photo_selector_bottom_done));
                        selected_btn.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
                        slf_preview_text.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
                        selected_btn.setClickable(false);
                        slf_preview_text.setClickable(false);
                    }else{
                            selected_btn.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_grid_bottom_right, mPhotoListAdapter.getPicList().size()));
                            slf_preview_text.setTextColor(SLFResourceUtils.getColor(R.color.white));
                            selected_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg));
                            selected_btn.setTextColor(SLFResourceUtils.getColor(R.color.black));
                            selected_btn.setClickable(true);
                            slf_preview_text.setClickable(true);
                    }
                }

            }
        });



        lvFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mCurPhotoList.clear();
                if (mAllPhotoFolderList.size() > 0) {
                    if (mAllPhotoFolderList.get(position).getPhotoList() != null) {
                        mCurPhotoList.addAll(mAllPhotoFolderList.get(position).getPhotoList());
                    }
                }
                mCurPhotoList.add(getCamraMediaData());
                //mPhotoListAdapter = new SLFPhotoListAdapter(getBaseContext(), mCurPhotoList, isSingle, selectedNum);
                mPhotoListAdapter = new SLFPhotoListAdapter(getBaseContext(), mCurPhotoList, selectedNum);
                gvPhotoList.setAdapter(mPhotoListAdapter);
                tvFileName.setText(mAllPhotoFolderList.get(position).getFolderName());

                lvFileList.setVisibility(View.GONE);
                mPhotoListAdapter.notifyDataSetChanged();
                gvPhotoList.setVisibility(View.VISIBLE);
                selected_btn.setVisibility(View.VISIBLE);
            }
        });
        selected_btn.setClickable(false);
        slf_preview_text.setClickable(false);
    }

    /**添加相机module*/
    private SLFMediaData getCamraMediaData(){
        SLFMediaData carmraMediaData = new SLFMediaData();
        carmraMediaData.setId(-1);
        carmraMediaData.setOriginalPath("");
        carmraMediaData.setMimeType("png");
        carmraMediaData.setDuration(0);
        carmraMediaData.setLength(0);
        return carmraMediaData;
    }


    private void takePhoto() {

//        Intent intent = new Intent(this,SLFTakeToCamra.class);
//        intent.putExtra("insert_album",getIntent().getBooleanExtra("insert_album",false));
//        startActivityForResult(intent,CAMERA_REQUEST);

        try {
            Intent intent = new Intent("android.media.action.VIDEO_CAMERA");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent,CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resumChecked(){
        newCurrentList.clear();
        newCurrentList.addAll(mCurPhotoList);
        if(oldPickPositions.size()==0){
            return;
        }else{
            for(int i=0;i<oldPickPositions.size();i++){
                int selectedpos = Integer.parseInt(oldPickPositions.get(i)) + (newCurrentList.size() - oldCurrentList.size());
                mPhotoListAdapter.setCurrentPosition(selectedpos);
            }
        }
        gvPhotoList.invalidate();
    }

    private void gotoPreview() {
        SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this,permissionStorage,permissionsGotoPerivewResult);
    }


    private void comeBack() {
        if (lvFileList.getVisibility() == View.VISIBLE) {
            lvFileList.setVisibility(View.GONE);
            selected_btn.setVisibility(View.VISIBLE);
            gvPhotoList.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    private void selectFile() {
        if (mAllPhotoFolderList.isEmpty()) {
            return;
        }
        gvPhotoList.setVisibility(View.INVISIBLE);
        lvFileList.setVisibility(View.VISIBLE);
    }


    private void getPhotos() {
        initGetPhotosRunnable();
        singleThreadExecutor =  Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(getPhotoRunable);
    }

    private void gotoFeedback(){
        initConfirmRunable();
        picPathLists = picPathLists = mPhotoListAdapter.getPicList();
        if(picPathLists == null){
            return;
        }

        if (picPathLists.size() > selectedNum) {
            showCenterToast("At most can choose "+ " "+selectedNum+"！");
            return;
        }
        if(picPathLists.size() > 0) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
            singleThreadExecutor.execute(confirmRunnable);
        }else{
            showCenterToast("Please choose a picture!");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            comeBack();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            SLFPhotoGridActivity.this.setResult(RESULT_OK, data);
            finish();
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        }
    }

    //创建回调处理
//创建监听权限的接口对象
    SLFPermissionManager.IPermissionsResult permissionsResumeResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
            if( mCurPhotoList.isEmpty()){
                isEvent = false;
                getPhotos();
            }
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsRequestResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
                isEvent = false;
                getPhotos();
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsGotoPerivewResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
            picPathLists = mPhotoListAdapter.getPicList();



            if(picPathLists == null){
                return;
            }

            if (picPathLists.size() > selectedNum) {
                showCenterToast("Please choose a picture!");
                return;
            }

            if (picPathLists.size() > 0) {
                Intent intent = new Intent(SLFPhotoGridActivity.this, SLFFeedbackPicPreviewActivity.class);
                intent.putExtra("photoPath", picPathLists);
                intent.putExtra("aspect_x", aspect_X);
                intent.putExtra("aspect_y", aspect_Y);
                intent.putExtra("app_color", getColor());
                intent.putExtra("direct_crop", isDirectCrop);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            } else {
                showCenterToast("Please choose a picture!");
            }

        }

        @Override
        public void forbitPermissons() {
//            finish();

            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
            return;
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsCamraResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            takePhoto();
        }

        @Override
        public void forbitPermissons() {
            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
            return;
        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        SLFPermissionManager.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFEventUpdatePhotolist event) {
        if (event.success) {
            // 更新数据
            resumChecked();
        }
    }

    private void setUploadUrl(int position,List<SLFMediaData> list){
        if(SLFCommonUpload.getListInstance().size()==9&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0))!=null&&SLFCommonUpload.getInstance().size()==9){
            if(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle){
                list.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(0));
                list.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(1));
                list.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl);
                list.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl);
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle = false;
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle = false;
            }else if(SLFCommonUpload.getListInstance().size()==9&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle){
                list.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(2));
                list.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(3));
                list.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).uploadUrl);
                list.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).uploadUrl);
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle = false;
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle = false;
            }else if(SLFCommonUpload.getListInstance().size()==9&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle&&SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle){
                list.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(4));
                list.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(5));
                list.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).uploadUrl);
                list.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).uploadUrl);
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle = false;
                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle = false;
            }
        }
    }
}
