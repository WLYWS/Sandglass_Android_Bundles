package com.wyze.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.media.metrics.Event;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot聊天中包含解决结果的反馈view
 */
public class SLFChatBotFaqFeedBackView extends ConstraintLayout implements View.OnClickListener {
    private LinearLayout ll_faq_answer;
    private TextView tv_faq_feedback_title;
    private SLFChatBotFaqListView sf_faq_feedback_question;
    private TextView tv_faq_feedback_answer;
    private ImageView iv_faq_question_up,iv_faq_question_center,iv_faq_question_down;
    private View view_question_line;
    private final int QUSTION_SOLUTION = 1;
    private final int QUSTION_NO_SOLUTION = 0;
    private int position;

    public SLFChatBotFaqFeedBackView (Context context) {
        super(context);
        initView(context);
    }

    public SLFChatBotFaqFeedBackView (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SLFChatBotFaqFeedBackView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView (Context context) {
        View faqFeedBackView = View.inflate(context,R.layout.slf_faq_feedback_result,null);
        ll_faq_answer = faqFeedBackView.findViewById(R.id.ll_faq_answer);
        tv_faq_feedback_title = faqFeedBackView.findViewById(R.id.tv_faq_feedback_title);
        tv_faq_feedback_answer = faqFeedBackView.findViewById(R.id.tv_faq_feedback_answer);
        sf_faq_feedback_question = faqFeedBackView.findViewById(R.id.sf_faq_feedback_question);
        iv_faq_question_up = faqFeedBackView.findViewById(R.id.iv_faq_question_up);
        iv_faq_question_center = faqFeedBackView.findViewById(R.id.iv_faq_question_center);
        iv_faq_question_down = faqFeedBackView.findViewById(R.id.iv_faq_question_down);
        view_question_line = faqFeedBackView.findViewById(R.id.view_question_line);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ll_faq_answer.getLayoutParams());
        layoutParams.width = SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116);
        ll_faq_answer.setLayoutParams(layoutParams);

        iv_faq_question_up.setOnClickListener(this);

    }

    public void setTitle (String title) {
        tv_faq_feedback_title.setText(title);
    }

    public void setContent (String content) {
        tv_faq_feedback_answer.setText(content);
    }

    public void setQustionList (String question, int question_index,int position) {
        this.position = position;
        if (TextUtils.isEmpty(question)){
            sf_faq_feedback_question.setVisibility(View.GONE);
            view_question_line.setVisibility(View.GONE);
            return;
        }else {
            sf_faq_feedback_question.setVisibility(View.VISIBLE);
            view_question_line.setVisibility(View.VISIBLE);
            sf_faq_feedback_question.setQustionType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
            sf_faq_feedback_question.setQuestionList(question,question_index,position);
        }

    }

    public void setAnswer_effective (int answer_effective) {
        if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_NO_SELECT.getValue()){
            iv_faq_question_up.setVisibility(View.VISIBLE);
            iv_faq_question_center.setVisibility(View.GONE);
            iv_faq_question_down.setVisibility(View.VISIBLE);
        }else if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_NOEFFECTIVE.getValue()){
            iv_faq_question_up.setVisibility(View.GONE);
            iv_faq_question_center.setVisibility(View.VISIBLE);
            iv_faq_question_down.setVisibility(View.GONE);
            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_down);
        }else if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_EFFECTIVE.getValue()){
            iv_faq_question_up.setVisibility(View.GONE);
            iv_faq_question_center.setVisibility(View.VISIBLE);
            iv_faq_question_down.setVisibility(View.GONE);
            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_up);
        }
    }

    @Override
    public void onClick (View v) {
        if (v.getId() == R.id.iv_faq_question_up) {
            iv_faq_question_up.setVisibility(View.GONE);
            iv_faq_question_center.setVisibility(View.VISIBLE);
            iv_faq_question_down.setVisibility(View.GONE);
            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_up);
        }else if (v.getId() == R.id.iv_faq_question_down){
            iv_faq_question_up.setVisibility(View.GONE);
            iv_faq_question_center.setVisibility(View.VISIBLE);
            iv_faq_question_down.setVisibility(View.GONE);
            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_down);
        }
        EventBus.getDefault().post(new SLFChatBotAnswerEffectEvent(QUSTION_NO_SOLUTION,position));
    }

    public void setPostion (int position) {
        this.position = position;
    }
}
