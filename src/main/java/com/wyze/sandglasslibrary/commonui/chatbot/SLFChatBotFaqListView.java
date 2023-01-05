package com.wyze.sandglasslibrary.commonui.chatbot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.wyze.sandglasslibrary.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by wangjian on 2023/1/3
 * chatbot聊天中FAQ问题呈现
 */
public class SLFChatBotFaqListView extends ConstraintLayout {
    public SLFChatBotFaqListView (@NonNull Context context) {
        super(context);
        initView(context);
    }

    public SLFChatBotFaqListView (@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SLFChatBotFaqListView (@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView (Context context) {
        View view = View.inflate(context, R.layout.slf_chat_bot_faq_list,null);

    }
}
