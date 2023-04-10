package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.Formatter;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.putrack.putrack.commonapi.PUTClickAgent;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFAgentEvent;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFileListAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFPhotoListAdapter;
import com.sandglass.sandglasslibrary.base.SLFPhotoBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.SLFPhotoFolderInfo;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventCompressVideo;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNoCompressVideo;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventUpdatePhotolist;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCropUtil;
import com.sandglass.sandglasslibrary.utils.SLFPermissionManager;
import com.sandglass.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.sandglass.sandglasslibrary.utils.SLFPhotoTools;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.SLFUtils;
import com.sandglass.sandglasslibrary.utils.SLFViewUtil;
//import com.sandglass.sandglasslibrary.utils.camralistener.SLFCamraContentObserver;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
import com.sandglass.sandglasslibrary.utils.videocompress.SLFVideoSlimmer;
//import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by yangjie
 * describe:选择相册展示页
 * time: 2022/12/6
 *
 * @author yangjie
 */
public class SLFPhotoGridActivity extends SLFPhotoBaseActivity{


    private ImageView ivBack;
    private ImageView ivPhotograph;
    private Button selected_btn;
    private String selectedImgPath;

    private TextView tvFileName;
    private LinearLayout photo_title_center;
    private TextView tvCancel;

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

    /**
     * 定义permissions
     */
    private String[] permissionCamrea = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    /**
     * 定义permissions
     */
    private String[] permissionStorageMax32 = android.os.Build.VERSION.SDK_INT > 29 ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} : new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * android13 权限增加
     */
    private String[] permissionStorage = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU?new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    }:permissionStorageMax32;

    private boolean isCreate;

    ArrayList<SLFMediaData> oldCurrentList = new ArrayList<>();
    ArrayList<SLFMediaData> newCurrentList = new ArrayList<>();
    ArrayList<String> oldPickPositions = new ArrayList<>();
    private static ExecutorService singleThreadExecutor;
    private static Runnable getPhotoRunable;
    private static Runnable confirmRunnable;
    private boolean isEvent;
    private TextView slf_preview_text;
    private SLFEventCompressVideo slfEventCompressVideo = new SLFEventCompressVideo(false, "", "", 0);
    //private SLFCamraReceiver camraReceiver;
    //private SLFCamraContentObserver slfCamraContentObserver;
    private Handler handler = new Handler();
    private SLFMediaData getMediaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SLFLogUtil.sdkd(TAG,"photoGrid onCreate");
        SLFStatusBarColorChange.transparencyBar(this);
        isCreate = true;
        initView();
        requestPermission();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isCreate) {
            isEvent = true;
            getMediaData = intent.getParcelableExtra("mediaData");
            SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::onNewIntent getmediadata::" + getMediaData);
            getPhotos();
        }
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
        tvCancel = findViewById(R.id.photo_id_cancel);

        //findViewById(R.id.slf_iv_arrow_down).setVisibility(View.VISIBLE);

        if (SLFPhotoSelectorUtils.selectType == SLFMediaType.Video) {
            tvFileName.setText("All videos");
        } else {
            tvFileName.setText("All pictures");
        }
        selected_btn.setText(SLFResourceUtils.getString(R.string.slf_feedback_grid_bottom_right_no_selected));
        SLFFontSet.setSLF_RegularFont(getContext(),tvFileName);
        SLFFontSet.setSLF_RegularFont(getContext(),selected_btn);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_preview_text);

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

    private void initGetPhotosRunnable() {
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

    private void initConfirmRunable() {
        confirmRunnable = new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < picPathLists.size(); i++) {
                    if (picPathLists.get(i).getMimeType().contains("jpg")
                            || picPathLists.get(i).getMimeType().contains("jpeg")) {

                        final String path = picPathLists.get(i).getOriginalPath();
                        final String thumPth = picPathLists.get(i).getThumbnailSmallPath();

                        String fileName = SLFUtils.getCharacterAndNumber() + ".jpg";
                        Bitmap bmp = SLFViewUtil.getBitmapFromPath(path);
                        Bitmap thumBmp = SLFViewUtil.getBitmapFromPath(thumPth);
                        File thumbleFile = new File(thumPth);
                        String filethumbName = SLFUtils.getCharacterAndNumber() + "thumb.jpg";
                        if (bmp != null) {
                            //裁剪
                            int rotate = SLFCropUtil.getExifRotation(new File(path));
                            try {
                                SLFViewUtil.compressImage(SLFCropUtil.rotateImage(bmp, rotate), SLFConstants.CROP_IMAGE_PATH, fileName, 200);
                                SLFViewUtil.compressImage(SLFCropUtil.rotateImage(thumBmp, rotate),SLFConstants.CROP_IMAGE_PATH, filethumbName, 50);
                            } catch (Exception e) {
                                SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":jpg jpeg picPathList crop error::" + e.toString());
                            }
                            picPathLists.get(i).setId(System.currentTimeMillis());
                            picPathLists.get(i).setOriginalPath(SLFConstants.CROP_IMAGE_PATH + fileName);
                            picPathLists.get(i).setLength(new File(SLFConstants.CROP_IMAGE_PATH + fileName).length());
                            picPathLists.get(i).setThumbnailSmallPath(SLFConstants.CROP_IMAGE_PATH+filethumbName);
                            picPathLists.get(i).setUploadStatus(SLFConstants.UPLOADIDLE);
                            picPathLists.get(i).setUploadPath(null);
                            picPathLists.get(i).setUploadThumPath(null);
                            picPathLists.get(i).setUploadUrl(null);
                            picPathLists.get(i).setUploadThumurl(null);
                            picPathLists.get(i).setFileName(fileName);
                            picPathLists.get(i).setFileSuccess(false);
                            picPathLists.get(i).setThumbSuccess(false);
                        }

                    } else if (picPathLists.get(i).getMimeType().contains("png")) {
                        final String path = picPathLists.get(i).getOriginalPath();
                        final String thumPth = picPathLists.get(i).getThumbnailSmallPath();
                        String fileName = SLFUtils.getCharacterAndNumber() + ".png";
                        File thumFile = new File(thumPth);
                        picPathLists.get(i).setId(System.currentTimeMillis());
                        picPathLists.get(i).setThumbnailSmallPath(thumPth);
                        Bitmap bmp = SLFViewUtil.getBitmapFromPath(path);
                        Bitmap thumBmp = SLFViewUtil.getBitmapFromPath(thumPth);
                        String filethumbName = SLFUtils.getCharacterAndNumber() + "thumb.png";
                        if (bmp != null) {
                            try {
                                //裁剪
                                int rotate = SLFCropUtil.getExifRotation(new File(path));
                                SLFViewUtil.compressImage(SLFCropUtil.rotateImage(bmp, rotate), SLFConstants.CROP_IMAGE_PATH, fileName, 200);
                                SLFViewUtil.compressImage(SLFCropUtil.rotateImage(thumBmp, rotate),SLFConstants.CROP_IMAGE_PATH, filethumbName, 50);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                                SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":png picPathList crop error::" + e.toString());
                            }
                            picPathLists.get(i).setOriginalPath(SLFConstants.CROP_IMAGE_PATH + fileName);
                            picPathLists.get(i).setLength(new File(SLFConstants.CROP_IMAGE_PATH + fileName).length());
                            picPathLists.get(i).setThumbnailSmallPath(SLFConstants.CROP_IMAGE_PATH+filethumbName);
                            picPathLists.get(i).setUploadStatus(SLFConstants.UPLOADIDLE);
                            picPathLists.get(i).setUploadPath(null);
                            picPathLists.get(i).setUploadThumPath(null);
                            picPathLists.get(i).setUploadUrl(null);
                            picPathLists.get(i).setUploadThumurl(null);
                            picPathLists.get(i).setFileName(fileName);
                            picPathLists.get(i).setFileSuccess(false);
                            picPathLists.get(i).setThumbSuccess(false);
                        }

                    } else if (picPathLists.get(i).getMimeType().contains("video")) {
                        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":video MimeType()---::" + picPathLists.get(i).getMimeType());
                        final String path = picPathLists.get(i).getOriginalPath();
                        final String thumPth = picPathLists.get(i).getThumbnailSmallPath();

                        String fileThumbleName = SLFUtils.getCharacterAndNumber() + "videoThumble.jpg";
                        String fileName = SLFUtils.getCharacterAndNumber() + ".mp4";
                        String newFilePath = SLFConstants.CROP_IMAGE_PATH + fileName;

                        try {
                            Bitmap btm = getVideoThumbnail(path);
                            if (btm != null) {
                                SLFViewUtil.compressImage(btm, SLFConstants.CROP_IMAGE_PATH, fileThumbleName, 300);
                            }
                            picPathLists.get(i).setId(System.currentTimeMillis());
                            picPathLists.get(i).setThumbnailSmallPath(SLFConstants.CROP_IMAGE_PATH + fileThumbleName);
                            picPathLists.get(i).setUploadStatus(SLFConstants.UPLOADIDLE);
                            picPathLists.get(i).setUploadPath(null);
                            picPathLists.get(i).setUploadThumPath(null);
                            picPathLists.get(i).setUploadUrl(null);
                            picPathLists.get(i).setUploadThumurl(null);
                            picPathLists.get(i).setFileSuccess(false);
                            picPathLists.get(i).setThumbSuccess(false);
                            SLFMediaData slfMediaData = picPathLists.get(i);
                            compressVideo(path, newFilePath, fileName, picPathLists.get(i).getId());
                        } catch (Exception e) {
                            Log.e(TAG, Log.getStackTraceString(e));
                            SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":video picPathList compressvideo error::" + e.toString());
                        }
                    }

                }

                if (!picPathLists.isEmpty()) {
                    if (SLFPhotoSelectorUtils.mListenter != null) {
                        setResult(RESULT_OK);
                        runOnUiThread(() -> SLFPhotoSelectorUtils.mListenter.onSelect(picPathLists));
                    }
                    finish();
                } else {
                    showCenterToast("Please choose a picture!");
                }
            }
        };
    }


    /**
     * 获取多媒体视频缩略图
     */
    private Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":getVideoThumbnail::bitmap success" );
        return bitmap;
    }

    /**
     * 压缩视频
     */
    private synchronized void compressVideo(String path, String newFilePath, String filename, long id) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String originPath = newFilePath;
        String oldFilePath = path;
        String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        File f = new File(path);
        long fileSize = f.length();
        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":video_width==before::" + width);
        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":video:" + height);
        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":fileSize before:" + Formatter.formatFileSize(getContext(), fileSize));
        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":fileSize before old:" + fileSize);
        SLFEventCompressVideo slfEventCompressVideo = new SLFEventCompressVideo(true, newFilePath, filename, id);
        if (Integer.parseInt(width) > Integer.parseInt(height)) {
            String temp = height;
            String temp2 = width;
            width = temp;
            height = temp2;
        }
        try {
            SLFVideoSlimmer.convertVideo(path, newFilePath, Integer.parseInt(width), Integer.parseInt(height), (Integer.parseInt(width)) * (Integer.parseInt(height)) * 2, new SLFVideoSlimmer.SLFProgressListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onFinish(boolean result) {

                    if (result) {

                        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::video compress success");


                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(newFilePath);
                        String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                        String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                        String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

                        File f = new File(newFilePath);
                        long fileSize = f.length();

                        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::vido_width after:" + width);
                        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::vido_height after:" + height);
                        SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::fileSize after:" + Formatter.formatFileSize(getContext(), fileSize));

                        EventBus.getDefault().post(new SLFEventCompressVideo(true, path, filename, id));
                    } else {
                        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":video compress faile");
                        String fileName = path.substring(path.lastIndexOf("/") + 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new SLFEventNoCompressVideo(path, fileName, id));
                            }
                        }, 1000);

                    }

                }


                @Override
                public void onProgress(float percent) {
                    //SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":compress progress:::" + String.valueOf(percent) + "%");
                }
            });


        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
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
//        try {
//            getContentResolver().unregisterContentObserver(slfCamraContentObserver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(camraReceiver!=null){
//            unregisterReceiver(camraReceiver);
//        }
    }

    private void requestPermission() {
        SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this, permissionStorage, permissionsRequestResult);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                comeBack();
            }
        });
//        photo_title_center.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                gvPhotoList.smoothScrollToPosition(0);
//                selectFile();
//            }
//        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SLFLogUtil.sdkd(TAG, "ActivityName:"+this.getClass().getSimpleName()+":click selected");
                //打点上传资源
                PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Leave_UploadResource,null);
                gotoFeedback();
                finish();
            }
        });

        gvPhotoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

                if (position == mCurPhotoList.size() - 1) {
                    if (mPhotoListAdapter.getPicList().size()+1 <= selectedNum ) {
                        SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this, permissionCamrea, permissionsCamraResult);
                    } else {
                        showCenterToast(SLFResourceUtils.getString(R.string.slf_only_select_3));
                    }
                } else {
                    mPhotoListAdapter.setCurrentPosition(position);
                    if (isDirectCrop) {
                        gotoPreview();
                    }
                    if (mPhotoListAdapter.getPicList().size() <= 0) {
                        selected_btn.setText(SLFResourceUtils.getString(R.string.slf_feedback_grid_bottom_right_no_selected));
                        selected_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_photo_selector_bottom_done));
                        selected_btn.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
                        slf_preview_text.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
                        selected_btn.setClickable(false);
                        slf_preview_text.setClickable(false);
                    } else {
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

    /**
     * 添加相机module
     */
    private SLFMediaData getCamraMediaData() {
        SLFMediaData carmraMediaData = new SLFMediaData();
        carmraMediaData.setId(-1);
        carmraMediaData.setOriginalPath("");
        carmraMediaData.setMimeType("png");
        carmraMediaData.setDuration(0);
        carmraMediaData.setLength(0);
        return carmraMediaData;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    private void takePhoto() {

        Intent intent = new Intent(this, SLFTakePhotoActivity.class);
        intent.putExtra("insert_album", true);
        startActivityForResult(intent, CAMERA_REQUEST);

//        try {
//           Intent intent = new Intent("android.media.action.VIDEO_CAMERA");
////              Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////                    | Intent.FLAG_ACTIVITY_NEW_TASK
////                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            startActivityForResult(intent,CAMERA_REQUEST);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void resumChecked() {
        newCurrentList.clear();
        newCurrentList.addAll(mCurPhotoList);
        if (getMediaData != null) {
            for (int i = 0; i < newCurrentList.size(); i++) {
                if (getFileName(getMediaData.getOriginalPath()).equals(getFileName(newCurrentList.get(i).getOriginalPath()))) {
                    mPhotoListAdapter.setCurrentPosition(i);
                    mPhotoListAdapter.notifyDataSetChanged();
                    selected_btn.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_grid_bottom_right, mPhotoListAdapter.getPicList().size()));
                    slf_preview_text.setTextColor(SLFResourceUtils.getColor(R.color.white));
                    selected_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg));
                    selected_btn.setTextColor(SLFResourceUtils.getColor(R.color.black));
                    selected_btn.setClickable(true);
                    slf_preview_text.setClickable(true);
                }
            }
        }
        if (oldPickPositions.size() == 0) {
            return;
        } else {
            for (int i = 0; i < oldPickPositions.size(); i++) {
                int selectedpos = Integer.parseInt(oldPickPositions.get(i)) + (newCurrentList.size() - oldCurrentList.size());
                mPhotoListAdapter.setCurrentPosition(selectedpos);
            }

        }

        gvPhotoList.invalidate();


    }

    public String getFileName(String pathandname) {
        if (!TextUtils.isEmpty(pathandname)) {
            int start = pathandname.lastIndexOf("/");
            int end = pathandname.lastIndexOf(".");
            if (start != -1 && end != -1) {
                return pathandname.substring(start + 1, end);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private void gotoPreview() {
        SLFPermissionManager.getInstance().chekPermissions(SLFPhotoGridActivity.this, permissionStorage, permissionsGotoPerivewResult);
    }


    private void comeBack() {

        if (lvFileList.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            gvPhotoList.smoothScrollToPosition(0);
            selectFile();
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
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(getPhotoRunable);
    }

    private void gotoFeedback() {
        initConfirmRunable();
        picPathLists = picPathLists = mPhotoListAdapter.getPicList();
        if (picPathLists == null) {
            return;
        }

        if (picPathLists.size() > selectedNum) {
            showCenterToast("At most can choose " + " " + selectedNum + "！");
            return;
        }
        if (picPathLists.size() > 0) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
            singleThreadExecutor.execute(confirmRunnable);
        } else {
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

    SLFPermissionManager.IPermissionsResult permissionsRequestResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
            isEvent = false;
            getPhotos();
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::preview permission pass goto preview page");
        }

        @Override
        public void forbitPermissons() {
//            finish();
//            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsGotoPerivewResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
            picPathLists = mPhotoListAdapter.getPicList();


            if (picPathLists == null) {
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
                intent.putExtra("from", "photogrid");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::preview permission pass goto preview page");
            } else {
                SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::no choose pictures");
                showCenterToast("Please choose a picture!");
            }

        }

        @Override
        public void forbitPermissons() {
//            finish();

//            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::preview permission not pass goto preview page");
            return;
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsCamraResult = new SLFPermissionManager.IPermissionsResult() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void passPermissons() {
                takePhoto();
                SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::camera permission not pass goto take photo");
        }

        @Override
        public void forbitPermissons() {
//            Toast.makeText(SLFPhotoGridActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::camera permission not pass goto take photo");
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
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::onevent update photolist data");
            resumChecked();
        }
    }
}
