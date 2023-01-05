package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextRightView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotSingleUserViewHolder extends SLFChatBotBaseViewHodler {

    private SLFChatBotIconTextRightView itemView;
    public SLFChatBotSingleUserViewHolder (@NonNull SLFChatBotIconTextRightView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        itemView.setPosition(position);
        itemView.setText(bean.getContent());
        itemView.setSendStatus(bean.getSend_msg_status());
    }
}
