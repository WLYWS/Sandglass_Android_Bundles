package com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.functionmoudle.activity.chatbot.SLFChatBotActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFDeviceGridAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFDeviceGridProblemAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFExAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFFeedbackListAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyleItemDecoration;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNetWorkChange;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadFeedbackEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoriesResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryCommonBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryDetailBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQFapListBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQProblemBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUnReadCount;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringUtil;
import com.sandglass.sandglasslibrary.utils.SLFUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:智能助手和反馈首页
 * time:2023/2/20
 */
public class SLFHelpAndFeedback<T> extends SLFBaseActivity implements View.OnClickListener, SLFHttpRequestCallback<T> {
    /**设备数据列表*/
    private List<SLFFirstPageFAQBean> deviceTypesList = new ArrayList<SLFFirstPageFAQBean>();
    /**设备数据problem数据集合**/
    private List<SLFFirstPageFAQProblemBean> problemTypesList = new ArrayList<>();
    /**设备数据faqlist数据集合*/
    private List<SLFFirstPageFAQFapListBean> faqFapListBeans = new ArrayList<>();
    /**智能机器人按钮*/
    private ImageView chatBotView;
    /**反馈按钮*/
    private Button feedbackBtn;
    /**设备icon列表*/
    private RecyclerView deviceRecycler;
    /**设备问题列表*/
    //private RecyclerView problemRecycler;
    private ExpandableListView problemRecycler;
    /**设备faqlist列表*/
    private RecyclerView faqlistRecycler;
    /**设备icon adapter*/
    private SLFDeviceGridAdapter slfDeviceGridAdapter;
    /**设备问题 adapter*/
    private SLFExAdapter slfExAdapter;
    /**标题栏右边图标*/
    private ImageView imgRight;
    /**无网络，请求失败页面linearlayout*/
    private LinearLayout no_network_linear;
    /**无网络，提示*/
    private TextView no_network_text;
    /**无网络，描述语*/
    private TextView no_network_describe;
    /**try again按钮*/
    private Button try_again_btn;
    /**有数据正常页面relateviewlayout*/
    private RelativeLayout has_data_relative;
    private boolean isChecked;
    private LinearLayoutManager mLayoutManager;
    private LinearLayoutManager mProblemLayoutManager;
    private List<SLFFirstPageFAQProblemBean> problemList;
    private int fristPostion = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_help_and_feedback);
        initTitle();
        initView();
        initRecyclerView();
        if(SLFCommonUtils.isNetworkAvailable(this)){
            hasDataPage();
            getDeviceTypesList();
        }else{
            noDataPage(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNewFeed();
    }

    private void initTitle(){
        TextView title = findViewById(R.id.slf_tv_title_name);
        imgRight = findViewById(R.id.slf_iv_right);
        title.setText(SLFResourceUtils.getString(R.string.slf_help_and_feedback_bar_title));
        SLFFontSet.setSLF_MediumFontt(this, title);
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.drawable.slf_help_feedback_format);
        setWH(imgRight,SLFResourceUtils.dp2px(getContext(),30),SLFResourceUtils.dp2px(getContext(),44));
        imgRight.setOnClickListener(this);
    }

    //动态设置view的宽高
    public void setWH(View view, int width,int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height=height;
        view.setLayoutParams(layoutParams);
    }

    private void initView(){
        chatBotView = findViewById(R.id.slf_help_and_feedback_chat_bot_img);
        feedbackBtn = findViewById(R.id.slf_goto_feedback);
        no_network_linear = findViewById(R.id.slf_first_page_no_network_linear);
        no_network_text = findViewById(R.id.slf_first_page_no_network_title);
        no_network_describe = findViewById(R.id.slf_first_page_no_network_describe);
        has_data_relative = findViewById(R.id.slf_first_page_has_data_relate);
        try_again_btn = findViewById(R.id.slf_first_page_try_again);
        SLFFontSet.setSLF_MediumFontt(getContext(), feedbackBtn);
        SLFFontSet.setSLF_RegularFont(getContext(), try_again_btn);
        SLFFontSet.setSLF_RegularFont(getContext(), no_network_text);
        SLFFontSet.setSLF_RegularFont(getContext(), no_network_describe);
        chatBotView.setOnClickListener(this);
        feedbackBtn.setOnClickListener(this);
        try_again_btn.setOnClickListener(this);
    }

    private void initRecyclerView() {
        deviceRecycler = findViewById(R.id.slf_all_devices_recycler);
        problemRecycler = findViewById(R.id.slf_devices_category_question);
        slfDeviceGridAdapter = new SLFDeviceGridAdapter(getContext(),deviceTypesList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        deviceRecycler.setLayoutManager(mLayoutManager);
        deviceRecycler.setAdapter(slfDeviceGridAdapter);
        slfDeviceGridAdapter.setOnItemClickListener((holder, position) -> {
            deviceTypesList.get(holder.getAdapterPosition()).setChecked(true);
            changedChecked(deviceTypesList,position);
            slfDeviceGridAdapter.notifyDataSetChanged();
            setSecondData(position);
        });
    }
    SLFFirstPageFAQProblemBean firstPageFAQProblemBean;
    private void getDeviceTypesList(){
        //TODO 获取所有设备类型
        showLoading();
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_CATEGORIES, SLFFirstPageFAQResponseBean.class, this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.slf_iv_right){
            //TODO gotoFeedbackFormat
            gotoFeedbackHistoryList();
        }else if(view.getId() == R.id.slf_help_and_feedback_chat_bot_img){
            //TODO gotoChatBot
            gotoChatBot();
        }else if(view.getId() == R.id.slf_goto_feedback){
            gotoFeedback();
        }else if(view.getId() == R.id.slf_first_page_try_again){
                requestNewFeed();
                getDeviceTypesList();
        }
    }
    /**是否有未读消息**/
    private void requestNewFeed(){
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_UN_READ_COUNT, SLFUnReadCount.class, this);
    }

    /**设置选中的状态*/
    private void changedChecked(List<SLFFirstPageFAQBean> list,int position){
        for(int i=0;i<list.size();i++){
                if(i != position){
                    list.get(i).setChecked(false);
                }
        }
    }

    private void noDataPage(boolean isNetWork){
        has_data_relative.setVisibility(View.GONE);
        no_network_linear.setVisibility(View.VISIBLE);
        if(isNetWork) {
            no_network_text.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network));
            no_network_describe.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network_sub));
        }else{
            no_network_text.setText(SLFResourceUtils.getString(R.string.slf_first_page_request_fail));
            no_network_describe.setText(SLFResourceUtils.getString(R.string.slf_first_page_request_fail_sub));
        }
    }

    private void hasDataPage(){
        has_data_relative.setVisibility(View.VISIBLE);
        no_network_linear.setVisibility(View.GONE);
    }

    private void gotoChatBot ( ) {
        Intent in = new Intent(getContext(), SLFChatBotActivity.class);
        in.putExtra(SLFConstants.CURRENTTIME, System.currentTimeMillis());
        startActivity(in);
    }

    private void gotoFeedback(){
        Intent in = new Intent(getContext(), SLFFeedbackSubmitActivity.class);
        startActivity(in);
    }
    private void gotoFeedbackHistoryList(){
        Intent in = new Intent(getContext(), SLFFeedbackListActivity.class);
        startActivity(in);
    }

    @Override
    public void onRequestNetFail(T type) {
        hideLoading();
       if( type == SLFFirstPageFAQResponseBean.class){
           noDataPage(true);
       }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        if(type instanceof SLFUnReadCount){
            SLFLogUtil.d("yj","data===weidu===="+((SLFUnReadCount)type).data);
            if(((SLFUnReadCount)type).data >0){
                /**有未读反馈*/
                imgRight.setImageResource(R.drawable.slf_first_page_new_feedback);
            }else{
                imgRight.setImageResource(R.drawable.slf_help_feedback_format);
            }
        }else if(type instanceof SLFFirstPageFAQResponseBean){
            SLFLogUtil.d("yj","data===SLFFirstPageFAQResponseBean===="+((SLFFirstPageFAQResponseBean)type).data.toString());
            List<SLFFirstPageFAQBean> newDatas =((SLFFirstPageFAQResponseBean)type).data;
            List<SLFFirstPageFAQBean> checkDatas = FaitleData(newDatas);
            deviceTypesList.clear();
            deviceTypesList.addAll(checkDatas);
            if(deviceTypesList.size()>0) {
                hasDataPage();
                deviceTypesList.get(0).setChecked(true);
                setSecondData(0);
            }else{
                noDataPage(false);
            }
            hideLoading();
        }
    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        hideLoading();
        if(type == SLFFirstPageFAQResponseBean.class){
            noDataPage(false);
        }
    }

    /**过滤数据，如果没有三级菜单则不显示*/
    private List<SLFFirstPageFAQBean> FaitleData(List<SLFFirstPageFAQBean> newDatas){
        if(newDatas==null||newDatas.size()==0){
            newDatas = new ArrayList<>();
        }else {
            int dataSize = newDatas.size();
           for(int i=0;i<dataSize;i++){
               if(newDatas.get(i)==null||newDatas.get(i).getSub()==null||newDatas.get(i).getSub().size()==0){
                    newDatas.remove(i);
                    i--;
                    dataSize--;
               }else{
                   int problemSize = newDatas.get(i).getSub().size();
                    for(int j=0;j<problemSize;j++){
                        if(newDatas.get(i).getSub().get(j)==null||newDatas.get(i).getSub().get(j).getFaqList()==null||newDatas.get(i).getSub().get(j).getFaqList().size()==0){
                             newDatas.get(i).getSub().remove(j);
                             j--;
                            problemSize--;
                            if(problemSize==0){
                                newDatas.remove(i);
                                i--;
                                dataSize--;
                            }
                        }
                    }
               }
           }
        }



        return newDatas;
    }



    private void setSecondData(int position){
        fristPostion = position;
        slfExAdapter = new SLFExAdapter(getContext(),deviceTypesList.get(position).getSub());
        problemRecycler.setAdapter(slfExAdapter);
        problemRecycler.setGroupIndicator(null);//除去自带的箭头，自带的箭头在父列表的最左边，不展开向下，展开向上
        problemRecycler.setDivider(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_first_page_line));//这个是设定每个Group之间的分割线。,默认有分割线，设置null没有分割线
        slfExAdapter.notifyDataSetChanged();
        problemRecycler.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPostion, long id) {
                SLFLogUtil.d("yj","groupPosition::::"+groupPosition+":::childPostion:::"+childPostion);
                slfExAdapter.setChildSelection(groupPosition,childPostion);
                slfExAdapter.notifyDataSetChanged();
                gotoFAQDetail(groupPosition,childPostion);
                return true;
            }
        });
    }

    private void gotoFAQDetail(int position,int childPostion){
        Intent in = new Intent(getContext(),SLFHelpAndFeedbackDetail.class);
        in.putExtra(SLFConstants.FAQ_TITLE_NAME,deviceTypesList.get(fristPostion).getName());
        in.putExtra(SLFConstants.FAQ_ID,deviceTypesList.get(fristPostion).getSub().get(position).getFaqList().get(childPostion).getId());
        startActivity(in);
    }

    /**获取是否有未读反馈*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFUnReadFeedbackEvent event) {
       if(event.hasUnRead){
           imgRight.setImageResource(R.drawable.slf_first_page_new_feedback);
       }else{
           imgRight.setImageResource(R.drawable.slf_help_feedback_format);
       }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFEventNetWorkChange event) {
        if (event.avisible.equals(SLFConstants.NETWORK_UNAVAILABILITY)) {
           // noDataPage(true);
        } else {
//            hasDataPage();
//            requestNewFeed();
//            getDeviceTypesList();
        }
    }
}
