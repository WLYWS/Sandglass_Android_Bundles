package com.wyze.sandglasslibrary.functionmoudle.activity.chatbot;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.dao.SLFChatBotDatabase;
import com.wyze.sandglasslibrary.dao.SLFMsgDao;
import com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot.SLFChatBotRecyclerAdapter;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotUpdateQuesionEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFEventCommon;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFaqMarkResponseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFaqSearchReslutBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFaqSearchResponseBean;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFFaqWelcomeHotQResponseBean;
import com.wyze.sandglasslibrary.net.SLFApiContant;
import com.wyze.sandglasslibrary.net.SLFHttpRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestConstants;
import com.wyze.sandglasslibrary.net.SLFHttpUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SLFChatBotActivity extends SLFBaseActivity implements SLFHttpRequestCallback {

    private SwipeRefreshLayout sw_faq_recycle;
    private RecyclerView rv_faq_chat_bot;
    private EditText et_faq_input;
    private List<SLFChatBotMsgData> faqMsgList = new ArrayList <>();
    private static final int dateItem = 1;
    private SLFChatBotRecyclerAdapter sLFChatBotRecyclerAdapter;
    private final String SPLIT_STR = "\"@%&\"";
    private SLFMsgDao msgDao;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slfchat_bot);
        msgDao = SLFChatBotDatabase.getInstance(this).bookDao();
        initView();
        initData();
    }

    private void initData ( ) {
        if(!searHistoryData()){
            //没有历史记录，请求欢迎词
            getWelcomeHotQuestion();
        }
    }

    /**
     * 查询历史记录
     * @return
     */
    private boolean searHistoryData ( ) {
        List<SLFChatBotMsgData> dataList = msgDao.selectAll();
        if (dataList ==null ||dataList.size()==0){
            return false;
        }
        faqMsgList.addAll(dataList);
        setRcycleApdater();
        return true;
    }

    /**
     * 获取欢迎语和热门问题
     */
    private void getWelcomeHotQuestion ( ) {
        SLFHttpUtils.getInstance().executeGet(this, SLFHttpRequestConstants.BASE_URL+SLFApiContant.FEEDBACK_FAQ_HOT, SLFFaqWelcomeHotQResponseBean.class,this);
    }

    /**
     * 搜索FAQ（用户提问问题）
     * @param content 用户提问的内容
     * @param returnOther 是否返回 faqList 默认为true
     */
    private void postSearch (String content,boolean returnOther ) {
        TreeMap requestMap = new TreeMap();
        requestMap.put("content",content);
        requestMap.put("returnOther",returnOther);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL+SLFApiContant.FEEDBACK_FAQ_SEARCH, requestMap, SLFFaqSearchResponseBean.class,this);
    }

    /**
     * 标记FAQ是否解决
     * @param faqid faq id
     * @param mark 标记 0=未解决、1=解决
     */
    private void postQuestionMark(long faqid,int mark){
        TreeMap requestMap = new TreeMap();
        requestMap.put("id",faqid);
        requestMap.put("mark",mark);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL+SLFApiContant.FEEDBACK_FAQ_MARK, requestMap, SLFFaqMarkResponseBean.class,this);

    }

    private void initView ( ) {
        TextView slf_tv_title_name = findViewById(R.id.slf_tv_title_name);
        slf_tv_title_name.setText(R.string.slf_faq_title);
        sw_faq_recycle = findViewById(R.id.sw_faq_recycle);
        rv_faq_chat_bot = findViewById(R.id.rv_faq_chat_bot);
        et_faq_input = findViewById(R.id.et_faq_input);
        sLFChatBotRecyclerAdapter = new SLFChatBotRecyclerAdapter(this);
        et_faq_input.setOnClickListener(view->onInput());
    }

    /**
     * 点击输入框
     */
    private void onInput ( ) {

    }

    @Override
    public void onRequestNetFail (Object type) {

    }

    @Override
    public void onRequestSuccess (String result, Object type) {
        if (type instanceof SLFFaqWelcomeHotQResponseBean){
            SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean = (SLFFaqWelcomeHotQResponseBean) type;
            showWelcomeData(sLFFaqWelcomeHotQResponseBean);
        }else if (type instanceof SLFFaqSearchResponseBean){
            SLFFaqSearchResponseBean sLFFaqSearchResponseBean = (SLFFaqSearchResponseBean)type;
            showSearchReslutData(sLFFaqSearchResponseBean);
        }else if (type instanceof SLFFaqMarkResponseBean){
            SLFFaqMarkResponseBean sLFFaqMarkResponseBean = (SLFFaqMarkResponseBean)type;
            showMarkReslutData(sLFFaqMarkResponseBean);
        }
    }

    @Override
    public void onRequestFail (String value, String failCode, Object type) {

    }
    /**
     * 显示查询结果
     * @param sLFFaqSearchResponseBean
     */

    private void showSearchReslutData (SLFFaqSearchResponseBean sLFFaqSearchResponseBean) {
        SLFFaqSearchReslutBean slfFaqSearchReslutBean = sLFFaqSearchResponseBean.data;
        if (!TextUtils.isEmpty(slfFaqSearchReslutBean.noDataText)&&slfFaqSearchReslutBean.answer==null){
            SLFChatBotMsgData slfChatBotRobotNoAnswerData= new SLFChatBotMsgData();
            slfChatBotRobotNoAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotNoAnswerData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
            slfChatBotRobotNoAnswerData.setContent(slfFaqSearchReslutBean.noDataText);
            faqMsgList.add(slfChatBotRobotNoAnswerData);
            sLFChatBotRecyclerAdapter.notifyDataSetChanged();
            //保存数据库
            msgDao.insertMsgData(slfChatBotRobotNoAnswerData);

        }else if (slfFaqSearchReslutBean.answer!=null){
            SLFChatBotMsgData slfChatBotRobotAnswerData= new SLFChatBotMsgData();
            slfChatBotRobotAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotAnswerData.setType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
            slfChatBotRobotAnswerData.setContent(slfFaqSearchReslutBean.answer.content);
            slfChatBotRobotAnswerData.setTitle(slfFaqSearchReslutBean.answer.title);
            slfChatBotRobotAnswerData.setFaqId(slfFaqSearchReslutBean.answer.id);
            int faqSize = 0;
            StringBuilder sb = new StringBuilder();
            for (String question:slfFaqSearchReslutBean.faqList){
                faqSize++;
                sb.append(question);
                if(faqSize!=slfFaqSearchReslutBean.faqList.size()){
                    sb.append(SPLIT_STR);
                }
            }
            slfChatBotRobotAnswerData.setQuestion(sb.toString());
            slfChatBotRobotAnswerData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NO_SELECT.getValue());
            faqMsgList.add(slfChatBotRobotAnswerData);
            sLFChatBotRecyclerAdapter.notifyDataSetChanged();
            //保存数据库
            msgDao.insertMsgData(slfChatBotRobotAnswerData);
        }
    }

    /**
     * 展示欢迎词和热门问题
     * @param sLFFaqWelcomeHotQResponseBean
     */
    private void showWelcomeData (SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean) {
        SLFChatBotMsgData slfChatBotTimeItemData= new SLFChatBotMsgData();
        slfChatBotTimeItemData.setMsgTime(System.currentTimeMillis());
        slfChatBotTimeItemData.setType(SLFChatBotMsgData.MsgType.SINGLE_TIME_MSG.getValue());
        faqMsgList.add(slfChatBotTimeItemData);

        SLFChatBotMsgData slfChatBotWelcomeData= new SLFChatBotMsgData();
        slfChatBotWelcomeData.setMsgTime(System.currentTimeMillis());
        slfChatBotWelcomeData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
        slfChatBotWelcomeData.setContent(sLFFaqWelcomeHotQResponseBean.data.welcome);
        faqMsgList.add(slfChatBotWelcomeData);

        int faqSize = 0;
        StringBuilder sb = new StringBuilder();
        SLFChatBotMsgData slfChatBotHotData= new SLFChatBotMsgData();
        slfChatBotHotData.setMsgTime(System.currentTimeMillis());
        slfChatBotHotData.setType(SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue());
        for (String question:sLFFaqWelcomeHotQResponseBean.data.hotFaq){
            faqSize++;
            sb.append(question);
            if(faqSize!=sLFFaqWelcomeHotQResponseBean.data.hotFaq.size()){
                sb.append(SPLIT_STR);
            }
        }
        slfChatBotHotData.setQuestion(sb.toString());
        faqMsgList.add(slfChatBotHotData);


        if(faqMsgList.size()>3){//表示不是新的faq

        }else {//表示新faq
            setRcycleApdater();
        }
    }

    /**
     * 初次展示recycle
     */
    private void setRcycleApdater ( ) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_faq_chat_bot.setLayoutManager(layoutManager);
        sLFChatBotRecyclerAdapter.setItemList(faqMsgList);
        rv_faq_chat_bot.setAdapter(sLFChatBotRecyclerAdapter);
    }

    /**
     * 展示mark结果
     * @param sLFFaqMarkResponseBean
     */
    private void showMarkReslutData (SLFFaqMarkResponseBean sLFFaqMarkResponseBean) {


    }

    /**
     * 触发hot刷新按钮
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotUpdateQuesionEvent event) {
        SLFChatBotMsgData slfChatBotUpdateQuesionData = faqMsgList.get(event.position);
        slfChatBotUpdateQuesionData.setQuestion_index(slfChatBotUpdateQuesionData.getQuestion_index()+5);
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //刷新数据库
        msgDao.insertMsgData(slfChatBotUpdateQuesionData);
    }

    /**
     * 点击问题
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotClickQuesionEvent event) {
        SLFChatBotMsgData slfChatBotItemClickQuesionData= new SLFChatBotMsgData();
        slfChatBotItemClickQuesionData.setMsgTime(System.currentTimeMillis());
        slfChatBotItemClickQuesionData.setType(SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue());
        slfChatBotItemClickQuesionData.setContent(event.question);
        faqMsgList.add(slfChatBotItemClickQuesionData);
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //发送question数据
        postSearch(event.question,true);
        //保存数据库
        msgDao.insertMsgData(slfChatBotItemClickQuesionData);
    }

    /**
     * 刷新问题的有效性
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotAnswerEffectEvent event) {
        SLFChatBotMsgData slfChatBotAnswerEffectData = faqMsgList.get(event.position);
        if (event.isSolution == 1){
            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_EFFECTIVE.getValue());
        }else if (event.isSolution == 0){
            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NOEFFECTIVE.getValue());
        }
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //发送FAQ是否解决
        postQuestionMark(slfChatBotAnswerEffectData.getFaqId(),event.isSolution);
        //刷新数据库(这块考虑啥时候刷新)
        msgDao.updateMsgData(slfChatBotAnswerEffectData);
    }

    /**
     * 点击没发送信息的警告按钮
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotClickNoSendWarnEvent event) {
        SLFChatBotMsgData slfChatBotClickNoSendData = faqMsgList.get(event.position);
        slfChatBotClickNoSendData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //重新发送数据
        postSearch(event.question,true);
        //刷新数据库
        msgDao.updateMsgData(slfChatBotClickNoSendData);
    }
}