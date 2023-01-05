package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.view.ViewGroup;

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
            slfChatBotBaseViewHodler = new SLFChatBotTimeViewHolder(new SLFChatBotDateView(context));
        }else if (viewType==SLFChatBotMsgData.MsgType.SINGLE_ROBOT_MSG.getValue()) {
            slfChatBotBaseViewHodler = new SLFChatBotSingleRobotViewHolder(new SLFChatBotIconTextLeftView(context));
        }else if (viewType==SLFChatBotMsgData.MsgType.SINGLE_USER_MSG.getValue()) {
            slfChatBotBaseViewHodler = new SLFChatBotSingleUserViewHolder(new SLFChatBotIconTextRightView(context));
        }else if (viewType==SLFChatBotMsgData.MsgType.HOT_ROBOT_MSG.getValue()) {
            slfChatBotBaseViewHodler = new SLFChatBotQuestionViewHolder(new SLFChatBotFaqListView(context));
        }else if (viewType==SLFChatBotMsgData.MsgType.FEEDBACK_ROBOT_MSG.getValue()) {
            slfChatBotBaseViewHodler = new SLFChatBotFeedBackViewHolder(new SLFChatBotFaqFeedBackView(context));
        }
        return slfChatBotBaseViewHodler;
    }

    @Override
    public void onBindViewHolder (@NonNull SLFChatBotBaseViewHodler holder, int position) {
        holder.bindView(itemList.get(position),position);
    }

    @Override
    public int getItemCount ( ) {
        return itemList.size();
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
