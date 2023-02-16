package com.sandglass.sandglasslibrary.functionmoudle.activity.chatbot;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.commonui.SLFClickEditText;
import com.sandglass.sandglasslibrary.commonui.SLFFITRelativeLayout;
import com.sandglass.sandglasslibrary.commonui.SLFToastUtil;
import com.sandglass.sandglasslibrary.dao.SLFDBEngine;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.chatbot.SLFChatBotRecyclerAdapter;
import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotUpdateQuesionEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFTenMsgData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqMarkResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqSearchReslutBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqSearchResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqWelcomeHotQResponseBean;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpChatBotRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.theme.SLFSetTheme;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    //是否需要增加时间item
    private boolean isFirstGetFromDataBase = false;
    private Handler handler;
    private long lastSendTime;
    private int msg_id = MAX_ID;
    private int tempLineCount;
    /**
     * 键盘高度
     */
    private int slfKeyBoardHeight;

    private LinearLayout slf_chat_bot_all_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slfchat_bot);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        slfdbEngine = new SLFDBEngine(this);
        initView();
        initData();
    }

    private void initData() {

        if ((System.currentTimeMillis() - SLFSpUtils.getLong(LAST_ENTER_PAGE, 0)) / (1000 * 60 * 60 * 24) > 7) {
            slfdbEngine.delete_all_msg();
            getWelcomeHotQuestion();
        } else {
            //请求数据库查找记录
            slfdbEngine.quary_ten_msg(msg_id);
        }

        SLFSpUtils.putCommit(LAST_ENTER_PAGE, System.currentTimeMillis());
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
     * @param returnOther 是否返回 faqList 默认为true
     */
    private void postSearch(String content, boolean returnOther, long requesTime) {
        TreeMap requestMap = new TreeMap();
        requestMap.put("content", content);
        requestMap.put("returnOther", returnOther);
        SLFHttpUtils.getInstance().executePost(this, SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_SEARCH, requestMap, SLFFaqSearchResponseBean.class, requesTime, this);
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
        slf_tv_title_name.setText(R.string.slf_faq_title);
        sw_faq_recycle = findViewById(R.id.sw_faq_recycle);
        rv_faq_chat_bot = findViewById(R.id.rv_faq_chat_bot);
        et_faq_input = findViewById(R.id.et_faq_input);
        ll_et_input = findViewById(R.id.ll_et_input);
        slf_chat_bot_all_linear = findViewById(R.id.slf_chat_bot_all_linear);
        SLFFITRelativeLayout chat_bot_root = findViewById(R.id.chat_bot_root);
        setListenerFotEditTexts();
        chat_bot_root.setBackgroundColor(SLFSetTheme.defaultBackgroundColor);
        SLFFontSet.setSLF_RegularFont(this, slf_tv_title_name);
        // 复写EditText的onTouch方法
//        et_faq_input.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // 当触摸的是EditText & 当前EditText可滚动时，则将事件交给EditText处理；
//                if ((v.getId() == R.id.et_faq_input && canVerticalScroll(et_faq_input))) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    // 否则将事件交由其父类处理
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        v.getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                }
//                return false;
//            }
//        });

        sLFChatBotRecyclerAdapter = new SLFChatBotRecyclerAdapter(this);
        //et_faq_input.setEnabled(false);
        view_click();
        handler = new Handler(getMainLooper());
    }

    // 判断当前EditText是否可滚动
    private boolean canVerticalScroll(EditText editText) {

        if (editText.getLineCount() > editText.getMaxLines()) {
            return true;
        }
        return false;
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
                    String send_msg = et_faq_input.getText().toString();
                    if (!TextUtils.isEmpty(send_msg)) {
                        sendMsg(et_faq_input.getText().toString(), SLFChatBotMsgData.SEND_FROM_INPUT);
                        et_faq_input.setText("");
                        // return true;
                    }
                }
                return true;//返回true，保留软键盘。false，隐藏软键盘
            }
        });
        //监听输入字数不得超过500
        et_faq_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempLineCount = et_faq_input.getLineCount();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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
                if (et_faq_input.getText().length() == 500) {
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
        return (System.currentTimeMillis() - lastSendTime) / 6000 > 5;
    }

    /**
     * 点击输入框
     */
    private void onInput() {
        et_faq_input.requestFocus();
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
        showNetworkError();
    }

    @Override
    public void onRequestSuccess(String result, Object type) {
        if (type instanceof SLFFaqWelcomeHotQResponseBean) {
            SLFFaqWelcomeHotQResponseBean sLFFaqWelcomeHotQResponseBean = (SLFFaqWelcomeHotQResponseBean) type;
            showWelcomeData(sLFFaqWelcomeHotQResponseBean);
        }
    }

    @Override
    public void onRequestFail(String value, String failCode, Object type) {
        showCenterToast(getResources().getString(R.string.slf_common_request_error));
    }

    /**
     * 显示查询结果
     *
     * @param sLFFaqSearchResponseBean
     */

    private void showSearchReslutData(SLFFaqSearchResponseBean sLFFaqSearchResponseBean, long requestTime) {
        SLFFaqSearchReslutBean slfFaqSearchReslutBean = sLFFaqSearchResponseBean.data;
        //先更新用户发送的小心状态
        showSendMsgStatus(true, requestTime);
        if (!TextUtils.isEmpty(slfFaqSearchReslutBean.noDataText) && slfFaqSearchReslutBean.answer == null) {
            SLFChatBotMsgData slfChatBotRobotNoAnswerData = new SLFChatBotMsgData();
            slfChatBotRobotNoAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotNoAnswerData.setType(SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue());
            slfChatBotRobotNoAnswerData.setContent(slfFaqSearchReslutBean.noDataText);
            faqMsgList.add(slfChatBotRobotNoAnswerData);
            notiftyAdapter();
            //保存数据库
            slfdbEngine.insert_msg(slfChatBotRobotNoAnswerData);

        } else if (slfFaqSearchReslutBean.answer != null) {
            SLFChatBotMsgData slfChatBotRobotAnswerData = new SLFChatBotMsgData();
            slfChatBotRobotAnswerData.setMsgTime(System.currentTimeMillis());
            slfChatBotRobotAnswerData.setType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
            slfChatBotRobotAnswerData.setContent(slfFaqSearchReslutBean.answer.content);
            slfChatBotRobotAnswerData.setTitle(slfFaqSearchReslutBean.answer.title);
            slfChatBotRobotAnswerData.setFaqId(slfFaqSearchReslutBean.answer.id);
            int faqSize = 0;
            StringBuilder sb = new StringBuilder();
            for (String question : slfFaqSearchReslutBean.faqList) {
                faqSize++;
                sb.append(question);
                if (faqSize != slfFaqSearchReslutBean.faqList.size()) {
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
     *
     * @param isSendSuccess
     * @param requestTime
     */
    private void showSendMsgStatus(boolean isSendSuccess, long requestTime) {
        for (SLFChatBotMsgData slfChatBotMsgData : faqMsgList) {
            if (slfChatBotMsgData.getMsgTime() == requestTime) {
                if (isSendSuccess) {
                    slfChatBotMsgData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue());
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
        faqMsgList.add(slfChatBotWelcomeData);
        slfdbEngine.insert_msg(slfChatBotWelcomeData);

        if (sLFFaqWelcomeHotQResponseBean.data.hotFaq.size() > 0) {
            int faqSize = 0;
            StringBuilder sb = new StringBuilder();
            SLFChatBotMsgData slfChatBotHotData = new SLFChatBotMsgData();
            slfChatBotHotData.setMsgTime(System.currentTimeMillis() + 1);
            slfChatBotHotData.setType(SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue());
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

        if (faqMsgList.size() > 3) {//表示不是新的faq

        } else {//表示新faq
            setRcycleApdater();
        }
    }

    private void addTimeItem() {
        SLFChatBotMsgData slfChatBotTimeItemData = new SLFChatBotMsgData();
        slfChatBotTimeItemData.setMsgTime(System.currentTimeMillis() - 2);
        slfChatBotTimeItemData.setType(SLFChatBotMsgData.MsgType.SINGLE_TIME_MSG.getValue());
        faqMsgList.add(slfChatBotTimeItemData);
        notiftyAdapter();
        slfdbEngine.insert_msg(slfChatBotTimeItemData);
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
        slfChatBotUpdateQuesionData.setQuestion_index(slfChatBotUpdateQuesionData.getQuestion_index() + 5);
        notiftyAdapter();
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
        lastSendTime = System.currentTimeMillis();
        if (isAddTimeItem()) {
            addTimeItem();
            isFirstGetFromDataBase = false;
        }
        SLFChatBotMsgData slfChatBotItemClickQuesionData = new SLFChatBotMsgData();
        long time = System.currentTimeMillis();
        slfChatBotItemClickQuesionData.setMsgTime(time);
        slfChatBotItemClickQuesionData.setType(SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue());
        slfChatBotItemClickQuesionData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        slfChatBotItemClickQuesionData.setContent(question);
        faqMsgList.add(slfChatBotItemClickQuesionData);
        notiftyAdapter();
        //发送question数据
        if (fromType == SLFChatBotMsgData.SEND_FROM_CLICK_RELATE) {
            postSearch(question, false, time);
        } else {
            postSearch(question, true, time);
        }
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
        if (event.isSolution == 1) {
            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_EFFECTIVE.getValue());
        } else if (event.isSolution == 0) {
            slfChatBotAnswerEffectData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NOEFFECTIVE.getValue());
        }
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //发送FAQ是否解决
        postQuestionMark(slfChatBotAnswerEffectData.getFaqId(), event.isSolution, slfChatBotAnswerEffectData.getMsgTime());
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
        lastSendTime = System.currentTimeMillis();
//        if (isAddTimeItem()){
//            addTimeItem();
//            isFirstGetFromDataBase = false;
//        }
        SLFChatBotMsgData slfChatBotClickNoSendData = faqMsgList.get(event.position);
        slfChatBotClickNoSendData.setSend_msg_status(SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue());
        sLFChatBotRecyclerAdapter.notifyDataSetChanged();
        //重新发送数据
        postSearch(event.question, true, slfChatBotClickNoSendData.getMsgTime());
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
            isFirstGetFromDataBase = true;
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
                isFirstGetFromDataBase = true;
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
        if (type == SLFFaqSearchResponseBean.class) {
            showSendMsgStatus(false, requestTime);
        } else if (type == SLFFaqMarkResponseBean.class) {
            updateMsgMarkStatus(requestTime);
        }
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
        if (type instanceof SLFFaqSearchResponseBean) {
            SLFFaqSearchResponseBean sLFFaqSearchResponseBean = (SLFFaqSearchResponseBean) type;
            showSearchReslutData(sLFFaqSearchResponseBean, requestTime);
        } else if (type instanceof SLFFaqMarkResponseBean) {
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
        if (type == SLFFaqSearchResponseBean.class) {
            showSendMsgStatus(false, requestTime);
        } else if (type == SLFFaqMarkResponseBean.class) {
            updateMsgMarkStatus(requestTime);
        }

    }

    /**
     * mark失败刷新mark数据
     *
     * @param requestTime
     */
    private void updateMsgMarkStatus(long requestTime) {
        for (SLFChatBotMsgData slfChatBotMsgData : faqMsgList) {
            if (slfChatBotMsgData.getMsgTime() == requestTime) {
                slfChatBotMsgData.setAnswer_effective(SLFChatBotMsgData.AnswerEffective.ANSWER_NO_SELECT.getValue());
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


                            SLFLogUtil.d(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord show");
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
                        SLFLogUtil.d(TAG, "ActivityName:" + this.getClass().getSimpleName() + ":keybord hide");
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