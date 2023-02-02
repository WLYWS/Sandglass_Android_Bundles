package com.wyze.sandglasslibrary.moudle.net.responsebean;
/**
 * Created by yangjie on 2023/1/31
 */
public class SLFLeaveMsgDataBean extends SLFLeaveMsgResponseBean {
    private SLFLeaveMsgRecord data;

    private SLFLeaveMsgDataBean(SLFLeaveMsgRecord data) {
        this.data = data;
    }

    public SLFLeaveMsgRecord getData() {
        return data;
    }

    public void setData(SLFLeaveMsgRecord data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SLFLeaveMsgDataBean{" +
                "tid='" + tid + '\'' +
                "data='" + data + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}