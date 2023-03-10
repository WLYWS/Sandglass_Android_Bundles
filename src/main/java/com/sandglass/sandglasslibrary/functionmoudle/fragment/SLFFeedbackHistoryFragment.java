package com.sandglass.sandglasslibrary.functionmoudle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonui.SLFSwipeRefreshLayout;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListDetailActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNetWorkChange;
import com.sandglass.sandglasslibrary.moudle.event.SLFSendLogSuceessEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadFeedbackEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadtoReadAllEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadtoReadEvent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@SuppressLint("ValidFragment")
public class SLFFeedbackHistoryFragment<T> extends Fragment implements SLFSwipeRefreshLayout.OnRefreshListener, SLFHttpRequestCallback<T> {

    private int type;

    private RecyclerView slf_histroy_feedback_list;
    private SLFSwipeRefreshLayout slf_feedback_list_refreshLayout;
    private SLFFeedbackItemBean slfFeedbackItemBean;
    private LinearLayout slf_histroy_no_item_linear;
    private TextView slf_feedback_no_feedback;
    private TextView slf_feedback_no_network;
    private Button slf_feedback_list_try_again;

    private List<SLFRecord> recodeList = new ArrayList <>();

    private SLFRecord slfRecode;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private SLFFeedbackListAdapter adapter;
    private int current_page = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isInitData = false;
    private boolean isRefresh;
    private boolean isViewShow;

    public SLFFeedbackHistoryFragment(){
    }

    public SLFFeedbackHistoryFragment(int type) {
        this.type = type;
        SLFLogUtil.d("SLFFeedbackAllHistoryFragment","ActivityName:"+this.getClass().getSimpleName()+":fragment type :" + type);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SLFLogUtil.d("yj","oncreateview");
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.slf_list_history_feedback, container, false);
        slf_histroy_feedback_list = view.findViewById(R.id.slf_histroy_feedback_list);
        slf_feedback_list_refreshLayout = view.findViewById(R.id.slf_feedback_list_refreshLayout);
        slf_histroy_no_item_linear = view.findViewById(R.id.slf_feedback_list_no_item_linear);
        slf_feedback_no_feedback = view.findViewById(R.id.slf_feedback_no_feedback);
        slf_feedback_no_network = view.findViewById(R.id.slf_feedback_no_network);
        slf_feedback_list_try_again = view.findViewById(R.id.slf_feedback_list_try_again);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_no_feedback);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_no_network);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_feedback_list_try_again);
        slf_feedback_list_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        initRefreshLayout();
        initRecyclerView();
        if (SLFCommonUtils.isNetworkAvailable(getActivity())) {
        }else{
            slf_feedback_list_refreshLayout.setVisibility(View.GONE);
            slf_histroy_no_item_linear.setVisibility(View.VISIBLE);
            slf_feedback_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network));
            slf_feedback_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_no_network_warning));
            slf_feedback_no_network.setVisibility(View.VISIBLE);
            slf_feedback_list_try_again.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            initData();
        }else{
        }
    }

    private void initView(){
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

    private void initData() {
        isInitData = true;
        if(recodeList!=null){
            recodeList.clear();
        }
        getFeedBackList(type,1);
    }

    private void getFeedBackList (int type,int current) {
        TreeMap map = new TreeMap();
        map.put("type", type);
        map.put("current", current);
        SLFHttpUtils.getInstance().executeGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_LIST_URL, map, SLFFeedbackItemResponseBean.class, this);
    }

    private void initRefreshLayout() {
        slf_feedback_list_refreshLayout.setProgressBackgroundColorSchemeResource(R.color.transparent);
        slf_feedback_list_refreshLayout.setColorSchemeResources(R.color.slf_theme_color);
        slf_feedback_list_refreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new SLFFeedbackListAdapter(recodeList,getActivity(),recodeList!=null&&recodeList.size()>0?true:false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        slf_histroy_feedback_list.setLayoutManager(mLayoutManager);
        slf_histroy_feedback_list.setAdapter(adapter);
        slf_histroy_feedback_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        current_page = current_page +1;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFeedBackList(type,current_page);
                            }
                        }, 500);
                    }

//                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
//                        mHandler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getFeedBackList(type,current_page+1);
//                            }
//                        }, 500);
//                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });


    }


    @Override
    public void onRefresh() {
        slf_feedback_list_refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        current_page = 1;
        isRefresh = true;
        getFeedBackList(type,current_page);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slf_feedback_list_refreshLayout.setRefreshing(false);
            }
        }, 1000);
        SLFLogUtil.d("SLFFeedbackAllHistoryFragment","FragmentName:"+this.getClass().getSimpleName()+":onrefresh :" + type);
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
    public void onRequestNetFail (T bean) {
        initView();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_network_error));
        SLFLogUtil.d("SLFFeedbackAllHistoryFragment","FragmentName:"+this.getClass().getSimpleName()+":onRequestNetFail type:" + type);
    }

    @Override
    public void onRequestSuccess (String result, T bean) {
        SLFLogUtil.d("SLFFeedbackinpressHistoryFragment", "FragmentName:" + this.getClass().getSimpleName() + ":onRequestSuccess type:" + type);
        if (bean instanceof SLFFeedbackItemResponseBean) {
            List<SLFRecord> newDatas = ((SLFFeedbackItemResponseBean) bean).data.getRecods();
            if (newDatas != null && newDatas.size() > 0) {
                if (isRefresh) {
                    adapter.updateList(newDatas, false, true);
                } else {
                    adapter.updateList(newDatas, true, false);
                    current_page++;
                }
            } else {
                adapter.updateList(null, false, false);
            }
            initView();

            adapter.setOnItemClickLitener((holder, position) -> {
                gotoFeedbackDetail(position);
            });
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        for(SLFRecord record : recodeList) {
            if(record.getRead()==0){
                EventBus.getDefault().post(new SLFUnReadFeedbackEvent(true));
                return;
            }
            EventBus.getDefault().post(new SLFUnReadFeedbackEvent(false));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestFail (String value, String failCode, T bean) {
        initView();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_request_error));
        SLFLogUtil.d("SLFFeedbackAllHistoryFragment","FragmentName:"+this.getClass().getSimpleName()+":onRequestFail type:" + type);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFSendLogSuceessEvent event) {
       if(event.success){
           if(event.position!=-1)
            recodeList.get(event.position).setSendLog(1);
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFUnReadtoReadEvent event) {
        for(int i=0;i<recodeList.size();i++){
            if(recodeList.get(i).getId() == event.slfRecord.getId()){
                recodeList.get(i).setRead(1);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFEventNetWorkChange event) {
        if (event.avisible.equals(SLFConstants.NETWORK_UNAVAILABILITY)) {
//            slf_feedback_list_refreshLayout.setVisibility(View.GONE);
//            slf_histroy_no_item_linear.setVisibility(View.VISIBLE);
//            slf_feedback_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network));
//            slf_feedback_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_no_network_warning));
//            slf_feedback_no_network.setVisibility(View.VISIBLE);
//            slf_feedback_list_try_again.setVisibility(View.VISIBLE);
        } else {
        }
    }

}
