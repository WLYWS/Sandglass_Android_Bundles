package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonapi.SLFCommonUpload;
import com.sandglass.sandglasslibrary.commonui.SLFScrollView;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFAndPhotoAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventCompressVideo;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNoCompressVideo;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLeaveMsgBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFRecord;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFSendLeaveMsgRepsonseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUploadFileReponseBean;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFEditTextScrollListener;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFFastClickUtils;
import com.sandglass.sandglasslibrary.utils.SLFPermissionManager;
import com.sandglass.sandglasslibrary.utils.SLFPhotoSelectorUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.keyboard.SLFSoftKeyBoardListener;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Greated by yangjie
 * descripe:继续留言
 * time:2023/2/7
 */
public class SLFContinueLeaveMsgActivity<T> extends SLFBaseActivity implements View.OnClickListener, TextWatcher, SLFHttpRequestCallback<T> {
    /**
     * scollrview控件
     */
    private SLFScrollView slfScrollView;
    /**
     * 标题栏标题文本
     */
    private TextView slfTitle;
    private TextView slf_continue_leave_descripe_title;
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
     * 键盘高度
     */
    private int slfKeyBoardHeight;
    /**
     * send按钮
     */
    private Button slf_send_btn;
    private ExecutorService singleUploadVideoExecutor;
    private boolean imageSuccessed;
    boolean hasUploadingFile = false;
    private SLFRecord slfRecord;
    /**多媒体附件*/
    private List<SLFLeveMsgRecordMoudle> attrlistResponselist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_continue_leave_msg);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        initTitle();
        initView();
        if (SLFCommonUtils.isNetworkAvailable(this)) {
            requestUploadUrls();
        } else {
            showNetworkError();
        }
        initPhontoSelector();
    }

    /**
     * 请求上传文件链接
     */
    private void requestUploadUrls() {
        TreeMap map = new TreeMap();
        map.put("num", 6);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.UPLOAD_FILE_URL, map, SLFUploadFileReponseBean.class, this);
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_list_leave_message_bottom_text));
        SLFFontSet.setSLF_MediumFontt(getContext(),slfTitle);
        slfBack.setOnClickListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {
        //SLFLogUtil.d(TAG,"feedbacksubmit initView");
        slfScrollView = findViewById(R.id.slf_continue_leave_scroll_view);
        slfEditProblem = findViewById(R.id.slf_continue_leave_question_content);
        slfPhotoSelector = findViewById(R.id.slf_continue_leave_gv_add_attach);
        slfFontCount = findViewById(R.id.slf_continue_leave_opinion_textnum);
        slf_send_btn = findViewById(R.id.slf_continue_leave_send);
        slf_continue_leave_descripe_title = findViewById(R.id.slf_continue_leave_descripe_title);
        setListenerFotEditTexts();
        slfEditProblem.setOnTouchListener(new SLFEditTextScrollListener(slfEditProblem));
        slfEditProblem.addTextChangedListener(this);
        slf_send_btn.setOnClickListener(this);
        slfFontCount.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_font_count, slfProblemWordNum.length()));
        slfRecord = (SLFRecord) getIntent().getSerializableExtra(SLFConstants.RECORD_DATA);
        SLFFontSet.setSLF_RegularFont(getContext(),slfEditProblem);
        SLFFontSet.setSLF_RegularFont(getContext(),slfFontCount);
        SLFFontSet.setSLF_MediumFontt(getContext(),slf_send_btn);
        SLFFontSet.setSLF_MediumFontt(getContext(),slf_continue_leave_descripe_title);
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
                SLFPermissionManager.getInstance().chekPermissions(SLFContinueLeaveMsgActivity.this, permissionStorage, permissionsStroageResult);
            } else {
                picPathLists.clear();
                picPathLists.addAll(slfMediaDataList);
                picPathLists.remove(slfMediaData);
                if (!slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                    Intent in = new Intent();
                    in.putExtra("from", "feedback");
                    in.setClass(SLFContinueLeaveMsgActivity.this, SLFFeedbackPicPreviewActivity.class);
                    in.putExtra("position", position);
                    in.putParcelableArrayListExtra("photoPath", picPathLists);
                    startActivity(in);
                } else {
                    // showCenterToast("正在上传……");
                }
            }
        });
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
            SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":leaveMsg photo permission  pass !!!");
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
        }

        @Override
        public void forbitPermissons() {
//            finish();
//            Toast.makeText(SLFContinueLeaveMsgActivity.this, "权限不通过!", Toast.LENGTH_SHORT).show();
            SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":leaveMsg photo permission not pass");
        }
    };

    /**
     * 上传图片或视频
     */
    private void uploadFiles() {
        SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":leaveMsg upload files");
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
                    SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, contentType, String.valueOf(slfMediaDataList.get(i).getId()) + "thumb", this);
                }
            } else {
                slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":leaveMsg upload files no network or uploadPath is null");
                //slfaddAttachAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setUploadUrl() {
        for (int position = 0; position < slfMediaDataList.size() - 1; position++) {
            if (slfMediaDataList.get(position).getUploadStatus().equals(SLFConstants.UPLOADIDLE)) {
                if (SLFCommonUpload.getListInstance().size() == 6 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)) != null && SLFCommonUpload.getInstance().size() == 6) {
                    if (SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(0));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(1));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 6 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle) {
                        slfMediaDataList.get(position).setUploadPath(SLFCommonUpload.getListInstance().get(2));
                        slfMediaDataList.get(position).setUploadThumPath(SLFCommonUpload.getListInstance().get(3));
                        slfMediaDataList.get(position).setUploadUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).uploadUrl);
                        slfMediaDataList.get(position).setUploadThumurl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).uploadUrl);
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).isIdle = false;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(3)).isIdle = false;
                    } else if (SLFCommonUpload.getListInstance().size() == 6 && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(4)).isIdle && SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(5)).isIdle) {
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


    //键盘监听事件
    public void setListenerFotEditTexts() {
        //键盘监听事件
        SLFSoftKeyBoardListener.setListener(SLFContinueLeaveMsgActivity.this,
                new SLFSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        slf_send_btn.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        slf_send_btn.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (SLFFastClickUtils.isFastDoubleClick(500)) {
            return;
        }
        if (view.getId() == R.id.slf_iv_back) {
            finish();
        } else if (view.getId() == R.id.slf_continue_leave_send) {
//            if (slfEditProblem.getText().toString().length() < 10) {
//                showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_problem_font_least));
//                return;
//            }

            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                    hasUploadingFile = true;
                } else {
                    hasUploadingFile = false;
                }
            }
            if (!hasUploadingFile) {
                if(slfRecord!=null&&!TextUtils.isEmpty(slfRecord.getContent())) {
                    showLoading();
                    SLFHttpUtils.getInstance().executePost(getContext(), SLFHttpRequestConstants.BASE_URL + SLFApiContant.POST_FEEDBACK_URL.replace("{id}",String.valueOf(slfRecord.getId())),getSendHistory(), SLFSendLeaveMsgRepsonseBean.class, this);
                }else{
                    showCenterToast("data is error");
                }
            } else {
                showCenterToast(SLFResourceUtils.getString(R.string.slf_submit_if_uploading));
            }
        }
    }

    /**
     * send feedback按钮是否可点击
     */
    private void setSendBtnCanClick(boolean isClick) {

        if (isClick) {
            slf_send_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_submit_bg));
            slf_send_btn.setTextColor(SLFResourceUtils.getColor(R.color.black));
            slf_send_btn.setClickable(true);
            slf_send_btn.setOnClickListener(this);
        } else {
            slf_send_btn.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_page_child_bg));
            slf_send_btn.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_question_type_title));
            slf_send_btn.setClickable(false);
        }
    }


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
        if (slfProblemWordNum.length() > 1000) {
            setSendBtnCanClick(false);
        } else {
            if(slfProblemWordNum.length() >0) {
                setSendBtnCanClick(true);
            }else{
                setSendBtnCanClick(false);
            }
        }
    }

    @Override
    public void onRequestNetFail(T type) {
        SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":leaveMsg onRequestNetFail");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                }
            }
            slfaddAttachAdapter.notifyDataSetChanged();
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        } else {
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        if (type instanceof SLFUploadFileReponseBean) {
            SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::requestScucess:::contiuneLeave::SLFUploadFileReponseBean::" + ":type:" + type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean) type,6);
            if(SLFCommonUpload.getInstance()!=null&&SLFCommonUpload.getInstance().size()>0&&SLFCommonUpload.getListInstance()!=null&&SLFCommonUpload.getListInstance().size()>0) {
                /**分配六个链接给图片和视频上传*/
                for (int i = 0; i < 6; i++) {
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.e(TAG,"ActivityName:"+this.getClass().getSimpleName()+":uploadPath--all-contiuneLeave---:" + SLFCommonUpload.getListInstance().get(i));
                }
            }
            hideLoading();
        } else if (type instanceof String) {
            String code = (String) type;
            SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+":requestScucess::contiuneLeave：:Integer::" + ":::type:::" + type);
            resultUploadImageOrVideo(code);
            hideLoading();
        } else if (type instanceof SLFSendLeaveMsgRepsonseBean) {
            SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":createFeedback--request--success:" + ((SLFSendLeaveMsgRepsonseBean) type).toString());
            SLFLeaveMsgRecord slfLeaveMsgRecord = new SLFLeaveMsgRecord(slfEditProblem.getText().toString(),System.currentTimeMillis(),true,attrlistResponselist);
            Intent in = new Intent();
            in.putExtra(SLFConstants.LEAVE_MSG_DATA,slfLeaveMsgRecord);
            setResult(Activity.RESULT_OK,in);
            hideLoading();
            finish();
        }

    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        SLFLogUtil.e(TAG, "ActivityName:"+this.getClass().getSimpleName()+":requestFail:continueleave:" + value + ":failCode:" + failCode);
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADFAIL);
                }
            }
            slfaddAttachAdapter.notifyDataSetChanged();
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
        } else {
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
        }
    }

    private synchronized void uploadvideo(String path, String filename, long id) {
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+"::continueleave:slfMediaDataList.size()::" + slfMediaDataList.size());
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (id == slfMediaDataList.get(i).getId()) {
                slfMediaDataList.get(i).setOriginalPath(path);
                slfMediaDataList.get(i).setFileName(filename);
                if (slfMediaDataList.get(i).getUploadPath() != null&&SLFCommonUtils.isNetworkAvailable(this)) {
                    if (slfMediaDataList.get(i).getUploadStatus().equals(SLFConstants.UPLOADING)) {
                        File file = new File(slfMediaDataList.get(i).getOriginalPath());
                        File thumbFile = new File(slfMediaDataList.get(i).getThumbnailSmallPath());
                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadUrl(), file, "video/mp4", String.valueOf(slfMediaDataList.get(i).getId()), SLFContinueLeaveMsgActivity.this);
                        SLFHttpUtils.getInstance().executePutFile(getContext(), slfMediaDataList.get(i).getUploadThumurl(), thumbFile, "image/jpg", String.valueOf(slfMediaDataList.get(i).getId()) + "thumb", SLFContinueLeaveMsgActivity.this);
                        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":continueleave:compelete");
                    }
                } else {
                    SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":continueleave:compelete:url:null");
                    slfMediaDataList.get(i).setUploadStatus(SLFConstants.UPLOADED);
                    //slfaddAttachAdapter.notifyDataSetChanged();
                }
            } else {
                SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":continueleave:::compelete object not equals");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SLFEventCompressVideo event) {
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":continueleave:onevent compelete");
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

        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":continueleave:onevent no compelete");
        singleUploadVideoExecutor = Executors.newSingleThreadExecutor();
        singleUploadVideoExecutor.execute(new Runnable() {
            @Override
            public void run() {
                uploadvideo(event.path, event.filename, event.id);
            }
        });

    }

    private synchronized void resultUploadImageOrVideo(String code) {
        for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
            if (code.equals(String.valueOf(slfMediaDataList.get(i).getId()))) {
                imageSuccessed = true;
                resultCodeMethod(code, imageSuccessed);
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

    /**创建send对象*/
    private TreeMap<String, Object> getSendHistory() {
        List<SLFLeaveMsgBean> attrList = new ArrayList<>();
        attrlistResponselist.clear();
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("content", slfEditProblem.getText().toString().trim());
        if (slfMediaDataList.size() - 1 > 0) {
            for (int i = 0; i < slfMediaDataList.size() - 1; i++) {
                SLFLeaveMsgBean slfLeaveMsgBean = new SLFLeaveMsgBean();
                SLFLeveMsgRecordMoudle slfLeveMsgRecordMoudle = new SLFLeveMsgRecordMoudle();
                slfLeaveMsgBean.setPath(slfMediaDataList.get(i).getUploadPath());
                slfLeveMsgRecordMoudle.setUrl(slfMediaDataList.get(i).getOriginalPath());
                slfLeaveMsgBean.setThumbnailPath(slfMediaDataList.get(i).getUploadThumPath());
                slfLeveMsgRecordMoudle.setThumbnailUrl(slfMediaDataList.get(i).getThumbnailSmallPath());
                slfLeaveMsgBean.setFileName(slfMediaDataList.get(i).getFileName());
                slfLeveMsgRecordMoudle.setFileName(slfMediaDataList.get(i).getFileName());
                slfLeaveMsgBean.setThumbnailContentType("image/png");
                slfLeveMsgRecordMoudle.setThumbnailContentType("image/png");
                if (slfMediaDataList.get(i).getMimeType().contains("video")) {
                    slfLeaveMsgBean.setContentType("video/mp4");
                    slfLeveMsgRecordMoudle.setContentType("video/mp4");
                } else if (slfMediaDataList.get(i).getMimeType().contains("png")) {
                    slfLeaveMsgBean.setContentType("image/png");
                    slfLeveMsgRecordMoudle.setContentType("image/png");
                } else if (slfMediaDataList.get(i).getMimeType().contains("jpg")) {
                    slfLeaveMsgBean.setContentType("image/jpg");
                    slfLeveMsgRecordMoudle.setContentType("image/jpg");
                } else if (slfMediaDataList.get(i).getMimeType().contains("jpeg")) {
                    slfLeaveMsgBean.setContentType("image/jpeg");
                    slfLeveMsgRecordMoudle.setContentType("image/jpeg");
                }
                attrList.add(slfLeaveMsgBean);
                attrlistResponselist.add(slfLeveMsgRecordMoudle);
            }
            map.put("attrList", attrList);
        }
        SLFLogUtil.d(TAG, "ActivityName:"+this.getClass().getSimpleName()+":getSendHistory:map:"+map.toString());
        return map;
    }

}
