package com.sandglass.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.commonui.SLFNoScrollListview;
import com.sandglass.sandglasslibrary.commonui.chatbot.SLFChatBotFaqFeedBackView;
import com.sandglass.sandglasslibrary.commonui.chatbot.SLFChatBotFaqListView;
import com.sandglass.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotAnswerEffectEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotFeedBackViewHolder extends SLFChatBotBaseViewHodler implements View.OnClickListener {

//    private SLFNoScrollListview lv_faq_feedback_list;
//    private TextView tv_relate_question;
    //private LinearLayout ll_faq_feedback_list;
    private LinearLayout ll_faq_answer;
//    private TextView tv_faq_feedback_title;
    //private SLFChatBotFaqListView sf_faq_feedback_question;
    private TextView tv_faq_feedback_answer;
    //private ImageView iv_faq_question_up,iv_faq_question_center,iv_faq_question_down;
    //private View view_question_line;
    private final int QUSTION_SOLUTION = 1;
    private final int QUSTION_NO_SOLUTION = 0;
    private int position;
    private final String SPLIT_STR = "\"@%&\"";
    private List<String> questions = new ArrayList <>() ;
    private String questionStr;
    private int mIndex;
    private Context context;

    public SLFChatBotFeedBackViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        ll_faq_answer = itemView.findViewById(R.id.ll_faq_answer);
//        tv_faq_feedback_title = itemView.findViewById(R.id.tv_faq_feedback_title);
        tv_faq_feedback_answer = itemView.findViewById(R.id.tv_faq_feedback_answer);
        //sf_faq_feedback_question = itemView.findViewById(R.id.sf_faq_feedback_question);
//        iv_faq_question_up = itemView.findViewById(R.id.iv_faq_question_up);
//        iv_faq_question_center = itemView.findViewById(R.id.iv_faq_question_center);
//        iv_faq_question_down = itemView.findViewById(R.id.iv_faq_question_down);
//        view_question_line = itemView.findViewById(R.id.view_question_line);
//        ll_faq_feedback_list = itemView.findViewById(R.id.ll_faq_feedback_list);
//        tv_relate_question = itemView.findViewById(R.id.tv_relate_question);
//        lv_faq_feedback_list = itemView.findViewById(R.id.lv_faq_feedback_list);
//        SLFFontSet.setSLF_RegularFont(context,tv_faq_feedback_title);
//        SLFFontSet.setSLF_RegularFont(context,tv_relate_question);
        SLFFontSet.setSLF_RegularFont(context,tv_faq_feedback_answer);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ll_faq_answer.getLayoutParams());
        layoutParams.width = SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.setMargins( SLFCommonUtils.dip2px(context,58), 0,0,0);
        ll_faq_answer.setLayoutParams(layoutParams);

//        iv_faq_question_up.setOnClickListener(this);
//        iv_faq_question_down.setOnClickListener(this);

//        lv_faq_feedback_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick (AdapterView <?> parent, View view, int position, long id) {
//                //EventBus.getDefault().post(position);
//                EventBus.getDefault().post(new SLFChatBotClickQuesionEvent(questions.get(position),SLFChatBotMsgData.SEND_FROM_CLICK_RELATE));
//            }
//        });
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        setPostion(position);
        //setTitle(bean.getTitle());
        setContent(bean.getContent());
//        setQustionList(bean.getQuestion(),position);
//        setAnswer_effective(bean.getAnswer_effective());
    }

//    public void setTitle (String title) {
//        tv_faq_feedback_title.setText(title);
//    }

    public void setContent (String content) {
        tv_faq_feedback_answer.setText(Html.fromHtml(content, 0));
    }

//    public void setQustionList (String question,int position) {
//        this.position = position;
//        if (TextUtils.isEmpty(question)){
//            ll_faq_feedback_list.setVisibility(View.GONE);
//            view_question_line.setVisibility(View.GONE);
//            return;
//        }else {
//            ll_faq_feedback_list.setVisibility(View.VISIBLE);
//            view_question_line.setVisibility(View.VISIBLE);
//            ll_faq_feedback_list.setQustionType(SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue());
//            showQuestionList(question,position);
//        }
//
//    }

    private void showQuestionList (String question, int position) {

        this.position = position;
        questionStr = question;
        questions.clear();
        String[] questionArr=new String[20];
//        if (TextUtils.isEmpty(question)){
//            ll_faq_feedback_list.setVisibility(View.GONE);
//            view_question_line.setVisibility(View.GONE);
//            return;
//        }else {
//            ll_faq_feedback_list.setVisibility(View.VISIBLE);
//            view_question_line.setVisibility(View.VISIBLE);
//        }
        if (question.contains(SPLIT_STR)){
            questionArr = question.split(SPLIT_STR);
        }else {
            questionArr[0] = question;
        }
        for (String questionItem:questionArr){
            if (!TextUtils.isEmpty(questionItem)){
                questions.add(questionItem.replaceAll("\n",""));
            }
        }
//        if (questions==null||questions.size()==0){
//            ll_faq_feedback_list.setVisibility(View.GONE);
//            view_question_line.setVisibility(View.GONE);
//            return;
//        }
//        lv_faq_feedback_list.setAdapter(new ArrayAdapter <String>(context, R.layout.slf_chat_bot_question_item,R.id.tv_question_item, questions));
    }

//    public void setAnswer_effective (int answer_effective) {
//        if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_NO_SELECT.getValue()){
//            iv_faq_question_up.setVisibility(View.VISIBLE);
//            iv_faq_question_center.setVisibility(View.GONE);
//            iv_faq_question_down.setVisibility(View.VISIBLE);
//        }else if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_NOEFFECTIVE.getValue()){
//            iv_faq_question_up.setVisibility(View.GONE);
//            iv_faq_question_center.setVisibility(View.VISIBLE);
//            iv_faq_question_down.setVisibility(View.GONE);
//            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_down);
//        }else if (answer_effective == SLFChatBotMsgData.AnswerEffective.ANSWER_EFFECTIVE.getValue()){
//            iv_faq_question_up.setVisibility(View.GONE);
//            iv_faq_question_center.setVisibility(View.VISIBLE);
//            iv_faq_question_down.setVisibility(View.GONE);
//            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_up);
//        }
//    }

    @Override
    public void onClick (View v) {
//        if (v.getId() == R.id.iv_faq_question_up) {
////            iv_faq_question_up.setVisibility(View.GONE);
////            iv_faq_question_center.setVisibility(View.VISIBLE);
////            iv_faq_question_down.setVisibility(View.GONE);
////            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_up);
//            EventBus.getDefault().post(new SLFChatBotAnswerEffectEvent(QUSTION_SOLUTION,position));
//        }else if (v.getId() == R.id.iv_faq_question_down){
////            iv_faq_question_up.setVisibility(View.GONE);
////            iv_faq_question_center.setVisibility(View.VISIBLE);
////            iv_faq_question_down.setVisibility(View.GONE);
////            iv_faq_question_center.setImageResource(R.mipmap.slf_faq_answer_down);
//            EventBus.getDefault().post(new SLFChatBotAnswerEffectEvent(QUSTION_NO_SOLUTION,position));
//        }
    }

    public void setPostion (int position) {
        this.position = position;
    }

}
