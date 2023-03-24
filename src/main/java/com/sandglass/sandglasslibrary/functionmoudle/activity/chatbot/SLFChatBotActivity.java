package com.sandglass.sandglasslibrary.functionmoudle.activity.chatbot;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFHttpStatusCode;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.commonui.SLFClickEditText;
import com.sandglass.sandglasslibrary.commonui.SLFFITRelativeLayout;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.dao.SLFDBEngine;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackListActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback.SLFHelpAndFeedback;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.chatbot.SLFChatBotRecyclerAdapter;
import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotUpdateQuesionEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFTenMsgData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqMarkResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqOpenAiResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqWelcomeHotQResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUnReadCount;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFUserInfoResponseBean;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpChatBotRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.theme.SLFSetTheme;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.keyboard.SLFSoftKeyBoardListener;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author wangjian  yangjie
 */
public class SLFChatBotActivity extends SLFBaseActivity implements SLFHttpRequestCallback, SLFHttpChatBotRequestCallback {
    //记录上次进入此页面的时间
    private static final int MAX_ID = 100000000;
    private static final String LAST_ENTER_PAGE = "LAST_ENTER_PAGE";
    private SwipeRefreshLayout sw_faq_recycle;
    private RecyclerView rv_faq_chat_bot;
    private SLFClickEditText et_faq_input;
    private List<SLFChatBotMsgData> faqMsgList = new ArrayList<>();
    private static final int dateItem = 1;
    private SLFChatBotRecyclerAdapter sLFChatBotRecyclerAdapter;
    private final String SPLIT_STR = "\"@%&\"";
    private SLFDBEngine slfdbEngine;
    private LinearLayout ll_et_input;
    private LinearLayout gotoFeedback;
    private LinearLayout gotoFaq;
    private LinearLayout slf_bottom_btn_linear;
    //是否需要增加时间item
    private boolean isFirstGetFromDataBase = false;
    private Handler handler;
    private long lastSendTime;
    private int msg_id = MAX_ID;
    private int tempLineCount;
    private int hotQuestion = 0;
    private String sessionId;
    /**
     * 键盘高度
     */
    private int slfKeyBoardHeight;

    private LinearLayout slf_chat_bot_all_linear;
    private long fromHelpTime = 0;
    private  String uuid;
    /**标题栏右边图标*/
    private ImageView imgRight;
    private CharSequence slfInputNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.activity_slfchat_bot);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        setKeyboardListener();
        fromHelpTime = System.currentTimeMillis();
        getUserInfo();
        slfdbEngine = new SLFDBEngine(this);
        initView();
        initData();

    }



    @Override
    protected void onResume() {
        super.onResume();
        requestNewFeed();
    }
    /**获取用户信息**/
    private void getUserInfo(){
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FIRST_PAGE_GET_USERINO, SLFUserInfoResponseBean.class, this);
    }

    private void initData() {
        if ((System.currentTimeMillis() - SLFSpUtils.getLong(SLFConstants.USER_ID+"_"+LAST_ENTER_PAGE, 0)) / (1000 * 60 * 60 * 24)>7) {
            slfdbEngine.delete_all_msgByUserId();
            uuid = getUUid();
            SLFSpUtils.putCommit(SLFConstants.UUID,uuid);
            getWelcomeHotQuestion();
        } else {
            //请求数据库查找记录
            lastSendTime = SLFSpUtils.getLong(SLFConstants.LASTSENDTIME,0L);
            if(lastSendTime==0L) {
                slfdbEngine.quary_ten_msg(msg_id);
                uuid = getUUid();
                SLFSpUtils.putCommit(SLFConstants.UUID, uuid);
                getWelcomeHotQuestion();
                SLFSpUtils.putCommit(SLFConstants.LASTSENDTIME, 1L);
            }else {
                if (((fromHelpTime - lastSendTime) / 1000 / 60) > 5) {
                    slfdbEngine.quary_ten_msg(msg_id);
                    uuid = getUUid();
                    SLFSpUtils.putCommit(SLFConstants.UUID, uuid);
                    getWelcomeHotQuestion();
                } else {
                    uuid = SLFSpUtils.getString(SLFConstants.UUID, "");
                    if (TextUtils.isEmpty(uuid)) {
                        uuid = getUUid();
                        SLFSpUtils.putCommit(SLFConstants.UUID, uuid);
                    }
                    slfdbEngine.quary_ten_msg(msg_id);
                }
            }
        }

        SLFSpUtils.putCommit(SLFConstants.USER_ID+"_"+LAST_ENTER_PAGE, System.currentTimeMillis());
    }

    /**
     * 获取欢迎语和热门问题
     */
    private void getWelcomeHotQuestion() {
        SLFHttpUtils.getInstance().executePathGet(this, SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_HOT, SLFFaqWelcomeHotQResponseBean.class, this);
    }

    /**
     * 搜索FAQ（用户提问问题）
     *
     * @param content     用户提问的内容
     * //@param returnOther 是否返回 faqList 默认为true
     * @param hotQuestion 1为点击热门问题，0为点击发送
     */
    private void postSearch(String content, int hotQuestion, long requesTime,String uuid) {
        TreeMap requestMap = new TreeMap();
//        requestMap.put("content", content);
//        requestMap.put("returnOther", returnOther);
        //SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_SEARCH, requestMap, SLFFaqSearchResponseBean.class, requesTime, this);
        requestMap.put("question",content);
        requestMap.put("sessionId",uuid);
        requestMap.put("hotQuestion",hotQuestion);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_OPENAI, requestMap, SLFFaqOpenAiResponseBean.class, requesTime, this);
    }

    /**
     * 标记FAQ是否解决
     *
     * @param faqid faq id
     * @param mark  标记 0=未解决、1=解决
     */
    private void postQuestionMark(long faqid, int mark, long requesTime) {
        TreeMap requestMap = new TreeMap();
        requestMap.put("id", faqid);
        requestMap.put("mark", mark);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_MARK, requestMap, SLFFaqMarkResponseBean.class, requesTime, this);

    }

    private void initView() {
        TextView slf_tv_title_name = findViewById(R.id.slf_tv_title_name);
        slf_tv_title_name.setText(R.string.slf_help_and_feedback_bar_title);
        SLFFontSet.setSLF_MediumFontt(this, slf_tv_title_name);
        imgRight = findViewById(R.id.slf_iv_right);
        sw_faq_recycle = findViewById(R.id.sw_faq_recycle);
        rv_faq_chat_bot = findViewById(R.id.rv_faq_chat_bot);
        et_faq_input = findViewById(R.id.et_faq_input);
        ll_et_input = findViewById(R.id.ll_et_input);
        slf_chat_bot_all_linear = findViewById(R.id.slf_chat_bot_all_linear);
        gotoFeedback = findViewById(R.id.slf_feedback_linear);
        gotoFaq = findViewById(R.id.slf_faq_linear);
        slf_bottom_btn_linear = findViewById(R.id.slf_bottom_btn_linear);
        et_faq_input.setHorizontallyScrolling(false);
        et_faq_input.setMaxLines(4);
        et_faq_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 5) { //actionId 4 for actionDone And 6 for actionSend

//perform action what you want

                    return true;

                } else {

                    return false;
                }

            }

        });
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.drawable.slf_help_feedback_format);
        setWH(imgRight,SLFResourceUtils.dp2px(getContext(),17),SLFResourceUtils.dp2px(getContext(),17));
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        SLFFITRelativeLayout chat_bot_root = findViewById(R.id.chat_bot_root);
        setListenerFotEditTexts();
        chat_bot_root.setBackgroundColor(SLFSetTheme.defaultBackgroundColor);
        SLFFontSet.setSLF_RegularFont(this, slf_tv_title_name);

        sLFChatBotRecyclerAdapter = new SLFChatBotRecyclerAdapter(this);
        //et_faq_input.setEnabled(false);
        view_click();
        handler = new Handler(getMainLooper());
        gotoFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFeedback();
            }
        });
        gotoFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFaq();
            }
        });
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFeedbackHistoryList();
            }
        });
    }

    /**是否有未读消息**/
    private void requestNewFeed(){
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_UN_READ_COUNT, SLFUnReadCount.class, this);
    }

    //动态设置view的宽高
    public void setWH(View view, int width,int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height=height;
        view.setLayoutParams(layoutParams);
    }

    private void gotoFaq () {
        Intent in = new Intent(getContext(), SLFHelpAndFeedback.class);
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

    private void setDrawableLeftSize(Button button,Drawable drawable) {
        //Drawable drawable = getResources().getDrawable(R.drawable.viewid);
        //图片宽度和高度
        drawable.setBounds(0,0,SLFResourceUtils.dp2px(getContext(),30),SLFResourceUtils.dp2px(getContext(),30));
        //把图片设置在左边
        button.setCompoundDrawables(drawable,null,null,null); //左上右下
    }
    // 判断当前EditText是否可滚动
    private boolean canVerticalScroll(EditText editText) {

        if (editText.getLineCount() > editText.getMaxLines()) {
            return true;
        }
        return false;
    }

    /**
     * 生成32位uuid
     */
    private String getUUid(){
        String uuid = UUID.randomUUID().toString();	//获取UUID并转化为String对象
        uuid = uuid.replace("-", "");
        return uuid;
    }

    //所有的点击事件
    private void view_click() {
        /**
         * 点击聊天输入框，弹出软键盘，recycleview平滑往上移
         */
//        ll_et_input.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View v) {
//                et_faq_input.requestFocus();
//                showSoftInput(SLFChatBotActivity.this,et_faq_input);
////                handler = new Handler(getMainLooper());
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run ( ) {
////                        rv_faq_chat_bot.smoothScrollToPosition(faqMsgList.size() - 1);
////                    }
////                }, 250);
//            }
//        });

        et_faq_input.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获取焦点
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run ( ) {
//                            rv_faq_chat_bot.smoothScrollToPosition(faqMsgList.size() - 1);
//                        }
//                    }, 250);
                } else {
                    //失去焦点
                    //处理我们的实际计算需求
                }
            }
        });
        /**
         * 设置recycleview的item间距
         */
        rv_faq_chat_bot.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = SLFCommonUtils.dip2px(SLFChatBotActivity.this, 16);
            }
        });

        /**
         * 滚动聊天列表退出软键盘
         */
        rv_faq_chat_bot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slf_bottom_btn_linear.setVisibility(View.VISIBLE);
                hideSoftInput(SLFChatBotActivity.this, et_faq_input);
                return false;
            }
        });

        /**
         * 键盘监听
         */
        et_faq_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {   // 按下完成按钮，这里和上面imeOptions对应
                    String send_msg = et_faq_input.getText().toString().trim();
                    if (!TextUtils.isEmpty(send_msg)) {
                        sendMsg(et_faq_input.getText().toString(), SLFChatBotMsgData.SEND_FROM_INPUT);
                        et_faq_input.setText("");
                        // return true;
                    }else{
                        //TODO 全是空格
                        et_faq_input.setText("");
                        return false;
                    }
                }
                return true;//返回true，保留软键盘。false，隐藏软键盘
            }
        });
        //监听输入字数不得超过500
        et_faq_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                slfInputNum = s;
                tempLineCount = et_faq_input.getLineCount();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                slfInputNum = s;
                SLFLogUtil.sdkd("yj","slfInputNum.length:::"+slfInputNum.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tempLineCount < et_faq_input.getLineCount()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv_faq_chat_bot.smoothScrollToPosition(faqMsgList.size() - 1);
                        }
                    }, 50);
                }
//                if (et_faq_input.getText().length() == 500) {
//                    SLFToastUtil.showToastWithMarginBottom(getResources().getString(R.string.slf_input_500_toast_text), SLFCommonUtils.getScreenHeight() / 2);
//                }
                if(slfInputNum.length()>=500){
                    SLFToastUtil.showToastWithMarginBottom(getResources().getString(R.string.slf_input_500_toast_text), SLFCommonUtils.getScreenHeight() / 2);
                }
            }
        });
        refresh();
    }

    /**
     * 下拉刷新
     */
    private void refresh() {
        sw_faq_recycle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                minIdfaqMsgList();
                slfdbEngine.quary_ten_msg(msg_id);
            }
        });
    }

    /**
     * 判定是否需要加时间item
     */
    public boolean isAddTimeItem() {
        return isFirstGetFromDataBase || exceedFiveMi();
    }

    /**
     * 距离上一次发送消息超过5分钟
     *
     * @return
     */
    private boolean exceedFiveMi() {
        lastSendTime = SLFSpUtils.getLong(SLFConstants.LASTSENDTIME,0);
        return ((System.currentTimeMillis() - lastSendTime) /1000 /60) > 5;
    }

    /**
     * 点击输入框
     */
    private void onInput() {
        et_faq_input.requestFocus();
        slf_bottom_btn_linear.setVisibility(View.GONE);
        showSoftInput(SLFChatBotActivity.this, et_faq_input);
//        Handler handler = new Handler(getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run ( ) {
//                rv_faq_chat_bot.scrollToPosition(faqMsgList.size() - 1);
//            }
//        }, 250);
    }

    @Override
    public void onRequestNetFail(Object type) {
        hideLoading();
        showNetworkError();
        SLFLogUtil.sdkd("yj","open ai request fail 含有敏感词：：network no");
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":requestNetFail:chatbot");
    }

    @Override
    public void onRequestSuccess(String result, Object type) {
        if (type instanceof SLFFaqWelcomeHotQResponseBean) {
            SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":requestSuccess:chatbot");
            SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean = (SLFFaqWelcomeHotQResponseBean) type;
            SLFSpUtils.putCommit(SLFConstants.LASTSENDTIME, System.currentTimeMillis());
            showWelcomeData(sLFFaqWelcomeHotQResponseBean);
        }else if(type instanceof SLFUnReadCount){
            SLFLogUtil.sdkd("yj","data===weidu===="+((SLFUnReadCount)type).data);
            if(((SLFUnReadCount)type).data >0){
                /**有未读反馈*/
                imgRight.setImageResource(R.drawable.slf_first_page_new_feedback);
            }else{
                imgRight.setImageResource(R.drawable.slf_help_feedback_format);
            }
        }else if(type instanceof SLFUserInfoResponseBean){
            SLFUserCenter.userInfoBean = (SLFUserInfoResponseBean) type;
        }
    }

    @Override
    public void onRequestFail(String value, String failCode, Object type) {
        hideLoading();
        if(failCode.equals(SLFHttpStatusCode.TOKEN_FAILED)){
            //TODO 重新请求token
            return;
        }
        showCenterToast(getResources().getString(R.string.slf_common_request_error));
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":requestFail:chatbot");
    }

    /**
     * 显示查询结果
     *
     * @param slfFaqOpenAiResponseBean
     */

    private void showSearchReslutData(SLFFaqOpenAiResponseBean slfFaqOpenAiResponseBean, long requestTime) {
        //SLFFaqSearchReslutBean slfFaqSearchReslutBean = sLFFaqSearchResponseBean.data;
        String answer = slfFaqOpenAiResponseBean.getData();
        //先更新用户发送的小心状态
        showSendMsgStatus(SLFHttpStatusCode.SUCCESS_CODE, requestTime);
//        if (TextUtils.isEmpty(answer)) {
//            SLFChatBotMsgData slfChatBotRobotNoAnswerData = new SLFChatBotMsgData();
//            slfChatBotRobotNoAnswerData.setMsgTime(System.currentTimeMillis());
//            slfChatBotRobotNoAnswerData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
//            slfChatBotRobotNoAnswerData.setContent(slfFaqSearchReslutBean.noDataText);
//            faqMsgList.add(slfChatBotRobotNoAnswerData);
//            notiftyAdapter();
//            //保存数据库
//            slfdbEngine.insert_msg(slfChatBotRobotNoAnswerData);
//
//        } else
//            if (slfFaqSearchReslutBean.answer != null) {
            SLFChatBotMsgData slfChatBotRobotAnswerData = new SLFChatBotMsgData();
            slfChatBotRobotAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotAnswerData.setType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
            slfChatBotRobotAnswerData.setContent(slfFaqOpenAiResponseBean.getData());
            slfChatBotRobotAnswerData.setUuid(uuid);
            faqMsgList.add(slfChatBotRobotAnswerData);
            notiftyAdapter();
            //保存数据库
            slfdbEngine.insert_msg(slfChatBotRobotAnswerData);
        }
//    }

    /**
     * 更新发送消息的状态
     *
     * @param isSendStatus
     * @param requestTime
     */
    private void showSendMsgStatus(String isSendStatus, long requestTime) {
        for (SLFChatBotMsgData slfChatBotMsgData : faqMsgList) {
            if (slfChatBotMsgData.getMsgTime() == requestTime) {
                if (isSendStatus.equals(SLFHttpStatusCode.SUCCESS_CODE)) {
                    slfChatBotMsgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue());
                } else if(isSendStatus.equals(SLFHttpStatusCode.OPAI_FAIL_CODE)){
                    slfChatBotMsgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SEND_ILLEGAL_WORD.getValue());
                } else {
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
     *
     * @param sLFFaqWelcomeHotQResponseBean
     */
    private void showWelcomeData(SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean) {
        addTimeItem();

        SLFChatBotMsgData slfChatBotWelcomeData = new SLFChatBotMsgData();
        slfChatBotWelcomeData.setMsgTime(System.currentTimeMillis());
        slfChatBotWelcomeData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
        slfChatBotWelcomeData.setContent(sLFFaqWelcomeHotQResponseBean.data.welcome);
        slfChatBotWelcomeData.setUuid(uuid);
        faqMsgList.add(slfChatBotWelcomeData);
        slfdbEngine.insert_msg(slfChatBotWelcomeData);

        if (sLFFaqWelcomeHotQResponseBean.data.hotFaq.size() > 0) {
            int faqSize = 0;
            StringBuilder sb = new StringBuilder();
            SLFChatBotMsgData slfChatBotHotData = new SLFChatBotMsgData();
            slfChatBotHotData.setMsgTime(System.currentTimeMillis() + 1);
            slfChatBotHotData.setType(SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue());
            slfChatBotHotData.setUuid(uuid);
            for (String question : sLFFaqWelcomeHotQResponseBean.data.hotFaq) {
                faqSize++;
                sb.append(question);
                if (faqSize != sLFFaqWelcomeHotQResponseBean.data.hotFaq.size()) {
                    sb.append(SPLIT_STR);
                }
            }
            slfChatBotHotData.setQuestion(sb.toString());
            faqMsgList.add(slfChatBotHotData);
            slfdbEngine.insert_msg(slfChatBotHotData);
        }

            if(faqMsgList.size()>3){
                setRcycleApdater();
                notiftyAdapter();
            }else {
                setRcycleApdater();
            }
    }

    private void addTimeItem() {
        SLFChatBotMsgData slfChatBotTimeItemData = new SLFChatBotMsgData();
        slfChatBotTimeItemData.setMsgTime(System.currentTimeMillis() - 2);
        slfChatBotTimeItemData.setType(SLFChatBotMsgData.MsgType.SINGLE_TIME_MSG.getValue());
        slfChatBotTimeItemData.setUuid(uuid);
        faqMsgList.add(slfChatBotTimeItemData);
        notiftyAdapter();
        slfdbEngine.insert_msg(slfChatBotTimeItemData);
    }

    //键盘监听事件
    public void setKeyboardListener() {
        //键盘监听事件
        SLFSoftKeyBoardListener.setListener(SLFChatBotActivity.this,
                new SLFSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        slf_bottom_btn_linear.setVisibility(View.GONE);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        slf_bottom_btn_linear.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 初次展示recycle
     */
    private void setRcycleApdater() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_faq_chat_bot.setLayoutManager(layoutManager);
        sLFChatBotRecyclerAdapter.setItemList(faqMsgList);
        rv_faq_chat_bot.setAdapter(sLFChatBotRecyclerAdapter);
    }

    /**
     * 展示mark结果
     *
     * @param sLFFaqMarkResponseBean
     */
    private void showMarkReslutData(SLFFaqMarkResponseBean sLFFaqMarkResponseBean) {
        SLFChatBotMsgData slfChatBotMarkReponseData = new SLFChatBotMsgData();
        slfChatBotMarkReponseData.setMsgTime(System.currentTimeMillis());
        slfChatBotMarkReponseData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
        slfChatBotMarkReponseData.setContent(sLFFaqMarkResponseBean.data);
        slfChatBotMarkReponseData.setUuid(uuid);
        faqMsgList.add(slfChatBotMarkReponseData);
        notiftyAdapter();
        //刷新数据库
        slfdbEngine.insert_msg(slfChatBotMarkReponseData);
    }

    /**
     * 触发hot刷新按钮
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFChatBotUpdateQuesionEvent event) {
        SLFChatBotMsgData slfChatBotUpdateQuesionData = faqMsgList.get(event.position);
        slfChatBotUpdateQuesionData.setQuestion_index(event.index);
        //notiftyAdapter();
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //刷新数据库
        slfdbEngine.update_msg(slfChatBotUpdateQuesionData);
    }

    /**
     * 点击问题
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFChatBotClickQuesionEvent event) {
        sendMsg(event.question, event.type);
    }

    /**
     * 键盘发送消息
     *
     * @param question
     * @param fromType
     */
    private void sendMsg(String question, int fromType) {
        if (isAddTimeItem()) {
            uuid = getUUid();
            SLFSpUtils.putCommit(SLFConstants.UUID,uuid);
//            addTimeItem();
//            isFirstGetFromDataBase = false;
        }
//        lastSendTime = System.currentTimeMillis();
//        SLFSpUtils.putCommit(SLFConstants.LASTSENDTIME,lastSendTime);
        SLFChatBotMsgData slfChatBotItemClickQuesionData = new SLFChatBotMsgData();
        long time = System.currentTimeMillis();
        slfChatBotItemClickQuesionData.setMsgTime(time);
        slfChatBotItemClickQuesionData.setType(SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue());
        slfChatBotItemClickQuesionData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        slfChatBotItemClickQuesionData.setContent(question);
        slfChatBotItemClickQuesionData.setUuid(uuid);
        faqMsgList.add(slfChatBotItemClickQuesionData);
        notiftyAdapter();
        //发送question数据
        if(fromType==SLFChatBotMsgData.SEND_FROM_INPUT) {
            hotQuestion = 0;
        }else if(fromType==SLFChatBotMsgData.SEND_FROM_CLICK_HOT){
            hotQuestion = 1;
        }

        postSearch(question,hotQuestion,time,uuid);
//        if (fromType == SLFChatBotMsgData.SEND_FROM_CLICK_RELATE) {
//            postSearch(question, false, time);
//        } else {
//            postSearch(question, true, time);
//        }
        //保存数据库
        slfdbEngine.insert_msg(slfChatBotItemClickQuesionData);
    }

    /**
     * 刷新问题的有效性
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFChatBotAnswerEffectEvent event) {
        SLFChatBotMsgData slfChatBotAnswerEffectData = faqMsgList.get(event.position);
//        if (event.isSolution == 1) {
//            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_EFFECTIVE.getValue());
//        } else if (event.isSolution == 0) {
//            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NOEFFECTIVE.getValue());
//        }
//        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //发送FAQ是否解决
        //postQuestionMark(slfChatBotAnswerEffectData.getFaqId(), event.isSolution, slfChatBotAnswerEffectData.getMsgTime());
        //刷新数据库(这块考虑啥时候刷新)
        slfdbEngine.update_msg(slfChatBotAnswerEffectData);
    }

    /**
     * 点击没发送信息的警告按钮
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFChatBotClickNoSendWarnEvent event) {
        hotQuestion = 0;
//        lastSendTime = System.currentTimeMillis();
//        SLFSpUtils.putCommit(SLFConstants.LASTSENDTIME,lastSendTime);
//        if (isAddTimeItem()){
//            uuid = getUUid();
//            SLFSpUtils.putCommit(SLFConstants.UUID,uuid);
////            addTimeItem();
////            isFirstGetFromDataBase = false;
//        }
        SLFChatBotMsgData slfChatBotClickNoSendData = faqMsgList.get(event.position);
        if(event.sendFlag==1) {
            slfChatBotClickNoSendData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        }else if(event.sendFlag==2){
            slfChatBotClickNoSendData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SEND_ILLEGAL_WORD.getValue());
        }
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //重新发送数据
        //postSearch(event.question, true, slfChatBotClickNoSendData.getMsgTime());
        if(event.sendFlag==1) {
            postSearch(event.question, hotQuestion, slfChatBotClickNoSendData.getMsgTime(), slfChatBotClickNoSendData.getUuid());
        }else if(event.sendFlag==2){
            showCenterToast(SLFResourceUtils.getString(R.string.slf_create_feedback_illegal_world_text));
        }
        //刷新数据库
        slfdbEngine.update_msg(slfChatBotClickNoSendData);
    }

    /**
     * 查询数据库结果
     *
     * @param slfChatBotMsgData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<SLFChatBotMsgData> slfChatBotMsgData) {
        if (slfChatBotMsgData == null || slfChatBotMsgData.size() == 0) {
            getWelcomeHotQuestion();
        } else {
            faqMsgList.addAll(slfChatBotMsgData);
            setRcycleApdater();
            rv_faq_chat_bot.scrollToPosition(faqMsgList.size() - 1);
           // isFirstGetFromDataBase = true;
        }

    }

    /**
     * 查询数据库10条数据结果
     *
     * @param sLFTenMsgData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFTenMsgData sLFTenMsgData) {
        List<SLFChatBotMsgData> tenMsgDataList = changeSendingMsgStuatus(sLFTenMsgData.slfChatBotMsgData);

        if (sw_faq_recycle.isRefreshing()) {
            sw_faq_recycle.setRefreshing(false);
            if (tenMsgDataList == null || tenMsgDataList.size() == 0 || msg_id == MAX_ID) {
                ////msg_id==MAX_ID:第一次获取minId时，faqMsgList中的对象id都为0，不能刷新mag_id, 防止刷新重复添加数据
                return;
            }
            int position = tenMsgDataList.size();
            LinearLayoutManager linearManager = (LinearLayoutManager) rv_faq_chat_bot.getLayoutManager();
            //最后一个可见view的位置
            int mLastVisibleItemPosition = linearManager.findLastVisibleItemPosition();
            tenMsgDataList.addAll(faqMsgList);
            faqMsgList.clear();
            faqMsgList.addAll(tenMsgDataList);
            Collections.sort(faqMsgList);
            sLFChatBotRecyclerAdapter.setItemList(faqMsgList);
            sLFChatBotRecyclerAdapter.notifyDataSetChanged();
            rv_faq_chat_bot.scrollToPosition(mLastVisibleItemPosition + position - 1);
        } else {
            if (tenMsgDataList == null || tenMsgDataList.size() == 0) {
                getWelcomeHotQuestion();
            } else {
                faqMsgList.addAll(tenMsgDataList);
                Collections.sort(faqMsgList);
                setRcycleApdater();
                rv_faq_chat_bot.scrollToPosition(faqMsgList.size() - 1);
               // isFirstGetFromDataBase = true;
            }
        }
        minIdfaqMsgList();
    }

    //修改因界面推出保存的消息发送状态正在发送为发送失败
    private List<SLFChatBotMsgData> changeSendingMsgStuatus(List<SLFChatBotMsgData> slfChatBotMsgData) {
        List<SLFChatBotMsgData> msgList = new ArrayList<>();
        if (slfChatBotMsgData == null) {
            return msgList;
        }
        for (SLFChatBotMsgData msgData : slfChatBotMsgData) {
            if (msgData.getSend_msg_status() == SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue()) {
                msgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SEND_FAIL_MSG.getValue());
                slfdbEngine.update_msg(msgData);
            }
            msgList.add(msgData);
        }
        return msgList;
    }

    //获取最小的id
    private void minIdfaqMsgList() {
        for (SLFChatBotMsgData sLFChatBotMsgData : faqMsgList) {
            if (sLFChatBotMsgData.getId() < msg_id && sLFChatBotMsgData.getId() != 0) {
                msg_id = sLFChatBotMsgData.getId();
            }
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
    protected void onPause() {
        super.onPause();
        //页面退出时隐藏软键盘
        SLFToastUtil.cancel();
        et_faq_input.clearFocus();
        hideSoftInput(SLFChatBotActivity.this, et_faq_input);
    }


    /**
     * 刷新adapter
     */
    public void notiftyAdapter() {
        sLFChatBotRecyclerAdapter.notifyItemInserted(faqMsgList.size() - 1);
        rv_faq_chat_bot.scrollToPosition(faqMsgList.size() - 1);

    }

    /**
     * 用户发送消息后的网络失败回调
     *
     * @param type
     * @param requestTime
     */
    @Override
    public void onRequestChatBotNetFail(Object type, long requestTime) {
        if (type == SLFFaqOpenAiResponseBean.class) {
            showSendMsgStatus(SLFHttpStatusCode.NO_NETWORK_FAILED, requestTime);
        }
//        else if (type == SLFFaqMarkResponseBean.class) {
//            updateMsgMarkStatus(requestTime);
//        }
    }

    /**
     * 成功回调
     *
     * @param result
     * @param type
     * @param requestTime
     */
    @Override
    public void onRequestChatBotSuccess(String result, Object type, long requestTime) {
//        if (type instanceof SLFFaqSearchResponseBean) {
//            SLFFaqSearchResponseBean sLFFaqSearchResponseBean = (SLFFaqSearchResponseBean) type;
//            showSearchReslutData(sLFFaqSearchResponseBean, requestTime);
//        }

        if (type instanceof SLFFaqOpenAiResponseBean) {
            SLFLogUtil.sdkd("yj","result：：：chat_bot::"+result);
            SLFFaqOpenAiResponseBean slfFaqOpenAiResponseBean = (SLFFaqOpenAiResponseBean) type;
            SLFSpUtils.putCommit(SLFConstants.LASTSENDTIME, System.currentTimeMillis());
            showSearchReslutData(slfFaqOpenAiResponseBean, requestTime);

        }
        else if (type instanceof SLFFaqMarkResponseBean) {
            SLFFaqMarkResponseBean sLFFaqMarkResponseBean = (SLFFaqMarkResponseBean) type;
            showMarkReslutData(sLFFaqMarkResponseBean);
        }
    }

    /**
     * 失败回调
     *
     * @param value
     * @param failCode
     * @param type
     * @param requestTime
     */
    @Override
    public void onRequestChatBotFail(String value, String failCode, Object type, long requestTime) {
        if(failCode.equals(SLFHttpStatusCode.TOKEN_FAILED)){
            //TODO 重新获取token
            return;
        }
        if (type == SLFFaqOpenAiResponseBean.class) {
            if(failCode.equals(SLFHttpStatusCode.OPAI_FAIL_CODE)){
                showSendMsgStatus(SLFHttpStatusCode.OPAI_FAIL_CODE, requestTime);
                showCenterToast(SLFResourceUtils.getString(R.string.slf_create_feedback_illegal_world_text));
            }else {
                showSendMsgStatus(SLFHttpStatusCode.REQUEST_FAILED, requestTime);
            }
        }
//        else if (type == SLFFaqMarkResponseBean.class) {
//            updateMsgMarkStatus(requestTime);
//        }

    }

    /**
     * mark失败刷新mark数据
     *
     * @param requestTime
     */
    private void updateMsgMarkStatus(long requestTime) {
        for (SLFChatBotMsgData slfChatBotMsgData : faqMsgList) {
            if (slfChatBotMsgData.getMsgTime() == requestTime) {
                //slfChatBotMsgData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NO_SELECT.getValue());
                sLFChatBotRecyclerAdapter.notifyDataSetChanged();
                slfdbEngine.update_msg(slfChatBotMsgData);
                break;
            }
        }
    }

    //键盘监听事件
    public void setListenerFotEditTexts() {
        //键盘监听事件
        SLFSoftKeyBoardListener.setListener(SLFChatBotActivity.this,
                new SLFSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        if (et_faq_input.hasFocus()) {


                            RelativeLayout.LayoutParams params =
                                    new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params =
                                    (RelativeLayout.LayoutParams) slf_chat_bot_all_linear.getLayoutParams();
                            params.setMargins(0, 0, 0, SLFResourceUtils.dp2px(getContext(), 330));//left,top,right,bottom
                            slf_chat_bot_all_linear.setLayoutParams(params);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rv_faq_chat_bot.smoothScrollToPosition(faqMsgList.size() - 1);
                                }
                            }, 700);


                            SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord show");
                        }

                    }

                    @Override
                    public void keyBoardHide(int height) {
                        RelativeLayout.LayoutParams params =
                                new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params =
                                (RelativeLayout.LayoutParams) slf_chat_bot_all_linear.getLayoutParams();
                        params.setMargins(0, 0, 0, 0);//left,top,right,bottom
                        slf_chat_bot_all_linear.setLayoutParams(params);
                        SLFLogUtil.sdkd(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord hide");
                    }
                });
    }

    //获取键盘高度
    //记录原始窗口高度
    private int mWindowHeight = 0;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前窗口实际的可见区域
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int height = r.height();
            if (mWindowHeight == 0) {
                //一般情况下，这是原始的窗口高度
                mWindowHeight = height;
            } else {
                if (mWindowHeight != height) {
                    //两次窗口高度相减，就是软键盘高度
                    slfKeyBoardHeight = mWindowHeight - height;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消动画，防止内容泄露
        if (sLFChatBotRecyclerAdapter != null) {
            sLFChatBotRecyclerAdapter.destroy();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

}