package com.wyze.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotUpdateQuesionEvent;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot聊天中FAQ问题呈现
 */
public class SLFChatBotFaqListView extends ConstraintLayout {
    private LinearLayout ll_faq_list;
    private TextView tv_top_question;
    private ImageView iv_update;
    private ListView lv_faq_list;
    private Context context;
    private List <String>  questionList = new ArrayList <>();
    private final String SPLIT_STR = "\"@%&\"";
    private String[] questions = new String[20];
    private String questionStr;
    private int mIndex;
    private int position;

    public SLFChatBotFaqListView (@NonNull Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public SLFChatBotFaqListView (@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public SLFChatBotFaqListView (@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView (Context context) {
        View slf_chat_bot_faq_list_view = View.inflate(context, R.layout.slf_chat_bot_faq_list,null);
        ll_faq_list = slf_chat_bot_faq_list_view.findViewById(R.id.ll_faq_list);
        tv_top_question = slf_chat_bot_faq_list_view.findViewById(R.id.tv_top_question);
        iv_update = slf_chat_bot_faq_list_view.findViewById(R.id.iv_update);
        lv_faq_list = slf_chat_bot_faq_list_view.findViewById(R.id.lv_faq_list);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ll_faq_list.getLayoutParams());
        layoutParams.width = SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116);
        ll_faq_list.setLayoutParams(layoutParams);

        iv_update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View v) {
                //更新question list
                if (questions!=null&&questions.length>5){
                    EventBus.getDefault().post(new SLFChatBotUpdateQuesionEvent(mIndex,position));
                }
            }
        });

        lv_faq_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> parent, View view, int position, long id) {
                //EventBus.getDefault().post(position);
                EventBus.getDefault().post(new SLFChatBotClickQuesionEvent(questionList.get(position)));
            }
        });

    }

    public void setQustionType (int type) {
        if (type == SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue()){
            iv_update.setVisibility(View.VISIBLE);
            tv_top_question.setText(R.string.slf_top_question);

        }else if (type == SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue()){
            iv_update.setVisibility(View.GONE);
            tv_top_question.setText(R.string.slf_related_question);
        }
    }

    public void setQuestionList (String question,int index,int position) {
        this.position = position;
        mIndex = index;
        questionStr = question;
        if (TextUtils.isEmpty(question)){
            lv_faq_list.setVisibility(View.GONE);
            return;
        }else {
            lv_faq_list.setVisibility(View.VISIBLE);
        }
        if (index==-1){
            lv_faq_list.setVisibility(View.GONE);
        }
        if (question.contains(SPLIT_STR)){
            questions = question.split(SPLIT_STR);
        }else {
            questions[0] = question;
        }
        if (questions.length>=index+5){
            for (int i = index; i<index+5;i++){
                questionList.add(questions[i]);
            }
        }else {
            for (int i = index; i<questions.length;i++){
                questionList.add(questions[i]);
            }
        }
        if (questionList==null||questionList.size()==0){
            return;
        }
        lv_faq_list.setAdapter(new ArrayAdapter <String>(context, R.layout.slf_chat_bot_question_item, questionList));
    }

    public void setPostion (int position) {
        this.position = position;
    }
}
