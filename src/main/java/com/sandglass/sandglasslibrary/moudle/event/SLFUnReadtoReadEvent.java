package com.sandglass.sandglasslibrary.moudle.event;

import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFRecord;

/**
 * Greated by yangjie
 * descrip:进行中某条反馈设为已读
 * time：2023/2/23
 */
public class SLFUnReadtoReadEvent {

    public SLFRecord slfRecord;

    public SLFUnReadtoReadEvent(SLFRecord slfRecord){
        this.slfRecord = slfRecord;
    }
}
