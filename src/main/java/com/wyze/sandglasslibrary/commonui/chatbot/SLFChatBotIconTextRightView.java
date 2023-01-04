package com.wyze.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot聊天中只包含icon和文字的item
 */
public class SLFChatBotIconTextRightView extends ConstraintLayout {

    private ImageView iv_chat_bot_user_icon;
    private TextView tv_chat_bot_user_text;

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
        tv_chat_bot_user_text.setMaxWidth(SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116));

    }
    public void setText(CharSequence text){
        tv_chat_bot_user_text.setText(text);
    }

}
