package com.wyze.sandglasslibrary.bean.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCreateFeedbackRepsonseBean extends SLFResponseBaseBean {
    public int data;
    public String instance_id;

    @Override
    public String toString ( ) {
        return "SLFCreateFeedbackRepsonseBean{" +
                "data=" + data +
                ", instance_id='" + instance_id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
