package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotClickNoSendWarnEvent {
    public String question;
    public int position;
    public int sendFlag;

    public SLFChatBotClickNoSendWarnEvent (String question,int position,int sendFlag) {
        this.question = question;
        this.position = position;
        this.sendFlag = sendFlag;
    }
}
