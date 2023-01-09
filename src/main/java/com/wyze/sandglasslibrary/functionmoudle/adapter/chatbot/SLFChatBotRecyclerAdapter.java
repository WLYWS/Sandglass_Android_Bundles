package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotDateView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqFeedBackView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotFaqListView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextRightView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotRecyclerAdapter extends RecyclerView.Adapter<SLFChatBotBaseViewHodler> {
    private List <SLFChatBotMsgData> itemList = new ArrayList <>();
    private Context context;

    public SLFChatBotRecyclerAdapter (Context context) {
        this.context = context;
    }

    public void setItemList (List <SLFChatBotMsgData> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SLFChatBotBaseViewHodler onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        SLFChatBotBaseViewHodler slfChatBotBaseViewHodler = null;
        if (viewType==SLFChatBotMsgData.MsgType.SINGLE_TIME_MSG.getValue()){
            View chatBotTimeview = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_chat_bot_date,parent,false);
            slfChatBotBaseViewHodler = new SLFChatBotTimeViewHolder(chatBotTimeview,context);
        }else if (viewType==SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue()) {
            View chatBotSingleRobotview = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_chat_bot_left,parent,false);
            slfChatBotBaseViewHodler = new SLFChatBotSingleRobotViewHolder(chatBotSingleRobotview,context);
        }else if (viewType==SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue()) {
            View chatBotSingleUserRobotview = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_chat_bot_right,parent,false);
            slfChatBotBaseViewHodler = new SLFChatBotSingleUserViewHolder(chatBotSingleUserRobotview,context);
        }else if (viewType==SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue()) {
            View chatBotQuestionview = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_chat_bot_faq_list,parent,false);
            slfChatBotBaseViewHodler = new SLFChatBotQuestionViewHolder(chatBotQuestionview,context);
        }else if (viewType==SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue()) {
            View chatBotFeedBackview = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_faq_feedback_result,parent,false);
            slfChatBotBaseViewHodler = new SLFChatBotFeedBackViewHolder(chatBotFeedBackview,context);
        }
        return slfChatBotBaseViewHodler;
    }

    @Override
    public void onBindViewHolder (@NonNull SLFChatBotBaseViewHodler holder, int position) {
        holder.bindView(itemList.get(position),position);
    }

    @Override
    public int getItemCount ( ) {
        return itemList==null?0:itemList.size();
    }

    @Override
    public int getItemViewType (int position) {
        if (itemList==null && itemList.size()<0){
            return 0;
        }
        SLFChatBotMsgData slfChatBotMsgData = itemList.get(position);
        return slfChatBotMsgData.getType();
    }
}
