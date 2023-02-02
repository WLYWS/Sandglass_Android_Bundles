package com.wyze.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.theme.SLFFontSet;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import androidx.annotation.NonNull;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotSingleRobotViewHolder extends SLFChatBotBaseViewHodler {

    private ImageView iv_chat_bot_robot_icon;
    private TextView tv_chat_bot_robot_text;

    public SLFChatBotSingleRobotViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        iv_chat_bot_robot_icon = itemView.findViewById(R.id.iv_chat_bot_robot_icon);
        tv_chat_bot_robot_text = itemView.findViewById(R.id.tv_chat_bot_robot_text);
        SLFFontSet.setSLF_RegularFont(context,tv_chat_bot_robot_text);

        tv_chat_bot_robot_text.setMaxWidth(SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116));
    }

    @Override
    public void bindView (SLFChatBotMsgData bean,int position) {
        setText(bean.getContent());
    }

    public void setText(CharSequence text){
        tv_chat_bot_robot_text.setText(text);
    }
}
