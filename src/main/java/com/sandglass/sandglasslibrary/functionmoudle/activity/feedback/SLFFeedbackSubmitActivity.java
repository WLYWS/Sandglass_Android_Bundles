package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.punet.punetwork.interf.UploadProgressListener;
import com.punet.punetwork.net.PUNApiContant;
import com.punet.punetwork.net.PUNHttpRequestCallback;
import com.punet.punetwork.net.PUNHttpRequestConstants;
import com.punet.punetwork.net.PUNHttpUtils;
import com.putrack.putrack.commonapi.PUTClickAgent;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFAgentEvent;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFPageAgentEvent;
import com.sandglass.sandglasslibrary.bean.SLFSPContant;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.interf.SLFCompressProgress;
import com.sandglass.sandglasslibrary.moudle.SLFUserDeviceSaved;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventCompressVideoProgress;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLeaveMsgBean;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLogAttrBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoriesResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryCommonBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryDetailBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCreateFeedbackRepsonseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFProlemDataBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUploadFileReponseBean;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.commonapi.SLFCommonUpload;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFAndPhotoAdapter;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.commonui.SLFCancelOrOkDialog;
import com.sandglass.sandglasslibrary.commonui.SLFScrollView;
import com.sandglass.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.sandglass.sandglasslibrary.interf.SLFUploadCompleteCallback;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventCompressVideo;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNetWorkChange;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserDeviceListResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFillagelWordResponseBean;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNoCompressVideo;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFEditTextScrollListener;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFCompressUtil;
import com.sandglass.sandglasslibrary.utils.SLFFastClickUtils;
import com.sandglass.sandglasslibrary.utils.SLFPermissionManager;
import com.sandglass.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.sandglass.sandglasslibrary.utils.SLFRegular;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.SLFViewUtil;
import com.sandglass.sandglasslibrary.utils.keyboard.SLFSoftKeyBoardListener;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
import com.sandglass.sandglasslibrary.utils.manager.SLFCacheToFileManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by yangjie
 * describe:反馈页的activity实现
 * time: 2022/12/6
 *
 * @author yangjie
 */
public class SLFFeedbackSubmitActivity<T> extends SLFBaseActivity implements View.OnClickListener, TextWatcher, SLFBottomDialog.OnSeletedTypeListener, PUNHttpRequestCallback<T>, SLFCompressProgress {
    /**
     * scollrview控件
     */
    private SLFScrollView slfScrollView;
    /**
     * 三级菜单标题
     */
    private TextView selector_service_title;
    private TextView selector_problem_title;
    private TextView selector_problem_overview_title;
    private TextView problem_description_title;
    private TextView selector_email_title;
    /**
     * service下拉控件
     */
    private TextView slfServiceSpinner;
    /**
     * problem下拉控件
     */
    private TextView slfProblemSpinner;
    /**
     * problem overview下拉控件
     */
    private TextView slfProblemOverviewSpinner;
    /**
     * problem布局
     */
    private LinearLayout slfProblemLinear;
    /**
     * problem overview布局
     */
    private LinearLayout slfProblemOverviewLinear;
    /**
     * email编辑框
     */
    private EditText slfEmailEdit;
    /**
     * email错误提示
     */
    private TextView slfEmailError;
    /**
     * send log选择框
     */
    private CheckBox slfSendLogCheck;
    /**
     * submit feedback按钮
     */
    private Button slfSumbmit;
    /**
     * 标题栏标题文本
     */
    private TextView slfTitle;
    /**
     * 状态栏返回按钮
     */
    private ImageView slfBack;
    /**
     * 问题编辑框
     */
    private EditText slfEditProblem;
    /**
     * 问题编辑框字数统计控件
     */
    private TextView slfFontCount;
    /**
     * 显示添加附件的控件
     */
    private GridView slfPhotoSelector;
    /**
     * 图片路径集合
     */
    private ArrayList<SLFMediaData> picPathLists;
    /**
     * 多媒体数据集合
     */
    private List<SLFMediaData> slfMediaDataList = new ArrayList<>();
    /**
     * 多媒体数据moudle
     */
    private SLFMediaData slfMediaData;
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
    /**
     * 展示缩略图的adapter
     */
    private SLFAndPhotoAdapter slfaddAttachAdapter;
    /**
     * 输入问题描述框记录的字符
     */
    private CharSequence slfProblemWordNum = new CharSequence() {
        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int i) {
            return 0;
        }

        @NonNull
        @Override
        public CharSequence subSequence(int i, int i1) {
            return null;
        }
    };
    private CharSequence slfEmailWordNum;
    /**
     * 获取email edit的高度
     */
    private int slfEmailHeight;
    /**
     * 键盘高度
     */
    private int slfKeyBoardHeight;
    /**
     * 获取email error的高度
     */
    private int slfEmailErrorHeight;
    /**
     * 弹出底部的dialog
     */
    private SLFBottomDialog bottomDialog;
    /**
     * 获取到dialog选中的type
     */
    private String typeSelected;
    /**
     * 无网页
     */
    private LinearLayout slf_submit_page_no_network_linear;
    /**
     * 无网页title
     */
    private TextView slf_submit_page_no_network_title;
    /**
     * 无网页内容
     */
    private TextView slf_submit_page_no_network_describe;
    /**
     * 无网页图片
     */
    private ImageView slf_submit_page_no_item_img;
    /**
     * try agaiin按钮
     */
    private Button slf_submit_page_try_again;

    private TextView slf_checkbox_text;
    private int service_checkedPosition = -1;
    private int problem_checkedPosition = -1;
    private int problem_overview_checkedPosition = -1;

    boolean serviceType = false;
    boolean problemType = false;
    boolean problemOverviewType = false;
    boolean type = false;
    boolean problemEdit = false;
    boolean emailEdit = false;
    boolean hasUploadingFile = false;

    private List<Object> slfServiceTypes = new ArrayList<>();
    private List<SLFCategoryDetailBean> slfProblemTypes = new ArrayList<>();
    private List<SLFCategoryCommonBean> slfProblemOverviewTypes = new ArrayList<>();
    private Map<Long, List<SLFCategoryDetailBean>> slfServiceMap = new HashMap<>();
    private Map<Long, List<SLFCategoryCommonBean>> slfProblemMap = new HashMap<>();
    private Map<Long, List<SLFCategoryBean>> slfServiceTitleMap = new HashMap<>();


    private SLFCategoryBean slfServiceType;
    private SLFCategoryDetailBean slfProblemType;
    private SLFCategoryCommonBean slfProblemOverviewType;
    private String oldServiceType = "";
    private String oldProblemType = "";

    private Runnable submitLogRunnable;

    private ExecutorService singleThreadExecutor;
    private ExecutorService singleUploadVideoExecutor;

    private ExecutorService singleUploadProgress;

    private boolean isSubmit = false;

    private SLFCategoriesResponseBean slfCategoriesResponseBean;


    private boolean imageSuccessed;

    private String appLogFileName="appLog.zip";
    private Drawable mClearDrawable;
    private Drawable drawableRight;
    private LinearLayout slf_common_loading_linear_submit;
    private TextView slf_common_loading_text_submit;
    private int seletedTypes;
    private Long seletedServiceType = -1L;
    private Long seletedProbleType = -1L;
    private Long seletedProblemOverviewType = -1L;
    private String seleteddeviceMoudle;
    private boolean isSendLogsuccess = false;
    private boolean isCallbackAppLog = false;
    private boolean isCallbackFirmwareLog = false;
    private static final String CACHE_FILE_PATH = SLFConstants.rootPath+"/updatetxt/feedback_category.txt";
    private SLFCacheToFileManager <SLFCategoriesResponseBean> cacheManager;
    private String categoryText;
    private String subCategoryText;
    private String serviceTypeText;
    private boolean isAllDataCache = false;
    private boolean isResolveUpload;
    private boolean isResolveDevicelist;
    private boolean isResolveAllData;

    private String from = "";

    private String emailStr = "";

    private Map<Long,Long> progressMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_photo_selector);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        cacheManager = new SLFCacheToFileManager(SLFCategoriesResponseBean.class);
        from = getIntent().getStringExtra(SLFConstants.FROM_FAQ);
        initTitle();
        initView();
        checkNetWork();
        initPhontoSelector();
        SLFApi.getInstance(this).setCompressProgress(this);
    }

    private void checkNetWork() {
        if (SLFCommonUtils.isNetworkAvailable(this)) {
            slfScrollView.setVisibility(View.VISIBLE);
            slfSumbmit.setVisibility(View.VISIBLE);
            slf_submit_page_no_network_linear.setVisibility(View.GONE);
            requestUploadUrls();
            requestUserDeviceList();
            SLFCategoriesResponseBean sLFCategoriesResponseBean = cacheManager.readObj(CACHE_FILE_PATH);
            showLoading();
            if (sLFCategoriesResponseBean!=null){
                if (SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY_CACHE,0)!=SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY,-1)){
                    requestAllData();
                    isAllDataCache = false;
                }else {
                    isAllDataCache = true;
                    showContent(sLFCategoriesResponseBean);
                }
            }else {
                isAllDataCache = false;
                requestAllData();
            }

        } else {
            slf_submit_page_no_network_linear.setVisibility(View.VISIBLE);
            slfScrollView.setVisibility(View.GONE);
            slfSumbmit.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        //SLFLogUtil.sdkd(TAG,"feedbacksubmit initView");
        slfScrollView = findViewById(R.id.slf_feed_back_scroll_view);
        selector_service_title = findViewById(R.id.selector_service_title);
        selector_problem_title = findViewById(R.id.selector_problem_title);
        selector_problem_overview_title = findViewById(R.id.selector_problem_overview_title);
        problem_description_title = findViewById(R.id.problem_description_title);
        selector_email_title = findViewById(R.id.selector_email_title);
        slfProblemLinear = findViewById(R.id.problem_type_seletor_linear);
        slfProblemOverviewLinear = findViewById(R.id.problem_overview_seletor_linear);
        slfEditProblem = findViewById(R.id.question_content);
        slfServiceSpinner = findViewById(R.id.spinner_service);
        slfProblemSpinner = findViewById(R.id.spinner_problem);
        slfProblemOverviewSpinner = findViewById(R.id.spinner_problem_overview);
        slfPhotoSelector = findViewById(R.id.slf_gv_add_attach);
        slfFontCount = findViewById(R.id.slf_tv_opinion_textnum);
        slfSendLogCheck = findViewById(R.id.slf_upload_log);
        slfSumbmit = findViewById(R.id.slf_submit_feedback);
        slfEmailEdit = findViewById(R.id.slf_email_eidt);
        slfEmailError = findViewById(R.id.slf_email_error);
        slf_checkbox_text = findViewById(R.id.slf_checkbox_text);
        //无网页控件
        slf_submit_page_no_network_linear = findViewById(R.id.slf_submit_page_no_network_linear);
        slf_submit_page_no_network_title = findViewById(R.id.slf_submit_page_no_network_title);
        slf_submit_page_no_network_describe = findViewById(R.id.slf_submit_page_no_network_describe);
        slf_submit_page_no_item_img = findViewById(R.id.slf_submit_page_no_item_img);
        slf_submit_page_try_again = findViewById(R.id.slf_submit_page_try_again);
        //loading
        slf_common_loading_linear_submit = findViewById(R.id.slf_common_loading_linear_submit);
        slf_common_loading_text_submit = findViewById(R.id.slf_common_loading_tv_content_submit);
        SLFFontSet.setSLF_RegularFont(getContext(), slf_common_loading_text_submit);

        if (SLFUserCenter.userInfoBean!=null&&SLFUserCenter.userInfoBean.getData()!=null&&SLFUserCenter.userInfoBean.getData().getEmail() != null) {
            emailStr = SLFUserCenter.userInfoBean.getData().getEmail();
            slfEmailEdit.setText(emailStr);
        }else{
            slfEmailEdit.setText(emailStr);
        }

        slfServiceSpinner.setOnClickListener(this);
        slfProblemSpinner.setOnClickListener(this);
        slf_submit_page_try_again.setOnClickListener(this);
        slfProblemOverviewSpinner.setOnClickListener(this);
        slfEditProblem.setOnTouchListener(new SLFEditTextScrollListener(slfEditProblem));
        slfEditProblem.addTextChangedListener(this);
        initDrawableRight(slfEmailEdit);
        setDrawableRightVisble(false, slfEmailEdit);
        slfEmailEdit.addTextChangedListener(editWatcher);
        //slfEmailEdit.setOnKeyListener(emailKeyLister);
        slfEmailEdit.setOnTouchListener(emailTouchListener);
        slf_common_loading_linear_submit.setVisibility(View.GONE);
        changeViewFoucs();
        slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
        SLFFontSet.setSLF_RegularFont(getContext(), selector_service_title);
        SLFFontSet.setSLF_RegularFont(getContext(), selector_problem_title);
        SLFFontSet.setSLF_RegularFont(getContext(), selector_problem_overview_title);
        SLFFontSet.setSLF_RegularFont(getContext(), slfServiceSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(), slfProblemSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(), slfProblemOverviewSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(), problem_description_title);
        SLFFontSet.setSLF_RegularFont(getContext(), slfFontCount);
        SLFFontSet.setSLF_RegularFont(getContext(), slfEditProblem);
        SLFFontSet.setSLF_RegularFont(getContext(), selector_email_title);
        SLFFontSet.setSLF_RegularFont(getContext(), slfEmailError);
        SLFFontSet.setSLF_RegularFont(getContext(), slfEmailEdit);
        SLFFontSet.setSLF_RegularFont(getContext(), slfSendLogCheck);
        SLFFontSet.setSLF_MediumFontt(getContext(), slfSumbmit);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_checkbox_text);
        setSubmitBtnCanClick(canSubmit());
        changeTextAndImg(slfServiceSpinner);
        changeTextAndImg(slfProblemSpinner);
        changeTextAndImg(slfProblemOverviewSpinner);
        setListenerFotEditTexts();
        slfEmailHeight = SLFViewUtil.getHeight(slfEmailEdit);
        slfEmailErrorHeight = SLFViewUtil.getHeight(slfEmailError);
        slfSendLogCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //打点勾选sendlog
                    PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_SelectSendLog,null);
                }else{
                    //打点取消sendlog
                    PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_CancelSendLog,null);
                }
            }
        });
        slfEmailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": EmailEdit onFocus");
                    setDrawableRightVisble(slfEmailEdit.getText().length() > 0, slfEmailEdit);
                } else {
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": EmailEdit no Focus");
                    setDrawableRightVisble(false, slfEmailEdit);
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        //SLFLogUtil.sdkd(TAG,"feedbacksubmit initTitle");
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_title_content));
        SLFFontSet.setSLF_RegularFont(getContext(), slfTitle);
        slfBack.setOnClickListener(this);
    }


    /**
     * 初始化选择图片控件相关
     */
    private void initPhontoSelector() {
        //SLFLogUtil.sdkd(TAG,"feedbacksubmit initPhontoSelector");
        picPathLists = new ArrayList<>();
        slfMediaData = new SLFMediaData();
        slfMediaData.setId(SystemClock.elapsedRealtime());
        slfMediaDataList.add(this.slfMediaData);
        /**初始化显示选中图片缩略图的adapter*/
        final SLFAndPhotoAdapter slfaddAttachAdapter = new SLFAndPhotoAdapter(getContext(), slfMediaDataList);
        this.slfaddAttachAdapter = slfaddAttachAdapter;
        slfPhotoSelector.setAdapter(slfaddAttachAdapter);

        slfPhotoSelector.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("yj","position:::click    :"+position+"::::id===::"+id);
            //打点查看资源文件
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_FeedbackDetail_EnterResourceDetail,null);
            if (slf_common_loading_linear_submit.getVisibility() == View.GONE) {
                if (position == slfMediaDataList.size() - 1 && TextUtils.isEmpty(slfMediaDataList.get(position).getOriginalPath())) {
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": click add photo or video permissions");
                    SLFPermissionManager.getInstance().chekPermissions(SLFFeedbackSubmitActivity.this, permissionStorage, permissionsStroageResult);
                } else {
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": click goto preview");
                    picPathLists.clear();
                    picPathLists.addAll(slfMediaDataList);
                    picPathLists.remove(slfMediaData);
                    if (slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADED)) {
                        Intent in = new Intent();
                        in.putExtra("from", "feedback");
                        in.setClass(SLFFeedbackSubmitActivity.this, SLFFeedbackPicPreviewActivity.class);
                        in.putExtra("position", position);
                        in.putParcelableArrayListExtra("photoPath", picPathLists);
                        startActivity(in);
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": goto preview complete" + ":picPathLists size:" + picPathLists.size());
                    } else if (slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADFAIL)) {
                        slfMediaDataList.get(position).setUploadStatus(SLFConstants.UPLOADING);
                        slfaddAttachAdapter.notifyDataSetChanged();
                        File file = new File(slfMediaDataList.get(position).getOriginalPath());
                        File thumbFile = new File(slfMediaDataList.get(position).getThumbnailSmallPath());
                        String contentType = "";
                        if (slfMediaDataList.get(position).getMimeType().contains("png")) {
                            contentType = "image/png";
                        } else if (slfMediaDataList.get(position).getMimeType().contains("jpg")) {
                            contentType = "image/jpg";
                        } else if (slfMediaDataList.get(position).getMimeType().contains("jpeg")) {
                            contentType = "image/jpeg";
                        } else if (slfMediaDataList.get(position).getMimeType().contains("video")) {
                            contentType = "video/mp4";
                        }
                        long percent = progressMap.get(slfMediaDataList.get(position).getId());
                        if(contentType.contains("video/mp4")) {
                            PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(position).getUploadUrl(), file, contentType, String.valueOf(slfMediaDataList.get(position).getId()), new UploadProgressListener() {
                                @Override
                                public void onProgress(long currentlength, long total) {
                                    double progress = (double) (currentlength*1.0/total);
                                    long progressFile = (long) (progress*(100-percent));
                                    long progressZone = percent + progressFile;
                                    if(progressZone<=100){
                                        slfMediaDataList.get(position).setProgress(progressZone);
                                        //slfaddAttachAdapter.notifyDataSetChanged();
                                        runOnUiThread(() -> slfaddAttachAdapter.notifyDataSetChanged());
                                    }
                                }
                            }, this);
                            PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(position).getUploadThumurl(), thumbFile, contentType, String.valueOf(slfMediaDataList.get(position).getId()) + "thumb", new UploadProgressListener() {
                                @Override
                                public void onProgress(long currentlength, long total) {
                                    Log.e("yjjjjjjjjj", "缩略图：：：：currentlength::" + currentlength + ":::total:::" + total);
                                }
                            }, this);
                        }else{
                            PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(position).getUploadUrl(), file, contentType, String.valueOf(slfMediaDataList.get(position).getId()),null,this);
                            PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(position).getUploadThumurl(), thumbFile, contentType, String.valueOf(slfMediaDataList.get(position).getId()) + "thumb",null, this);
                        }
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":uploadFiles requested completed");
                    } else {
                        //
                    }
                }
            }
        });
    }

    private void initDrawableRight(EditText view) {
        mClearDrawable = SLFResourceUtils.getDrawable(R.drawable.slf_feed_back_email_close_all);
        mClearDrawable = view.getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = SLFResourceUtils.getDrawable(R.drawable.slf_feed_back_email_close_all);
        }
        //drawable.setBounds ：设置Drawable的边界，必须要有
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
    }


    private void setDrawableRightVisble(boolean visible, EditText view) {
        drawableRight = visible ? mClearDrawable : null;
        view.setCompoundDrawables(view.getCompoundDrawables()[0], view.getCompoundDrawables()[1], drawableRight, view.getCompoundDrawables()[3]);
    }


    /**
     * 箭头方向切换
     */
    private void changeTextAndImg(TextView view) {
        if (bottomDialog != null && bottomDialog.isShowing()) {
            SLFViewUtil.setTextViewDrawableLTRD(view, R.drawable.slf_btn_icon_up, "right");
        } else {
            SLFViewUtil.setTextViewDrawableLTRD(view, R.drawable.slf_btn_icon_down, "right");
        }
    }

    @Override
    public void onClick(View view) {
        //SLFLogUtil.sdkd(TAG,"feedbacksubmit onClick");
        if (SLFFastClickUtils.isFastDoubleClick(500)) {
            return;
        }
        if (view.getId() == R.id.slf_iv_back) {
            if (serviceType || problemType || problemOverviewType || problemEdit || !slfEmailEdit.getText().toString().equals(emailStr) || (slfMediaDataList.size() - 1 > 0)) {
                showReSureDialog();
            } else {
                finish();
            }
            // finish();
        } else if (view.getId() == R.id.slf_submit_feedback) {
            //打点提交反馈
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_Submit,null);
            isSendLogsuccess = false;
            if (slfEditProblem.getText().toString().length() < 10) {
                showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_problem_font_least));
                return;
            }

            if (!SLFRegular.isEmail(slfEmailEdit.getText().toString().trim())) {
                slfEmailError.setVisibility(View.VISIBLE);
                return;
            } else {
                if (slfEmailError.getVisibility() == View.VISIBLE) {
                    slfEmailError.setVisibility(View.INVISIBLE);
                }
            }
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                    hasUploadingFile = true;
                } else {
                    hasUploadingFile = false;
                }
            }
            if (!hasUploadingFile) {
                slf_common_loading_linear_submit.setVisibility(View.VISIBLE);
                changeViewFoucs();
                requestIllegalWorld();
            } else {
                showCenterToast(SLFResourceUtils.getString(R.string.slf_submit_if_uploading));
            }


        } else if (view.getId() == R.id.spinner_service) {
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_service_title), getServiceTypeData(getSLFCategoriesResponseBean()), service_checkedPosition);
            changeTextAndImg(slfServiceSpinner);
            //打点选择serviceType
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_SelectServiceType,null);
        } else if (view.getId() == R.id.spinner_problem) {
            //TODO selector problem
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_title), getProblemTypeData(slfServiceType, slfServiceMap), problem_checkedPosition);
            changeTextAndImg(slfProblemSpinner);
            //打点选择serviceType
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_SelectProblemType,null);
        } else if (view.getId() == R.id.spinner_problem_overview) {
            //TODO selector problem overview
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_overview_title), getSlfProblemOverviewData(slfProblemType, slfProblemMap), problem_overview_checkedPosition);
            changeTextAndImg(slfProblemOverviewSpinner);
            //打点选择serviceType
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_SelectProblemOver,null);
        } else if (view.getId() == R.id.slf_submit_page_try_again) {
            checkNetWork();
        }
    }

    /**
     * 处理sendlog选中和未选中接口提交
     */
    private void checkedSendLog() {
        if (slfSendLogCheck.isChecked()) {
            //showLoading();
            //sumbitLogFiles();
            sumbitLogFiles();
            SLFApi.getInstance(getContext()).setUploadLogCompleteCallBack(new SLFUploadCompleteCallback() {
                @Override
                public void isUploadAppLogComplete(boolean isComplete) {
                    SLFLogUtil.sdkd("yj", "complete---app-callback--");
                    //isCallbackAppLog = true;
                }
            });
            if (SLFApi.getInstance(SLFApi.getSLFContext()).getAppLogCallBack() != null) {
                SLFApi.getInstance(SLFApi.getSLFContext()).getAppLogCallBack().getUploadAppLogUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(7)).uploadUrl,
                        "application/zip");
            }
        } else {
            //showLoading();

            PUNHttpUtils.getInstance().executePost(getContext(), PUNHttpRequestConstants.BASE_URL + PUNApiContant.CREATE_FEEDBACK_URL, getCreateFeedBackTreemap(), SLFCreateFeedbackRepsonseBean.class, this);
        }
    }

    private void showReSureDialog() {
        SLFCancelOrOkDialog slfHintDialog = new SLFCancelOrOkDialog(getActivity(), SLFCancelOrOkDialog.STYLE_CANCEL_OK);
        slfHintDialog.setTitle(SLFResourceUtils.getString(R.string.slf_feedback_dilaog_resure_title));
        slfHintDialog.setContentText(SLFResourceUtils.getString(R.string.slf_feedback_dilaog_resure_content));
        slfHintDialog.setLeftBtnText(SLFResourceUtils.getString(R.string.slf_title_cancel));
        slfHintDialog.setRightBtnText(SLFResourceUtils.getString(R.string.slf_feedback_dilaog_resure_ok));
        slfHintDialog.setOnListener(new SLFCancelOrOkDialog.OnHintDialogListener() {

            @Override
            public void onClickOk() {
                //打点放弃反馈
                PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_Feedback_AbandonSubmit,null);
                finish();
            }

            @Override
            public void onClickCancel() {
                slfHintDialog.dismiss();
            }

            @Override
            public void onClickOther() {

            }
        });

        slfHintDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //打点进入反馈提交页
        PUTClickAgent.pageTypeAgent(SLFPageAgentEvent.SLF_FeedbackPage,SLFPageAgentEvent.SLF_PAGE_START,null);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (serviceType || problemType || problemOverviewType || problemEdit || !slfEmailEdit.getText().toString().equals(emailStr) || (slfMediaDataList.size() - 1 > 0)) {
                showReSureDialog();
            } else {
                finish();
            }


                return false;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * submit feedback按钮是否可点击
     */
    private void setSubmitBtnCanClick(boolean isClick) {

        if (isClick) {
            slfSumbmit.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg));
            slfSumbmit.setTextColor(SLFResourceUtils.getColor(R.color.black));
            slfSumbmit.setClickable(true);
            slfSumbmit.setOnClickListener(this);
        } else {
            slfSumbmit.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_bg));
            slfSumbmit.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
            slfSumbmit.setClickable(false);
        }

    }

    /**
     * @param
     */
    private void changeViewFoucs() {
        if (slf_common_loading_linear_submit.getVisibility() == View.VISIBLE) {
            slfServiceSpinner.setClickable(false);
            slfProblemSpinner.setClickable(false);
            slfProblemOverviewSpinner.setClickable(false);
            slfEditProblem.setFocusable(false);
            slfEditProblem.setFocusableInTouchMode(false);
            slfEmailEdit.setFocusable(false);
            slfEmailEdit.setFocusableInTouchMode(false);
            slfSendLogCheck.setClickable(false);
            slfSumbmit.setClickable(false);
        } else {
            slfServiceSpinner.setClickable(true);
            slfProblemSpinner.setClickable(true);
            slfProblemOverviewSpinner.setClickable(true);
            slfEditProblem.setFocusable(true);
            slfEditProblem.setFocusableInTouchMode(true);
            slfEmailEdit.setFocusable(true);
            slfEmailEdit.setFocusableInTouchMode(true);
            slfSendLogCheck.setClickable(true);
            slfSumbmit.setClickable(canSubmit());
        }
    }

    //创建回调处理
//创建监听权限的接口对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        SLFPermissionManager.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    SLFPermissionManager.IPermissionsResult permissionsStroageResult = new SLFPermissionManager.IPermissionsResult() {
        @Override
        public void passPermissons() {
            // Toast.makeText(CameraActivity.this, "权限通过!", Toast.LENGTH_SHORT).show();
            SLFPhotoSelectorUtils.with(getContext())
                    .selectedNum(4 - slfMediaDataList.size())
                    .SLFMediaQuality(SLFMediaType.All)
                    .open(selectMediaList -> {
                        slfMediaDataList.remove(slfMediaData);
                        slfMediaDataList.addAll(selectMediaList);
                        slfMediaDataList.add(slfMediaData);
                        setUploadUrl();
                        slfaddAttachAdapter.notifyDataSetChanged();
                        uploadFiles();
                    });
            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": click add photo or video uploaded files");
            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": selectedNum:" + (4 - slfMediaDataList.size()));
        }

        @Override
        public void forbitPermissons() {
//            finish();
            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ": permissions not passed");
            Toast.makeText(SLFFeedbackSubmitActivity.this, "permissions not passed!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 上传图片或视频
     */
    private void uploadFiles() {
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":uploadFiles:::" + slfMediaDataList.size());
        String contentType = "";
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (slfMediaDataList.get(i).getUploadPath() == null) {
                slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                //slfaddAttachAdapter.notifyDataSetChanged();
                SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":uploadFiles uploadUrl is null");
            } else {
                if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING) && !slfMediaDataList.get(i).getMimeType().contains("video")) {
                    File file = new File(slfMediaDataList.get(i).getOriginalPath());
                    File thumbFile = new File(slfMediaDataList.get(i).getThumbnailSmallPath());
                    if (slfMediaDataList.get(i).getMimeType().contains("png")) {
                        contentType = "image/png";
                    } else if (slfMediaDataList.get(i).getMimeType().contains("jpg")) {
                        contentType = "image/jpg";
                    } else if (slfMediaDataList.get(i).getMimeType().contains("jpeg")) {
                        contentType = "image/jpeg";
                    } else if (slfMediaDataList.get(i).getMimeType().contains("video")) {
                        contentType = "video/mp4";
                    }
                    if(contentType.contains("video/mp4")){
                        Log.e("yj","视频类型");
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, contentType, String.valueOf(slfMediaDataList.get(i).getId()), new UploadProgressListener() {
                            @Override
                            public void onProgress(long currentlength, long total) {
                                Log.e("yjjjjjjjjj","文件：：：：currentlength::000："+currentlength+":::total:::"+total);
                            }
                        }, this);
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, contentType, String.valueOf(slfMediaDataList.get(i).getId()) + "thumb", new UploadProgressListener() {
                            @Override
                            public void onProgress(long currentlength, long total) {

                            }
                        }, this);
                    }else {
                        Log.e("yj","图片类型");
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, contentType, String.valueOf(slfMediaDataList.get(i).getId()), null, this);
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, contentType, String.valueOf(slfMediaDataList.get(i).getId()) + "thumb", null, this);
                    }
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":uploadFiles requested completed");
                }
            }
        }
    }


    /**
     * email的touchlistener
     */
    View.OnTouchListener emailTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (drawableRight != null) {
                    if (event.getRawX() >= (slfEmailEdit.getRight() - SLFResourceUtils.px2dp(getContext(), 60) - slfEmailEdit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":click clean Email");
                        clearEmailEdit();
                        setSubmitBtnCanClick(canSubmit());
                        return true;
                    }
                }
            }
            return false;
        }
    };

    /**
     * 问题描述输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        slfProblemWordNum = charSequence;
        if (slfProblemWordNum.length() < 1000) {
            slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
            setFontCount();
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        slfProblemWordNum = charSequence;

    }

    @Override
    public void afterTextChanged(Editable editable) {
        setFontCount();
        if(slfProblemWordNum.length() >0) {
            if(TextUtils.isEmpty(slfEditProblem.getText().toString().trim())){
                slfEditProblem.setText("");
                setSubmitBtnCanClick(false);
                return;
            }
            setSubmitBtnCanClick(canSubmit());
        }
        setSubmitBtnCanClick(canSubmit());
    }


    /**
     * 设置问题描述的字数限制显示
     */
    private void setFontCount() {
        if (slfProblemWordNum.length() <= 1000) {
            slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
            if (slfProblemWordNum.length() == 1000) {
                slfFontCount.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_page_email_error));
            } else {
                slfFontCount.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
            }
        }
    }

    /**
     * 清空email输入框
     */
    private void clearEmailEdit() {
        if (!TextUtils.isEmpty(slfEmailEdit.getText().toString().trim())) {
            hideEmailError();
            slfEmailEdit.setText("");
        }
    }

    /**
     * emaiError显隐逻辑
     */
    private void hideEmailError() {
//        if (!TextUtils.isEmpty(slfEmailEdit.getText().toString().trim())) {
        if (slfEmailError.getVisibility() == View.VISIBLE) {
            slfEmailError.setVisibility(View.INVISIBLE);
        }
        //       }
    }

    /**
     * 是否可以提交
     */
    private boolean canSubmit() {

        if (!TextUtils.isEmpty(slfServiceSpinner.getText())) {
            serviceType = true;
            if (slfProblemLinear.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(slfProblemSpinner.getText())) {
                problemType = true;
                if (slfProblemOverviewLinear.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(slfProblemOverviewSpinner.getText())) {
                    problemOverviewType = true;
                } else {
                    if (slfProblemOverviewLinear.getVisibility() == View.GONE) {
                        problemOverviewType = true;
                    } else {
                        problemOverviewType = false;
                    }
                }
            } else {
                if (slfProblemLinear.getVisibility() == View.GONE) {
                    problemType = true;
                    problemOverviewType = true;
                } else {
                    problemType = false;
                }
            }
        } else {
            serviceType = false;
        }

        if (serviceType && problemType && problemOverviewType) {
            type = true;
        }else{
            type = false;
        }

        if (TextUtils.isEmpty(slfEditProblem.getText().toString().trim())) {
            problemEdit = false;
        } else {
            problemEdit = true;
        }

        if (!TextUtils.isEmpty(slfEmailEdit.getText().toString().trim())) {
            emailEdit = true;
        } else {
            emailEdit = false;
        }
        SLFUserDeviceSaved userInfo = getUserInfo();
        if (type && problemEdit && emailEdit && userInfo!=null) {
            return true;
        } else {
            return false;
        }


    }

    TextWatcher editWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            slfEmailWordNum = charSequence;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setSubmitBtnCanClick(canSubmit());
            if (slfEmailWordNum.length() == 0) {
                if (slfEmailError.getVisibility() == View.VISIBLE) {
                    slfEmailError.setVisibility(View.INVISIBLE);
                }
            }
            setDrawableRightVisble(slfEmailEdit.getText().length() > 0, slfEmailEdit);
        }
    };


    //键盘监听事件
    public void setListenerFotEditTexts() {
        //键盘监听事件
        SLFSoftKeyBoardListener.setListener(SLFFeedbackSubmitActivity.this,
                new SLFSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        slfSumbmit.setVisibility(View.GONE);
//                        if (slfEmailEdit.hasFocus()) {
//                            slfScrollView.smoothScrollTo(0, slfKeyBoardHeight + slfEmailErrorHeight + slfEmailHeight + SLFResourceUtils.dp2px(getContext(), 40));
//                        }
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord show");
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slfSumbmit.setVisibility(View.VISIBLE);
                            }
                        },100);

                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord hide");
                    }
                });
    }

    //获取键盘高度
    //记录原始窗口高度
    private int mWindowHeight = 0;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前窗口实际的可见区域
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int height = r.height();
            if (mWindowHeight == 0) {
                //一般情况下，这是原始的窗口高度
                mWindowHeight = height;
            } else {
                if (mWindowHeight != height) {
                    //两次窗口高度相减，就是软键盘高度
                    slfKeyBoardHeight = mWindowHeight - height;
                }
            }
        }
    };

    /**
     * 显示bottomDialog设置dialog
     */
    private <T extends Object> void showServiceTypeDialog(String title, List<T> dataList, int postion) {
        bottomDialog = new SLFBottomDialog(this, dataList);
        bottomDialog.show();
        bottomDialog.setTitleText(title);
        bottomDialog.setonSeletedTypeListener(this);
        bottomDialog.setPositionChecked(postion);
        bottomDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_service_title))) {
                    changeTextAndImg(slfServiceSpinner);
                    if (service_checkedPosition != -1) {
                        slfServiceType = ((List<SLFCategoryBean>) dataList).get(service_checkedPosition);
                        if (slfServiceType != null) {
                            if (slfServiceMap.get(slfServiceType.id) != null && slfServiceMap.get(slfServiceType.id).size() > 0) {
                                if (!oldServiceType.equals(slfServiceType.name)) {
                                    slfProblemLinear.setVisibility(View.GONE);
                                    slfProblemOverviewLinear.setVisibility(View.GONE);
                                    slfProblemSpinner.setText("");
                                    slfProblemOverviewSpinner.setText("");
                                    problem_checkedPosition = -1;
                                    problem_overview_checkedPosition = -1;
                                    bottomDialog.flushProblem(slfServiceMap.get(slfServiceType.id));
                                }
                                slfProblemLinear.setVisibility(View.VISIBLE);
                            } else {
                                slfProblemLinear.setVisibility(View.GONE);
                                slfProblemOverviewLinear.setVisibility(View.GONE);
                                slfProblemOverviewSpinner.setText("");
                                problem_overview_checkedPosition = -1;
                            }
                            slfServiceSpinner.setText(slfServiceType.name);
                            oldServiceType = slfServiceType.name;
                            if (!TextUtils.isEmpty(slfServiceType.name)) {
                                serviceType = true;
                            } else {
                                serviceType = false;
                            }
                        } else {
                            //showCenterToast("获取服务类型失败");
                        }

                    }
                } else if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_title))) {
                    changeTextAndImg(slfProblemSpinner);
                    if (problem_checkedPosition != -1) {
                        if (slfServiceType != null && slfServiceMap != null) {
                            slfProblemType = ((List<SLFCategoryDetailBean>) dataList).get(problem_checkedPosition);
                            slfProblemSpinner.setText(slfProblemType.name);
                            if (slfProblemType != null && slfProblemMap != null && slfProblemMap.get(slfProblemType.id).size() > 0) {
                                if (!oldProblemType.equals(slfProblemType.name)) {
                                    slfProblemOverviewLinear.setVisibility(View.GONE);
                                    slfProblemOverviewSpinner.setText("");
                                    problem_overview_checkedPosition = -1;
                                    bottomDialog.flushProblemOverview(slfProblemMap.get(slfProblemType.id));
                                }
                                slfProblemOverviewLinear.setVisibility(View.VISIBLE);
                                oldProblemType = slfProblemType.name;
                            } else {
                                slfProblemOverviewLinear.setVisibility(View.GONE);
                            }
                        } else {
                            //showCenterToast("获取问题类型失败");
                        }
                    }
                } else {
                    changeTextAndImg(slfProblemOverviewSpinner);
                    if (problem_overview_checkedPosition != -1) {
                        if (slfProblemType != null && slfProblemMap != null && (slfProblemMap.get(slfProblemType.id) != null) && (slfProblemMap.get(slfProblemType.id)).size() > 0) {
                            slfProblemOverviewType = ((List<SLFCategoryCommonBean>) dataList).get(problem_overview_checkedPosition);
                            slfProblemOverviewSpinner.setText(slfProblemOverviewType.name);
                            if (!TextUtils.isEmpty(slfProblemOverviewType.name)) {
                                problemOverviewType = true;

                            } else {
                                problemOverviewType = false;
                            }
                        } else {

                        }
                    }

                }
                setSubmitBtnCanClick(canSubmit());
                SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":show bottom dialog");
            }
        });
    }

    /**
     * 请求用户列表
     */
    private void requestUserDeviceList(){
        PUNHttpUtils.getInstance().executePathGet(getContext(),
                PUNHttpRequestConstants.BASE_URL +PUNApiContant.USER_DEVICE_LIST, SLFUserDeviceListResponseBean.class, this);
        }

    /**
     * 请求后台配置数据
     */
    private void requestAllData() {
        //showLoading();
        PUNHttpUtils.getInstance().executePathGet(getContext(),
                PUNHttpRequestConstants.BASE_URL + PUNApiContant.CATEGORY_URL, SLFCategoriesResponseBean.class, this);
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":request servicetype/problemtype/subtype data");
    }

    /**
     * 请求上传文件链接
     */
    private void requestUploadUrls() {
        TreeMap map = new TreeMap();
        map.put("num", 8);
        PUNHttpUtils.getInstance().executeGet(getContext(),
                PUNHttpRequestConstants.BASE_URL + PUNApiContant.UPLOAD_FILE_URL, map, SLFUploadFileReponseBean.class, this);
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":request upload url 8");
    }

    /**
     * 敏感词汇校验
     */
    private void requestIllegalWorld() {
        if (TextUtils.isEmpty(slfEditProblem.getText().toString().trim())) {
            showToast("Content is empty!");
            return;
        } else {
            String content = slfEditProblem.getText().toString();
            TreeMap<String, Object> contentMap = new TreeMap<>();
            contentMap.put("content", content);
            PUNHttpUtils.getInstance().executePost(getContext(), PUNHttpRequestConstants.BASE_URL + PUNApiContant.FEEDBACK_ILLEGLA_WORD, contentMap, SLFillagelWordResponseBean.class, this);
        }
    }

    /**
     * 得到后台配置数据解析
     */
    private SLFCategoriesResponseBean getSLFCategoriesResponseBean() {

        return slfCategoriesResponseBean;
    }

    /**
     * 获取用户设备列表
     **/
    private List<Object> getUserTypeData(SLFCategoriesResponseBean slfCategoriesResponseBean) {
        if (SLFUserCenter.userDeviceListBean != null && SLFUserCenter.userDeviceListBean.getData() != null && SLFUserCenter.userDeviceListBean.getData()!= null && SLFUserCenter.userDeviceListBean.getData().size() > 0) {
            slfServiceTypes.clear();
            slfServiceMap.clear();
            slfServiceTitleMap.clear();
            slfServiceTypes.add(SLFResourceUtils.getResources().getString(R.string.slf_user_device_service_type));
            if (slfCategoriesResponseBean != null && slfCategoriesResponseBean.data != null && slfCategoriesResponseBean.data.size() > 0) {
                for (int i = 0; i < slfCategoriesResponseBean.data.size(); i++) {
                    SLFProlemDataBean serviceTypeTitle = slfCategoriesResponseBean.data.get(i);
                    List<SLFCategoryBean> listCategory = new ArrayList<>();
                    if (serviceTypeTitle.name.contains("All device types")) {
                        SLFLogUtil.sdkd("yj","serviceTypeTitle.size::::"+serviceTypeTitle.sub.size());
                        for (int j = 0; j < serviceTypeTitle.sub.size(); j++) {
                            for (int k = 0; k < SLFUserCenter.userDeviceListBean.getData().size(); k++) {
                                SLFCategoryBean categoryBean = new SLFCategoryBean();
                                categoryBean.id = serviceTypeTitle.sub.get(j).id + 10000 + k;
                                categoryBean.name = SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceName();
                                categoryBean.deviceModel = SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceModel();
                                categoryBean.sub = new ArrayList<>();
                                if(!TextUtils.isEmpty(serviceTypeTitle.sub.get(j).deviceModel)) {
                                    if (SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceModel().contains(serviceTypeTitle.sub.get(j).deviceModel)
                                            || SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceModel().contains(serviceTypeTitle.sub.get(j).deviceModel.toLowerCase())
                                            || SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceModel().contains(serviceTypeTitle.sub.get(j).deviceModel.toUpperCase())) {
                                        categoryBean.sub.addAll(serviceTypeTitle.sub.get(j).sub);
                                        SLFUserDeviceSaved userDeviceSaved = new SLFUserDeviceSaved(SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceId(),
                                                SLFUserCenter.userDeviceListBean.getData().get(k).getDeviceModel(), serviceTypeTitle.sub.get(j).id,
                                                SLFUserCenter.userDeviceListBean.getData().get(k).getFirmwareVersion());
                                        SLFUserCenter.getInstance().put(categoryBean.id, userDeviceSaved);
                                    }

                                }
                                listCategory.add(categoryBean);
                                slfServiceMap.put(categoryBean.id, categoryBean.sub);
                                slfServiceTypes.add(categoryBean);

                                if(slfServiceTypes.size()<=1){
                                    slfServiceTypes.clear();
                                }
                            }
                            slfServiceTitleMap.put(serviceTypeTitle.sub.get(j).id + 100, listCategory);
                        }
                        if (slfServiceTitleMap != null && slfServiceTitleMap.size() > 0) {
                            setServiceTitleMapConner(slfServiceTitleMap);
                        }

                    }
                }
            }
        }

        return slfServiceTypes;
    }

    /**
     * 获取servicetype列表
     */
    private List<Object> getServiceTypeData(SLFCategoriesResponseBean slfCategoriesResponseBean) {
        slfServiceTypes.clear();
        slfServiceTypes = getUserTypeData(slfCategoriesResponseBean);
        if (slfCategoriesResponseBean != null && slfCategoriesResponseBean.data != null && slfCategoriesResponseBean.data.size() > 0) {
            for (int i = 0; i < slfCategoriesResponseBean.data.size(); i++) {
                SLFProlemDataBean serviceTypeTitle = slfCategoriesResponseBean.data.get(i);
                if (serviceTypeTitle.sub != null && serviceTypeTitle.sub.size() > 0) {
                    slfServiceTypes.add(serviceTypeTitle.name);
                    slfServiceTypes.addAll(serviceTypeTitle.sub);
                    slfServiceTitleMap.put(serviceTypeTitle.id, serviceTypeTitle.sub);
                    for (int j = 0; j < serviceTypeTitle.sub.size(); j++) {
                        SLFCategoryBean slfServiceType = serviceTypeTitle.sub.get(j);
                        slfServiceMap.put(slfServiceType.id, slfServiceType.sub);
                    }
                }
            }
            if (slfServiceTitleMap != null && slfServiceTitleMap.size() > 0) {
                setServiceTitleMapConner(slfServiceTitleMap);
            }
            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":get servicetype data success");
        } else {
            //showCenterToast("没有获取到问题数据类型");
            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":get servicetype data fail");
        }
        SLFLogUtil.sdkd("yj", "slfserviceTyps--:" + slfServiceTypes.toString());
        return slfServiceTypes;
    }

    /**
     * 获取problem列表
     */
    private List<SLFCategoryDetailBean> getProblemTypeData(SLFCategoryBean serviceType, Map<Long, List<SLFCategoryDetailBean>> serviceMap) {
        slfProblemTypes.clear();
        slfProblemMap.clear();
        if (serviceType != null && serviceMap != null && serviceMap.size() > 0) {
            slfProblemTypes.addAll(serviceMap.get(serviceType.id));
            setListRoundConner(slfProblemTypes);
            for (int i = 0; i < slfProblemTypes.size(); i++) {
                SLFCategoryDetailBean problemType = slfProblemTypes.get(i);
                slfProblemMap.put(problemType.id, problemType.sub);
            }
        }
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":get problemtype data success");
        return slfProblemTypes;
    }

    /**
     * 获取problemoverview列表
     */
    private List<SLFCategoryCommonBean> getSlfProblemOverviewData(SLFCategoryDetailBean problemType, Map<Long, List<SLFCategoryCommonBean>> problemMap) {
        slfProblemOverviewTypes.clear();
        if (problemType != null && problemMap != null && problemMap.size() > 0) {
            slfProblemOverviewTypes.addAll(problemMap.get(problemType.id));
            setListRoundConner(slfProblemOverviewTypes);
        }
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":get problemOverview data success");
        return slfProblemOverviewTypes;
    }

    /**
     * serviceType设置圆角方法
     */
    private void setServiceTitleMapConner(Map<Long, List<SLFCategoryBean>> serviceTitleMap) {
        Iterator<Map.Entry<Long, List<SLFCategoryBean>>> it = serviceTitleMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, List<SLFCategoryBean>> entry = it.next();
            for (int i = 0; i < entry.getValue().size(); i++) {
                if(entry.getValue().get(i) instanceof SLFCategoryBean) {
                    if (entry.getValue().size() == 1) {
                        ((SLFCategoryBean) entry.getValue().get(i)).setRound_type(SLFConstants.ALL_ROUND);
                    } else {
                        if (i == 0) {
                            ((SLFCategoryBean) entry.getValue().get(i)).setRound_type(SLFConstants.ROUND_FIRST);
                        } else if (i == entry.getValue().size() - 1) {
                            ((SLFCategoryBean) entry.getValue().get(i)).setRound_type(SLFConstants.ROUND_END);
                        } else {
                            ((SLFCategoryBean) entry.getValue().get(i)).setRound_type(SLFConstants.ROUND_MIDDLE);
                        }
                    }
                }
            }
        }
    }

    /**
     * problem和problemOverview获取数据设置list圆角
     */
    private <T extends Object> void setListRoundConner(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            T obj = list.get(i);
            if (list.size() == 1) {
                if (obj instanceof SLFCategoryBean) {
                    ((SLFCategoryBean) obj).setRound_type(SLFConstants.ALL_ROUND);
                } else if (obj instanceof SLFCategoryDetailBean) {
                    ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ALL_ROUND);
                } else if (obj instanceof SLFCategoryCommonBean) {
                    ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ALL_ROUND);
                }
            } else {
                if (i == 0) {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    } else if (obj instanceof SLFCategoryCommonBean) {
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    }
                } else if (i == list.size() - 1) {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_END);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_END);
                    } else if (obj instanceof SLFCategoryCommonBean) {
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_END);
                    }
                } else {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    } else if (obj instanceof SLFCategoryCommonBean) {
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    }
                }
            }

        }
    }

    /**
     * sendlog checked
     */
    private void sumbitLogFiles() {
        initLogFiles();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(submitLogRunnable);
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":submit log files");
    }

    /**
     * init sendlog
     */
    private void initLogFiles() {
        submitLogRunnable = new Runnable() {
            @Override
            public void run() {
                SLFLogUtil.sdkAppenderFlush(true);
                /**压缩成zip*/
                SLFCompressUtil.zipFile(SLFConstants.apiLogPath, "*", SLFConstants.feedbacklogPath + "sdkLog.zip", new SLFCompressUtil.OnCompressSuccessListener() {
                    @Override
                    public void onSuccess() {
                        isSubmit = true;
                        if (isSubmit) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    File logFile = new File(SLFConstants.feedbacklogPath + "sdkLog.zip");
                                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::logFile.size------::" + logFile.length());
                                    String uploadUrl = SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(6)).uploadUrl;
                                    PUNHttpUtils.getInstance().executePutFile(getContext(), uploadUrl, logFile, "application/zip", SLFConstants.photoCode, null,SLFFeedbackSubmitActivity.this);

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure() {
                        isSubmit = false;
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::compress  logFils error");
                    }
                });
            }
        };
    }

    /**
     * goto success page
     */
    private void gotoFeedbackSuccess(int logId) {
        Intent in = new Intent(getContext(), SLFFeedbackSuccessActivity.class);
        in.putExtra(SLFConstants.LOGID, logId);
        in.putExtra(SLFConstants.FROM_FAQ,from);
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::go to feedback success page ::logid::" + logId);
        startActivity(in);
        finish();
    }

    @Override
    public void onRequestNetFail(Object type) {
        SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::requestNetFail");
        hideLoading();
        slf_common_loading_linear_submit.setVisibility(View.GONE);
        changeViewFoucs();
        if (type instanceof String) {
            String code = (String) type;
            if (SLFConstants.photoCode.equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            } else {
                for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                    if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                        slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                    }
                }
                slfaddAttachAdapter.notifyDataSetChanged();
                showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
            }
        } else if (type instanceof SLFillagelWordResponseBean) {
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
//        else if(type instanceof SLFUploadFileReponseBean){
//           // showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
//            isHideLoading("isResolveUpload");
//        }else if(type instanceof SLFUserDeviceListResponseBean){
//            isHideLoading("isResolveDevicelist");
//        }else if(type instanceof SLFCategoriesResponseBean){
//            isHideLoading("isResolveAllData");
//        }
        else{
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
    }

    @Override
    public void onRequestSuccess(String result, Object type) {

        if (type instanceof SLFCategoriesResponseBean) {
            //isHideLoading("isResolveAllData");
            hideLoading();
            cacheManager.delete(CACHE_FILE_PATH);
            SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY,SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY_CACHE,0));
            cacheManager.saveObj(CACHE_FILE_PATH,(SLFCategoriesResponseBean)type);
            showContent(type);

        } else if (type instanceof SLFUploadFileReponseBean) {
            //isHideLoading("isResolveUpload");
            SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::requestScucess::SLFUploadFileReponseBean::" + ":::type:::" + type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean) type, 8);
            if (SLFCommonUpload.getInstance() != null && SLFCommonUpload.getInstance().size() > 0 && SLFCommonUpload.getListInstance() != null && SLFCommonUpload.getListInstance().size() > 0) {
                /**分配前六个链接给图片和视频上传*/
                for (int i = 0; i < 6; i++) {
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::uploadPath--all----:::" + SLFCommonUpload.getListInstance().get(i));
                }
            }
            //isHideLoading("isResolveUpload");
        } else if (type instanceof SLFillagelWordResponseBean) {
            boolean isIllagelWorld = (boolean) ((SLFillagelWordResponseBean) type).isData();

            if (isIllagelWorld) {
                slf_common_loading_linear_submit.setVisibility(View.GONE);
                //changeViewFoucs();
                showCenterToast(SLFResourceUtils.getString(R.string.slf_create_feedback_illegal_world_text));
                new Handler().postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
                    @Override
                    public void run() {
                        changeViewFoucs();
                    }
                }, 2500); //
            } else {
                checkedSendLog();
            }
        } else if (type instanceof String) {
            String code = (String) type;
            SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::requestScucess::Integer::" + ":::type:::" + type);
            if (SLFConstants.photoCode.equals(code)) {
                SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::logfile-----upload---complete");
                PUNHttpUtils.getInstance().executePost(getContext(), PUNHttpRequestConstants.BASE_URL + PUNApiContant.CREATE_FEEDBACK_URL, getCreateFeedBackTreemap(), SLFCreateFeedbackRepsonseBean.class, this);
            } else {
                resultUploadImageOrVideo(code);
                isUploadSuccess();
            }
            hideLoading();
        } else if (type instanceof SLFCreateFeedbackRepsonseBean) {
            SLFLogUtil.sdkd("yj", "ActivityName:" + this.getClass().getSimpleName() + "::createFeedback-----success:" + ((SLFCreateFeedbackRepsonseBean) type).data);
            isSendLogsuccess = true;
            gotoFeedbackSuccess(((SLFCreateFeedbackRepsonseBean) type).data);
            slf_common_loading_linear_submit.setVisibility(View.GONE);
            changeViewFoucs();
        } else if(type instanceof SLFUserDeviceListResponseBean){
            SLFUserCenter.userDeviceListBean = (SLFUserDeviceListResponseBean) type;
            //isHideLoading("isResolveDevicelist");
            SLFLogUtil.sdkd("yj","SLFUserCenter.userDeviceListBean::::::"+SLFUserCenter.userDeviceListBean.getData().size());
        }
    }

    private void resultCodeMethod(SLFMediaData slfMediaData) {
        slfMediaData.setFileSuccess(true);
    }

    private void resultCodeThumbMethod(SLFMediaData slfMediaData) {
        slfMediaData.setThumbSuccess(true);
    }

    private void isUploadSuccess(){
        for(int i=0;i<slfMediaDataList.size()-1;i++){
            if(slfMediaDataList.get(i).isFileSuccess()&&slfMediaDataList.get(i).isThumbSuccess()){
                slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                slfaddAttachAdapter.notifyDataSetChanged();
                slfMediaDataList.get(i).setFileSuccess(false);
                slfMediaDataList.get(i).setThumbSuccess(false);
            }
        }
    }
        private void isHideLoading(String isTrueString){
            if(isTrueString.equals("isResolveDevicelist")){
                isResolveDevicelist = true;
            }else if(isTrueString.equals("isResolveAllData")){
                isResolveAllData = true;
            }else if(isTrueString.equals("isResolveUpload")){
                isResolveUpload = true;
            }
            if(isAllDataCache){
                if(isResolveDevicelist && isResolveUpload){
                    hideLoading();
                    isResolveDevicelist = false;
                    isResolveUpload = false;
                    isResolveAllData = false;
                    isAllDataCache = false;
                }else{
                    return;
                }
            }
    }

    private void showContent (Object type) {
        SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::requestScucess::SLFCategoriesResponseBean::" + ":::type:::" + type.toString());
        this.slfCategoriesResponseBean = (SLFCategoriesResponseBean) type;
        //isHideLoading("isResolveAllData");
        hideLoading();
    }

//    private void canGotoSubmit(int logId){
//        if(slfSendLogCheck.isChecked()){
//            if(isSendLogsuccess&&isCallbackAppLog){
//                gotoFeedbackSuccess(logId);
//            }
//        }
//    }

    @Override
    public void onRequestFail(String value, String failCode, Object type) {
        SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::requestFail::" + value + ":::failCode:::" + failCode);
        hideLoading();
        slf_common_loading_linear_submit.setVisibility(View.GONE);
        changeViewFoucs();
        if (type instanceof String) {
            String code = (String) type;
            if (SLFConstants.photoCode.equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            } else {
                for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                    if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                        slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                    }
                }
                slfaddAttachAdapter.notifyDataSetChanged();
                showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
            }
        } else if (type instanceof SLFillagelWordResponseBean) {
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
        }
//        else if(type instanceof SLFUploadFileReponseBean){
//            isHideLoading("isResolveUpload");
//        } else if(type instanceof SLFUserDeviceListResponseBean) {
//            isHideLoading("isResolveDevicelist");
//        } else if(type instanceof SLFCategoriesResponseBean){
//            isHideLoading("isResolveAllData");
//        }
        else{
            hideLoading();
        }
        new Handler().postDelayed(new Runnable() {  // 开启的runnable也会在这个handler所依附线程中运行，即主线程
            @Override
            public void run() {
                changeViewFoucs();
            }
        }, 3000); //
    }

    private synchronized void resultUploadImageOrVideo(String code) {
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                resultCodeMethod(slfMediaDataList.get(i));
            }
            if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()) + "thumb")) {
                resultCodeThumbMethod(slfMediaDataList.get(i));
            }
        }
    }

    private void resultCodeMethod(String code, boolean imageSuccess) {
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                if (slfMediaDataList.get(i).getUploadPath() != null) {
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                    slfaddAttachAdapter.notifyDataSetChanged();
                    imageSuccess = false;
                } else {
                    imageSuccess = false;
                }
            }
        }
    }


    private void setUploadUrl() {

        for (int position = 0; position < slfMediaDataList.size() - 1; position++) {
            if (slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADIDLE)) {
                if (SLFCommonUpload.getListInstance().size() == 8 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)) != null && SLFCommonUpload.getInstance().size() == 8) {
                    if (SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(0));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(1));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 8 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(2));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(3));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 8 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(4));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(5));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle = false;
                    }
                    slfMediaDataList.get(position).setUploadStatus(SLFConstants.UPLOADING);
                }
            }

        }

    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }

    private TreeMap<String, Object> getCreateFeedBackTreemap() {
        ArrayList<SLFLogAttrBean> logAttrBeans = new ArrayList<>();
        ArrayList<SLFLeaveMsgBean> attrList = new ArrayList<>();
        TreeMap<String, Object> map = new TreeMap<>();
        SLFUserDeviceSaved userInfo = getUserInfo();
        if(!TextUtils.isEmpty(userInfo.getDeviceid())) {
            map.put("deviceId", userInfo.getDeviceid());
        }
        if(!TextUtils.isEmpty(userInfo.getDeviceMoudle())) {
            map.put("deviceModel", userInfo.getDeviceMoudle());
        }
        map.put("deviceTimezone", SLFUserCenter.getDeviceTimeZone());
        map.put("serviceType", userInfo.getServiceTypeid());
        map.put("serviceTypeText", serviceTypeText);
        if (slfProblemLinear.getVisibility() == View.VISIBLE) {
            map.put("category", seletedProbleType);
            map.put("categoryText", categoryText);
        }
        if (slfProblemOverviewLinear.getVisibility() == View.VISIBLE) {
            map.put("subCategory", seletedProblemOverviewType);
            map.put("subCategoryText", subCategoryText);
        }
        map.put("content", slfEditProblem.getText().toString());
        map.put("email", slfEmailEdit.getText().toString().trim());
        if (slfSendLogCheck.isChecked()) {
            map.put("sendLog", 1);
            SLFLogAttrBean logAttrAppBean = new SLFLogAttrBean();
            /**appLogBean*/
            logAttrAppBean.setPath(SLFCommonUpload.getListInstance().get(7));
            if (!TextUtils.isEmpty(appLogFileName)) {
                logAttrAppBean.setFileName(appLogFileName);
            }
            logAttrAppBean.setContentType("application/zip");
            /**firmwareLogBean*/
//            SLFLogAttrBean logAttrFirmwareBean = new SLFLogAttrBean();
//
//            logAttrFirmwareBean.setPath(SLFCommonUpload.getListInstance().get(8));
//            if (!TextUtils.isEmpty(firmwareLogFileName)) {
//                logAttrFirmwareBean.setFileName(firmwareLogFileName);
//            }
//            logAttrFirmwareBean.setContentType("application/zip");
            /*sdkLogBean*/
            SLFLogAttrBean logAttrPluginBean = new SLFLogAttrBean();
            logAttrPluginBean.setPath(SLFCommonUpload.getListInstance().get(6));
            logAttrPluginBean.setFileName("sdkLog.zip");
            logAttrPluginBean.setContentType("application/zip");
            logAttrBeans.add(logAttrAppBean);
            //logAttrBeans.add(logAttrFirmwareBean);
            logAttrBeans.add(logAttrPluginBean);
            map.put("logAttrList", logAttrBeans);
        } else {
            map.put("sendLog", 0);
        }
        if (slfMediaDataList.size() - 1 > 0) {
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADED)) {
                    SLFLeaveMsgBean slfLeaveMsgBean = new SLFLeaveMsgBean();
                    slfLeaveMsgBean.setPath(slfMediaDataList.get(i).getUploadPath());
                    slfLeaveMsgBean.setThumbnailPath(slfMediaDataList.get(i).getUploadThumPath());
                    slfLeaveMsgBean.setFileName(slfMediaDataList.get(i).getFileName());
                    slfLeaveMsgBean.setThumbnailContentType("image/png");
                    if (slfMediaDataList.get(i).getMimeType().contains("video")) {
                        slfLeaveMsgBean.setContentType("video/mp4");
                    } else if (slfMediaDataList.get(i).getMimeType().contains("png")) {
                        slfLeaveMsgBean.setContentType("image/png");
                    } else if (slfMediaDataList.get(i).getMimeType().contains("jpg")) {
                        slfLeaveMsgBean.setContentType("image/jpg");
                    } else if (slfMediaDataList.get(i).getMimeType().contains("jpeg")) {
                        slfLeaveMsgBean.setContentType("image/jpeg");
                    }
                    attrList.add(slfLeaveMsgBean);
                }
            }
            map.put("attrList", attrList);
        }
        if(!TextUtils.isEmpty(userInfo.getFirmewareVersion())) {
            map.put("firmwareVersion", userInfo.getFirmewareVersion());
        }
        map.put("appVersion", SLFUserCenter.getAppVersionName());
        map.put("pluginVersion", SLFUserCenter.getPluginversion());
        map.put("phoneType", 2);
        //SLFLogUtil.sdkd("yj", "手机型号：" + SLFUserCenter.getPhoneModel());
        map.put("phoneModel", SLFUserCenter.getPhoneModel());
        map.put("phoneOsVersion", SLFUserCenter.getOSVersion());
        map.put("phoneId", SLFUserCenter.getPhone_id());
        map.put("phoneFactoryModel", SLFUserCenter.getPhoneFactoryModel());
        SLFLogUtil.sdkd(TAG, "提交内容" + map);
        return map;
    }

    private synchronized void uploadvideo(String path, String filename, long id) {
        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::uploadvideo:::slfMediaDataList.size()::" + slfMediaDataList.size());
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (id == slfMediaDataList.get(i).getId()) {
                slfMediaDataList.get(i).setOriginalPath(path);
                slfMediaDataList.get(i).setFileName(filename);
                long percent = progressMap.get(id);
                Log.d("yjjjjjjjjj","percent::::"+percent);
                if (slfMediaDataList.get(i).getUploadPath() == null) {
                    SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::uploadvideo url is null");
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                } else {
                    //slfaddAttachAdapter.notifyDataSetChanged();
                    SLFMediaData slfData = slfMediaDataList.get(i);
                    if (slfData.getUploadStatus().equals(SLFConstants.UPLOADING)) {
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::uploadvideo start");
                        File file = new File(slfMediaDataList.get(i).getOriginalPath());
                        File thumbFile = new File(slfMediaDataList.get(i).getThumbnailSmallPath());
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, "video/mp4", String.valueOf(slfMediaDataList.get(i).getId()), new UploadProgressListener() {
                            @Override
                            public void onProgress(long currentlength, long total) {
                                double progress = (double) (currentlength*1.0/total);
                                long progressFile = (long) (progress*(100-percent));
                                long progressZone = percent + progressFile;
                                if(progressZone<=100){
                                    slfData.setProgress(progressZone);
                                    //slfaddAttachAdapter.notifyDataSetChanged();
                                    runOnUiThread(() -> slfaddAttachAdapter.notifyDataSetChanged());
                                }
                            }
                        },SLFFeedbackSubmitActivity.this);
                        PUNHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, "image/jpg", String.valueOf(slfMediaDataList.get(i).getId()) + "thumb", new UploadProgressListener() {
                            @Override
                            public void onProgress(long currentlength, long total) {

                            }
                        },SLFFeedbackSubmitActivity.this);
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::request uploadvideo net");
                    }
                }
            } else {
                SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::not current video");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFEventNetWorkChange event) {
        if (event.avisible.equals(SLFConstants.NETWORK_UNAVAILABILITY)) {
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                }
            }
            slfaddAttachAdapter.notifyDataSetChanged();
        } else {

        }
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SLFEventCompressVideo event) {

        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::onevent video compress compelete");
        singleUploadVideoExecutor = Executors.newSingleThreadExecutor();
        singleUploadVideoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                uploadvideo(event.path, event.filename, event.id);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SLFEventNoCompressVideo event) {

        SLFLogUtil.sdke(TAG, "ActivityName:" + this.getClass().getSimpleName() + "::onevent video compress not compelete");
        singleUploadVideoExecutor = Executors.newSingleThreadExecutor();
        singleUploadVideoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                uploadvideo(event.path, event.filename, event.id);
            }
        });

    }

    @Override
    public void getSeletedType(Long id, String name, int position, String title, int seletedType,String deviceMoudle) {
        typeSelected = name;
        if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_service_title))) {
            service_checkedPosition = position;
        } else if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_title))) {
            problem_checkedPosition = position;
        } else {
            problem_overview_checkedPosition = position;
        }
        if (seletedType == 1) {
            seletedServiceType = id;
            serviceTypeText = name;
            seleteddeviceMoudle = deviceMoudle;
        } else if (seletedType == 2) {
            seletedProbleType = id;
            categoryText = name;
        } else {
            seletedProblemOverviewType = id;
            subCategoryText = name;
        }
    }

    //获取用户选中信息
    private SLFUserDeviceSaved getUserInfo() {
        SLFUserDeviceSaved userDeviceSaved = null;
        if (seletedServiceType != -1L && seletedServiceType > 10000) {
            userDeviceSaved = SLFUserCenter.getInstance().get(seletedServiceType);
        } else {
            userDeviceSaved = new SLFUserDeviceSaved("", seleteddeviceMoudle, seletedServiceType,
                    "");
        }
            return userDeviceSaved;
        }

    @Override
    protected void onPause() {
        super.onPause();
        //打点退出反馈提交页
        PUTClickAgent.pageTypeAgent(SLFPageAgentEvent.SLF_FeedbackPage,SLFPageAgentEvent.SLF_PAGE_END,null);
    }

    @Override
    public void getCompressProgress(long id, long percent) {
        progressMap.clear();
        progressMap.put(id,percent);
        singleUploadProgress = Executors.newSingleThreadExecutor();
        singleUploadProgress.execute(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<slfMediaDataList.size()-1;i++){
                    if(slfMediaDataList.get(i).getId()==id){
                        slfMediaDataList.get(i).setProgress(percent);
                        runOnUiThread(() -> slfaddAttachAdapter.notifyDataSetChanged());
                    }
                }
            }
        });

    }
}