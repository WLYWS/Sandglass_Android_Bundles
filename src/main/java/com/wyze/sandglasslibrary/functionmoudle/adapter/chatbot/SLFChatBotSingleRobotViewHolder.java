package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.view.View;

import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotSingleRobotViewHolder extends SLFChatBotBaseViewHodler {

    private SLFChatBotIconTextLeftView itemView;
    public SLFChatBotSingleRobotViewHolder (@NonNull SLFChatBotIconTextLeftView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        itemView.setText(bean.getContent());
    }
}
