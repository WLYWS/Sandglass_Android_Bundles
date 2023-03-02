package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotClickQuesionEvent {
    public String question;
    public int type;

    public SLFChatBotClickQuesionEvent (String question,int type) {
        this.question = question;
        this.type = type;
    }
}
