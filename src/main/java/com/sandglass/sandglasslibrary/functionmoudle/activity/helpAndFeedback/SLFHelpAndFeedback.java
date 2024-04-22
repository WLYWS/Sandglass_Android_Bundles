package com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.punet.punetwork.net.PUNApiContant;
import com.punet.punetwork.net.PUNHttpRequestCallback;
import com.punet.punetwork.net.PUNHttpRequestConstants;
import com.punet.punetwork.net.PUNHttpUtils;
import com.putrack.putrack.commonapi.PUTClickAgent;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFAgentEvent;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFPageAgentEvent;
import com.sandglass.sandglasslibrary.bean.SLFSPContant;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFDeviceGridAdapter;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFExAdapter;
import com.sandglass.sandglasslibrary.moudle.event.SLFEventNetWorkChange;
import com.sandglass.sandglasslibrary.moudle.event.SLFFinishActivity;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFaqCategoryEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQFapListBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQProblemBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQResponseBean;

import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.logutil.logutil.SLFLogUtil;
import com.sandglass.sandglasslibrary.utils.manager.SLFCacheToFileManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:智能助手和反馈首页
 * time:2023/2/20
 */
public class SLFHelpAndFeedback<T> extends SLFBaseActivity implements View.OnClickListener, PUNHttpRequestCallback<T> {
    /**设备数据列表*/
    private List<SLFFirstPageFAQBean> deviceTypesList = new ArrayList<SLFFirstPageFAQBean>();
    /**设备数据problem数据集合**/
    private List<SLFFirstPageFAQProblemBean> problemTypesList = new ArrayList<>();
    /**设备数据faqlist数据集合*/
    private List<SLFFirstPageFAQFapListBean> faqFapListBeans = new ArrayList<>();
    /**智能机器人按钮*/
    //private ImageView chatBotView;
    /**反馈按钮*/
    //private Button feedbackBtn;
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
    private static final String CACHE_FILE_PATH = SLFConstants.rootPath+"/updatetxt/faq_category.txt";
    private SLFCacheToFileManager<SLFFirstPageFAQResponseBean> cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_help_and_feedback);
        initTitle();
        initView();
        initRecyclerView();
        cacheManager = new SLFCacheToFileManager(SLFFirstPageFAQResponseBean.class);
        if(SLFCommonUtils.isNetworkAvailable(this)){
            hasDataPage();
            SLFFirstPageFAQResponseBean slfFirstPageFAQResponseBean = cacheManager.readObj(CACHE_FILE_PATH);
            if (slfFirstPageFAQResponseBean!=null){
                if (SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQCATEGORY_CACHE,0)!=SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQCATEGORY,-1)){
                    getDeviceTypesList();
                }else {
                    showContent(slfFirstPageFAQResponseBean);
                }
            }else {
                getDeviceTypesList();
            }
        }else{
            noDataPage(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //打点进入faq列表页
        PUTClickAgent.pageTypeAgent(SLFPageAgentEvent.SLF_FAQListPage,SLFPageAgentEvent.SLF_PAGE_START,null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //打点退出faq列表页
        PUTClickAgent.pageTypeAgent(SLFPageAgentEvent.SLF_FAQListPage,SLFPageAgentEvent.SLF_PAGE_END,null);
    }

    private void initTitle(){
        TextView title = findViewById(R.id.slf_tv_title_name);
        title.setText(SLFResourceUtils.getString(R.string.slf_help_and_feedback_faq_list));
        SLFFontSet.setSLF_MediumFontt(this, title);
    }



    private void initView(){
//        chatBotView = findViewById(R.id.slf_help_and_feedback_chat_bot_img);
//        feedbackBtn = findViewById(R.id.slf_goto_feedback);
        no_network_linear = findViewById(R.id.slf_first_page_no_network_linear);
        no_network_text = findViewById(R.id.slf_first_page_no_network_title);
        no_network_describe = findViewById(R.id.slf_first_page_no_network_describe);
        has_data_relative = findViewById(R.id.slf_first_page_has_data_relate);
        try_again_btn = findViewById(R.id.slf_first_page_try_again);
        //SLFFontSet.setSLF_MediumFontt(getContext(), feedbackBtn);
        SLFFontSet.setSLF_RegularFont(getContext(), try_again_btn);
        SLFFontSet.setSLF_RegularFont(getContext(), no_network_text);
        SLFFontSet.setSLF_RegularFont(getContext(), no_network_describe);
//        chatBotView.setOnClickListener(this);
//        feedbackBtn.setOnClickListener(this);
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
            //打点切换一级菜单
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_FAQ_ChangeClassification,null);
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
        PUNHttpUtils.getInstance().executePathGet(getContext(),
                PUNHttpRequestConstants.BASE_URL + PUNApiContant.FEEDBACK_FAQ_CATEGORIES, SLFFirstPageFAQResponseBean.class, this);
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestUrl（Get）:"+PUNHttpRequestConstants.BASE_URL + PUNApiContant.FEEDBACK_FAQ_CATEGORIES);
    }

    @Override
    public void onClick(View view) {
      if(view.getId() == R.id.slf_first_page_try_again){
                getDeviceTypesList();
        }
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

//    private void gotoChatBot ( ) {
//        Intent in = new Intent(getContext(), SLFChatBotActivity.class);
//        in.putExtra(SLFConstants.CURRENTTIME, System.currentTimeMillis());
//        startActivity(in);
//    }
//
//    private void gotoFeedback(){
//        Intent in = new Intent(getContext(), SLFFeedbackSubmitActivity.class);
//        startActivity(in);
//    }
//    private void gotoFeedbackHistoryList(){
//        Intent in = new Intent(getContext(), SLFFeedbackListActivity.class);
//        startActivity(in);
//    }

    @Override
    public void onRequestNetFail(T type) {
        hideLoading();
       if( type == SLFFirstPageFAQResponseBean.class){
           noDataPage(true);
       }
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestSuccess:type:("+type.getClass()+")\nresult:"+result);
        if(type instanceof SLFFirstPageFAQResponseBean){
            cacheManager.delete(CACHE_FILE_PATH);
            SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQCATEGORY,SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQCATEGORY_CACHE,0));
            cacheManager.saveObj(CACHE_FILE_PATH,(SLFFirstPageFAQResponseBean)type);
            showContent((SLFFirstPageFAQResponseBean) type);
        }
    }

    private void showContent (SLFFirstPageFAQResponseBean type) {
        SLFLogUtil.sdkd("yj","data===SLFFirstPageFAQResponseBean===="+ type.data.toString());
        List<SLFFirstPageFAQBean> newDatas = type.data;
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

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        hideLoading();
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestFail:failCode:"+failCode+"*value:"+value);
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
                SLFLogUtil.sdkd("yj","groupPosition::::"+groupPosition+":::childPostion:::"+childPostion);
                slfExAdapter.setChildSelection(groupPosition,childPostion);
                slfExAdapter.notifyDataSetChanged();
                gotoFAQDetail(groupPosition,childPostion);
                //打点进入faq详情页
                PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_FAQ_EnterDetail,null);
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

    /**
     * 接受缓存更新事件，要更新缓存
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFUpdateFaqCategoryEvent event) {
        SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQCATEGORY_CACHE,event.updateTime);
        getDeviceTypesList();
    }

    /**
     * 关闭页面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFFinishActivity event) {
        if(event.finish){
            finish();
        }
    }
}
