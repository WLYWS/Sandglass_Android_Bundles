package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFLeaveMsgResponseBean extends SLFResponseBaseBean {
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFCommonPostResponseBean{" +
                "tid='" + tid + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
