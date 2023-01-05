package com.wyze.sandglasslibrary.moudle.event;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotClickNoSendWarnEvent {
    public String question;
    public int position;

    public SLFChatBotClickNoSendWarnEvent (String question,int position) {
        this.question = question;
        this.position = position;
    }
}
