package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonui.SLFRecordButton;
import com.wyze.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.utils.SLFFileUtils;
import com.wyze.sandglasslibrary.utils.SLFPermissionManager;
import com.wyze.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S110")
public class SLFTakePhotoActivity extends SLFBaseActivity {

    private ProcessCameraProvider mCameraProvider;
    private Preview mPreview;
    private PreviewView mPreviewView;
    private Camera mCamera;
    private ImageCapture mImageCapture;
    private ImageAnalysis mImageAnalysis;
    private VideoCapture mVideoCapture;
    private SLFRecordButton takePhotoBtn;
    private CheckBox cbFlash;
    private CheckBox cbFront;

    private Size targetSize;
    private int videoBitRate;

    private boolean isBack = true;
    private boolean isAnalyzing;
    private boolean isVideoMode;
    private boolean isRecording;

    //private final SLFMultiFormatReader multiFormatReader = new MultiFormatReader();

    private long startTime;
    private boolean isTooShot;
    private static final int REQUEST_CODE = 101;
    private boolean isInsertAlbum;

    /**
     * 定义permissions
     */
    private String[] permissionStorage = android.os.Build.VERSION.SDK_INT > 29 ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} : new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] permissionCamrea = new String[]{Manifest.permission.CAMERA};
    private String[] permissionMic = new String[]{Manifest.permission.RECORD_AUDIO};
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slf_activity_camera);
        mPreviewView = findViewById(R.id.previewView);

        takePhotoBtn = findViewById(R.id.iv_take_photo);
        cbFlash = findViewById(R.id.cb_switch_flash);
        cbFront = findViewById(R.id.cb_switch_front);
        isInsertAlbum = getIntent().getBooleanExtra("insert_album",false);

        requestPermission();

        switch(SLFPhotoSelectorUtils.quality) {
            case Low:
                targetSize = new Size(720,1280);
                videoBitRate = 5 * 1024 * 1024;
                break;
            case High:
                targetSize = new Size(1440,2160);
                videoBitRate = 8 * 1024 * 1024;
                break;
            case Medium:
            default:
                targetSize = new Size(1080,1920);
                videoBitRate = 6 * 1024 * 1024;
                break;
        }

        initListener();

    }

    private void requestPermission() {
        SLFPermissionManager.getInstance().chekPermissions(SLFTakePhotoActivity.this, permissionCamrea, permissionsCamraResult);
        if(isInsertAlbum){
            SLFPermissionManager.getInstance().chekPermissions(SLFTakePhotoActivity.this, permissionStorage, permissionsStroageResult);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener(){

        mPreviewView.setOnTouchListener((v, event) -> {
            FocusMeteringAction action = new FocusMeteringAction.Builder(
                    mPreviewView.getMeteringPointFactory()
                            .createPoint(event.getX(), event.getY())).build();
            try {
                showTapView((int) event.getX(), (int) event.getY());
                mCamera.getCameraControl().startFocusAndMetering(action);
            } catch (Exception e) {
                SLFLogUtil.e("CameraX", "Error focus camera");
            }
            return false;
        });

        takePhotoBtn.setRecordButtonListener(new SLFRecordButton.SLFRecordButtonListener() {
            @Override
            public void onClick() {
                if (SLFPhotoSelectorUtils.selectType == SLFMediaType.Video) {
                    showCenterToast("You can only shoot videos !");
                    return;
                }
                takenPictureInternal(true);
            }

            @Override
            public void onLongClick() {
                if (SLFPhotoSelectorUtils.selectType == SLFMediaType.Image) {
                    showCenterToast("You can only take pictures !");
                    return;
                }
                SLFPermissionManager.getInstance().chekPermissions(SLFTakePhotoActivity.this, permissionMic, permissionsMicrphoneResult);
            }

            @Override
            public void onLongClickFinish(int result) {
                isTooShot = result == SLFRecordButton.RECORD_SHORT;
                stopVideoMode();
                toggleRecordingStatus();
            }
        });


        cbFlash.setOnCheckedChangeListener((buttonView, isChecked) -> mCamera.getCameraControl().enableTorch(isChecked));

        cbFront.setOnCheckedChangeListener((buttonView, isChecked) -> onChangeGo(!isBack));
    }

    public void onChangeGo(boolean isBack) {
        if (mCameraProvider != null) {
            this.isBack = isBack;
            bindPreview(mCameraProvider);
            if (mImageAnalysis != null) {
                mImageAnalysis.clearAnalyzer();
            }
        }
    }


    @SuppressLint("RestrictedApi")
    public void onAnalyzeGo(View view) {
        if (mImageAnalysis == null) {
            return;
        }

        if (!isAnalyzing) {
            mImageAnalysis.setAnalyzer(CameraXExecutors.mainThreadExecutor(), image -> {
//                analyzeQRCode(image)
            });
        } else {
            mImageAnalysis.clearAnalyzer();
        }
        isAnalyzing = !isAnalyzing;
//        binding.qrCodeZone.setVisibility(isAnalyzing ? View.VISIBLE : View.GONE)
    }


    private void setupCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                mCameraProvider = cameraProviderFuture.get();
                bindPreview(mCameraProvider);
            } catch (Exception e) {
                //
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        bindPreview(cameraProvider, false);
    }

    @SuppressLint("RestrictedApi")
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider,boolean isVideo) {
        Preview.Builder previewBuilder = new Preview.Builder();
        ImageCapture.Builder captureBuilder = new ImageCapture.Builder();
        captureBuilder.setTargetResolution(targetSize);
//                .setTargetRotation(mPreviewView.getDisplay().getRotation())
        CameraSelector cameraSelector = isBack ? CameraSelector.DEFAULT_BACK_CAMERA
                : CameraSelector.DEFAULT_FRONT_CAMERA;

        mImageAnalysis = new ImageAnalysis.Builder()
//                .setTargetRotation(mPreviewView.getDisplay().getRotation())
                .setTargetResolution(targetSize)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        mVideoCapture = new VideoCapture.Builder()
//                .setTargetRotation(mPreviewView.getDisplay().getRotation())
                .setTargetResolution(targetSize)
                .setVideoFrameRate(24)
                .setBitRate(videoBitRate)
                .build();

        mPreview = previewBuilder.build();

        mImageCapture =  captureBuilder.build();

        cameraProvider.unbindAll();
        if (isVideo) {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mVideoCapture);
        } else {
            mCamera = cameraProvider.bindToLifecycle(this, cameraSelector,
                    mPreview, mImageCapture, mImageAnalysis);
        }
        mPreview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
    }

    private void showTapView(int x, int y) {
        PopupWindow popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.slf_flash_hes_circle_up);
        popupWindow.setContentView(imageView);
        popupWindow.showAsDropDown(mPreviewView, x, y);
        mPreviewView.postDelayed(popupWindow::dismiss, 600);
    }

    @SuppressLint("RestrictedApi")
    private void takenPictureInternal(boolean isExternal) {

        String filePath = "";
        String name =  SLFUtils.getCharacterAndNumber();
        File file = new File(SLFConstants.CROP_IMAGE_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        filePath = SLFConstants.CROP_IMAGE_PATH + name + ".jpg";
        File saveFile = new File(filePath);

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(saveFile)
                        .build();

        if (mImageCapture != null) {
            mImageCapture.takePicture(outputFileOptions, CameraXExecutors.mainThreadExecutor(),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            if(isInsertAlbum){
                                SLFLogUtil.d("yj","isinsertalbum");
                                SLFFileUtils.insertImageToGalley(saveFile,"slf");
                            }

                            Intent intent = new Intent();
                            SLFMediaData mediaData = new SLFMediaData();
                            mediaData.setId(System.currentTimeMillis());
                            mediaData.setOriginalPath(saveFile.getPath());
                            mediaData.setMimeType("image/jpg");
                            mediaData.setLength(saveFile.length());

                            //if (SLFPhotoSelectorUtils.isOnlyCamera) {
                                ArrayList<SLFMediaData> picPathLists = new ArrayList<>();
                                picPathLists.add(mediaData);
                                intent.putParcelableArrayListExtra("photoPath", picPathLists);
                                intent.putExtra("aspect_x", getIntent().getIntExtra("aspect_x", 0));
                                intent.putExtra("aspect_y", getIntent().getIntExtra("aspect_y", 0));
                                intent.putExtra("app_color", getIntent().getIntExtra("app_color", SLFResourceUtils.getColor(R.color.slf_theme_color)));
                                intent.putExtra("from","takephoto");
                                intent.putExtra("take_photo", mediaData);
                                intent.setClass(getActivity(), SLFFeedbackPicPreviewActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);
//                            } else {
//                                intent.putExtra("take_photo", mediaData);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            SLFLogUtil.d("Camera", "onError:" + exception.getMessage());
                        }
                    });
        }
    }

    private void startVideoMode() {
        isVideoMode = true;
        bindPreview(mCameraProvider, isVideoMode);
    }

    private void stopVideoMode() {
        isVideoMode = false;
        bindPreview(mCameraProvider, isVideoMode);
    }

    @SuppressLint({"RestrictedApi", "MissingPermission"})
    private void recordVideo() {

        String name = SLFUtils.getCharacterAndNumber();
        String filePath = SLFConstants.CROP_IMAGE_PATH + name + ".mp4";
        File file = new File(SLFConstants.CROP_IMAGE_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        File saveFile = new File(filePath);

        //创建文件
        VideoCapture.OutputFileOptions videoOptions = new VideoCapture.OutputFileOptions.Builder(saveFile)
                .build();

        try {
            mVideoCapture.startRecording(videoOptions,
                    CameraXExecutors.mainThreadExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            if (isTooShot) {
                                showCenterToast("Video time is too short !");
                                return;
                            }
                            long duration = System.currentTimeMillis() - startTime;
                            if(isInsertAlbum){
                                SLFFileUtils.insertVideoToGalley(saveFile,"slf");
                            }

                            Intent intent = new Intent();
                            SLFMediaData mediaData = new SLFMediaData();
                            mediaData.setId(System.currentTimeMillis());
                            mediaData.setOriginalPath(saveFile.getPath());
                            mediaData.setMimeType("video/mp4");
                            mediaData.setDuration(duration);
                            mediaData.setLength(saveFile.length());

//                            if (SLFPhotoSelectorUtils.isOnlyCamera) {
                                ArrayList<SLFMediaData> picPathLists = new ArrayList<>();
                                picPathLists.add(mediaData);
                                intent.putParcelableArrayListExtra("photoPath", picPathLists);
                                intent.putExtra("aspect_x", getIntent().getIntExtra("aspect_x", 0));
                                intent.putExtra("aspect_y", getIntent().getIntExtra("aspect_y", 0));
                                intent.putExtra("app_color", getIntent().getIntExtra("app_color", SLFResourceUtils.getColor(R.color.slf_theme_color)));
                                intent.putExtra("form","takephoto");
                                intent.putExtra("take_photo", mediaData);
                                intent.setClass(getActivity(), SLFFeedbackPicPreviewActivity.class);
                                startActivityForResult(intent, REQUEST_CODE);
//                            } else {
//                                intent.putExtra("take_photo", mediaData);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
                          }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message,
                                            @Nullable Throwable cause) {
                            SLFLogUtil.e("videoCaptureError", message);
                        }
                    }
            );
        } catch (Exception e) {
            SLFLogUtil.e("Camera", "Record video error:"+e.getMessage());
        }

    }

    @SuppressLint("RestrictedApi")
    private void toggleRecordingStatus() {
        if (!isVideoMode) return;

        isRecording = !isRecording;

        // Stop recording when toggle to false.
        if (!isRecording && mVideoCapture != null) {
            mVideoCapture.stopRecording();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    SLFPermissionManager.IPermissionsResult permissionsCamraResult = new SLFPermissionManager.IPermissionsResult() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void passPermissons() {
            setupCamera(mPreviewView);
        }

        @Override
        public void forbitPermissons() {
            finish();
            showCenterToast("No permission to take photos");
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsStroageResult = new SLFPermissionManager.IPermissionsResult() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void passPermissons() {

        }

        @Override
        public void forbitPermissons() {
            showCenterToast("No permission to save photos");
            finish();
        }
    };

    SLFPermissionManager.IPermissionsResult permissionsMicrphoneResult = new SLFPermissionManager.IPermissionsResult() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void passPermissons() {
            startTime = System.currentTimeMillis();
            startVideoMode();
            recordVideo();
        }

        @Override
        public void forbitPermissons() {
            showCenterToast("You can only take pictures !");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        SLFPermissionManager.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            SLFLogUtil.d("yj","takephoto finish");
            finish();
        }
    }

}
