package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Created by yangjie on 2023/2/22
 * describe:是否还有未读反馈
 * time:2023/2/22
 */
public class SLFUnReadFeedbackEvent {
    public boolean hasUnRead;

    public SLFUnReadFeedbackEvent(boolean hasUnRead) {
       this.hasUnRead = hasUnRead;
    }
}
