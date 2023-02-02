package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqListView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickQuesionEvent;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotUpdateQuesionEvent;
import com.wyze.sandglasslibrary.theme.SLFFontSet;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotQuestionViewHolder extends SLFChatBotBaseViewHodler {

    private LinearLayout ll_faq_list;
    private TextView tv_top_question;
    private ImageView iv_update;
    private ListView lv_faq_list;
    private Context context;
    private List <String> questionList = new ArrayList <>();
    private final String SPLIT_STR = "\"@%&\"";
    private List<String> questions = new ArrayList <>() ;
    private String questionStr;
    private int mIndex;
    private int position;


    public SLFChatBotQuestionViewHolder (@NonNull View itemView,Context context) {
        super(itemView);
        this.context = context;
        ll_faq_list = itemView.findViewById(R.id.ll_faq_list);
        tv_top_question = itemView.findViewById(R.id.tv_top_question);
        iv_update = itemView.findViewById(R.id.iv_update);
        lv_faq_list = itemView.findViewById(R.id.lv_faq_list);
        SLFFontSet.setSLF_RegularFont(context,tv_top_question);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ll_faq_list.getLayoutParams());
        layoutParams.width = SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116);
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.setMargins( SLFCommonUtils.dip2px(context,58), 0,0,0);
        ll_faq_list.setLayoutParams(layoutParams);

        iv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //更新question list
                if (questions!=null&&questions.size()>5&&mIndex==0){
                    EventBus.getDefault().post(new SLFChatBotUpdateQuesionEvent(mIndex,position));
                }
            }
        });

        lv_faq_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView <?> parent, View view, int position, long id) {
                //EventBus.getDefault().post(position);
                EventBus.getDefault().post(new SLFChatBotClickQuesionEvent(questionList.get(position),SLFChatBotMsgData.SEND_FROM_CLICK_HOT));
            }
        });
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        setPostion(position);
        setQustionType(bean.getType());
        setQuestionList(bean.getQuestion(),bean.getQuestion_index(),position);
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
        String[] questionArr=new String[20];
        questionList.clear();
        questions.clear();
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
            questionArr = question.split(SPLIT_STR);
        }else {
            questionArr[0] = question;
        }
        for (String questionItem:questionArr){
            if (!TextUtils.isEmpty(questionItem)){
                questions.add(questionItem);
            }
        }
        if (questions.size()>=index+5){
            for (int i = index; i<index+5;i++){
                questionList.add(questions.get(i).replaceAll("\n",""));
            }
        }else {
            for (int i = index; i<questions.size();i++){
                questionList.add(questions.get(i).replaceAll("\n",""));
            }
        }
        if (questionList==null||questionList.size()==0){
            return;
        }

        lv_faq_list.setAdapter(new ArrayAdapter <String>(context, R.layout.slf_chat_bot_question_item,R.id.tv_question_item, questionList){});
    }

    public void setPostion (int position) {
        this.position = position;
    }

}
