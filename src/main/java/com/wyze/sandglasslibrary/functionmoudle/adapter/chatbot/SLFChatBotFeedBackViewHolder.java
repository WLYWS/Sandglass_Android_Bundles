package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqFeedBackView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotFeedBackViewHolder extends SLFChatBotBaseViewHodler {

    private SLFChatBotFaqFeedBackView itemView;
    public SLFChatBotFeedBackViewHolder (@NonNull SLFChatBotFaqFeedBackView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        itemView.setPostion(position);
        itemView.setTitle(bean.getTitle());
        itemView.setContent(bean.getContent());
        itemView.setQustionList(bean.getQuestion(),bean.getQuestion_index(),position);
        itemView.setAnswer_effective(bean.getAnswer_effective());
    }
}
