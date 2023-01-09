package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqFeedBackView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqListView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotFeedBackViewHolder extends SLFChatBotBaseViewHodler implements View.OnClickListener {

    private LinearLayout ll_faq_answer;
    private TextView tv_faq_feedback_title;
    //private SLFChatBotFaqListView sf_faq_feedback_question;
    private TextView tv_faq_feedback_answer;
    private ImageView iv_faq_question_up,iv_faq_question_center,iv_faq_question_down;
    private View view_question_line;
    private final int QUSTION_SOLUTION = 1;
    private final int QUSTION_NO_SOLUTION = 0;
    private int position;

    public SLFChatBotFeedBackViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        ll_faq_answer = itemView.findViewById(R.id.ll_faq_answer);
        tv_faq_feedback_title = itemView.findViewById(R.id.tv_faq_feedback_title);
        tv_faq_feedback_answer = itemView.findViewById(R.id.tv_faq_feedback_answer);
        //sf_faq_feedback_question = itemView.findViewById(R.id.sf_faq_feedback_question);
        iv_faq_question_up = itemView.findViewById(R.id.iv_faq_question_up);
        iv_faq_question_center = itemView.findViewById(R.id.iv_faq_question_center);
        iv_faq_question_down = itemView.findViewById(R.id.iv_faq_question_down);
        view_question_line = itemView.findViewById(R.id.view_question_line);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ll_faq_answer.getLayoutParams());
        layoutParams.width = SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.setMargins( SLFCommonUtils.dip2px(context,58), 0,0,0);
        ll_faq_answer.setLayoutParams(layoutParams);

        iv_faq_question_up.setOnClickListener(this);
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        setPostion(position);
        setTitle(bean.getTitle());
        setContent(bean.getContent());
        setQustionList(bean.getQuestion(),bean.getQuestion_index(),position);
        setAnswer_effective(bean.getAnswer_effective());
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
            //sf_faq_feedback_question.setVisibility(View.GONE);
            view_question_line.setVisibility(View.GONE);
            return;
        }else {
            //sf_faq_feedback_question.setVisibility(View.VISIBLE);
            view_question_line.setVisibility(View.VISIBLE);
            //sf_faq_feedback_question.setQustionType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
            //sf_faq_feedback_question.setQuestionList(question,question_index,position);
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
