package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Created by yangjie on 2023/2/15
 */
public class SLFSendLogSuceessEvent {
    public boolean success;
    public int position;
    public SLFSendLogSuceessEvent(boolean success,int position) {
        this.success = success;
        this.position = position;
    }
}
