package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/2/7
 */
public class SLFFeedbackItemResponseBean extends SLFResponseBaseBean {
    public SLFFeedbackItemBean data;
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFFeedbackItemResponseBean{" +
                "data=" + data +
                ", tid='" + tid + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
