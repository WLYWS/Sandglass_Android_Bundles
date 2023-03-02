package com.sandglass.sandglasslibrary.functionmoudle.adapter.chatbot;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextLeftView;
import com.sandglass.sandglasslibrary.commonui.chatbot.SLFChatBotIconTextRightView;
import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.sandglass.sandglasslibrary.moudle.event.SLFChatBotClickNoSendWarnEvent;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;

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
    private AnimationSet animationSet;
    private ObjectAnimator animator;


    public SLFChatBotSingleUserViewHolder (@NonNull View itemView, Context context) {
        super(itemView);
        iv_chat_bot_user_icon = itemView.findViewById(R.id.iv_chat_bot_user_icon);
        tv_chat_bot_user_text = itemView.findViewById(R.id.tv_chat_bot_user_text);
        iv_chat_bot_user_warn = itemView.findViewById(R.id.iv_chat_bot_user_warn);
        tv_chat_bot_user_text.setMaxWidth(SLFCommonUtils.getScreenWidth()-SLFCommonUtils.dip2px(context,116));
        SLFFontSet.setSLF_RegularFont(context,tv_chat_bot_user_text);

        iv_chat_bot_user_icon.setImageResource(R.mipmap.slf_chat_bot_user_icon);
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
        if (animator!=null){
            animator.cancel();
            animator=null;
            //
        }
        iv_chat_bot_user_warn.setVisibility(View.GONE);
        if (send_msg_status == SLFChatBotMsgData.MsgSendStatus.SEND_FAIL_MSG.getValue()){
            iv_chat_bot_user_warn.setImageResource(R.mipmap.slf_chat_bot_user_warn);
            iv_chat_bot_user_warn.setVisibility(View.VISIBLE);
            iv_chat_bot_user_warn.setRotation(0);
            //animationSet.reset();
        }else if(send_msg_status == SLFChatBotMsgData.MsgSendStatus.SENDED_MSG.getValue()){
            iv_chat_bot_user_warn.setVisibility(View.GONE);
            //animationSet.reset();
            if (animator!=null&&animator.isRunning()){
                animator.cancel();
            }
        }else if (send_msg_status == SLFChatBotMsgData.MsgSendStatus.SENDING_MSG.getValue()){
            //显示正在加载
            iv_chat_bot_user_warn.setImageResource(R.mipmap.slf_chat_bot_sending);
            iv_chat_bot_user_warn.setVisibility(View.VISIBLE);
            animator = ObjectAnimator.ofFloat(iv_chat_bot_user_warn, "rotation",0, 360);
            animator.setDuration(1000);
            animator.setRepeatCount(ValueAnimator.INFINITE);  // 无限循环
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
            //iv_chat_bot_user_warn.startAnimation(animationSet);
        }
    }

    private void loading ( ) {
        animationSet = new AnimationSet(true);
        RotateAnimation animation_rotate = new RotateAnimation(0, +359,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation_rotate.setRepeatCount(-1);
        animation_rotate.setStartOffset(0);
        animation_rotate.setDuration(1000);
        LinearInterpolator lir = new LinearInterpolator();
        animationSet.setInterpolator(lir);
        animationSet.addAnimation(animation_rotate);
    }

    public void setPosition (int position) {
        this.position = position;
    }
}
