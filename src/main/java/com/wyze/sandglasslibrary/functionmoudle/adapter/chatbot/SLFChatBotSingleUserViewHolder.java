package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.wyze.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextRightView;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotSingleUserViewHolder extends SLFChatBotBaseViewHodler {


    private ImageView iv_chat_bot_user_icon;
    private TextView tv_chat_bot_user_text;
    private ImageView iv_chat_bot_user_warn;
    private int position;


    public SLFChatBotSingleUserViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        iv_chat_bot_user_icon = itemView.findViewById(R.id.iv_chat_bot_user_icon);
        tv_chat_bot_user_text = itemView.findViewById(R.id.tv_chat_bot_user_text);
        iv_chat_bot_user_warn = itemView.findViewById(R.id.iv_chat_bot_user_warn);
        tv_chat_bot_user_text.setMaxWidth(SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116));

        iv_chat_bot_user_warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                EventBus.getDefault().post(new SLFChatBotClickNoSendWarnEvent(tv_chat_bot_user_text.getText().toString(),position));
            }
        });
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        this.position = position;
        tv_chat_bot_user_text.setText(bean.getContent());
        setSendStatus(bean.getSend_msg_status());
    }

    public void setText(CharSequence text){
        tv_chat_bot_user_text.setText(text);
    }

    public void setSendStatus (int send_msg_status) {
        if (send_msg_status == SLFChatBotMsgData.MsgSendStatus.SEND_FAIL_MSG.getValue()){
            iv_chat_bot_user_warn.setImageResource(R.mipmap.slf_chat_bot_user_warn);
            iv_chat_bot_user_warn.setVisibility(View.VISIBLE);
        }else if(send_msg_status == SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue()){
            iv_chat_bot_user_warn.setVisibility(View.GONE);
        }else if (send_msg_status == SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue()){
            //显示正在加载
            iv_chat_bot_user_warn.setVisibility(View.VISIBLE);
        }
    }

    public void setPosition (int position) {
        this.position = position;
    }
}
