package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFChatBotUpdateQuesionEvent {
    public int index;
    public int position;

    public SLFChatBotUpdateQuesionEvent (int index, int position) {
        this.index = index;
        this.position = position;
    }
}
