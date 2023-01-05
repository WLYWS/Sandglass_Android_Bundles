package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.view.View;

import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotDateView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotTimeViewHolder extends SLFChatBotBaseViewHodler{

    private SLFChatBotDateView itemView;
    public SLFChatBotTimeViewHolder (@NonNull SLFChatBotDateView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        itemView.setDateText(String.valueOf(bean.getMsgTime()));
    }
}
