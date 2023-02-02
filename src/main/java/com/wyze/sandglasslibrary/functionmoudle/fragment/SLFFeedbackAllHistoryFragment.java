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
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListActivity;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListDetailActivity;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecode;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class SLFFeedbackAllHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String text;

    private RecyclerView slf_histroy_feedback_list;
    private SwipeRefreshLayout slf_feedback_list_refreshLayout;
    private SLFFeedbackItemBean slfFeedbackItemBean;
    private LinearLayout slf_histroy_no_item_linear;

    private List<SLFRecode> recodeList;

    private SLFRecode slfRecode;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private SLFFeedbackListAdapter adapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public SLFFeedbackAllHistoryFragment(String textString) {
        this.text = textString;
    }

    public static SLFFeedbackAllHistoryFragment newInstance(String textString) {
        SLFFeedbackAllHistoryFragment mFragment = new SLFFeedbackAllHistoryFragment(textString);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.slf_list_history_feedback, container, false);
        slf_histroy_feedback_list = view.findViewById(R.id.slf_histroy_feedback_list);
        slf_feedback_list_refreshLayout = view.findViewById(R.id.slf_feedback_list_refreshLayout);
        slf_histroy_no_item_linear = view.findViewById(R.id.slf_feedback_list_no_item_linear);
        initView();
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
        recodeList = new ArrayList<>();
        recodeList.clear();
        for (int i = 0; i < 50; i++) {
            if (i <= 10) {
                slfRecode = new SLFRecode(i, "ABC", "RVI", "RVI问题", "连接问题", "连接不上了",
                        "not good enough", System.currentTimeMillis(), 0, 0, 0);
            } else if (i > 10 && i < 30) {
                slfRecode = new SLFRecode(i, "CEO", "NVI", "NVI问题", "硬件问题", "硬件不好用",
                        "very very very bad", System.currentTimeMillis(), 0, 1, 1);
            } else {
                slfRecode = new SLFRecode(i, "PINK", "XVI", "XVI问题", "其他问题", "莫名问题",
                        "OH MY GOD！！！", System.currentTimeMillis(), 4, 0, 1);
            }
            if (!text.equals("first")) {
                recodeList.add(slfRecode);
            } else {
                if(slfRecode.getStatus()==1){
                    recodeList.add(slfRecode);
                }
            }
        }
        slfFeedbackItemBean = new SLFFeedbackItemBean(1, 50, recodeList);
    }

    private void initRefreshLayout() {
        slf_feedback_list_refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        slf_feedback_list_refreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        adapter = new SLFFeedbackListAdapter(getDatas(0, PAGE_COUNT), getContext(), getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        mLayoutManager = new LinearLayoutManager(getContext());
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
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
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

    private List<SLFRecode> getDatas(final int firstIndex, final int lastIndex) {
        List<SLFRecode> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < recodeList.size()) {
                resList.add(recodeList.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<SLFRecode> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    @Override
    public void onRefresh() {
        slf_feedback_list_refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        updateRecyclerView(0, PAGE_COUNT);
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
}
