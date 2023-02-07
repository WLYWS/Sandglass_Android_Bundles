package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFeedbackDetailAdapter;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFragmentAdapter;
import com.wyze.sandglasslibrary.functionmoudle.fragment.SLFFeedbackAllHistoryFragment;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackDetailItemBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFeedbackItemBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecode;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:反馈列表详情页
 * time:2023/1/30
 */
public class SLFFeedbackListDetailActivity extends SLFBaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    /**处理状态*/
    private TextView slf_title_status;
    /**反馈id*/
    private TextView slf_feedback_id;
    /**问题类型*/
    private TextView slf_feedback_question_type;
    /**留言列表*/
    private RecyclerView slf_feedback_leave_list;
    /**底部继续留言按钮*/
    private RelativeLayout slf_feedback_bottom_relative;
    /**更新加载的布局*/
    private SwipeRefreshLayout slf_feedback_list_detail_refreshLayout;
    /**留言列表adapter*/
    private SLFFeedbackDetailAdapter adapter;
    /**传过来的反馈对象*/
    private SLFRecode slfRecode;//记录对象
    /**状态栏文字*/
    private TextView slfTitle;
    /**状态栏返回按钮*/
    private ImageView slfBack;
    /**状态栏右边文字*/
    private TextView slfRightTitle;
    /**item数据模型*/
    private SLFFeedbackDetailItemBean slfFeedbackDetailItemBean;
    /**list数据*/
    private List<SLFLeaveMsgRecord> slfLeaveMsgRecordList;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private LinearLayoutManager mLayoutManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SLFLeaveMsgRecord slfLeaveMsgRecord;
    private List<SLFLeveMsgRecordMoudle> mediaList;//反馈的多媒体集合
    private SLFLeveMsgRecordMoudle mediaData;//多媒体对象
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
    }
    /**初始化refreshlayout*/
    private void initRefreshLayout() {
        slf_feedback_list_detail_refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        slf_feedback_list_detail_refreshLayout.setOnRefreshListener(this);
    }
    /**初始化数据*/
    private void initData(){
        slfLeaveMsgRecordList = new ArrayList<>();
        slfLeaveMsgRecordList.clear();
        for (int i = 0; i < 50; i++) {
            mediaList  = new ArrayList<>();
            mediaList.clear();
            if(i==2){
                for(int j=0;j<3;j++){
                    mediaData = new SLFLeveMsgRecordMoudle("","video/mp4","","","image/jpg");
                    mediaList.add(mediaData);
                }
            }
            if(i==4){
                for(int j=0;j<4;j++){
                    mediaData = new SLFLeveMsgRecordMoudle("","image/jpeg","","","image/jpg");
                    mediaList.add(mediaData);
                }
            }
           if(i%2==0){
               slfLeaveMsgRecord = new SLFLeaveMsgRecord("what are you doing?", System.currentTimeMillis(),true,mediaList);
           }else{
               slfLeaveMsgRecord = new SLFLeaveMsgRecord("hello,I'm worker!Can I help your?",System.currentTimeMillis(),false,null);
           }
            slfLeaveMsgRecordList.add(slfLeaveMsgRecord);
        }
        slfFeedbackDetailItemBean = new SLFFeedbackDetailItemBean(1, 50, slfLeaveMsgRecordList);
    }

    /**初始化view*/
    private void initView(){
        slfRecode = (SLFRecode) getIntent().getSerializableExtra(SLFConstants.RECORD_DATA);
       slf_title_status = findViewById(R.id.slf_feedback_list_item_title_status);
       slf_feedback_id = findViewById(R.id.slf_feedback_list_item_title_id);
       slf_feedback_question_type = findViewById(R.id.slf_feedback_list_item_type);
       slf_feedback_leave_list = findViewById(R.id.slf_feedback_list_leave_message_list);
       slf_feedback_bottom_relative = findViewById(R.id.slf_feedback_list_bottom_relative);
       slf_feedback_list_detail_refreshLayout = findViewById(R.id.slf_feedback_list_detail_refreshLayout);
       if(slfRecode!=null){
           if(slfRecode.getStatus()==0) {
               slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_to_be_processed));
               slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_warning_color));
           }else if(slfRecode.getStatus()==1){
               slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_in_progress));
               slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
           }else if(slfRecode.getStatus()==4){
               slf_title_status.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_finished));
               slf_title_status.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_text_color));
           }
           if(slfRecode.getSendLog()==0){
               slfRightTitle.setVisibility(View.VISIBLE);
               slfRightTitle.setText(SLFResourceUtils.getString(R.string.slf_feedback_send_log));
               slfRightTitle.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
           }
           slf_feedback_id.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_list_item_id,slfRecode.getId()));
           slf_feedback_question_type.setText(slfRecode.getServiceTypeText()+"/"+slfRecode.getCategoryText()+"/"+slfRecode.getSubCategoryText());
       }
    }
    /**初始化RecyclerView*/
    private void initRecyclerView(){
        adapter = new SLFFeedbackDetailAdapter(getDatas(0, PAGE_COUNT), getContext(), getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
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
        });
    }
    /**初始化statusBar*/
    private void initTitleBar(){
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfRightTitle = findViewById(R.id.slf_tv_title_right);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_list_leave_message_title_text));
        slfBack.setOnClickListener(this);
    }

    private List<SLFLeaveMsgRecord> getDatas(final int firstIndex, final int lastIndex) {
        List<SLFLeaveMsgRecord> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < slfLeaveMsgRecordList.size()) {
                resList.add(slfLeaveMsgRecordList.get(i));
            }
        }
        return resList;
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<SLFLeaveMsgRecord> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.slf_iv_back){
            finish();
        }
    }

    @Override
    public void onRefresh() {
        slf_feedback_list_detail_refreshLayout.setRefreshing(true);
        adapter.resetDatas();
        updateRecyclerView(0, PAGE_COUNT);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                slf_feedback_list_detail_refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}
