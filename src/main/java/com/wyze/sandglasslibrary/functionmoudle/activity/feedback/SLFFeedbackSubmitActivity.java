package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
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

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.bean.SLFUserCenter;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFCategoriesResponseBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFCategoryBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFCategoryCommonBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFCategoryDetailBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFProlemDataBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFUploadFileReponseBean;
import com.wyze.sandglasslibrary.commonapi.SLFCommonUpload;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFAndPhotoAdapter;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFCancelOrOkDialog;
import com.wyze.sandglasslibrary.commonui.SLFScrollView;
import com.wyze.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.net.ApiContant;
import com.wyze.sandglasslibrary.net.SLFHttpRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestConstants;
import com.wyze.sandglasslibrary.net.SLFHttpUtils;
import com.wyze.sandglasslibrary.uiutils.SLFEditTextScrollListener;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFCompressUtil;
import com.wyze.sandglasslibrary.utils.SLFPermissionManager;
import com.wyze.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.wyze.sandglasslibrary.utils.SLFRegular;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;
import com.wyze.sandglasslibrary.utils.SLFSubmitFile;
import com.wyze.sandglasslibrary.utils.SLFViewUtil;
import com.wyze.sandglasslibrary.utils.keyboard.SLFSoftKeyBoardListener;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
     * 图片选择统计控件
     */
    private TextView slfPhotoCount;
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
    private int oldEmailLength;
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

    private List<Object> slfServiceTypes = new ArrayList<>();
    private List<SLFCategoryDetailBean> slfProblemTypes = new ArrayList<>();
    private List<SLFCategoryCommonBean> slfProblemOverviewTypes = new ArrayList<>();
    private Map<Integer, List<SLFCategoryDetailBean>> slfServiceMap = new HashMap<Integer, List<SLFCategoryDetailBean>>();
    private Map<Integer, List<SLFCategoryCommonBean>> slfProblemMap = new HashMap<Integer, List<SLFCategoryCommonBean>>();
    private Map<Integer,List<SLFCategoryBean>> slfServiceTitleMap = new HashMap<>();

    //private SLFServiceType slfServiceType;
    //private SLFProblemType slfProblemType;
    //private SLFProblemOverviewType slfProblemOverviewType;

    private SLFCategoryBean slfServiceType;
    private SLFCategoryDetailBean slfProblemType;
    private SLFCategoryCommonBean slfProblemOverviewType;
    private String oldServiceType = "";
    private String oldProblemType = "";

    private  Runnable submitLogRunnable;

    private boolean isSubmit = false;

    private SLFCategoriesResponseBean slfCategoriesResponseBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_photo_selector);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        initTitle();
        initView();
        requestUploadUrls();
        requestAllData();
        initPhontoSelector();
    }

    /**
     * 初始化view
     */
    private void initView() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initView");
        slfScrollView = findViewById(R.id.slf_feed_back_scroll_view);
        slfProblemLinear = findViewById(R.id.problem_type_seletor_linear);
        slfProblemOverviewLinear = findViewById(R.id.problem_overview_seletor_linear);
        slfEditProblem = findViewById(R.id.question_content);
        slfServiceSpinner = findViewById(R.id.spinner_service);
        slfProblemSpinner = findViewById(R.id.spinner_problem);
        slfProblemOverviewSpinner = findViewById(R.id.spinner_problem_overview);
        slfPhotoSelector = findViewById(R.id.slf_gv_add_attach);
        slfFontCount = findViewById(R.id.slf_tv_opinion_textnum);
        slfPhotoCount = findViewById(R.id.slf_photo_count);
        slfSendLogCheck = findViewById(R.id.slf_upload_log);
        slfSumbmit = findViewById(R.id.slf_submit_feedback);
        slfEmailEdit = findViewById(R.id.slf_email_eidt);
        slfEmailError = findViewById(R.id.slf_email_error);
        slfServiceSpinner.setOnClickListener(this);
        slfProblemSpinner.setOnClickListener(this);
        slfProblemOverviewSpinner.setOnClickListener(this);
        slfEditProblem.setOnTouchListener(new SLFEditTextScrollListener(slfEditProblem));
        slfEditProblem.addTextChangedListener(this);
        slfEmailEdit.addTextChangedListener(editWatcher);
        slfEmailEdit.setOnKeyListener(emailKeyLister);
        slfEmailEdit.setOnTouchListener(emailTouchListener);
        slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
        setSubmitBtnCanClick(canSubmit());
        changeTextAndImg(slfServiceSpinner);
        changeTextAndImg(slfProblemSpinner);
        changeTextAndImg(slfProblemOverviewSpinner);
        setListenerFotEditTexts();
        slfEmailHeight = SLFViewUtil.getHeight(slfEmailEdit);
        slfEmailErrorHeight = SLFViewUtil.getHeight(slfEmailError);
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initTitle");
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_title_content));
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
                SLFPermissionManager.getInstance().chekPermissions(SLFFeedbackSubmitActivity.this, permissionStorage, permissionsStroageResult);
            } else {
                picPathLists.clear();
                picPathLists.addAll(slfMediaDataList);
                picPathLists.remove(slfMediaData);
                Intent in = new Intent();
                in.setClass(SLFFeedbackSubmitActivity.this, SLFFeedbackPicPreviewActivity.class);
                in.putExtra("position", position);
                in.putParcelableArrayListExtra("photoPath", picPathLists);
                startActivity(in);
            }
        });
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
        if (view.getId() == R.id.slf_iv_back) {
            if (serviceType || problemType || problemOverviewType || problemEdit || emailEdit||!slfSendLogCheck.isChecked()) {
                showReSureDialog();
            } else {
                finish();
            }
            // finish();
        } else if (view.getId() == R.id.slf_submit_feedback) {
            //TODO submit feedback
            if (slfEditProblem.getText().toString().trim().length() < 10) {
                showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_problem_font_least));
                return;
            }

            if (!SLFRegular.isEmail(slfEmailEdit.getText().toString().trim())) {
                slfEmailError.setVisibility(View.VISIBLE);
                setSubmitBtnCanClick(false);
                oldEmailLength = slfEmailEdit.getText().toString().trim().length();
                return;
            }
            if(slfSendLogCheck.isChecked()){
                showLoading();
                sumbitLogFiles();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    gotoFeedbackSuccess();
                }
            },2000);  //延迟2s// 秒执行

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
                        slfaddAttachAdapter.notifyDataSetChanged();

                    });
        }

        @Override
        public void forbitPermissons() {
//            finish();
            Toast.makeText(SLFFeedbackSubmitActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
        }
    };
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
                if (event.getRawX() >= (slfEmailEdit.getRight() - SLFResourceUtils.px2dp(getContext(), 60) - slfEmailEdit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    clearEmailEdit();
                    setSubmitBtnCanClick(canSubmit());
                    return true;
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
    View.OnKeyListener emailKeyLister = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                hideEmailError();
                if (type && problemEdit && emailEdit) {
                    setSubmitBtnCanClick(true);
                }
            }
            return false;
        }
    };

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
        if (!TextUtils.isEmpty(slfEmailEdit.getText().toString().trim())) {
            if (slfEmailError.getVisibility() == View.VISIBLE) {
                slfEmailError.setVisibility(View.INVISIBLE);
            }
        }
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
            if (slfEmailError.getVisibility() == View.VISIBLE) {
                if (oldEmailLength <= slfEmailWordNum.length()) {
                    slfEmailError.setVisibility(View.VISIBLE);
                    setSubmitBtnCanClick(false);
                } else {
                    if (type && problemEdit && emailEdit) {
                        setSubmitBtnCanClick(true);
                    }
                }
            }
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
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        slfSumbmit.setVisibility(View.VISIBLE);
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
                                    problem_overview_checkedPosition  = -1;
                                }
                                slfProblemLinear.setVisibility(View.VISIBLE);
                            } else {
                                slfProblemLinear.setVisibility(View.GONE);
                                slfProblemOverviewLinear.setVisibility(View.GONE);
                                slfProblemOverviewSpinner.setText("");
                                problem_overview_checkedPosition  = -1;
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
    private void requestAllData(){
        showLoading();
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL+ ApiContant.CATEGORY_URL, SLFCategoriesResponseBean.class,this);
    }

    /**
     * 请求上传文件链接
     */
    private void requestUploadUrls(){
        TreeMap map = new TreeMap();
        map.put("num",9);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL+ApiContant.UPLOAD_FILE_URL,map, SLFUploadFileReponseBean.class,this);
    }

    /**
     * 得到后台配置数据解析
     */
    private SLFCategoriesResponseBean getSLFCategoriesResponseBean(){

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
                if(serviceTypeTitle.sub!=null&& serviceTypeTitle.sub.size()>0){
                    slfServiceTypes.add(serviceTypeTitle.name);
                    slfServiceTypes.addAll(serviceTypeTitle.sub);
                    slfServiceTitleMap.put(serviceTypeTitle.id,serviceTypeTitle.sub);
                    for(int j=0;j< serviceTypeTitle.sub.size();j++){
                        SLFCategoryBean slfServiceType = serviceTypeTitle.sub.get(i);
                        slfServiceMap.put(slfServiceType.id,slfServiceType.sub);
                    }
                }
            }
            if(slfServiceTitleMap!=null&&slfServiceTitleMap.size()>0) {
                setServiceTitleMapConner(slfServiceTitleMap);
            }
        } else {
            showCenterToast("没有获取到问题数据类型");
        }
        return slfServiceTypes;
    }

    /**
     * 获取problem列表
     */
    private List<SLFCategoryDetailBean> getProblemTypeData(SLFCategoryBean serviceType, Map<Integer, List<SLFCategoryDetailBean>> serviceMap) {
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
        return slfProblemTypes;
    }

    /**
     * 获取problemoverview列表
     */
    private List<SLFCategoryCommonBean> getSlfProblemOverviewData(SLFCategoryDetailBean problemType, Map<Integer, List<SLFCategoryCommonBean>> problemMap) {
        slfProblemOverviewTypes.clear();
        if (problemType != null && problemMap != null && problemMap.size() > 0) {
            slfProblemOverviewTypes.addAll(problemMap.get(problemType.id));
            setListRoundConner(slfProblemOverviewTypes);
        }
        return slfProblemOverviewTypes;
    }
    /**serviceType设置圆角方法*/
    private void setServiceTitleMapConner(Map<Integer,List<SLFCategoryBean>> serviceTitleMap){
        Iterator<Map.Entry<Integer,List<SLFCategoryBean>>> it = serviceTitleMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, List<SLFCategoryBean>> entry = it.next();
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
                } else if (obj instanceof SLFCategoryCommonBean){
                    ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ALL_ROUND);
                }
            } else {
                if (i == 0) {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    } else if (obj instanceof SLFCategoryCommonBean){
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_FIRST);
                    }
                } else if (i == list.size() - 1) {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_END);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_END);
                    } else if (obj instanceof SLFCategoryCommonBean){
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_END);
                    }
                } else {
                    if (obj instanceof SLFCategoryBean) {
                        ((SLFCategoryBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    } else if (obj instanceof SLFCategoryDetailBean) {
                        ((SLFCategoryDetailBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    } else if (obj instanceof SLFCategoryCommonBean){
                        ((SLFCategoryCommonBean) obj).setRound_type(SLFConstants.ROUND_MIDDLE);
                    }
                }
            }

        }
    }

    /**sendlog checked*/
    private void sumbitLogFiles(){
            submitLogRunnable = new Runnable() {
                @Override
                public void run() {
                    /**压缩成zip*/
                    SLFCompressUtil.zipFile(SLFConstants.apiLogPath, "*", SLFConstants.feedbacklogPath + "appLog.zip", new SLFCompressUtil.OnCompressSuccessListener() {
                        @Override
                        public void onSuccess() {

                            if (isSubmit) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SLFSubmitFile.submitLogissue(getActivity(),"url");
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure() {
                            isSubmit = false;
                        }
                    });
                }
            };
        }

        /**goto success page*/
        private void gotoFeedbackSuccess(){
            Intent in = new Intent(getContext(),SLFFeedbackSuccessActivity.class);
            startActivity(in);
        }

    @Override
    public void onRequestNetFail() {
        SLFLogUtil.e(TAG,"requestNetFail");
        hideLoading();
    }

    @Override
    public void onRequestSuccess(String result, Object type) {

        if(type instanceof SLFCategoriesResponseBean){
            SLFLogUtil.e(TAG,"requestScucess::SLFCategoriesResponseBean::"+":::type:::"+type.toString());
            this.slfCategoriesResponseBean  = (SLFCategoriesResponseBean)type;
        }else if(type instanceof SLFUploadFileReponseBean){
            SLFLogUtil.e(TAG,"requestScucess::SLFUploadFileReponseBean::"+":::type:::"+type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean)type);
            /**分配前六个链接给图片和视频上传*/
                for(int i=0;i<6;i++){
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.d(TAG,"uploadPath--all----:::"+SLFCommonUpload.getListInstance().get(i));
                }
        }
        hideLoading();
    }

    @Override
    public void onRequestFail(String value, String failCode) {
        SLFLogUtil.e(TAG,"requestFail::"+value+":::failCode:::"+failCode);
        hideLoading();
    }
}