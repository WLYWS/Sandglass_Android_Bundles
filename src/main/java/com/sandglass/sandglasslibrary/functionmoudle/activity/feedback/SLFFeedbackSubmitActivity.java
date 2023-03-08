package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLeaveMsgBean;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLogAttrBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoriesResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryCommonBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryDetailBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCreateFeedbackRepsonseBean;
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
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNoCompressVideo;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
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
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.SLFViewUtil;
import com.sandglass.sandglasslibrary.utils.keyboard.SLFSoftKeyBoardListener;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

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
public class SLFFeedbackSubmitActivity<T> extends SLFBaseActivity implements View.OnClickListener, TextWatcher, SLFBottomDialog.OnSeletedTypeListener, SLFHttpRequestCallback<T> {
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
    private String[] permissionStorage = android.os.Build.VERSION.SDK_INT > 29 ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} : new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
    private Map<Long, List<SLFCategoryDetailBean>> slfServiceMap = new HashMap<Long, List<SLFCategoryDetailBean>>();
    private Map<Long, List<SLFCategoryCommonBean>> slfProblemMap = new HashMap<Long, List<SLFCategoryCommonBean>>();
    private Map<Long, List<SLFCategoryBean>> slfServiceTitleMap = new HashMap<>();


    private SLFCategoryBean slfServiceType;
    private SLFCategoryDetailBean slfProblemType;
    private SLFCategoryCommonBean slfProblemOverviewType;
    private String oldServiceType = "";
    private String oldProblemType = "";

    private Runnable submitLogRunnable;

    private ExecutorService singleThreadExecutor;
    private ExecutorService singleUploadVideoExecutor;

    private boolean isSubmit = false;

    private SLFCategoriesResponseBean slfCategoriesResponseBean;


    private boolean imageSuccessed;

    private String appLogFileName;
    private String firmwareLogFileName;
    private Drawable mClearDrawable;
    private Drawable drawableRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_photo_selector);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        initTitle();
        initView();
        if (SLFCommonUtils.isNetworkAvailable(this)) {
            requestUploadUrls();
            requestAllData();
        } else {
            showNetworkError();
        }
        initPhontoSelector();
    }

    /**
     * 初始化view
     */
    private void initView() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initView");
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
        slfServiceSpinner.setOnClickListener(this);
        slfProblemSpinner.setOnClickListener(this);
        slfProblemOverviewSpinner.setOnClickListener(this);
        slfEditProblem.setOnTouchListener(new SLFEditTextScrollListener(slfEditProblem));
        slfEditProblem.addTextChangedListener(this);
        initDrawableRight(slfEmailEdit);
        setDrawableRightVisble(false, slfEmailEdit);
        slfEmailEdit.addTextChangedListener(editWatcher);
        //slfEmailEdit.setOnKeyListener(emailKeyLister);
        slfEmailEdit.setOnTouchListener(emailTouchListener);
        slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
        SLFFontSet.setSLF_RegularFont(getContext(),selector_service_title);
        SLFFontSet.setSLF_RegularFont(getContext(),selector_problem_title);
        SLFFontSet.setSLF_RegularFont(getContext(),selector_problem_overview_title);
        SLFFontSet.setSLF_RegularFont(getContext(),slfServiceSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(),slfProblemSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(),slfProblemOverviewSpinner);
        SLFFontSet.setSLF_RegularFont(getContext(),problem_description_title);
        SLFFontSet.setSLF_RegularFont(getContext(),slfFontCount);
        SLFFontSet.setSLF_RegularFont(getContext(),slfEditProblem);
        SLFFontSet.setSLF_RegularFont(getContext(),selector_email_title);
        SLFFontSet.setSLF_RegularFont(getContext(),slfEmailError);
        SLFFontSet.setSLF_RegularFont(getContext(),slfEmailEdit);
        SLFFontSet.setSLF_RegularFont(getContext(),slfSendLogCheck);
        SLFFontSet.setSLF_MediumFontt(getContext(),slfSumbmit);
        setSubmitBtnCanClick(canSubmit());
        changeTextAndImg(slfServiceSpinner);
        changeTextAndImg(slfProblemSpinner);
        changeTextAndImg(slfProblemOverviewSpinner);
        setListenerFotEditTexts();
        slfEmailHeight = SLFViewUtil.getHeight(slfEmailEdit);
        slfEmailErrorHeight = SLFViewUtil.getHeight(slfEmailError);
        slfEmailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": EmailEdit onFocus");
                    setDrawableRightVisble(slfEmailEdit.getText().length() > 0, slfEmailEdit);
                } else {
                    SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": EmailEdit no Focus");
                    setDrawableRightVisble(false, slfEmailEdit);
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initTitle");
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_title_content));
        SLFFontSet.setSLF_RegularFont(getContext(),slfTitle);
        slfBack.setOnClickListener(this);
    }


    /**
     * 初始化选择图片控件相关
     */
    private void initPhontoSelector() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initPhontoSelector");
        picPathLists = new ArrayList<>();
        slfMediaData = new SLFMediaData();
        slfMediaData.setId(SystemClock.elapsedRealtime());
        slfMediaDataList.add(this.slfMediaData);
        /**初始化显示选中图片缩略图的adapter*/
        final SLFAndPhotoAdapter slfaddAttachAdapter = new SLFAndPhotoAdapter(getContext(), slfMediaDataList);
        this.slfaddAttachAdapter = slfaddAttachAdapter;
        slfPhotoSelector.setAdapter(slfaddAttachAdapter);

        slfPhotoSelector.setOnItemClickListener((parent, view, position, id) -> {
            if (position == slfMediaDataList.size() - 1 && TextUtils.isEmpty(slfMediaDataList.get(position).getOriginalPath())) {
                SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": click add photo or video permissions");
                SLFPermissionManager.getInstance().chekPermissions(SLFFeedbackSubmitActivity.this, permissionStorage, permissionsStroageResult);
            } else {
                SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": click goto preview");
                picPathLists.clear();
                picPathLists.addAll(slfMediaDataList);
                picPathLists.remove(slfMediaData);
                if (!slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                    Intent in = new Intent();
                    in.putExtra("from","feedback");
                    in.setClass(SLFFeedbackSubmitActivity.this, SLFFeedbackPicPreviewActivity.class);
                    in.putExtra("position", position);
                    in.putParcelableArrayListExtra("photoPath", picPathLists);
                    startActivity(in);
                    SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": goto preview complete"+":picPathLists size:"+picPathLists.size());
                } else {
                   // showCenterToast("正在上传……");
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
        //SLFLogUtil.d(TAG,"feedbacksubmit onClick");
        if (SLFFastClickUtils.isFastDoubleClick(500)) {
            return;
        }
        if (view.getId() == R.id.slf_iv_back) {
            if (serviceType || problemType || problemOverviewType || problemEdit || emailEdit || (slfMediaDataList.size() - 1 > 0)) {
                showReSureDialog();
            } else {
                finish();
            }
            // finish();
        } else if (view.getId() == R.id.slf_submit_feedback) {
            //TODO submit feedback
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
                if (slfSendLogCheck.isChecked()) {
                    showLoading();
                    //sumbitLogFiles();
                    if (SLFApi.getInstance(getContext()).getAppLogCallBack() != null) {
                        SLFApi.getInstance(getContext()).getAppLogCallBack().getUploadAppLogUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(7)).uploadUrl,
                                SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(8)).uploadUrl);
                    }
                    SLFApi.getInstance(getContext()).setUploadLogCompleteCallBack(new SLFUploadCompleteCallback() {
                        @Override
                        public void isUploadComplete(boolean isComplete, String appFileName, String firmwarFileName) {
                            SLFLogUtil.d("yj", "complete--callback--");
                            appLogFileName = appFileName;
                            firmwareLogFileName = firmwarFileName;
                            sumbitLogFiles();
                        }
                    });
                } else {
                    showLoading();
                    SLFHttpUtils.getInstance().executePost(getContext(), SLFHttpRequestConstants.BASE_URL + SLFApiContant.CREATE_FEEDBACK_URL, getCreateFeedBackTreemap(), SLFCreateFeedbackRepsonseBean.class, this);
                }
            } else {
                showCenterToast(SLFResourceUtils.getString(R.string.slf_submit_if_uploading));
            }


        } else if (view.getId() == R.id.spinner_service) {
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_service_title), getServiceTypeData(getSLFCategoriesResponseBean()), service_checkedPosition);
            changeTextAndImg(slfServiceSpinner);
        } else if (view.getId() == R.id.spinner_problem) {
            //TODO selector problem
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_title), getProblemTypeData(slfServiceType, slfServiceMap), problem_checkedPosition);
            changeTextAndImg(slfProblemSpinner);
        } else if (view.getId() == R.id.spinner_problem_overview) {
            //TODO selector problem overview
            showServiceTypeDialog(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_overview_title), getSlfProblemOverviewData(slfProblemType, slfProblemMap), problem_overview_checkedPosition);
            changeTextAndImg(slfProblemOverviewSpinner);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (serviceType || problemType || problemOverviewType || problemEdit || emailEdit || (slfMediaDataList.size() - 1 > 0)) {
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
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": click add photo or video uploaded files");
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": selectedNum:"+(4 - slfMediaDataList.size()));
        }

        @Override
        public void forbitPermissons() {
//            finish();
            SLFLogUtil.d(TAG,"ActivityName:"+this.getClass().getSimpleName()+": permissions not passed");
            Toast.makeText(SLFFeedbackSubmitActivity.this, "permissions not passed!", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 上传图片或视频
     */
    private void uploadFiles() {
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":uploadFiles:::" + slfMediaDataList.size());
        String contentType = "";
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (slfMediaDataList.get(i).getUploadPath() != null&&SLFCommonUtils.isNetworkAvailable(this)) {
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
                    SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, contentType, String.valueOf(slfMediaDataList.get(i).getId()), this);
                    SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, contentType,String.valueOf(slfMediaDataList.get(i).getId())+"thumb" , this);
                    SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":uploadFiles requested completed");
                }
            } else {
                slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                //slfaddAttachAdapter.notifyDataSetChanged();
                SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":uploadFiles uploadUrl is null");
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
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":click clean Email");
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
        if (slfProblemWordNum.length() <= 1000) {
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
        setSubmitBtnCanClick(canSubmit());
    }

    /**
     * email的删除输入监听
     */
//    View.OnKeyListener emailKeyLister = new View.OnKeyListener() {
//        @Override
//        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                hideEmailError();
//                if (type && problemEdit && emailEdit) {
//                    setSubmitBtnCanClick(true);
//                }
//            }
//            return false;
//        }
//    };

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
                } else {
                    problemType = false;
                }
            }
        } else {
            serviceType = false;
        }

        if (serviceType && problemType && problemOverviewType) {
            type = true;
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

        if (type && problemEdit && emailEdit) {
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
                        slfSumbmit.setVisibility(View.INVISIBLE);
                        if (slfEmailEdit.hasFocus()) {
                            slfScrollView.smoothScrollTo(0, slfKeyBoardHeight + slfEmailErrorHeight + slfEmailHeight + SLFResourceUtils.dp2px(getContext(), 3));
                        }
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":keybord show");
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        slfSumbmit.setVisibility(View.VISIBLE);
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":keybord hide");
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
                            showCenterToast("获取服务类型失败");
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
                                }
                                slfProblemOverviewLinear.setVisibility(View.VISIBLE);
                                oldProblemType = slfProblemType.name;
                            } else {
                                slfProblemOverviewLinear.setVisibility(View.GONE);
                            }
                        } else {
                            showCenterToast("获取问题类型失败");
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
                SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":show bottom dialog");
            }
        });
    }

    @Override
    public void getSeletedType(String name, int position, String title) {
        typeSelected = name;
        if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_service_title))) {
            service_checkedPosition = position;
        } else if (title.equals(SLFResourceUtils.getString(R.string.slf_feedback_selector_problem_title))) {
            problem_checkedPosition = position;
        } else {
            problem_overview_checkedPosition = position;
        }

    }

    /**
     * 请求后台配置数据
     */
    private void requestAllData() {
        showLoading();
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.CATEGORY_URL, SLFCategoriesResponseBean.class, this);
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":request servicetype/problemtype/subtype data");
    }

    /**
     * 请求上传文件链接
     */
    private void requestUploadUrls() {
        TreeMap map = new TreeMap();
        map.put("num", 9);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.UPLOAD_FILE_URL, map, SLFUploadFileReponseBean.class, this);
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":request upload url 9");
    }

    /**
     * 得到后台配置数据解析
     */
    private SLFCategoriesResponseBean getSLFCategoriesResponseBean() {

        return slfCategoriesResponseBean;
    }

    /**
     * 获取servicetype列表
     */
    private List<Object> getServiceTypeData(SLFCategoriesResponseBean slfCategoriesResponseBean) {

        if (slfCategoriesResponseBean != null && slfCategoriesResponseBean.data != null && slfCategoriesResponseBean.data.size() > 0) {
            slfServiceTypes.clear();
            slfServiceMap.clear();
            slfServiceTitleMap.clear();
            for (int i = 0; i < slfCategoriesResponseBean.data.size(); i++) {
                SLFProlemDataBean serviceTypeTitle = slfCategoriesResponseBean.data.get(i);
                if (serviceTypeTitle.sub != null && serviceTypeTitle.sub.size() > 0) {
                    slfServiceTypes.add(serviceTypeTitle.name);
                    slfServiceTypes.addAll(serviceTypeTitle.sub);
                    slfServiceTitleMap.put(serviceTypeTitle.id, serviceTypeTitle.sub);
                    for (int j = 0; j < serviceTypeTitle.sub.size(); j++) {
                        SLFCategoryBean slfServiceType = serviceTypeTitle.sub.get(i);
                        slfServiceMap.put(slfServiceType.id, slfServiceType.sub);
                    }
                }
            }
            if (slfServiceTitleMap != null && slfServiceTitleMap.size() > 0) {
                setServiceTitleMapConner(slfServiceTitleMap);
            }
            SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":get servicetype data success");
        } else {
            showCenterToast("没有获取到问题数据类型");
            SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":get servicetype data fail");
        }
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
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":get problemtype data success");
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
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":get problemOverview data success");
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
                if (entry.getValue().size() == 1) {
                    entry.getValue().get(i).setRound_type(SLFConstants.ALL_ROUND);
                } else {
                    if (i == 0) {
                        entry.getValue().get(i).setRound_type(SLFConstants.ROUND_FIRST);
                    } else if (i == entry.getValue().size() - 1) {
                        entry.getValue().get(i).setRound_type(SLFConstants.ROUND_END);
                    } else {
                        entry.getValue().get(i).setRound_type(SLFConstants.ROUND_MIDDLE);
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
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":submit log files");
    }

    /**
     * init sendlog
     */
    private void initLogFiles() {
        submitLogRunnable = new Runnable() {
            @Override
            public void run() {
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
                                    SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::logFile.size------::" + logFile.length());
                                    String uploadUrl = SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(6)).uploadUrl;
                                    SLFHttpUtils.getInstance().executePutFile(getContext(), uploadUrl, logFile, "application/zip", "6", SLFFeedbackSubmitActivity.this);

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure() {
                        isSubmit = false;
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::compress  logFils error");
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
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::go to feedback success page ::logid::"+logId);
        startActivity(in);
    }

    @Override
    public void onRequestNetFail(Object type) {
        SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::requestNetFail");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("6".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            } else {
                for(int i=0;i<slfMediaDataList.size()-1;i++){
                    if(code.equals(String.valueOf(slfMediaDataList.get(i).getId()))){
                        slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                    }
                }
                slfaddAttachAdapter.notifyDataSetChanged();
                showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
            }
        }else{
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
    }

    @Override
    public void onRequestSuccess(String result, Object type) {

        if (type instanceof SLFCategoriesResponseBean) {
            SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::requestScucess::SLFCategoriesResponseBean::" + ":::type:::" + type.toString());
            this.slfCategoriesResponseBean = (SLFCategoriesResponseBean) type;
        } else if (type instanceof SLFUploadFileReponseBean) {
            SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::requestScucess::SLFUploadFileReponseBean::" + ":::type:::" + type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean) type,9);
            if(SLFCommonUpload.getInstance()!=null&&SLFCommonUpload.getInstance().size()>0&&SLFCommonUpload.getListInstance()!=null&&SLFCommonUpload.getListInstance().size()>0) {
                /**分配前六个链接给图片和视频上传*/
                for (int i = 0; i < 6; i++) {
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::uploadPath--all----:::" + SLFCommonUpload.getListInstance().get(i));
                }
            }
        } else if (type instanceof String) {
            String code = (String) type;
            SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::requestScucess::Integer::" + ":::type:::" + type);
            if ("6".equals(code)) {
                SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::logfile-----upload---complete");
                SLFHttpUtils.getInstance().executePost(getContext(), SLFHttpRequestConstants.BASE_URL + SLFApiContant.CREATE_FEEDBACK_URL, getCreateFeedBackTreemap(), SLFCreateFeedbackRepsonseBean.class, this);
            } else {
                resultUploadImageOrVideo(code);
            }
        } else if (type instanceof SLFCreateFeedbackRepsonseBean) {
            SLFLogUtil.d("yj", "ActivityName:"+this.getClass().getSimpleName()+"::createFeedback-----success:" + ((SLFCreateFeedbackRepsonseBean) type).data);
            gotoFeedbackSuccess(((SLFCreateFeedbackRepsonseBean) type).data);
        }
        hideLoading();
    }

    @Override
    public void onRequestFail(String value, String failCode, Object type) {
        SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::requestFail::" + value + ":::failCode:::" + failCode);
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("6".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            } else {
                for(int i=0;i<slfMediaDataList.size()-1;i++){
                    if(code.equals(String.valueOf(slfMediaDataList.get(i).getId()))){
                        slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                    }
                }
                slfaddAttachAdapter.notifyDataSetChanged();
                showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
            }
        }else{
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
        }

    }

    private synchronized void resultUploadImageOrVideo(String code) {
        for(int i=0;i<slfMediaDataList.size()-1;i++){
            if(code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                imageSuccessed = true;
                resultCodeMethod(code, imageSuccessed);
            }
        }
    }

    private void resultCodeMethod(String code, boolean imageSuccess) {
        for(int i=0;i<slfMediaDataList.size()-1;i++){
            if(code.equals(String.valueOf(slfMediaDataList.get(i).getId()))){
                if (slfMediaDataList.get(i).getUploadPath() != null) {
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                    slfaddAttachAdapter.notifyDataSetChanged();
                    imageSuccess = false;
                }else {
                    imageSuccess = false;
                }
            }
        }
    }


    private void setUploadUrl() {

        for (int position = 0; position < slfMediaDataList.size() - 1; position++) {
            if (slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADIDLE)) {
                if (SLFCommonUpload.getListInstance().size() == 9 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)) != null && SLFCommonUpload.getInstance().size() == 9) {
                    if (SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(0));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(1));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 9 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(2));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(3));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 9 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle) {
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

    private TreeMap<String, Object> getCreateFeedBackTreemap() {
        ArrayList<SLFLogAttrBean> logAttrBeans = new ArrayList<>();
        ArrayList<SLFLeaveMsgBean> attrList = new ArrayList<>();
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("deviceId", SLFUserCenter.deviceId);
        map.put("deviceModel", SLFUserCenter.deviceModel);
        map.put("deviceTimezone", SLFUserCenter.getDeviceTimeZone());
        map.put("serviceType", slfServiceType.id);
        if (slfProblemLinear.getVisibility() == View.VISIBLE) {
            map.put("category", slfProblemType.id);
        }
        if (slfProblemOverviewLinear.getVisibility() == View.VISIBLE) {
            map.put("subCategory", slfProblemOverviewType.id);
        }
        map.put("content", slfEditProblem.getText().toString());
        map.put("email", slfEmailEdit.getText().toString().trim());
        map.put("phone", "18611223366");
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
            SLFLogAttrBean logAttrFirmwareBean = new SLFLogAttrBean();

            logAttrFirmwareBean.setPath(SLFCommonUpload.getListInstance().get(8));
            if (!TextUtils.isEmpty(firmwareLogFileName)) {
                logAttrFirmwareBean.setFileName(firmwareLogFileName);
            }
            logAttrFirmwareBean.setContentType("application/zip");
            /*sdkLogBean*/
            SLFLogAttrBean logAttrPluginBean = new SLFLogAttrBean();
            logAttrPluginBean.setPath(SLFCommonUpload.getListInstance().get(6));
            logAttrPluginBean.setFileName("sdkLog.zip");
            logAttrPluginBean.setContentType("application/zip");
            logAttrBeans.add(logAttrAppBean);
            logAttrBeans.add(logAttrFirmwareBean);
            logAttrBeans.add(logAttrPluginBean);
            map.put("logAttrList", logAttrBeans);
        } else {
            map.put("sendLog", 0);
        }
        if (slfMediaDataList.size() - 1 > 0) {
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
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
            map.put("attrList", attrList);
        }
        map.put("firmwareVersion", SLFUserCenter.firmwareVersion);
        map.put("appVersion", SLFUserCenter.getAppVersionName());
        map.put("pluginVersion", SLFUserCenter.getPluginversion());
        map.put("phoneType", 2);
        SLFLogUtil.d("yj","手机型号："+SLFUserCenter.getPhoneModel());
        map.put("phoneModel", SLFUserCenter.getPhoneModel());
        map.put("phoneOsVersion", SLFUserCenter.getOSVersion());
        map.put("phoneId", SLFUserCenter.getPhone_id());
        map.put("phoneFactoryModel", SLFUserCenter.getPhoneFactoryModel());

        return map;
    }

    private synchronized void uploadvideo(String path, String filename,long id) {
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::uploadvideo:::slfMediaDataList.size()::" + slfMediaDataList.size());
        for(int i=0;i<slfMediaDataList.size()-1;i++){
            if(id == slfMediaDataList.get(i).getId()){
                slfMediaDataList.get(i).setOriginalPath(path);
                slfMediaDataList.get(i).setFileName(filename);
                if (slfMediaDataList.get(i).getUploadPath() != null&&SLFCommonUtils.isNetworkAvailable(this)) {
                    if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::uploadvideo start");
                        File file = new File(slfMediaDataList.get(i).getOriginalPath());
                        File thumbFile = new File(slfMediaDataList.get(i).getThumbnailSmallPath());
                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, "video/mp4", String.valueOf(slfMediaDataList.get(i).getId()), SLFFeedbackSubmitActivity.this);
                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, "image/jpg", String.valueOf(slfMediaDataList.get(i).getId())+"thumb", SLFFeedbackSubmitActivity.this);
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::request uploadvideo net");
                    }
                } else {
                    SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::uploadvideo url is null");
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                    //slfaddAttachAdapter.notifyDataSetChanged();
                }
            }else{
                SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::not current video");
            }
        }
//        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
//            if (slfMediaDataList.get(i).equals(slfMediaData)) {
//                slfMediaDataList.get(i).setOriginalPath(path);
//                slfMediaDataList.get(i).setFileName(filename);
//                if (slfMediaDataList.get(i).getUploadPath() != null) {
//                    if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
//                        SLFLogUtil.d("videocompress", "compelete---");
//                        File file = new File(slfMediaDataList.get(i).getOriginalPath());
//                        File thumbFile = new File(slfMediaDataList.get(i).getThumbnailSmallPath());
//                        SLFLogUtil.d("videocompress", "compelete--222222-");
//                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, "video/mp4", String.valueOf(slfMediaDataList.get(i).getId()), SLFFeedbackSubmitActivity.this);
//                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, "image/jpg", String.valueOf(slfMediaDataList.get(i).getId())+"thumb", SLFFeedbackSubmitActivity.this);
//                        SLFLogUtil.d("videocompress", "compelete--33333-");
//
//                    }
//                } else {
//                    SLFLogUtil.d("videocompress", "compelete---url---null");
//                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
//                    slfaddAttachAdapter.notifyDataSetChanged();
//                }
//            } else {
//                SLFLogUtil.d("videocompress", "compelete---object--not--equals");
//            }

        //}
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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SLFEventCompressVideo event) {

        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::onevent video compress compelete");
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

        SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::onevent video compress not compelete");
        singleUploadVideoExecutor = Executors.newSingleThreadExecutor();
        singleUploadVideoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                uploadvideo(event.path, event.filename, event.id);
            }
        });

    }
}