package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.commonapi.SLFCommonUpload;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFRefreshFooter;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFRereshHeader;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFSmartRefreshLayout;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFeedbackDetailAdapter;
import com.sandglass.sandglasslibrary.interf.SLFUploadCompleteCallback;
import com.sandglass.sandglasslibrary.moudle.event.SLFSendLogSuceessEvent;
import com.sandglass.sandglasslibrary.moudle.net.requestbean.SLFLogAttrBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemResponseBean;
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
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCompressUtil;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Greated by yangjie
 * describe:反馈列表详情页
 * time:2023/1/30
 */
public class SLFFeedbackListDetailActivity<T> extends SLFBaseActivity implements View.OnClickListener,SLFHttpRequestCallback<T> {
    /**
     * 处理状态
     */
    private TextView slf_title_status;
    /**
     * 反馈id
     */
    private TextView slf_feedback_id;
    /**
     * 问题类型
     */
    private TextView slf_feedback_question_type;
    /**
     * 留言列表
     */
    private RecyclerView slf_feedback_leave_list;
    /**
     * 底部继续留言按钮
     */
    private RelativeLayout slf_feedback_bottom_relative;
    private TextView slf_feedback_list_leave_message_bottom_text;
    /**
     * 更新加载的布局
     */
    private SLFSmartRefreshLayout slf_feedback_list_detail_refreshLayout;
    /**
     * 留言列表adapter
     */
    private SLFFeedbackDetailAdapter adapter;
    /**
     * 传过来的反馈对象
     */
    private SLFRecord slfRecode;//记录对象
    /**
     * 状态栏文字
     */
    private TextView slfTitle;
    /**
     * 状态栏返回按钮
     */
    private ImageView slfBack;
    /**
     * 状态栏右边文字
     */
    private TextView slfRightTitle;
    /**
     * item数据模型
     */
    private SLFFeedbackDetailItemBean slfFeedbackDetailItemBean;
    /**
     * list数据
     */
    private List<SLFLeaveMsgRecord> slfLeaveMsgRecordList;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SLFLeaveMsgRecord slfLeaveMsgRecord;
    private List<SLFLeveMsgRecordMoudle> mediaList;//反馈的多媒体集合
    private SLFLeveMsgRecordMoudle mediaData;//多媒体对象
    private Runnable submitLogRunnable;
    private boolean isSubmit = false;
    private ExecutorService singleThreadExecutor;
    private String appLogFileName = "appLog.zip";
    private int currentPage = 1;
    private List<SLFLeaveMsgRecord> newDatas;
    private int REQUEST_CODE = 0;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private int pages;
    private int position;
    private boolean isRefresh;
    private boolean isCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_feedback_list_detail);
        isCreate = true;
        initFromData();
        initTitleBar();
        initView();
        initRecyclerView();
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //此处是跳转的result回调方法
                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                    SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+":onActivityReuslt:");
                    SLFLeaveMsgRecord slfLeaveMsgRecord = (SLFLeaveMsgRecord) result.getData().getSerializableExtra(SLFConstants.LEAVE_MSG_DATA);
                    slfLeaveMsgRecordList.add(slfLeaveMsgRecord);
                    adapter.notifyDataSetChanged();
                    slf_feedback_leave_list.scrollToPosition(slfLeaveMsgRecordList.size() - 1);
                } else {
                    //showToast("跳转回来");
                }
            }
        });
        initData();
    }

    /**
     * 初始化必须数据
     *
     */
    private void initFromData(){
        position = getIntent().getIntExtra(SLFConstants.RECORD_DATA_POSITION,-1);
        slfRecode = (SLFRecord) getIntent().getSerializableExtra(SLFConstants.RECORD_DATA);
        slfLeaveMsgRecordList = new ArrayList<>();
    }
    /**
     * 刷新数据
     */
    private void initData() {
        showLoading();
        currentPage = 1;
        isRefresh = true;
        getFeedBackDetailList(currentPage);
    }

    private void refresh() {
        currentPage = 1;
        isRefresh = true;
        getFeedBackDetailList(currentPage);
    }


    private void getFeedBackDetailList (int currentPage) {
        TreeMap map = new TreeMap();
        map.put("current", currentPage);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_HISTORY_LIST_URL.replace("{id}",String.valueOf(slfRecode.getId())), map, SLFFeedbackDetailItemResponseBean.class, this);
    }
    /**
     * 初始化view
     */
    private void initView() {
        slf_title_status = findViewById(R.id.slf_feedback_list_item_title_status);
        slf_feedback_id = findViewById(R.id.slf_feedback_list_item_title_id);
        slf_feedback_question_type = findViewById(R.id.slf_feedback_list_item_type);
        slf_feedback_leave_list = findViewById(R.id.slf_feedback_list_leave_message_list);
        slf_feedback_bottom_relative = findViewById(R.id.slf_feedback_list_bottom_relative);
        slf_feedback_list_leave_message_bottom_text = findViewById(R.id.slf_feedback_list_leave_message_bottom_text);
        slf_feedback_list_detail_refreshLayout = findViewById(R.id.slf_feedback_list_detail_refreshLayout);
        if (slfRecode != null) {
            if (slfRecode.getStatus() == 0) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_to_be_processed));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_warning_color));
                slf_feedback_bottom_relative.setVisibility(View.VISIBLE);
            } else if (slfRecode.getStatus() == 1||slfRecode.getStatus() == 2) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_in_progress));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
                slf_feedback_bottom_relative.setVisibility(View.VISIBLE);
            } else if (slfRecode.getStatus() == 4) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_finished));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_text_color));
                slf_feedback_bottom_relative.setVisibility(View.GONE);
            }
            if (slfRecode.getSendLog() == 0) {
                requestUploadUrls();
                slfRightTitle.setVisibility(View.VISIBLE);
                slfRightTitle.setText(SLFResourceUtils.getString(R.string.slf_feedback_send_log));
                slfRightTitle.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
                slfRightTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                slfRightTitle.setVisibility(View.GONE);
            }
            slf_feedback_id.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_list_item_id, slfRecode.getId()));
            if(TextUtils.isEmpty(slfRecode.getServiceTypeText())&&TextUtils.isEmpty(slfRecode.getCategoryText())&&TextUtils.isEmpty(slfRecode.getSubCategoryText())){
                slf_feedback_question_type.setText("");
            }else if(!TextUtils.isEmpty(slfRecode.getServiceTypeText())&&!TextUtils.isEmpty(slfRecode.getCategoryText())&&TextUtils.isEmpty(slfRecode.getSubCategoryText())){
                slf_feedback_question_type.setText(slfRecode.getServiceTypeText() + "/" + slfRecode.getCategoryText());
            }else if(!TextUtils.isEmpty(slfRecode.getServiceTypeText())&&TextUtils.isEmpty(slfRecode.getCategoryText())&&TextUtils.isEmpty(slfRecode.getSubCategoryText())){
                slf_feedback_question_type.setText(slfRecode.getServiceTypeText());
            } else {
                slf_feedback_question_type.setText(slfRecode.getServiceTypeText() + "/" + slfRecode.getCategoryText() + "/" + slfRecode.getSubCategoryText());
            }
        }else{
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+":slfRecode is null:");
        }
        SLFFontSet.setSLF_RegularFont(getContext(),slf_title_status);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_id);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_question_type);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_list_leave_message_bottom_text);
        slf_feedback_bottom_relative.setOnClickListener(this);

    }

    /**
     * 初始化RecyclerView
     */
    @SuppressLint("ResourceAsColor")
    private void initRecyclerView() {
        adapter = new SLFFeedbackDetailAdapter(getContext(),slfLeaveMsgRecordList);
//        adapter.setHasStableIds(true);

//关闭动画效果
        SimpleItemAnimator sa=(SimpleItemAnimator )slf_feedback_leave_list.getItemAnimator();
        sa.setSupportsChangeAnimations(false);
//设置动画为空
        slf_feedback_leave_list.setItemAnimator(null);
        slf_feedback_leave_list.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        slf_feedback_leave_list.setLayoutManager(mLayoutManager);
        slf_feedback_leave_list.setAdapter(adapter);
        slf_feedback_list_detail_refreshLayout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
        slf_feedback_list_detail_refreshLayout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
        slf_feedback_list_detail_refreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
        slf_feedback_list_detail_refreshLayout.setEnableFooterFollowWhenNoMoreData(true);
        slf_feedback_list_detail_refreshLayout.setPrimaryColors(R.color.transparent, android.R.color.transparent);
        slf_feedback_list_detail_refreshLayout.setRefreshHeader(new SLFRereshHeader(getContext()));
        slf_feedback_list_detail_refreshLayout.setRefreshFooter(new SLFRefreshFooter(getContext()));
        slf_feedback_list_detail_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refresh();
            }
        });
        slf_feedback_list_detail_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getFeedBackDetailList(currentPage);
            }
        });
        //slf_feedback_list_detail_refreshLayout.autoRefresh();//自动刷新
    }

    /**
     * 初始化statusBar
     */
    private void initTitleBar() {
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfRightTitle = findViewById(R.id.slf_tv_title_right);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_list_leave_message_title_text));
        slfBack.setOnClickListener(this);
        slfRightTitle.setOnClickListener(this);
        SLFFontSet.setSLF_RegularFont(getContext(),slfTitle);
        SLFFontSet.setSLF_MediumFontt(getContext(),slfRightTitle);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.slf_iv_back) {
            finish();
        } else if (view.getId() == R.id.slf_tv_title_right) {
            showLoading();
            SLFApi.getInstance(getContext()).setUploadLogCompleteCallBack(new SLFUploadCompleteCallback() {
                @Override
                public void isUploadAppLogComplete(boolean isComplete) {
                    SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+":feedbackList Detail upload app log complete callback:");
                    sumbitLogFiles();
                }
            });
            if (SLFApi.getInstance(getContext()).getAppLogCallBack() != null) {
                SLFApi.getInstance(SLFApi.getSLFContext()).getAppLogCallBack().getUploadAppLogUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl,
                       "application/zip");
            }
        } else if (view.getId() == R.id.slf_feedback_list_bottom_relative) {
            gotoContinueLeaveActivity();
        }
    }

    private void gotoContinueLeaveActivity() {

        Intent intent = new Intent(SLFFeedbackListDetailActivity.this, SLFContinueLeaveMsgActivity.class);
        intent.putExtra(SLFConstants.RECORD_DATA,slfRecode);
        intentActivityResultLauncher.launch(intent);
    }


    /**
     * sendlog
     */
    private void sumbitLogFiles() {
        initLogFiles();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(submitLogRunnable);
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
                                    SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::logFile.size::" + logFile.length());
                                    String uploadUrl = SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl;
                                    SLFHttpUtils.getInstance().executePutFile(getContext(), uploadUrl, logFile, "application/zip", "0", SLFFeedbackListDetailActivity.this);
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure() {
                        isSubmit = false;
                        SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+"::application log compress  error::");
                    }
                });
            }
        };
    }

    /**
     * 请求上传文件链接
     */
    private void requestUploadUrls() {
        TreeMap map = new TreeMap();
        map.put("num", 2);
        //showLoading();
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.UPLOAD_FILE_URL, map, SLFUploadFileReponseBean.class, this);
    }

    @Override
    public void onRequestNetFail(T type) {
        SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+":FeedbackList Detail onRequestNetFail::");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("0".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            }
        }else if(type instanceof SLFFeedbackDetailItemResponseBean){
            if(isRefresh){
                slf_feedback_list_detail_refreshLayout.finishRefresh();
                isRefresh = false;
            } else{
                slf_feedback_list_detail_refreshLayout.finishLoadMore();
            }
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        hideLoading();
        if (type instanceof SLFUploadFileReponseBean) {
            SLFLogUtil.sdkd(TAG,"ActivityName:"+this.getClass().getSimpleName()+":requestScucess::feedbackDetail::SLFUploadFileReponseBean" + ":type:" + type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean) type,3);
            if(SLFCommonUpload.getInstance()!=null&&SLFCommonUpload.getInstance().size()>0&&SLFCommonUpload.getListInstance()!=null&&SLFCommonUpload.getListInstance().size()>0) {
                /**分配3个链接给log上传*/
                for (int i = 0; i < 3; i++) {
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.sdkd(TAG, "uploadPath--all--feedbackDetail--:::" + SLFCommonUpload.getListInstance().get(i));
                }
            }
        }else if(type instanceof String){
            String code = (String) type;
            SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":requestScucess::feedbackDetail：:Integer::" + ":type:" + type);
            if ("0".equals(code)) {
                SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":requestScucess::logfile--feedbackDetail---upload---complete");
                SLFHttpUtils.getInstance().executePost(getContext(), SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_LOG_URL.replace("{id}",String.valueOf(slfRecode.getId())), getSendLog(), SLFSendLeaveMsgRepsonseBean.class, this);
            }
        }else if(type instanceof SLFSendLeaveMsgRepsonseBean){
            showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_list_send_log));
            slfRightTitle.setVisibility(View.GONE);
            EventBus.getDefault().post(new SLFSendLogSuceessEvent(true,position));
            SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":requestScucess::SLFSendLeaveMsgRepsonseBean");
        }else if (type instanceof SLFFeedbackDetailItemResponseBean){
            pages = ((SLFFeedbackDetailItemResponseBean) type).data.getPages();
            showFeedBackAdapter((SLFFeedbackDetailItemResponseBean)type);
            SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":requestScucess::SLFFeedbackDetailItemResponseBean");
        }

    }

    private void showFeedBackAdapter (SLFFeedbackDetailItemResponseBean bean) {
        newDatas = bean.data.getRecods();
            if (newDatas != null && newDatas.size() > 0) {
                if(isRefresh){
                    adapter.updateList(newDatas,false,true);
                    slf_feedback_list_detail_refreshLayout.finishRefresh();
                    isRefresh = false;
                }else {
                    adapter.updateList(newDatas, true,false);
                    slf_feedback_list_detail_refreshLayout.finishLoadMore();
                }
                currentPage++;
            } else {
                adapter.updateList(null, false,false);
                slf_feedback_list_detail_refreshLayout.finishLoadMore();
            }
                currentPage++;
            adapter.notifyDataSetChanged();
         if(!isRefresh) {
             slf_feedback_leave_list.scrollToPosition(slfLeaveMsgRecordList.size() - 1);
         }

    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":feedbackDetail requestOptionFail");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("0".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            }
        }else if(type instanceof SLFFeedbackDetailItemResponseBean){
            if(isRefresh){
                slf_feedback_list_detail_refreshLayout.finishRefresh();
                isRefresh = false;
            } else{
                slf_feedback_list_detail_refreshLayout.finishLoadMore();
            }
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_request_error));
        }
    }

    private TreeMap<String, Object> getSendLog() {
            ArrayList<SLFLogAttrBean> logAttrBeans = new ArrayList<>();
            TreeMap<String, Object> map = new TreeMap<>();
            SLFLogAttrBean logAttrAppBean = new SLFLogAttrBean();
            /**appLogBean*/
            logAttrAppBean.setPath(SLFCommonUpload.getListInstance().get(1));
            if (!TextUtils.isEmpty(appLogFileName)) {
                logAttrAppBean.setFileName(appLogFileName);
            }
            logAttrAppBean.setContentType("application/zip");
            /**firmwareLogBean*/
//            SLFLogAttrBean logAttrFirmwareBean = new SLFLogAttrBean();
//
//            logAttrFirmwareBean.setPath(SLFCommonUpload.getListInstance().get(2));
//            if (!TextUtils.isEmpty(firmwareLogFileName)) {
//                logAttrAppBean.setFileName(firmwareLogFileName);
//            }
//            logAttrFirmwareBean.setContentType("application/zip");
            /*sdkLogBean*/
            SLFLogAttrBean logAttrPluginBean = new SLFLogAttrBean();
            logAttrPluginBean.setPath(SLFCommonUpload.getListInstance().get(0));
            logAttrPluginBean.setFileName("sdkLog.zip");
            logAttrPluginBean.setContentType("application/zip");
            logAttrBeans.add(logAttrAppBean);
            logAttrBeans.add(logAttrPluginBean);
            map.put("logAttrList", logAttrBeans);
        SLFLogUtil.sdke(TAG,"ActivityName:"+this.getClass().getSimpleName()+":getSendlog MAP :" + map.toString());
        return map;
    }
}
