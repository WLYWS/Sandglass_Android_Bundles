package com.sandglass.sandglasslibrary.moudle.event;

import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFRecord;

/**
 * Greated by yangjie
 * descrip:所有反馈某条反馈设为已读
 * time：2023/2/23
 */
public class SLFUnReadtoReadAllEvent {

    public SLFRecord slfRecord;

    public SLFUnReadtoReadAllEvent(SLFRecord slfRecord){
        this.slfRecord = slfRecord;
    }
}
