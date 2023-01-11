package com.wyze.sandglasslibrary.functionmoudle.activity.chatbot;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFClickEditText;
import com.wyze.sandglasslibrary.dao.SLFChatBotDatabase;
import com.wyze.sandglasslibrary.dao.SLFDBEngine;
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
import com.wyze.sandglasslibrary.net.SLFHttpChatBotRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestCallback;
import com.wyze.sandglasslibrary.net.SLFHttpRequestConstants;
import com.wyze.sandglasslibrary.net.SLFHttpUtils;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFSpUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SLFChatBotActivity extends SLFBaseActivity implements SLFHttpRequestCallback ,SLFHttpChatBotRequestCallback {
    //记录上次进入此页面的时间
    private static final String LAST_ENTER_PAGE = "LAST_ENTER_PAGE";
    private SwipeRefreshLayout sw_faq_recycle;
    private RecyclerView rv_faq_chat_bot;
    private SLFClickEditText et_faq_input;
    private List<SLFChatBotMsgData> faqMsgList = new ArrayList <>();
    private static final int dateItem = 1;
    private SLFChatBotRecyclerAdapter sLFChatBotRecyclerAdapter;
    private final String SPLIT_STR = "\"@%&\"";
    private SLFDBEngine slfdbEngine;
    private LinearLayout ll_et_input;
    //是否需要增加时间item
    private boolean isFirstGetFromDataBase = false;
    private Handler handler;
    private long lastSendTime;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slfchat_bot);
        slfdbEngine = new SLFDBEngine(this);
        initView();
        initData();
    }

    private void initData ( ) {

        if ((System.currentTimeMillis()-SLFSpUtils.getLong(LAST_ENTER_PAGE,0))/(1000*60*60*24)>7){
            slfdbEngine.delete_all_msg();
            getWelcomeHotQuestion();
        }else {
            //请求数据库查找记录
            slfdbEngine.quary_all_msg();
        }

        SLFSpUtils.putCommit(LAST_ENTER_PAGE,System.currentTimeMillis());
    }

    /**
     * 获取欢迎语和热门问题
     */
    private void getWelcomeHotQuestion ( ) {
        SLFHttpUtils.getInstance().executePathGet(this, SLFHttpRequestConstants.BASE_URL+SLFApiContant.FEEDBACK_FAQ_HOT, SLFFaqWelcomeHotQResponseBean.class,this);
    }

    /**
     * 搜索FAQ（用户提问问题）
     * @param content 用户提问的内容
     * @param returnOther 是否返回 faqList 默认为true
     */
    private void postSearch (String content,boolean returnOther,long requesTime) {
        TreeMap requestMap = new TreeMap();
        requestMap.put("content",content);
        requestMap.put("returnOther",returnOther);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL+SLFApiContant.FEEDBACK_FAQ_SEARCH, requestMap, SLFFaqSearchResponseBean.class,requesTime,this);
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
//        sw_faq_recycle = findViewById(R.id.sw_faq_recycle);
        rv_faq_chat_bot = findViewById(R.id.rv_faq_chat_bot);
        et_faq_input = findViewById(R.id.et_faq_input);
        ll_et_input = findViewById(R.id.ll_et_input);
        sLFChatBotRecyclerAdapter = new SLFChatBotRecyclerAdapter(this);
        //et_faq_input.setEnabled(false);
        ll_et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                et_faq_input.requestFocus();
                showSoftInput(SLFChatBotActivity.this,et_faq_input);
                handler = new Handler(getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run ( ) {
                        rv_faq_chat_bot.smoothScrollToPosition(faqMsgList.size() - 1);
                    }
                }, 250);
            }
        });
        rv_faq_chat_bot.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets (@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = SLFCommonUtils.dip2px(SLFChatBotActivity.this,16);
            }
        });

        rv_faq_chat_bot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch (View v, MotionEvent event) {
                hideSoftInput(SLFChatBotActivity.this,et_faq_input);
                return false;
            }
        });

        et_faq_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {   // 按下完成按钮，这里和上面imeOptions对应
                    String send_msg = et_faq_input.getText().toString();
                    if (!TextUtils.isEmpty(send_msg)){
                        sendMsg(et_faq_input.getText().toString(),SLFChatBotMsgData.SEND_FROM_INPUT);
                        et_faq_input.setText("");
                       // return true;
                    }
                }
                return true;//返回true，保留软键盘。false，隐藏软键盘
            }
        });

    }

    /**
     * 判定是否需要加时间item
     */
    public boolean isAddTimeItem(){
        return isFirstGetFromDataBase||exceedFiveMi();
    }

    /**
     * 距离上一次发送消息超过5分钟
     * @return
     */
    private boolean exceedFiveMi ( ) {
        return (System.currentTimeMillis()-lastSendTime)/6000>5;
    }

    /**
     * 点击输入框
     */
    private void onInput ( ) {
        et_faq_input.requestFocus();
        showSoftInput(SLFChatBotActivity.this,et_faq_input);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run ( ) {
//                rv_faq_chat_bot.scrollToPosition(faqMsgList.size()-1);
//            }
//        });
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run ( ) {
                rv_faq_chat_bot.scrollToPosition(faqMsgList.size() - 1);
            }
        }, 250);
    }

    @Override
    public void onRequestNetFail (Object type) {

    }

    @Override
    public void onRequestSuccess (String result, Object type) {
        if (type instanceof SLFFaqWelcomeHotQResponseBean){
            SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean = (SLFFaqWelcomeHotQResponseBean) type;
            showWelcomeData(sLFFaqWelcomeHotQResponseBean);
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

    private void showSearchReslutData (SLFFaqSearchResponseBean sLFFaqSearchResponseBean,long requestTime) {
        SLFFaqSearchReslutBean slfFaqSearchReslutBean = sLFFaqSearchResponseBean.data;
        //先更新用户发送的小心状态
        showSendMsgStatus(true,requestTime);
        if (!TextUtils.isEmpty(slfFaqSearchReslutBean.noDataText)&&slfFaqSearchReslutBean.answer==null){
            SLFChatBotMsgData slfChatBotRobotNoAnswerData= new SLFChatBotMsgData();
            slfChatBotRobotNoAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotNoAnswerData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
            slfChatBotRobotNoAnswerData.setContent(slfFaqSearchReslutBean.noDataText);
            faqMsgList.add(slfChatBotRobotNoAnswerData);
            notiftyAdapter();
            //保存数据库
            slfdbEngine.insert_msg(slfChatBotRobotNoAnswerData);

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
            notiftyAdapter();
            //保存数据库
            slfdbEngine.insert_msg(slfChatBotRobotAnswerData);
        }
    }

    /**
     * 更新发送消息的状态
     * @param isSendSuccess
     * @param requestTime
     */
    private void showSendMsgStatus (boolean isSendSuccess, long requestTime) {
        for(SLFChatBotMsgData slfChatBotMsgData:faqMsgList){
            if (slfChatBotMsgData.getMsgTime()==requestTime){
               if (isSendSuccess){
                   slfChatBotMsgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue());
               }else {
                   slfChatBotMsgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SEND_FAIL_MSG.getValue());
               }
               sLFChatBotRecyclerAdapter.notifyDataSetChanged();

               slfdbEngine.update_msg(slfChatBotMsgData);
               break;
            }
        }
    }

    /**
     * 展示欢迎词和热门问题
     * @param sLFFaqWelcomeHotQResponseBean
     */
    private void showWelcomeData (SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean) {
        addTimeItem();

        SLFChatBotMsgData slfChatBotWelcomeData= new SLFChatBotMsgData();
        slfChatBotWelcomeData.setMsgTime(System.currentTimeMillis());
        slfChatBotWelcomeData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
        slfChatBotWelcomeData.setContent(sLFFaqWelcomeHotQResponseBean.data.welcome);
        faqMsgList.add(slfChatBotWelcomeData);
        slfdbEngine.insert_msg(slfChatBotWelcomeData);

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
        slfdbEngine.insert_msg(slfChatBotHotData);


        if(faqMsgList.size()>3){//表示不是新的faq

        }else {//表示新faq
            setRcycleApdater();
        }
    }

    private void addTimeItem ( ) {
        SLFChatBotMsgData slfChatBotTimeItemData= new SLFChatBotMsgData();
        slfChatBotTimeItemData.setMsgTime(System.currentTimeMillis());
        slfChatBotTimeItemData.setType(SLFChatBotMsgData.MsgType.SINGLE_TIME_MSG.getValue());
        faqMsgList.add(slfChatBotTimeItemData);
        notiftyAdapter();
        slfdbEngine.insert_msg(slfChatBotTimeItemData);
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
        SLFChatBotMsgData slfChatBotMarkReponseData= new SLFChatBotMsgData();
        slfChatBotMarkReponseData.setMsgTime(System.currentTimeMillis());
        slfChatBotMarkReponseData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
        slfChatBotMarkReponseData.setContent(sLFFaqMarkResponseBean.data);
        faqMsgList.add(slfChatBotMarkReponseData);
        notiftyAdapter();
        //刷新数据库
        slfdbEngine.insert_msg(slfChatBotMarkReponseData);
    }

    /**
     * 触发hot刷新按钮
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotUpdateQuesionEvent event) {
        SLFChatBotMsgData slfChatBotUpdateQuesionData = faqMsgList.get(event.position);
        slfChatBotUpdateQuesionData.setQuestion_index(slfChatBotUpdateQuesionData.getQuestion_index()+5);
        notiftyAdapter();
        //刷新数据库
        slfdbEngine.update_msg(slfChatBotUpdateQuesionData);
    }

    /**
     * 点击问题
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotClickQuesionEvent event) {
        sendMsg(event.question,event.type);
    }

    /**
     * 键盘发送消息
     * @param question
     * @param fromType
     */
    private void sendMsg (String question,int fromType) {
        lastSendTime = System.currentTimeMillis();
        if (isAddTimeItem()){
            addTimeItem();
            isFirstGetFromDataBase = false;
        }
        SLFChatBotMsgData slfChatBotItemClickQuesionData= new SLFChatBotMsgData();
        long time = System.currentTimeMillis();
        slfChatBotItemClickQuesionData.setMsgTime(time);
        slfChatBotItemClickQuesionData.setType(SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue());
        slfChatBotItemClickQuesionData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        slfChatBotItemClickQuesionData.setContent(question);
        faqMsgList.add(slfChatBotItemClickQuesionData);
        notiftyAdapter();
        //发送question数据
        if (fromType==SLFChatBotMsgData.SEND_FROM_CLICK_RELATE){
            postSearch(question,false,time);
        }else {
            postSearch(question,true,time);
        }
        //保存数据库
        slfdbEngine.insert_msg(slfChatBotItemClickQuesionData);
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
        slfdbEngine.update_msg(slfChatBotAnswerEffectData);
    }

    /**
     * 点击没发送信息的警告按钮
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (SLFChatBotClickNoSendWarnEvent event) {
        lastSendTime = System.currentTimeMillis();
        if (isAddTimeItem()){
            addTimeItem();
            isFirstGetFromDataBase = false;
        }
        SLFChatBotMsgData slfChatBotClickNoSendData = faqMsgList.get(event.position);
        slfChatBotClickNoSendData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //重新发送数据
        postSearch(event.question,true,slfChatBotClickNoSendData.getMsgTime());
        //刷新数据库
        slfdbEngine.update_msg(slfChatBotClickNoSendData);
    }

    /**
     * 查询数据库结果
     * @param slfChatBotMsgData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List <SLFChatBotMsgData> slfChatBotMsgData){
        if (slfChatBotMsgData ==null ||slfChatBotMsgData.size()==0){
            getWelcomeHotQuestion();
        }else {
            faqMsgList.addAll(slfChatBotMsgData);
            setRcycleApdater();
            rv_faq_chat_bot.scrollToPosition(faqMsgList.size()-1);
            isFirstGetFromDataBase = true;
        }

    }

    //弹出键盘

    public static void showSoftInput(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);

    }

//隐藏键盘

    public static void hideSoftInput(Context context, View view) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    @Override
    protected void onPause ( ) {
        super.onPause();
        //页面退出时隐藏软键盘
        et_faq_input.clearFocus();
        hideSoftInput(SLFChatBotActivity.this,et_faq_input);
    }


    /**
     * 刷新adapter
     */
    public void notiftyAdapter(){
        sLFChatBotRecyclerAdapter.notifyItemInserted(faqMsgList.size() - 1);
        rv_faq_chat_bot.scrollToPosition(faqMsgList.size()-1);

    }

    /**
     * 用户发送消息后的网络失败回调
     * @param type
     * @param requestTime
     */
    @Override
    public void onRequestChatBotNetFail (Object type, long requestTime) {
        showSendMsgStatus(false,requestTime);
    }

    /**
     * 成功回调
     * @param result
     * @param type
     * @param requestTime
     */
    @Override
    public void onRequestChatBotSuccess (String result, Object type, long requestTime) {
        if (type instanceof SLFFaqSearchResponseBean){
            SLFFaqSearchResponseBean sLFFaqSearchResponseBean = (SLFFaqSearchResponseBean)type;
            showSearchReslutData(sLFFaqSearchResponseBean,requestTime);
        }
    }

    /**
     * 失败回调
     * @param value
     * @param failCode
     * @param typ
     * @param requestTime
     */
    @Override
    public void onRequestChatBotFail (String value, String failCode, Object typ, long requestTime) {
        showSendMsgStatus(false,requestTime);
    }

    @Override
    protected void onDestroy ( ) {
        super.onDestroy();
        //取消动画，防止内容泄露
        sLFChatBotRecyclerAdapter.destroy();
        handler.removeCallbacksAndMessages(null);
    }
}