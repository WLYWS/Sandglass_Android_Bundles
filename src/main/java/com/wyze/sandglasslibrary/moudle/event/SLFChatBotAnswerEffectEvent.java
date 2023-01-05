package com.wyze.sandglasslibrary.moudle.event;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotAnswerEffectEvent {
    public int isSolution;
    public int position;

    public SLFChatBotAnswerEffectEvent (int isSolution, int position) {
        this.isSolution = isSolution;
        this.position = position;
    }
}
