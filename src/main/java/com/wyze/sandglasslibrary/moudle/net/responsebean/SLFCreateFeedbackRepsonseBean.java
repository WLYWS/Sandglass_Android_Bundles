package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCreateFeedbackRepsonseBean extends SLFResponseBaseBean {
    public int data;
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFCreateFeedbackRepsonseBean{" +
                "data=" + data +
                ", tid='" + tid + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
