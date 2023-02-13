package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/2/8
 */
public class SLFFeedbackDetailItemResponseBean extends SLFResponseBaseBean{
    public String tid;
    public SLFFeedbackDetailItemBean data;

    @Override
    public String toString ( ) {
        return "SLFFeedbackDetailItemResponseBean{" +
                "tid='" + tid + '\'' +
                ", data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
