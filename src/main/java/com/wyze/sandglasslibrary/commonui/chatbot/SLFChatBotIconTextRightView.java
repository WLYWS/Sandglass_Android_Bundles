package com.wyze.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot聊天中只包含icon和文字的item
 */
public class SLFChatBotIconTextRightView extends ConstraintLayout {

    private ImageView iv_chat_bot_user_icon;
    private TextView tv_chat_bot_user_text;
    private ImageView iv_chat_bot_user_warn;
    private int position;

    public SLFChatBotIconTextRightView (Context context) {
        super(context);
        initView(context);
    }

    public SLFChatBotIconTextRightView (Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SLFChatBotIconTextRightView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView (Context context) {
        View rightChatItemLayout = View.inflate(context, R.layout.slf_chat_bot_right,null);
        iv_chat_bot_user_icon = rightChatItemLayout.findViewById(R.id.iv_chat_bot_user_icon);
        tv_chat_bot_user_text = rightChatItemLayout.findViewById(R.id.tv_chat_bot_user_text);
        iv_chat_bot_user_warn = rightChatItemLayout.findViewById(R.id.iv_chat_bot_user_warn);
        tv_chat_bot_user_text.setMaxWidth(SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116));

        iv_chat_bot_user_warn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick (View v) {
                EventBus.getDefault().post(new SLFChatBotClickNoSendWarnEvent(tv_chat_bot_user_text.getText().toString(),position));
            }
        });

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
