package com.wyze.sandglasslibrary.functionmoudle.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonui.SLFToastUtil;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListActivity;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListDetailActivity;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFaqSearchResponseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemResponseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecode;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFUploadFileReponseBean;
import com.wyze.sandglasslibrary.net.SLFApiContant;
import com.wyze.sandglasslibrary.net.SLFHttpRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestConstants;
import com.wyze.sandglasslibrary.net.SLFHttpUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@SuppressLint("ValidFragment")
public class SLFFeedbackAllHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SLFHttpRequestCallback<SLFFeedbackItemResponseBean> {

    private int type;

    private RecyclerView slf_histroy_feedback_list;
    private SwipeRefreshLayout slf_feedback_list_refreshLayout;
    private SLFFeedbackItemBean slfFeedbackItemBean;
    private LinearLayout slf_histroy_no_item_linear;

    private List<SLFRecode> recodeList = new ArrayList <>();

    private SLFRecode slfRecode;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private SLFFeedbackListAdapter adapter;
    private int current_page = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isInitData = false;

    public SLFFeedbackAllHistoryFragment(int type) {
        this.type = type;
    }

//    public static SLFFeedbackAllHistoryFragment newInstance(String textString) {
//        SLFFeedbackAllHistoryFragment mFragment = new SLFFeedbackAllHistoryFragment(textString);
//        return mFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.slf_list_history_feedback, container, false);
        slf_histroy_feedback_list = view.findViewById(R.id.slf_histroy_feedback_list);
        slf_feedback_list_refreshLayout = view.findViewById(R.id.slf_feedback_list_refreshLayout);
        slf_histroy_no_item_linear = view.findViewById(R.id.slf_feedback_list_no_item_linear);
        //initView();
        initRefreshLayout();
        initRecyclerView();
        return view;
    }

    private void initView(){
        if(recodeList==null||recodeList.size()==0){
            slf_feedback_list_refreshLayout.setVisibility(View.GONE);
            slf_histroy_no_item_linear.setVisibility(View.VISIBLE);
        }else{
            slf_feedback_list_refreshLayout.setVisibility(View.VISIBLE);
            slf_histroy_no_item_linear.setVisibility(View.GONE);
        }
    }

    private void initData() {
        isInitData = true;
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
        slf_feedback_list_refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        slf_feedback_list_refreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new SLFFeedbackListAdapter(recodeList,getActivity(),recodeList!=null&&recodeList.size()>0?true:false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        slf_histroy_feedback_list.setLayoutManager(mLayoutManager);
        slf_histroy_feedback_list.setAdapter(adapter);
        //slf_histroy_feedback_list.setItemAnimator(new DefaultItemAnimator());

        slf_histroy_feedback_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFeedBackList(type,current_page+1);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFeedBackList(type,current_page+1);
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
            SLFLogUtil.d("yj","position----"+position);
            gotoFeedbackDetail(position);
        });
    }


    @Override
    public void onRefresh() {
        slf_feedback_list_refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        current_page = 1;
        getFeedBackList(type,current_page);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slf_feedback_list_refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void gotoFeedbackDetail(int position){
        Intent in = new Intent();
        in.setClass(getContext(), SLFFeedbackListDetailActivity.class);
        in.putExtra(SLFConstants.RECORD_DATA,recodeList.get(position));
        startActivity(in);
    }

    @Override
    public void onRequestNetFail (SLFFeedbackItemResponseBean bean) {
        initView();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_network_error));
    }

    @Override
    public void onRequestSuccess (String result, SLFFeedbackItemResponseBean bean) {
        List<SLFRecode> newDatas = bean.data.getRecods();
        if (newDatas!=null&&newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
        initView();
        current_page++;
    }

    @Override
    public void onRequestFail (String value, String failCode, SLFFeedbackItemResponseBean bean) {
        initView();
        SLFToastUtil.showCenterText(SLFResourceUtils.getString(R.string.slf_common_request_error));
    }
}
