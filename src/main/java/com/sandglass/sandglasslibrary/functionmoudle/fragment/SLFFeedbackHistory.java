package com.sandglass.sandglasslibrary.functionmoudle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFLazyLoadFragment;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFRefreshFooter;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFRereshHeader;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.commonui.slfrefreshlayout.SLFSmartRefreshLayout;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListDetailActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.sandglass.sandglasslibrary.moudle.event.SLFSendLogSuceessEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadtoReadAllEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFRecord;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SLFFeedbackHistory<T> extends SLFLazyLoadFragment implements  SLFHttpRequestCallback<T> {
    public final static int IN_PROGRESS = 1;

    private RecyclerView slf_histroy_feedback_list;
    private SLFSmartRefreshLayout slf_feedback_list_refreshLayout;
    private SLFFeedbackItemBean slfFeedbackItemBean;
    private LinearLayout slf_histroy_no_item_linear;
    private TextView slf_feedback_no_feedback;
    private TextView slf_feedback_no_network;
    private Button slf_feedback_list_try_again;

    private List<SLFRecord> recodeList = new ArrayList<>();

    private SLFRecord slfRecode;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private SLFFeedbackListAdapter adapter;
    private int current_page = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isInitData = false;
    private String isRefresh;
    private boolean isCreated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        isCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    protected int setContentView() {
        return R.layout.slf_list_history_feedback;
    }

    @SuppressLint("NewApi")
    @Override
    protected void lazyLoad() {
        initRecyclerView();
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isCreated){
            initRecyclerView();
            initView();
            initData();
        }
        isCreated = false;
    }

    private void initView() {
        slf_histroy_no_item_linear = findViewById(R.id.slf_feedback_list_no_item_linear);
        slf_feedback_no_feedback = findViewById(R.id.slf_feedback_no_feedback);
        slf_feedback_no_network = findViewById(R.id.slf_feedback_no_network);
        slf_feedback_list_try_again = findViewById(R.id.slf_feedback_list_try_again);
        LayoutInflater inflater = LayoutInflater.from(SLFApi.getSLFContext());

        SLFFontSet.setSLF_RegularFont(getContext(), slf_feedback_no_feedback);
        SLFFontSet.setSLF_RegularFont(getContext(), slf_feedback_no_network);
        SLFFontSet.setSLF_RegularFont(getContext(), slf_feedback_list_try_again);

        slf_feedback_list_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    initData();
            }
        });

    }

    private void initData(){
        SLFToastUtil.showLoading(getActivity(), SLFToastUtil.LOADING_TYPE_WHITE,false);
        isRefresh = "refresh";
        current_page = 1;
        getFeedBackList(IN_PROGRESS,current_page);
    }

    private void refresh(){
        isRefresh = "refresh";
        current_page = 1;
        getFeedBackList(IN_PROGRESS,current_page);
    }

    private void chageView(){
        if(SLFCommonUtils.isNetworkAvailable(getActivity())) {
            if (recodeList == null || recodeList.size() == 0) {
                slf_feedback_list_refreshLayout.setVisibility(View.GONE);
                slf_histroy_no_item_linear.setVisibility(View.VISIBLE);
                slf_feedback_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_no_item));
                slf_feedback_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_top_text_normal));
                slf_feedback_no_network.setVisibility(View.GONE);
                slf_feedback_list_try_again.setVisibility(View.GONE);
            } else {
                slf_feedback_list_refreshLayout.setVisibility(View.VISIBLE);
                slf_histroy_no_item_linear.setVisibility(View.GONE);
            }
        }else{
            slf_feedback_list_refreshLayout.setVisibility(View.GONE);
            slf_histroy_no_item_linear.setVisibility(View.VISIBLE);
            slf_feedback_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network));
            slf_feedback_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_no_network_warning));
            slf_feedback_no_network.setVisibility(View.VISIBLE);
            slf_feedback_list_try_again.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void initRecyclerView() {
        slf_feedback_list_refreshLayout = findViewById(R.id.slf_feedback_list_refreshLayout);
        slf_histroy_feedback_list = findViewById(R.id.slf_histroy_feedback_list);
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.slf_feedback_list_footview,null);
        adapter = new SLFFeedbackListAdapter(recodeList,getActivity(),recodeList!=null&&recodeList.size()>0?true:false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        slf_histroy_feedback_list.setLayoutManager(mLayoutManager);
        slf_histroy_feedback_list.setAdapter(adapter);
        slf_feedback_list_refreshLayout.setEnableRefresh(false);
        slf_feedback_list_refreshLayout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
        slf_feedback_list_refreshLayout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
        slf_feedback_list_refreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
        slf_feedback_list_refreshLayout.setEnableFooterFollowWhenNoMoreData(true);
        slf_feedback_list_refreshLayout.setPrimaryColors(R.color.transparent, android.R.color.transparent);
        slf_feedback_list_refreshLayout.setRefreshHeader(new SLFRereshHeader(getContext()));
        slf_feedback_list_refreshLayout.setRefreshFooter(new SLFRefreshFooter(getContext()));
        slf_feedback_list_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
               refresh();
            }
        });
        slf_feedback_list_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getFeedBackList(IN_PROGRESS,current_page);
            }
        });
        //slf_feedback_list_refreshLayout.autoRefresh();//自动刷新
    }


    private void getFeedBackList (int type,int current) {
        TreeMap map = new TreeMap();
        map.put("type", type);
        map.put("current", current);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_LIST_URL, map, SLFFeedbackItemResponseBean.class, this);
    }

    private void gotoFeedbackDetail(int position){
        Intent in = new Intent();
        in.setClass(getContext(), SLFFeedbackListDetailActivity.class);
        if(recodeList.get(position).getRead()==0){
            recodeList.get(position).setRead(1);
            adapter.notifyDataSetChanged();
        }
        EventBus.getDefault().post(new SLFUnReadtoReadAllEvent(recodeList.get(position)));
        in.putExtra(SLFConstants.RECORD_DATA_POSITION,position);
        in.putExtra(SLFConstants.RECORD_DATA,recodeList.get(position));
        startActivity(in);
    }

    @Override
    public void onRequestNetFail(T type) {
        chageView();
        SLFToastUtil.hideLoading();
        //SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_network_error));
        if(type instanceof SLFFeedbackDetailItemResponseBean) {
            if (isRefresh.equals("refresh")) {
                slf_feedback_list_refreshLayout.finishRefresh();
                isRefresh = "isCanHasmore";
            } else {
                slf_feedback_list_refreshLayout.finishLoadMore();
            }
            SLFLogUtil.sdkd("SLFFeedbackAllHistoryFragment", "FragmentName:" + this.getClass().getSimpleName() + ":onRequestNetFail type:" + type);
        }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        SLFLogUtil.sdkd("SLFFeedbackinpressHistoryFragment", "FragmentName:" + this.getClass().getSimpleName() + ":onRequestSuccess type:" + type);
        if (type instanceof SLFFeedbackItemResponseBean) {
            List<SLFRecord> newDatas = ((SLFFeedbackItemResponseBean) type).data.getRecods();
            if (newDatas != null && newDatas.size() > 0) {
                if (isRefresh.equals("refresh")) {
                    adapter.updateList(newDatas, false, true);
                    slf_feedback_list_refreshLayout.finishRefresh();//传入false表示刷新失败
                    isRefresh = "isCanHasmore";
                } else if(isRefresh.equals("isCanHasmore")){
                    adapter.updateList(newDatas, true, false);
                    slf_feedback_list_refreshLayout.finishLoadMore(500);//传入false表示刷新失败
                }
                current_page++;
            } else {
                adapter.updateList(null, false, false);
                slf_feedback_list_refreshLayout.finishLoadMore(500);//传入false表示刷新失败
            }
            chageView();
            SLFToastUtil.hideLoading();
            adapter.setOnItemClickLitener((holder, position) -> {
                gotoFeedbackDetail(position);
            });
        }
    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        chageView();
        SLFToastUtil.hideLoading();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_request_error));
        if(type instanceof SLFFeedbackDetailItemResponseBean) {
            if (isRefresh.equals("refresh")) {
                slf_feedback_list_refreshLayout.finishRefresh();
                isRefresh = "isCanHasmore";
            } else {
                slf_feedback_list_refreshLayout.finishLoadMore();
            }
            SLFLogUtil.sdkd("SLFFeedbackAllHistoryFragment", "FragmentName:" + this.getClass().getSimpleName() + ":onRequestFail type:" + type);
        }
    }

    private void autoRefresh() {
        slf_feedback_list_refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                current_page = 1;
                isRefresh = "auto";
                getFeedBackList(IN_PROGRESS,current_page);
                SLFLogUtil.sdkd("swiperefreshlayout","auto refresh::"+current_page);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFSendLogSuceessEvent event) {
        if(event.success){
            if(event.position!=-1) {
                recodeList.get(event.position).setSendLog(1);
            }
        }
    }
}
