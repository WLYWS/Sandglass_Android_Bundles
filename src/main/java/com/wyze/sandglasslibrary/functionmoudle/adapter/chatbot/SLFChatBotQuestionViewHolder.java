package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqListView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotQuestionViewHolder extends SLFChatBotBaseViewHodler {

    private SLFChatBotFaqListView itemView;
    public SLFChatBotQuestionViewHolder (@NonNull SLFChatBotFaqListView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        itemView.setPostion(position);
        itemView.setQustionType(bean.getType());
        itemView.setQuestionList(bean.getQuestion(),bean.getQuestion_index(),position);
    }
}
