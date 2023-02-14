package com.sandglass.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.view.View;

import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wangjian on 2023/1/5
 */
public abstract class SLFChatBotBaseViewHodler extends RecyclerView.ViewHolder {

    public SLFChatBotBaseViewHodler (@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindView(SLFChatBotMsgData bean,int position);
}
