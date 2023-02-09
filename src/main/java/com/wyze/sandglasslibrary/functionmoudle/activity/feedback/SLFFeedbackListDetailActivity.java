package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonapi.SLFApi;
import com.wyze.sandglasslibrary.commonapi.SLFCommonUpload;
import com.wyze.sandglasslibrary.commonui.SLFToastUtil;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFeedbackDetailAdapter;
import com.wyze.sandglasslibrary.interf.SLFUploadCompleteCallback;
import com.wyze.sandglasslibrary.moudle.net.requestbean.SLFLogAttrBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemResponseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecord;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFSendLeaveMsgRepsonseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFUploadFileReponseBean;
import com.wyze.sandglasslibrary.net.SLFApiContant;
import com.wyze.sandglasslibrary.net.SLFHttpRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestConstants;
import com.wyze.sandglasslibrary.net.SLFHttpUtils;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFCompressUtil;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

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
public class SLFFeedbackListDetailActivity<T> extends SLFBaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SLFHttpRequestCallback<T> {
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
    /**
     * 更新加载的布局
     */
    private SwipeRefreshLayout slf_feedback_list_detail_refreshLayout;
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
    private String appLogFileName;
    private String firmwareLogFileName;
    private int currentPage = 1;
    private List<SLFLeaveMsgRecord> newDatas;
    private int REQUEST_CODE = 0;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private int pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_feedback_list_detail);
        initData();
        initTitleBar();
        initView();
        initRefreshLayout();
        initRecyclerView();
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //此处是跳转的result回调方法
                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                    SLFLeaveMsgRecord slfLeaveMsgRecord = (SLFLeaveMsgRecord) result.getData().getSerializableExtra(SLFConstants.LEAVE_MSG_DATA);
                    slfLeaveMsgRecordList.add(slfLeaveMsgRecord);
                    adapter.notifyDataSetChanged();
                } else {
                    //showToast("跳转回来");
                }
            }
        });
    }

    /**
     * 初始化refreshlayout
     */
    private void initRefreshLayout() {
        slf_feedback_list_detail_refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        slf_feedback_list_detail_refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        slfRecode = (SLFRecord) getIntent().getSerializableExtra(SLFConstants.RECORD_DATA);
        slfLeaveMsgRecordList = new ArrayList<>();
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
        slf_feedback_list_detail_refreshLayout = findViewById(R.id.slf_feedback_list_detail_refreshLayout);
        if (slfRecode != null) {
            if (slfRecode.getStatus() == 0) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_to_be_processed));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_warning_color));
            } else if (slfRecode.getStatus() == 1||slfRecode.getStatus() == 2) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_in_progress));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
            } else if (slfRecode.getStatus() == 4) {
                slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_finished));
                slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_text_color));
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
        }
        slf_feedback_bottom_relative.setOnClickListener(this);

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        adapter = new SLFFeedbackDetailAdapter(slfLeaveMsgRecordList, getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        slf_feedback_leave_list.setLayoutManager(mLayoutManager);
        slf_feedback_leave_list.setAdapter(adapter);
        //slf_histroy_feedback_list.setItemAnimator(new DefaultItemAnimator());

        slf_feedback_leave_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFeedBackDetailList(currentPage+1);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFeedBackDetailList(currentPage+1);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        adapter.setOnItemClickLitener((holder, position) -> {
            SLFLogUtil.d("yj", "position----" + position);
        });
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
    }

//    private List<SLFLeaveMsgRecord> getDatas(final int firstIndex, final int lastIndex) {
//        List<SLFLeaveMsgRecord> resList = new ArrayList<>();
//        for (int i = firstIndex; i < lastIndex; i++) {
//            if (i < slfLeaveMsgRecordList.size()) {
//                resList.add(slfLeaveMsgRecordList.get(i));
//            }
//        }
//        return resList;
//    }
//
//    private void updateRecyclerView(int fromIndex, int toIndex) {
//        List<SLFLeaveMsgRecord> newDatas = getDatas(fromIndex, toIndex);
//        if (newDatas.size() > 0) {
//            adapter.updateList(newDatas, true);
//        } else {
//            adapter.updateList(null, false);
//        }
//    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.slf_iv_back) {
            finish();
        } else if (view.getId() == R.id.slf_tv_title_right) {
            showLoading();
            //sumbitLogFiles();
            if (SLFApi.getInstance(getContext()).getAppLogCallBack() != null) {
                SLFApi.getInstance(getContext()).getAppLogCallBack().getUploadAppLogUrl(SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(1)).uploadUrl,
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(2)).uploadUrl);
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
        } else if (view.getId() == R.id.slf_feedback_list_bottom_relative) {
            gotoContinueLeaveActivity();
        }
    }

    private void gotoContinueLeaveActivity() {

        Intent intent = new Intent(SLFFeedbackListDetailActivity.this, SLFContinueLeaveMsgActivity.class);
        intent.putExtra(SLFConstants.RECORD_DATA,slfRecode);
        intentActivityResultLauncher.launch(intent);
    }

    @Override
    public void onRefresh() {
        slf_feedback_list_detail_refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        //updateRecyclerView(0, PAGE_COUNT);
        currentPage = 1;
        getFeedBackDetailList(currentPage);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slf_feedback_list_detail_refreshLayout.setRefreshing(false);
            }
        }, 1000);
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
                SLFCompressUtil.zipFile(SLFConstants.apiLogPath, "*", SLFConstants.feedbacklogPath + "pluginLog.zip", new SLFCompressUtil.OnCompressSuccessListener() {
                    @Override
                    public void onSuccess() {
                        isSubmit = true;
                        if (isSubmit) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    File logFile = new File(SLFConstants.feedbacklogPath + "pluginLog.zip");
                                    SLFLogUtil.d(TAG, "logFile.size------::" + logFile.length());
                                    String uploadUrl = SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(0)).uploadUrl;
                                    SLFHttpUtils.getInstance().executePutFile(getContext(), uploadUrl, logFile, "application/zip", "0", SLFFeedbackListDetailActivity.this);
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure() {
                        isSubmit = false;
                        SLFLogUtil.d("yj", "compress  error-----::");
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
        map.put("num", 3);
        showLoading();
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.UPLOAD_FILE_URL, map, SLFUploadFileReponseBean.class, this);
    }

    @Override
    public void onRequestNetFail(T type) {
        SLFLogUtil.e(TAG, "feedbackDetail requestNetFail");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("0".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            }
        }else{
            showCenterToast(SLFResourceUtils.getString(R.string.slf_common_network_error));
        }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        hideLoading();
        if (type instanceof SLFUploadFileReponseBean) {
            SLFLogUtil.e(TAG, "requestScucess::feedbackDetail::SLFUploadFileReponseBean::" + ":::type:::" + type.toString());
            SLFCommonUpload.setSLFcommonUpload((SLFUploadFileReponseBean) type,3);
            if(SLFCommonUpload.getInstance()!=null&&SLFCommonUpload.getInstance().size()>0&&SLFCommonUpload.getListInstance()!=null&&SLFCommonUpload.getListInstance().size()>0) {
                /**分配3个链接给log上传*/
                for (int i = 0; i < 3; i++) {
                    SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                    SLFLogUtil.d(TAG, "uploadPath--all--feedbackDetail--:::" + SLFCommonUpload.getListInstance().get(i));
                }
            }
        }else if(type instanceof String){
            String code = (String) type;
            SLFLogUtil.e(TAG, "requestScucess::feedbackDetail：:Integer::" + ":::type:::" + type);
            if ("0".equals(code)) {
                SLFLogUtil.d(TAG, "logfile--feedbackDetail---upload---complete");
                SLFHttpUtils.getInstance().executePost(getContext(), SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_LOG_URL.replace("{id}",String.valueOf(slfRecode.getId())), getSendLog(), SLFSendLeaveMsgRepsonseBean.class, this);
            }
        }else if(type instanceof SLFSendLeaveMsgRepsonseBean){
            showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_list_send_log));
        }else if (type instanceof SLFFeedbackDetailItemResponseBean){
            pages = ((SLFFeedbackDetailItemResponseBean) type).data.getPages();
            showFeedBackAdapter((SLFFeedbackDetailItemResponseBean)type);
        }

    }

    private void showFeedBackAdapter (SLFFeedbackDetailItemResponseBean bean) {
        newDatas = bean.data.getRecods();
            if (newDatas != null && newDatas.size() > 0) {
                adapter.updateList(newDatas, true);
            } else {
                adapter.updateList(null, false);
            }
                currentPage++;
    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        SLFLogUtil.e(TAG, "feedbackDetail requestOptionFail");
        hideLoading();
        if (type instanceof String) {
            String code = (String) type;
            if ("0".equals(code)) {
                SLFToastUtil.showCenterSubmitFailText();
            }
        }else{
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
            SLFLogAttrBean logAttrFirmwareBean = new SLFLogAttrBean();

            logAttrFirmwareBean.setPath(SLFCommonUpload.getListInstance().get(2));
            if (!TextUtils.isEmpty(firmwareLogFileName)) {
                logAttrAppBean.setFileName(firmwareLogFileName);
            }
            logAttrFirmwareBean.setContentType("application/zip");
            /*pluginLogBean*/
            SLFLogAttrBean logAttrPluginBean = new SLFLogAttrBean();
            logAttrPluginBean.setPath(SLFCommonUpload.getListInstance().get(0));
            logAttrPluginBean.setFileName("pluginLog.zip");
            logAttrPluginBean.setContentType("application/zip");
            logAttrBeans.add(logAttrAppBean);
            logAttrBeans.add(logAttrFirmwareBean);
            logAttrBeans.add(logAttrPluginBean);
            map.put("logAttrList", logAttrBeans);

        return map;
    }
}
